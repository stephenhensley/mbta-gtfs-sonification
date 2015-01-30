package org.shensley.gtfs;

import java.io.IOException;
import java.net.SocketException;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;
//import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.onebusaway.cli.CommandLineInterfaceLibrary;
import org.onebusaway.guice.jsr250.LifecycleService;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;




//import java.util.ArrayList;

public class MainOutput {
	
	private static final String ARG_VEHICLE_POSITIONS_URL = "vehiclePositionsUrl";

	public static void main(String[] args) throws IOException, Exception {
		MainOutput m = new MainOutput();
		m.run(args);
	}
	
	@SuppressWarnings("unused")
	private void run(String[] args) throws Exception, SocketException {
		if(args.length == 0 || CommandLineInterfaceLibrary.wantsHelp(args)) {
			printUsage();
			System.exit(-1);
		}else{
			for(int i = 0; i< args.length;i++){
				System.out.println(args[i]);
			}
		}
		Options options = new Options();
		buildOptions(options);
		Parser parser = new GnuParser();
		CommandLine cli = parser.parse(options, args);		
		Set<Module> modules = new HashSet<Module>();
		
		RealtimeModule.addModuleAndDependencies(modules);
		
		Injector injector = Guice.createInjector(modules);
		injector.injectMembers(this);

		StaticHandler staticHandler = injector.getInstance(StaticHandler.class);
		
		//In original project, injecting the Server creates a dataServlet, which I use as reference for my FormatOutput class,
		//	since I won't be using a web server.
		FormatOutput formatOutput = injector.getInstance(FormatOutput.class);
		RealtimeService service = injector.getInstance(RealtimeService.class);
		service.setVehiclePositionsUri(new URI(
				cli.getOptionValue(ARG_VEHICLE_POSITIONS_URL)));
		
		
		
		
		LifecycleService lifecycleService = injector.getInstance(LifecycleService.class);
		lifecycleService.start();	
	}
	
	private void printUsage() {
		CommandLineInterfaceLibrary.printUsage(getClass());
	}
	
	private void buildOptions(Options options){
		options.addOption(ARG_VEHICLE_POSITIONS_URL,true,"");
	}

}
