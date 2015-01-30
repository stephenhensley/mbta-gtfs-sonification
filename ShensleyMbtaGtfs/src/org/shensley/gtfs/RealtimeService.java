package org.shensley.gtfs;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Singleton;
import org.eclipse.jetty.websocket.WebSocket.Connection;
import org.eclipse.jetty.websocket.WebSocket.OnBinaryMessage;


import org.eclipse.jetty.websocket.WebSocketClient;
import org.eclipse.jetty.websocket.WebSocketClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.transit.realtime.GtfsRealtime.FeedEntity;
import com.google.transit.realtime.GtfsRealtime.FeedHeader;
import com.google.transit.realtime.GtfsRealtime.FeedMessage;
import com.google.transit.realtime.GtfsRealtime.Position;
import com.google.transit.realtime.GtfsRealtime.VehicleDescriptor;
import com.google.transit.realtime.GtfsRealtime.VehiclePosition;
import com.google.transit.realtime.GtfsRealtime.TripDescriptor;


@Singleton
public class RealtimeService {
	
	private static final Logger _log = LoggerFactory.getLogger(RealtimeService.class);
	
	private URI _vehiclePositionsUri;
	
	private ScheduledExecutorService _executor;
	
	private WebSocketClientFactory _webSocketFactory;
	
	private WebSocketClient _webSocketClient;
	
	private IncrementalWebSocket _incrementalWebSocket;
	
	private Future<Connection> _webSocketConnection;
	
	private Map<String, String> _vehicleIdsByEntityIds = new HashMap<String, String>();
	
	private Map<String, Vehicle> _vehiclesById = new ConcurrentHashMap<String, Vehicle>();

	//private Map<String, String> _trainLineByRoute = new HashMap<String, String>();

	private List<VehicleListener> _listeners = new CopyOnWriteArrayList<VehicleListener>();

	private final RefreshTask _refreshTask = new RefreshTask();

	private int _refreshInterval = 20;

	private boolean _dynamicRefreshInterval = true;

	private long _mostRecentRefresh = -1;
	
	public void setVehiclePositionsUri(URI uri){
		_vehiclePositionsUri = uri;
	}
	
	@PostConstruct
	public void start() throws Exception {
		String scheme = _vehiclePositionsUri.getScheme();
		if(scheme.equals("ws") || scheme.equals("wss")){
			_webSocketFactory = new WebSocketClientFactory();
			_webSocketFactory.start();
			_webSocketClient = _webSocketFactory.newWebSocketClient();
			_webSocketClient.setMaxBinaryMessageSize(16384000);
			_incrementalWebSocket = new IncrementalWebSocket();
			_webSocketConnection = _webSocketClient.open(_vehiclePositionsUri, _incrementalWebSocket);
		}else{
			_executor = Executors.newSingleThreadScheduledExecutor();
			_executor.schedule(_refreshTask, 0 , TimeUnit.SECONDS);
		}
	}

	@PreDestroy
	public void stop() throws Exception {
		if(_webSocketConnection != null) {
			_webSocketConnection.cancel(false);
		}
		if(_webSocketClient != null){
			_webSocketClient = null;
		}
		if (_webSocketFactory != null){
			_webSocketFactory.stop();
			_webSocketFactory = null;
		}
		if(_executor != null) {
			_executor.shutdownNow();
		}
	}
	
	public List<Vehicle> getAllVehicles(){
		return new ArrayList<Vehicle>(_vehiclesById.values());
	}
	
	public void addListener(VehicleListener listener){
		_listeners.add(listener);
	}
	
	public void removeListener(VehicleListener listener){
		_listeners.remove(listener);
	}
	
	private void refresh() throws IOException {
		
		_log.info("refreshing vehicle positions.");
		
		URL url = _vehiclePositionsUri.toURL();
		FeedMessage feed = FeedMessage.parseFrom(url.openStream());
		
		boolean hadUpdate = processDataset(feed);
		
		if (hadUpdate){
			if(_dynamicRefreshInterval){
				updateRefreshInterval();
			}
		}
		
		_executor.schedule(_refreshTask, _refreshInterval, TimeUnit.SECONDS);
		
	}
	
	private boolean processDataset(FeedMessage feed){
		List<Vehicle> vehicles = new ArrayList<Vehicle>();
		
		boolean update = false;
		
		for (FeedEntity entity : feed.getEntityList()){
			if(entity.hasIsDeleted() && entity.getIsDeleted()) {
				String vehicleId = _vehicleIdsByEntityIds.get(entity.getId());
				if(vehicleId == null){
					_log.warn("unknown entity id in deletion request: " + entity.getId());
					continue;
				}
				_vehiclesById.remove(vehicleId);
				continue;
			}
			if(!entity.hasVehicle()){
				_log.warn("entity doesn't have vehicle");
				continue;
			}
			VehiclePosition vehicle = entity.getVehicle();
			
			//checks for trip, if null continue
			if (!vehicle.hasTrip()){
				_log.warn("entity doesn't have trip");
				continue;
			}
			
			TripDescriptor trip = vehicle.getTrip();
			
			
			
			String vehicleId = getVehicleId(vehicle);
			if(vehicleId == null){
				_log.warn("vehicleId is null");
				continue;
			}
			_vehicleIdsByEntityIds.put(entity.getId(), vehicleId);
			
			//checks for position, continues if none;
			if(!vehicle.hasPosition()) {
				_log.warn("vehicle doesn't have position");
				continue;
			}
			Position position = vehicle.getPosition();
			
			//now we build the vehicle for our own object.
			Vehicle v = new Vehicle();
			v.setId(vehicleId);
			//positions
			v.setLat(position.getLatitude());
			v.setLon(position.getLongitude());
			v.setBearing(position.getBearing());
			//Trip Descriptors
			v.setRouteId(trip.getRouteId());
			v.setTrip(trip.getTripId());
			v.setLastUpdate(System.currentTimeMillis());
			
			//now check if the vehicle we are updating already exists
			Vehicle existing = _vehiclesById.get(vehicleId);
			if(existing == null || existing.getLat() != v.getLat()
					|| existing.getLon() != v.getLon()) {
				_vehiclesById.put(vehicleId, v);
				update = true;
			}else {
				v.setLastUpdate(existing.getLastUpdate());
				
			}
			boolean isTrain = v.getRouteId().contains("_");
			if(isTrain){
				vehicles.add(v);
			}
		}
		
		if(update){
			_log.info("vehicles updated: " + vehicles.size() + '\n');
			
			StringBuilder b = new StringBuilder();
			b.append("\n");
			b.append("Information: \nIndex,\tVehicle Id,\tRouteId,\tTripId,\tLat,\tLon,\tBearing\n");
			int z = 0;

			for(Vehicle v : vehicles){
				b.append(Integer.toString(z) + '\t' + v.getId() + '\t' + v.getRouteId() + '\t' + v.getTripId() + '\t' + v.getLat() + '\t' + v.getLon() + '\t' + v.getBearing() + '\n');
				z++;
			}
			b.append("=======================================================================\n");
			_log.info(b.toString());
			System.out.print(b.toString());
		}
		
		for (VehicleListener listener : _listeners){
			listener.handleVehicles(vehicles);
		}

		return update;
	}
	
	/**
	 * @param vehicle
	 * @return
	 */
	private String getVehicleId(VehiclePosition vehicle){
		if(!vehicle.hasVehicle()){
			return null;
		}
		VehicleDescriptor desc = vehicle.getVehicle();
		if(!desc.hasId()){
			return null;
		}
		return desc.getId();
	}
	
	private void updateRefreshInterval(){
		long t = System.currentTimeMillis();
		if (_mostRecentRefresh != -1){
			int refreshInterval = (int) ((t- _mostRecentRefresh)/(2 * 1000));
			_refreshInterval = Math.max(10, refreshInterval);
			_log.info("refresh interval: " + _refreshInterval);
		}
		_mostRecentRefresh = t;
	}
	
	private class RefreshTask implements Runnable {
		@Override
		public void run() {
			try {
				refresh();
			}catch (Exception ex) {
				_log.error("error refreshing GTFS-realtime data", ex);
			}
		}
	}
	
	private class IncrementalWebSocket implements OnBinaryMessage {
		
		@Override
		public void onOpen(Connection connection){
			
		}
		
		@Override
		public void onMessage(byte[] buf, int offset, int length){
			if (offset != 0 || buf.length != length){
				byte trimmed[] = new byte[length];
				System.arraycopy(buf,offset, trimmed, 0, length);
				buf = trimmed;
			}
			FeedMessage message = parseMessage(buf);
			FeedHeader header = message.getHeader();
			switch (header.getIncrementality()){
			case FULL_DATASET:
				processDataset(message);
				break;
			case DIFFERENTIAL:
				processDataset(message);
				break;
			default:
				_log.warn("unknown incrementality: " + header.getIncrementality());
			}
		}
		@Override
		public void onClose(int closeCode, String message) {
			
		}
		
		private FeedMessage parseMessage(byte[] buf) {
			try {
				return FeedMessage.parseFrom(buf);
			}catch (InvalidProtocolBufferException ex){
				throw new IllegalStateException(ex);
			}

		}
	}
}
