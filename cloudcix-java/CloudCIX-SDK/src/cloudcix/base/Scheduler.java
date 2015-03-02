package cloudcix.base;

import java.util.Properties;
import java.util.TimerTask;

import cloudcix.api.Auth;


public class Scheduler extends TimerTask {
	private Utilities utilities = new Utilities();
	
	@Override
	public void run() {
		refresh_api_token();
	}
	
	private void refresh_api_token() {
		Properties settings = utilities.get_config();
		
		Auth cli = new Auth();
		
		CloudCIXAuth credentials = new CloudCIXAuth(null, null, settings.getProperty("CLOUDCIX_API_ID_MEMBER"), utilities.api_token);
		
		Response response = cli.keystone.create(credentials);
		
		if (response.code == 201) {
			utilities.setApi_token(response.headers.get("X-Subject-Token").get(0));
		} else {
			utilities.get_admin_token();
		}
	}
	
}
