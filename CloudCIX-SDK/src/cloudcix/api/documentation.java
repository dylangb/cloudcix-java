package cloudcix.api;

import cloudcix.base.APIClient;

public class documentation {
	String _application_name = "Documentation";
	
	/**
	 * Client for Documentation Application service.
	 */
	public APIClient application = new APIClient(_application_name, "Application/");

	public documentation() {
		
	}

	public APIClient getApplication() {
		return application;
	}


}
