package cloudcix.api;

import cloudcix.base.APIClient;
import cloudcix.base.Utilities;

public class Antenna {
	String _application_name = "Antenna";
	
	/**
	 * Client for Antenna Antenna service.
	 */
	public APIClient antenna = new APIClient(_application_name, "Antenna/", Utilities.getContextPath());

	public Antenna() {
		
	}

	public APIClient getAntenna() {
		return antenna;
	}

}
