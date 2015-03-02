/**
 * Runs through some samples on how to use CloudCIX JAVA SDK.
 */
import java.util.Map;
import java.util.HashMap;

import org.json.simple.JSONObject;

import cloudcix.base.CloudCIXAuth;
import cloudcix.base.Response;
import cloudcix.base.Utilities;
import cloudcix.api.Auth;
import cloudcix.api.Membership;

/**
 * @author CloudCIX developers
 *
 */
public class HelloWorld {

	public static void main(String[] args) {
		System.out.println("Starting hello world sample.");
		
		System.out.println("Getting admin token.");
		Utilities utilities = new Utilities();
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
