package cloudcix.api;

import cloudcix.base.APIClient;

public class antenna {
	String _application_name = "Antenna";
	
	/**
	 * Client for Antenna Antenna service.
	 */
	public APIClient antenna = new APIClient(_application_name, "Antenna/");

	public antenna() {
		
	}

	public APIClient getAntenna() {
		return antenna;
	}

}
