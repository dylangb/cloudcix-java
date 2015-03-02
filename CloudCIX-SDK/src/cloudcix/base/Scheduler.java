package cloudcix.base;

import java.util.Properties;
import java.util.TimerTask;

import cloudcix.api.auth;


public class Scheduler extends TimerTask {
	private utils utilities = new utils();
	
	@Override
	public void run() {
		refresh_api_token();
	}
	
	private void refresh_api_token() {
		Properties settings = utilities.get_config();
		
		auth cli = new auth();
		
		CloudCIXAuth credentials = new CloudCIXAuth(null, null, settings.getProperty("CLOUDCIX_API_ID_MEMBER"), utilities.api_token);
		
		Response response = cli.keystone.create(credentials);
		
		if (response.code == 201) {
			utilities.setApi_token(response.headers.get("X-Subject-Token").get(0));
		} else {
			utilities.get_admin_token();
		}
	}
	
}
