package tests;

import java.net.SocketTimeoutException;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Properties;

import org.junit.Test;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import cloudcix.api.*;
import cloudcix.base.CloudCIXAuth;
import cloudcix.base.Response;
import cloudcix.base.Utilities;



public class tests {

	Utilities utilities = new Utilities("");
	
	@Test
	public void test_get_config() {
		System.out.println("Starting test get config.");
		Properties config = utilities.get_config();
		System.out.println(config.toString());
	}
	
	@Test
	public void test_membership() throws SocketTimeoutException {
		
//		Auth cli = new Auth();
//		CloudCIXAuth credentials = new CloudCIXAuth("dylan.buckley@mohago.com", "Capslock1", "11", null);
//		System.out.println(credentials.get_auth_ref().toJSONString());
//		Response response = cli.keystone.create(credentials);
//		System.out.println(response.code + " " + response.message);
//		System.out.println("Body: " + (response.body.toJSONString()));
//		String user_token = "";
//		JSONObject token = null;
//		if (response.code == 201) {
//			token = (JSONObject) response.body.get("token");
//			JSONObject user = (JSONObject) token.get("user");
//			System.out.println("idAdress: " + user.get("idAddress"));
//			user_token = response.headers.get("X-Subject-Token").get(0);
//			System.out.println("X-Subject-Token: " + user_token);
//		}
		Membership mem = new Membership();
		Map a = new HashMap<String, String>();
		a.put("fields", "(idMember)");
		
		Response resp = mem.getMember().read("e76cb8ad0c41402a98e716b33dd4e611", null, null, a, null, null);
		JSONObject body = resp.body;
		JSONArray idList = (JSONArray) body.get("content");
		HashSet<Long> idMemberSet = new HashSet<Long>();
		for(Object entry : idList){
			idMemberSet.add((Long)((JSONObject)entry).get("idMember"));
		}
		System.out.print(resp.body.toJSONString());
	}
	
	@Test
	public void test_keystone() throws SocketTimeoutException {
		System.out.println("Starting keystone tests.");
		System.out.println("Getting User token.");
		Auth cli = new Auth();
		CloudCIXAuth credentials = new CloudCIXAuth("dylan.buckley@mohago.com", "Capslock1", "11", null);
		System.out.println(credentials.get_auth_ref().toJSONString());
		Response response = cli.keystone.create(credentials);
		System.out.println(response.code + " " + response.message);
		System.out.println("Body: " + (response.body.toJSONString()));
		String user_token = "";
		if (response.code == 201) {
			JSONObject token = (JSONObject) response.body.get("token");
			JSONObject user = (JSONObject) token.get("user");
			System.out.println("idAdress: " + user.get("idAddress"));
			user_token = response.headers.get("X-Subject-Token").get(0);
			System.out.println("X-Subject-Token: " + user_token);
		}
		
		System.out.println("Getting Admin Token.");
		String admin_token = utilities.get_admin_token();
		System.out.println(admin_token);
		
		System.out.println("Reading the User token.");
		response = cli.keystone.read(admin_token, user_token);
		System.out.println(response.code + " " + response.message);
		System.out.println("Body: " + (response.body.toJSONString()));
		
		System.out.println("Deleting the User token");
		response = cli.keystone.delete(admin_token, user_token);
		System.out.println(response.code + " " + response.message);

		System.out.println("Creating scoped token by domain.");
		credentials.scope_domain("1");
		System.out.println(credentials.get_auth_ref().toJSONString());
		response = cli.keystone.create(credentials);
		System.out.println(response.code + " " + response.message);
		System.out.println("Body: " + (response.body.toJSONString()));
		user_token = "";
		if (response.code == 201) {
			JSONObject token = (JSONObject) response.body.get("token");
			JSONObject user = (JSONObject) token.get("user");
			System.out.println("idAdress: " + user.get("idAddress"));
			user_token = response.headers.get("X-Subject-Token").get(0);
			System.out.println("X-Subject-Token: " + user_token);
		}
		
		System.out.println("Deleting the User token");
		response = cli.keystone.delete(admin_token, user_token);
		System.out.println(response.code + " " + response.message);

		System.out.println("Creating scoped token by project.");
		credentials.scope_project("1");
		System.out.println(credentials.get_auth_ref().toJSONString());
		response = cli.keystone.create(credentials);
		System.out.println(response.code + " " + response.message);
		System.out.println("Body: " + (response.body.toJSONString()));
		user_token = "";
		if (response.code == 201) {
			JSONObject token = (JSONObject) response.body.get("token");
			JSONObject user = (JSONObject) token.get("user");
			System.out.println("idAdress: " + user.get("idAddress"));
			user_token = response.headers.get("X-Subject-Token").get(0);
			System.out.println("X-Subject-Token: " + user_token);
		}
		
		System.out.println("Deleting the User token");
		response = cli.keystone.delete(admin_token, user_token);
		System.out.println(response.code + " " + response.message);

		System.out.println("keystone tests finished.");
	}
	
	@Test
	public void test_language_list() throws SocketTimeoutException {
		System.out.println("Starting test language list.");
		
		Auth auth_cli = new Auth();
		CloudCIXAuth credentials = new CloudCIXAuth("user@mail.com", "super53cr3t", "yourIdMember", null);
		Response auth_response = auth_cli.keystone.create(credentials);
		String token = "";
		
		if (auth_response.code != 201) {
			System.out.println(auth_response.body.toJSONString());
			System.exit(1);
		} else {
			Map<String, String> query_params = new HashMap<String, String>();
			query_params.put("fields", "(languageCode)");
			
			token = auth_response.headers.get("X-Subject-Token").get(0);
			
			Membership cli = new Membership();
	
			Response response = cli.language.list(token, null, query_params, null, null);
			
			System.out.println(response.body.toJSONString());
		}
	}
	
	@Test
	public void hello_world() throws SocketTimeoutException {
		System.out.println("Starting hello world sample.");
		
		System.out.println("Getting admin token.");
		String admin_token = utilities.get_admin_token();
		System.out.println("Admin token: " + admin_token);
		
		System.out.println("Getting user token.");
		String user_token = "";
		Auth auth_client = new Auth();
		CloudCIXAuth credentials = new CloudCIXAuth("user@mail.com", "super53cr3t", "yourIdMember", null);
		Response response = auth_client.keystone.create(credentials);
		if (response.code != 201) {
			System.out.println(response.body.toJSONString());
			System.exit(1);
		} else {
			user_token = response.headers.get("X-Subject-Token").get(0);
		}
		System.out.println("User token: " + user_token);
		
		System.out.println("Validate the token.");
		response = auth_client.keystone.read(admin_token, user_token);
		if (response.code != 200) {
			System.out.println(response.body.toJSONString());
			System.exit(1);
		}
		System.out.println("Token validated.");
		
		System.out.println("Read the address.");
		JSONObject token_data = (JSONObject) response.body.get("token");
		JSONObject user_data = (JSONObject) token_data.get("user");
		String idAddress = user_data.get("idAddress").toString();
		Membership membership_client = new Membership();
		response = membership_client.address.read(user_token, idAddress, null, null, null, null);
		if (response.code != 200) {
			System.out.println(response.body.toJSONString());
			System.exit(1);
		}
		System.out.println("Address data: " + response.body.toJSONString());
		
		System.out.println("Create a department.");
		String idMember = user_data.get("idMember").toString();
		JSONObject request = new JSONObject();
		request.put("name", "test_create_department");
		Map<String, String> path_params = new HashMap<String, String>();
		path_params.put("idMember", idMember);
		response = membership_client.department.create(user_token, request, null, path_params, null);
		if (response.code != 201) {
			System.out.println(response.body.toJSONString());
			System.exit(1);
		}
		System.out.println("Department data: " + response.body.toJSONString());
		
		System.out.println("Update a department.");
		JSONObject content = (JSONObject) response.body.get("content");
		String idDepartment = content.get("idDepartment").toString();
		request = new JSONObject();
		request.put("name", "test_update_department.");
		response = membership_client.department.update(user_token, idDepartment, request, null, path_params, null);
		if (response.code != 200 && response.code != 204) {
			System.out.println(response.body.toJSONString());
			System.exit(1);
		}
		if (response.code != 204) System.out.println("Department data: " + response.body.toJSONString());
		
		System.out.println("Read a department.");
		response = membership_client.department.read(user_token, idDepartment, null, null, path_params, null);
		if (response.code != 200) {
			System.out.println(response.body.toJSONString());
			System.exit(1);
		}
		System.out.println("Department data: " + response.body.toJSONString());
		
		System.out.println("List departments.");
		response = membership_client.department.list(user_token, null, null, path_params, null);
		if (response.code != 200) {
			System.out.println(response.body.toJSONString());
			System.exit(1);
		}
		System.out.println("Departments data: " + response.body.toJSONString());
		
		System.out.println("Delete department.");
		response = membership_client.department.delete(user_token, idDepartment, null, null, path_params, null);
		if (response.code != 204) {
			System.out.println(response.body.toJSONString());
			System.exit(1);
		}
		System.out.println("Checking it was deleted.");
		response = membership_client.department.read(user_token, idDepartment, null, null, path_params, null);
		if (response.code != 404) {
			System.out.println(response.body.toJSONString());
			System.exit(1);
		}
		System.out.println("Response:" + response.body.toJSONString());
		
		System.out.println("Delete user token.");
		response = auth_client.keystone.delete(admin_token, user_token);
		if (response.code != 204) {
			System.out.println(response.body.toJSONString());
			System.exit(1);
		}
		System.out.println("Checking it was deleted.");
		response = auth_client.keystone.read(admin_token, user_token);
		if (response.code != 404) {
			System.out.println(response.body.toJSONString());
			System.exit(1);
		}
		System.out.println("Token was deleted.");
		
		System.out.println("Hello world sample finished.");
	}
}
