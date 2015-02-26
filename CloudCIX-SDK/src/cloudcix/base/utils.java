package cloudcix.base;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import cloudcix.api.auth;
import cloudcix.base.CloudCIXAuth;
import cloudcix.base.Response;


public class utils {
	private InputStream input = null;
	public String api_token;
	
	/**
	 * Reads the cloudcix_settings file and get its properties.
	 * @return	Properties object containing the settings.
	 */
	public Properties get_config() {
		Properties prop = new Properties();
		
		try {
			input = new FileInputStream("cloudcix_settings.properties");
			prop.load(input); 
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return prop;
	}
	
	/**
	 * Generates the service complete URI based on the parameters needed by the method.
	 * @param base_url			base URL of the service, i.e.: https://api.cloudcix.com
	 * @param application_name	name of the application, except for Keystone every service has a name.
	 * @param api_version		version of the API, i.e.: /v1/, /v1.2/, etc.
	 * @param service_url		the service URL without the base and version, i.e.: Address/
	 * @param pk				the ID of the object, it's usually needed when performing read, update, partial update or delete.
	 * @param query_params		the list of parameters with its values to add to the URL.
	 * @param path_params		the list of parameters with its values that must be replaced in the URL.
	 * @return					the complete URI to invoke the method.
	 */
	public String get_uri(String base_url, String application_name, String api_version, String service_url, String pk, Map<String, String> path_params, Map<String, String> query_params) {
		if (base_url.endsWith("/")) base_url = base_url.substring(0, base_url.length() - 1);
		base_url += "/" + application_name + api_version + service_url;
		if (path_params != null && !path_params.isEmpty()) {
			for (Map.Entry<String, String> entry : path_params.entrySet()) {
				String name = "{" + entry.getKey() + "}";
				if (base_url.contains(name)) {
					base_url = base_url.replace(name, entry.getValue());
				}
			}
		}
		
		if (pk != null && !pk.isEmpty()) {
			base_url += pk + "/";
		}

		String query = "";
		if (query_params != null && !query_params.isEmpty()) {
			try {
				boolean first = true;
				for (Map.Entry<String, String> entry : query_params.entrySet()) {
					String key_value = "";
					if (entry.getKey().toLowerCase() == "fields") {
						key_value = entry.getKey() + "=" + entry.getValue();
					} else {
						key_value = entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), "UTF-8");
					}
					if (first) {
						first = false;
						query += "?";
					} else {
						query += "&";
					}
					query += key_value;
				}	
			} catch (UnsupportedEncodingException ex) {
				// Do not include it for the time being
			}
		}
		
		base_url += query;
		
		return base_url;
	}

	/**
	 * Returns the current Administrative token. If it does not exist it generates a new one.
	 * @return	the administrative token.
	 */
	public String get_admin_token() {
		if (api_token == null || api_token.isEmpty()) {
			get_new_admin_token();
		}
		return api_token;
	}

	/**
	 * Generates a new Administrative token using the credentials stored in the cloudcix_settings file.
	 * If the settings are wrong or it cannot generate a new Administrative token it will throw an exception. 
	 */
	public void get_new_admin_token() {
		Properties settings = get_config();
		
		auth cli = new auth();
		CloudCIXAuth credentials = new CloudCIXAuth(settings.getProperty("CLOUDCIX_API_USERNAME"), settings.getProperty("CLOUDCIX_API_PASSWORD"), settings.getProperty("CLOUDCIX_API_ID_MEMBER"), null);
		Response response = cli.keystone.create(credentials);
		
		if (response.code == 201) {
			setApi_token(response.headers.get("X-Subject-Token").get(0));
			TimerTask timerTask = new Scheduler();
			Timer timer = new Timer(true);
			timer.scheduleAtFixedRate(timerTask, 0, 55 * 60 * 1000);
		} else {
			try { 
				throw new Exception("Can't create administrative token, check your settings file, please.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public String getApi_token() {
		return api_token;
	}

	public void setApi_token(String api_token) {
		this.api_token = api_token;
	}
	
}
