package org.shensley.gtfs;

import java.io.IOException;
import java.util.List;

//import java.util.ArrayList;

public class MainOutput {

	public static void main(String[] args) throws IOException, Exception {
		// TODO Auto-generated method stub
		MainOutput m = new MainOutput();
		m.run();
	}

	private void run() throws Exception {
		StaticHandler _mainHandler = new StaticHandler();
		List<RouteStatic> trainRoutes = _mainHandler.getTrainRoutes();

		// Below is just for testing
		for (RouteStatic r : trainRoutes) {
			System.out.println(r.getId() + '\t' + r.getLongName());
		}
		System.out.println("Done Running");
	}

}
