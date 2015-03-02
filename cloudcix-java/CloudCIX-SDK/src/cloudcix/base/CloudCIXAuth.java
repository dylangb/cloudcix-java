package cloudcix.base;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class CloudCIXAuth {
	
	private String username, password, idMember, token_id;
	JSONObject scope;
	
	/**
	 * Constructor method for the CloudCIXAuth object.
	 * In order to create authorization credentials the following rules must apply:
	 * 1) If start_token_id is not empty or null start_username and start_password will be ignored.
	 * 2) If start_token_id is empty or null start_username and start_password are required. 
	 * 	  Otherwise Keystone will return a 4** HTTP code.
	 * 3) If the first response returned a list of Members, it indicates that the credentials have multiple logins.
	 *    In order to select a Member the start_token_id must be passed along with the selected start_idMember.
	 * 4) start_idMember can be set in the first request along with the start_username and start_pasword
	 *    if the user wants to skip the step commented above. 
	 * In order to get a new token before it expires the start_token_id is required.
	 * @param start_username	the username of the user.
	 * @param start_password	the password of the user.
	 * @param start_idMember	the idMember of the user, optional if the passed in credentials have only one Member. 
	 * @param start_token_id	the token identifying the logged in user.
	 */
	public CloudCIXAuth(String start_username, String start_password, String start_idMember, String start_token_id) {
		username = start_username;
		password = start_password;
		idMember = start_idMember;
		token_id = start_token_id;
	}
	
	/**
	 * This method generates a JSONObject containing the needed request to generate a new token.
	 * @return	A JSONObject containing the created request with the parameters passed to the constructor.
	 */
	public JSONObject get_auth_ref() {
		JSONObject response = new JSONObject();
		
		JSONObject auth = new JSONObject();
		JSONObject identity = new JSONObject();
		JSONArray methods = new JSONArray();
		methods.add("cloudcix_auth");
		JSONObject cloudcix_auth = new JSONObject();
		
		if (token_id != null && !token_id.isEmpty()) {
			JSONObject token = new JSONObject();
			token.put("id", token_id);
			cloudcix_auth.put("token", token);
		} else {
			cloudcix_auth.put("username", username);
			cloudcix_auth.put("password", password);
		}
		if (idMember != null && !idMember.isEmpty()) cloudcix_auth.put("idMember", idMember);
		
		identity.put("methods", methods);
		identity.put("cloudcix_auth", cloudcix_auth);
		
		auth.put("identity", identity);
		if (scope != null && !scope.isEmpty()) auth.put("scope", scope);
		
		response.put("auth", auth);
		
		return response;
	}
	
	/**
	 * This method allows the user to scope a token within a domain. 
	 * It must be invoked before sending the credentials to the service.
	 * Notice that you can only scope by domain or project. 
	 * @param id	Unique id of the domain.
	 */
	public void scope_domain(String id) {
		scope = new JSONObject();

		JSONObject domain = new JSONObject();
		domain.put("id", id);
		
		scope.put("domain", domain);
		
	}
	
	/**
	 * This method allows the user to scope a token within a project. 
	 * It must be invoked before sending the credentials to the service.
	 * Notice that you can only scope by domain or project. 
	 * @param id	Unique id of the project.
	 */
	public void scope_project(String id) {
		scope = new JSONObject();
		JSONObject project = new JSONObject();
		project.put("id", id);
		
		scope.put("project", project);
		
	}
	
	/**
	 * This method deletes the selected scope for the credentials.
	 */
	public void unscope() {
		scope = null; 
	}
	
}
