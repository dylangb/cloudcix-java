package cloudcix.base;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class APIClient {
	Properties settings = new Properties();
	String application, service_url, api_version;
	private Utilities utilities = new Utilities("");
	
	public APIClient() {
		settings = utilities.get_config();
	}
	
	public APIClient(String start_application, String start_service_url, String contextPath) {
		api_version = "/v1/";
		application = start_application;
		service_url = start_service_url;
		utilities = new Utilities(contextPath);
		settings = utilities.get_config();
		
	}
	
	public APIClient(String start_application, String start_service_url, String start_api_version, String contextPath) {
		application = start_application;
		service_url = start_service_url;
		api_version = start_api_version;
		utilities = new Utilities(contextPath);
		settings = utilities.get_config();
	}
	
	public Properties getSettings() {
		return settings;
	}

	public String getService_url() {
		return service_url;
	}

	public String getApi_version() {
		return api_version;
	}

	/**
	 * Invokes a service method using the specified data.
	 * @param method 			the HTTP verb to use: DELETE, GET, HEAD, PATCH or PUT.  
	 * @param token				the token use to authenticate.
	 * @param pk				the ID of the object, it's usually needed when performing read, update, partial update or delete. 
	 * @param data				the object which will be sent in the body. 
	 * @param query_params		the list of parameters with its values to add to the URL.
	 * @param path_params		the list of parameters with its values that must be replaced in the URL.
	 * @param header_params		the list of headers with its values to sent in the request.
	 * @return					the response given by the service which consist of:
	 * 								code		an integer with the returned HTTP code.
	 * 								message 	the HTTP message in text format.
	 * 								headers		a list of headers and its values returned by the service.
	 * 								body		the JSON body returned by the service if anything was returned. 
	 */
	private Response _call(String method, String token, String pk, JSONObject data, Map<String, String> query_params, Map<String, String> path_params, Map<String, String> header_params) {
		Response response = new Response();
		
		JSONParser parser = new JSONParser();

		try {
			String uri = utilities.get_uri(settings.getProperty("CLOUDCIX_SERVER_URL"), application, api_version, service_url, pk, path_params, query_params);
			URL url = new URL(uri);
			if (uri.startsWith("https")) {
				HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
				conn.setRequestMethod(method);
				conn.setRequestProperty("Accept", "application/json");
				conn.setRequestProperty("X-Auth-Token", token);
				if (header_params != null) {
					for (Map.Entry<String, String> entry : path_params.entrySet()) {
						conn.setRequestProperty(entry.getKey(), entry.getValue());
					}
				}
				
				if (data != null) {
					conn.setRequestProperty("Content-Type", "application/json");
					conn.setDoOutput(true);
					String json_string = data.toJSONString();
					OutputStream output = conn.getOutputStream();
					output.write(json_string.getBytes("UTF-8"));
					output.flush();
				}
				
				response.code = conn.getResponseCode(); 
				response.message = conn.getResponseMessage();
				response.headers = conn.getHeaderFields();

				BufferedReader br;
				if (response.code >= 400) {
					br = new BufferedReader(new InputStreamReader((conn.getErrorStream())));	
				} else {
					br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				}
		 
				String output = "", final_output = "";
				while ((output = br.readLine()) != null) {
					final_output += output;
				}
				if (response.code != 204) response.body = (JSONObject) parser.parse(final_output);
				
				conn.disconnect();
			} else {
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod(method);
				conn.setRequestProperty("Accept", "application/json");
				conn.setRequestProperty("X-Auth-Token", token);
				if (header_params != null) {
					for (Map.Entry<String, String> entry : path_params.entrySet()) {
						conn.setRequestProperty(entry.getKey(), entry.getValue());
					}
				}
				
				if (data != null) {
					conn.setRequestProperty("Content-Type", "application/json");
					conn.setDoOutput(true);
					JSONObject request = (JSONObject) data;
					String json_string = request.toJSONString();
					OutputStream output = conn.getOutputStream();
					output.write(json_string.getBytes("UTF-8"));
					output.flush();
				}
				
				response.code = conn.getResponseCode(); 
				response.message = conn.getResponseMessage();
				response.headers = conn.getHeaderFields();

				BufferedReader br;
				if (response.code >= 400) {
					br = new BufferedReader(new InputStreamReader((conn.getErrorStream())));	
				} else {
					br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				}
		 
				String output = "", final_output = "";
				while ((output = br.readLine()) != null) {
					final_output += output;
				}
				if (response.code != 204) response.body = (JSONObject) parser.parse(final_output);

				conn.disconnect();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ParseException pe) {
			pe.printStackTrace();
		}
		
		return response;
	}

	/**
	 * Returns a Response object which contains the result of invoking the create method on the selected service.
	 * The token argument is required in most methods for authorization purposes.
	 * The data consist on the JSON body that will be sent in the request.
	 * The query_params are used to specify extra parameters allowed in the service, i.e.: fields=(name,) or field=(*,).
	 * 		In the first case it will return the field called name as well as the URI and other fixed fields.
	 * 		In the second case it will return all plain fields.
	 * 		Remember that in order to get fields containing objects they must be specified, i.e.: fields=(*,member)
	 * Before invoking the method the user must know if the path parameters are required by checking the base URL. 
	 * If it contains any {param_name} the path_params needs to be filled with the param_name and its value to create the 
	 * proper base URL.
	 * If the service requires other extra headers besides the X-Auth-Token they will be added to the header_params.      
	 * @param token				the token use to authenticate.
	 * @param data				the object which will be sent in the body. 
	 * @param query_params		the list of parameters with its values to add to the URL.
	 * @param path_params		the list of parameters with its values that must be replaced in the URL.
	 * @param header_params		the list of headers with its values to sent in the request.
	 * @return					the response given by the service which consist of:
	 * 								code		an integer with the returned HTTP code.
	 * 								message 	the HTTP message in text format.
	 * 								headers		a list of headers and its values returned by the service.
	 * 								body		the JSON body returned by the service if anything was returned. 
	 * @return
	 */
	public Response create(String token, JSONObject data, Map<String, String> query_params, Map<String, String> path_params, Map<String, String> header_params) {
		return _call("POST", token, null, data, query_params, path_params, header_params);
	}

	/**
	 * Returns a Response object which contains the result of invoking the update method on the selected service.
	 * The token argument is required in most methods for authorization purposes.
	 * The pk argument identifies uniquely the object to update.
	 * The data consist on the JSON body that will be sent in the request.
	 * The query_params are used to specify extra parameters allowed in the service, i.e.: fields=(name,) or field=(*,).
	 * 		In the first case it will return the field called name as well as the URI and other fixed fields.
	 * 		In the second case it will return all plain fields.
	 * 		Remember that in order to get fields containing objects they must be specified, i.e.: fields=(*,member)
	 * 		Notice that if the fields field is not specified this method will return HTTP code 204 and a null body.
	 * Before invoking the method the user must know if the path parameters are required by checking the base URL. 
	 * If it contains any {param_name} the path_params needs to be filled with the param_name and its value to create the 
	 * proper base URL.
	 * If the service requires other extra headers besides the X-Auth-Token they will be added to the header_params.      
	 * @param token				the token use to authenticate.
	 * @param pk				the ID of the object if required. 
	 * @param data				the object which will be sent in the body. 
	 * @param query_params		the list of parameters with its values to add to the URL.
	 * @param path_params		the list of parameters with its values that must be replaced in the URL.
	 * @param header_params		the list of headers with its values to sent in the request.
	 * @return					the response given by the service which consist of:
	 * 								code		an integer with the returned HTTP code.
	 * 								message 	the HTTP message in text format.
	 * 								headers		a list of headers and its values returned by the service.
	 * 								body		the JSON body returned by the service if anything was returned. 
	 * @return
	 */
	public Response update(String token, String pk, JSONObject data, Map<String, String> query_params, Map<String, String> path_params, Map<String, String> header_params) {
		return _call("PUT", token, pk, data, query_params, path_params, header_params);
	}

	/**
	 * Returns a Response object which contains the result of invoking the partial update method on the selected service.
	 * The token argument is required in most methods for authorization purposes.
	 * The pk argument identifies uniquely the object to update.
	 * The data consist on the JSON body that will be sent in the request.
	 * The query_params are used to specify extra parameters allowed in the service, i.e.: fields=(name,) or field=(*,).
	 * 		In the first case it will return the field called name as well as the URI and other fixed fields.
	 * 		In the second case it will return all plain fields.
	 * 		Remember that in order to get fields containing objects they must be specified, i.e.: fields=(*,member)
	 * Before invoking the method the user must know if the path parameters are required by checking the base URL. 
	 * If it contains any {param_name} the path_params needs to be filled with the param_name and its value to create the 
	 * proper base URL.
	 * If the service requires other extra headers besides the X-Auth-Token they will be added to the header_params.      
	 * @param token				the token use to authenticate.
	 * @param pk				the ID of the object if required. 
	 * @param data				the object which will be sent in the body. 
	 * @param query_params		the list of parameters with its values to add to the URL.
	 * @param path_params		the list of parameters with its values that must be replaced in the URL.
	 * @param header_params		the list of headers with its values to sent in the request.
	 * @return					the response given by the service which consist of:
	 * 								code		an integer with the returned HTTP code.
	 * 								message 	the HTTP message in text format.
	 * 								headers		a list of headers and its values returned by the service.
	 * 								body		the JSON body returned by the service if anything was returned. 
	 * @return
	 */
	public Response partial_update(String token, String pk, JSONObject data, Map<String, String> query_params, Map<String, String> path_params, Map<String, String> header_params) {
		return _call("PATCH", token, pk, data, query_params, path_params, header_params);
	}

	/**
	 * Returns a Response object which contains the result of invoking the read method on the selected service.
	 * The token argument is required in most methods for authorization purposes.
	 * The pk argument identifies uniquely the object to read.
	 * The data consist on the JSON body that will be sent in the request if required.
	 * The query_params are used to specify extra parameters allowed in the service, i.e.: fields=(name,) or field=(*,).
	 * 		In the first case it will return the field called name as well as the URI and other fixed fields.
	 * 		In the second case it will return all plain fields.
	 * 		Remember that in order to get fields containing objects they must be specified, i.e.: fields=(*,member)
	 * Before invoking the method the user must know if the path parameters are required by checking the base URL. 
	 * If it contains any {param_name} the path_params needs to be filled with the param_name and its value to create the 
	 * proper base URL.
	 * If the service requires other extra headers besides the X-Auth-Token they will be added to the header_params.      
	 * @param token				the token use to authenticate.
	 * @param pk				the ID of the object if required. 
	 * @param data				the object which will be sent in the body. 
	 * @param query_params		the list of parameters with its values to add to the URL.
	 * @param path_params		the list of parameters with its values that must be replaced in the URL.
	 * @param header_params		the list of headers with its values to sent in the request.
	 * @return					the response given by the service which consist of:
	 * 								code		an integer with the returned HTTP code.
	 * 								message 	the HTTP message in text format.
	 * 								headers		a list of headers and its values returned by the service.
	 * 								body		the JSON body returned by the service if anything was returned. 
	 * @return
	 */
	public Response read(String token, String pk, JSONObject data, Map<String, String> query_params, Map<String, String> path_params, Map<String, String> header_params) {
		return _call("GET", token, pk, data, query_params, path_params, header_params);
	}

	/**
	 * Returns a Response object which contains the result of invoking the delete method on the selected service.
	 * The token argument is required in most methods for authorization purposes.
	 * The pk argument identifies uniquely the object to delete.
	 * The data consist on the JSON body that will be sent in the request if required.
	 * The query_params are used to specify extra parameters allowed in the service, i.e.: fields=(name,) or field=(*,).
	 * 		In the first case it will return the field called name as well as the URI and other fixed fields.
	 * 		In the second case it will return all plain fields.
	 * 		Remember that in order to get fields containing objects they must be specified, i.e.: fields=(*,member)
	 * Before invoking the method the user must know if the path parameters are required by checking the base URL. 
	 * If it contains any {param_name} the path_params needs to be filled with the param_name and its value to create the 
	 * proper base URL.
	 * If the service requires other extra headers besides the X-Auth-Token they will be added to the header_params.      
	 * @param token				the token use to authenticate.
	 * @param pk				the ID of the object if required. 
	 * @param data				the object which will be sent in the body. 
	 * @param query_params		the list of parameters with its values to add to the URL.
	 * @param path_params		the list of parameters with its values that must be replaced in the URL.
	 * @param header_params		the list of headers with its values to sent in the request.
	 * @return					the response given by the service which consist of:
	 * 								code		an integer with the returned HTTP code.
	 * 								message 	the HTTP message in text format.
	 * 								headers		a list of headers and its values returned by the service.
	 * 								body		the JSON body returned by the service if anything was returned. 
	 * @return
	 */
	public Response delete(String token, String pk, JSONObject data, Map<String, String> query_params, Map<String, String> path_params, Map<String, String> header_params) {
		return _call("DELETE", token, pk, data, query_params, path_params, header_params);
	}

	/**
	 * Returns a Response object which contains the result of invoking the list method on the selected service.
	 * The token argument is required in most methods for authorization purposes.
	 * The data consist on the JSON body that will be sent in the request.
	 * The query_params are used to specify extra parameters allowed in the service, i.e.: fields=(name,) or field=(*,).
	 * 		In the first case it will return the field called name as well as the URI and other fixed fields.
	 * 		In the second case it will return all plain fields.
	 * 		Remember that in order to get fields containing objects they must be specified, i.e.: fields=(*,member)
	 * 		List usually supports also the following fields: 
	 * 			page: an integer that indicates the starting page to filter starting by 0.
	 * 			limit: an integer that indicates the total number of records to return.
	 * 			order: the field that must be used to sort the response. Check the service documentation to get the list 
	 * 				   of supported fields.
	 * 		In order to filter by certain field values check the service documentation to get the supported fields and samples.
	 * Before invoking the method the user must know if the path parameters are required by checking the base URL. 
	 * If it contains any {param_name} the path_params needs to be filled with the param_name and its value to create the 
	 * proper base URL.
	 * If the service requires other extra headers besides the X-Auth-Token they will be added to the header_params.      
	 * @param token				the token use to authenticate.
	 * @param data				the object which will be sent in the body. 
	 * @param query_params		the list of parameters with its values to add to the URL.
	 * @param path_params		the list of parameters with its values that must be replaced in the URL.
	 * @param header_params		the list of headers with its values to sent in the request.
	 * @return					the response given by the service which consist of:
	 * 								code		an integer with the returned HTTP code.
	 * 								message 	the HTTP message in text format.
	 * 								headers		a list of headers and its values returned by the service.
	 * 								body		the JSON body returned by the service if anything was returned. 
	 * @return
	 */
	public Response list(String token, JSONObject data, Map<String, String> query_params, Map<String, String> path_params, Map<String, String> header_params) {
		return _call("GET", token, null, data, query_params, path_params, header_params);
	}

	/**
	 * Returns a Response object which contains the result of invoking the head method on the selected service.
	 * This method is usually used to get the response code and message to know if something exists or not. 
	 * Most times the response body will be null.
	 * The token argument is required in most methods for authorization purposes.
	 * The pk argument identifies uniquely the object if required.
	 * The data consist on the JSON body that will be sent in the request if require.
	 * The query_params are used to specify extra parameters allowed in the service.
	 * Before invoking the method the user must know if the path parameters are required by checking the base URL. 
	 * If it contains any {param_name} the path_params needs to be filled with the param_name and its value to create the 
	 * proper base URL.
	 * If the service requires other extra headers besides the X-Auth-Token they will be added to the header_params.      
	 * @param token				the token use to authenticate.
	 * @param pk				the ID of the object if required. 
	 * @param data				the object which will be sent in the body. 
	 * @param query_params		the list of parameters with its values to add to the URL.
	 * @param path_params		the list of parameters with its values that must be replaced in the URL.
	 * @param header_params		the list of headers with its values to sent in the request.
	 * @return					the response given by the service which consist of:
	 * 								code		an integer with the returned HTTP code.
	 * 								message 	the HTTP message in text format.
	 * 								headers		a list of headers and its values returned by the service.
	 * 								body		the JSON body returned by the service if anything was returned. 
	 * @return
	 */
	public Response head(String token, String pk, JSONObject data, Map<String, String> query_params, Map<String, String> path_params, Map<String, String> header_params) {
		return _call("HEAD", token, pk, data, query_params, path_params, header_params);
	}

}
