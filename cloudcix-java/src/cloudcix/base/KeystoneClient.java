package cloudcix.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import java.util.Properties;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import cloudcix.base.CloudCIXAuth;

public class KeystoneClient {
	Properties settings = new Properties();
	String service_url;
	Utilities utilities;

	public KeystoneClient(String start_service_url, String contextPath) {
		service_url = start_service_url;

		utilities = new Utilities(contextPath);
		settings = utilities.get_config();
	}

	public Properties getSettings() {
		return settings;
	}

	public void setSettings(Properties settings) {
		this.settings = settings;
	}

	public String getService_url() {
		return service_url;
	}

	public void setService_url(String service_url) {
		this.service_url = service_url;
	}

	/**
	 * Invokes a Keystone method using the specified data.
	 * 
	 * @param method
	 *            the HTTP verb to use: DELETE, GET, HEAD, PATCH or PUT.
	 * @param admin_token
	 *            the administrative token used to perform the request.
	 * @param user_token
	 *            the user token used to perform the request.
	 * @param request
	 *            the object which will be sent in the body.
	 * @return the response given by the service which consist of: code an
	 *         integer with the returned HTTP code. message the HTTP message in
	 *         text format. headers a list of headers and its values returned by
	 *         Keystone. body the JSON body returned by Keystone if anything was
	 *         returned.
	 * @throws SocketTimeoutException 
	 * 
	 */
	private Response _call(String method, String admin_token,
			String user_token, JSONObject request) throws SocketTimeoutException {
		Response response = new Response();

		JSONParser parser = new JSONParser();

		try {
			
			String uri = this.getSettings().getProperty("OPENSTACK_KEYSTONE_URL")
					+ service_url;
			URL url = new URL(uri);
			if (uri.startsWith("https")) {
				HttpsURLConnection conn = (HttpsURLConnection) url
						.openConnection();
				conn.setRequestMethod(method);
				conn.setRequestProperty("Accept", "application/json");

				if (admin_token != null && !admin_token.isEmpty())
					conn.setRequestProperty("X-Auth-Token", admin_token);
				if (user_token != null && !user_token.isEmpty())
					conn.setRequestProperty("X-Subject-Token", user_token);

				if (request != null && !request.isEmpty()) {
					conn.setRequestProperty("Content-Type", "application/json");
					conn.setConnectTimeout(3000);
					conn.setReadTimeout(10000);
					conn.setDoOutput(true);
					OutputStream output = conn.getOutputStream();
					output.write(request.toJSONString().getBytes("UTF-8"));
					output.flush();
				}

				response.code = conn.getResponseCode();
				response.message = conn.getResponseMessage();
				response.headers = conn.getHeaderFields();

				BufferedReader br;
				if (response.code >= 400) {
					br = new BufferedReader(new InputStreamReader(
							(conn.getErrorStream())));
				} else {
					br = new BufferedReader(new InputStreamReader(
							(conn.getInputStream())));
				}
				String output = "", final_output = "";
				while ((output = br.readLine()) != null) {
					final_output += output;
				}
				if (response.code != 204)
					response.body = (JSONObject) parser.parse(final_output);

				conn.disconnect();
			} else {
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setRequestMethod(method);
				conn.setRequestProperty("Accept", "application/json");

				if (admin_token != null && !admin_token.isEmpty())
					conn.setRequestProperty("X-Auth-Token", admin_token);
				if (user_token != null && !user_token.isEmpty())
					conn.setRequestProperty("X-Subject-Token", user_token);

				if (request != null && !request.isEmpty()) {
					conn.setRequestProperty("Content-Type", "application/json");
					conn.setDoOutput(true);
					OutputStream output = conn.getOutputStream();
					output.write(request.toJSONString().getBytes("UTF-8"));
					output.flush();
				}

				response.code = conn.getResponseCode();
				response.message = conn.getResponseMessage();
				response.headers = conn.getHeaderFields();

				BufferedReader br;
				if (response.code >= 400) {
					br = new BufferedReader(new InputStreamReader(
							(conn.getErrorStream())));
				} else {
					br = new BufferedReader(new InputStreamReader(
							(conn.getInputStream())));
				}
				String output = "", final_output = "";
				while ((output = br.readLine()) != null) {
					final_output += output;
				}
				if (response.code != 204)
					response.body = (JSONObject) parser.parse(final_output);

				conn.disconnect();
			}
		} catch (MalformedURLException e) {

		} catch (ParseException pe) {
			// pe.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (IOException e) {
			if (e.getCause() instanceof SocketTimeoutException) {
				throw new SocketTimeoutException();
			}
		}

		return response;
	}

	/**
	 * This method gets a new token based on the CloudCIXAuth object passed. The
	 * CloudCIX object can contain: 1) A set of username and password to
	 * authenticate the user. idMember is optional but it can be included in
	 * order to select directly the idMember in the case the user has multiple
	 * logins. 2) A token to get a new token. idMember is optional if the user
	 * is only getting a new token before it expires. If the user has multiple
	 * logins the first request returned a list of members and the user must
	 * specify the idMember in the following request in order to get a valid
	 * token for the selected idMember.
	 * 
	 * @param auth
	 *            the CloudCIXAuth object containing the credentials to sent to
	 *            Keystone.
	 * @return the response given by the Keystone which consist of: code an
	 *         integer with the returned HTTP code. message the HTTP message in
	 *         text format. headers a list of headers and its values returned by
	 *         Keystone. body the JSON body returned by Keystone if anything was
	 *         returned.
	 * @throws SocketTimeoutException 
	 * @throws IOException
	 */
	public Response create(CloudCIXAuth auth) throws SocketTimeoutException {
		// response = new Response();

		JSONObject request = auth.get_auth_ref();

		Response response = _call("POST", null, null, request);

		return response;
	}

	/**
	 * This method validates an user token.
	 * 
	 * @param admin_token
	 *            A token with administrative privileges.
	 * @param user_token
	 *            The user token to validate.
	 * @return the response given by the Keystone which consist of: code an
	 *         integer with the returned HTTP code. message the HTTP message in
	 *         text format. headers a list of headers and its values returned by
	 *         Keystone. body the JSON body returned by Keystone if anything was
	 *         returned.
	 * @throws IOException
	 */
	public Response read(String admin_token, String user_token)
			throws SocketTimeoutException {
		return _call("GET", admin_token, user_token, null);
	}

	/**
	 * This method expires an user token.
	 * 
	 * @param admin_token
	 *            A token with administrative privileges.
	 * @param user_token
	 *            The user token to validate.
	 * @return the response given by the Keystone which consist of: code an
	 *         integer with the returned HTTP code. message the HTTP message in
	 *         text format. headers a list of headers and its values returned by
	 *         Keystone. body the JSON body returned by Keystone if anything was
	 *         returned.
	 * @throws IOException
	 */
	public Response delete(String admin_token, String user_token)
			throws SocketTimeoutException {
		return _call("DELETE", admin_token, user_token, null);
	}

}
