package cloudcix.api;

import cloudcix.base.APIClient;

public class Antenna {
	String _application_name = "Antenna";
	
	/**
	 * Client for Antenna Antenna service.
	 */
	public APIClient antenna = new APIClient(_application_name, "Antenna/");

	public Antenna() {
		
	}

	public APIClient getAntenna() {
		return antenna;
	}

}
