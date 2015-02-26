# JAVA SDK for CloudCIX API #

A JAVA SDK implementation to make the work with CloudCIX API fun.

API Docs: http://docs.cloudcix.com

CloudCIX is developed by CIX: http://www.cix.ie

# Installation #

In order to install it you must download the source code and compile it.
Then add it to your build path in your project.

# Dependencies

This SDK uses json-simple to handle JSON request and response objects.
It can be downloaded in the following link:
https://code.google.com/p/json-simple/

In order to run the unit test in the source code the JUnit library is used.
It can be downloaded in the following link:
https://github.com/junit-team/junit/wiki/Download-and-Install

Those libraries are not included in the repository. You must add them
to your build path on the source project.

# Required settings #

When you run your project you should set the settings variable 
in the root of your application in a file called cloudcix_setting.properties

Required `CLOUDCIX` and `OPENSTACK` settings


    CLOUDCIX_SERVER_URL = https://api.cloudcix.com
    CLOUDCIX_API_USERNAME = user@cloudcix.com
    CLOUDCIX_API_PASSWORD = super53cr3t3
    CLOUDCIX_API_ID_MEMBER = 2243
    OPENSTACK_KEYSTONE_URL = http://keystone.cloudcix.com:5000/v3

# Considerations about Response

The Response object contains the items returned by a HTTP response:
code      An int value containing the HTTP response code.
message   A String value containing the HTTP response code message.
headers   A Map<String, List<String>> object containig the headers
	  returned by the service as JAVA gets them.
body	  A JSONObject containing the body response of the service.

Notice that the body will contain plain basic items as other JSONObject.
In order to get them properly they need to be casted.

Given the following body:
    
    {
    	"token":{
    		"methods":["cloudcix_auth"],
    		"issued_at":"2015-02-26T10:59:44.442140Z",
    		"expires_at":"2015-02-26T11:59:44.442109Z",
    		"audit_ids":["7KFpicgLQc2fXRT4BOVQPQ"],
    		"user":{
    			"startDate":"2011-03-27T00:00:00.000000",
    			"idLanguage":3,
    			"globalUser":true,
    			"department":{
    				"name":"PerSOA",
    				"idDepartment":18
    			},
    			"idMember":1,
    			...
    			...
    		}
    	}
    }

token contains an object so users must do:

    //Assume Response variable is named "response"
    JSONObject token = (JSONObject) response.body.get("token");

Now if you want the user:

    JSONObject user = (JSONObject) token.get("user");

And if you want the department object:

    JSONObject department = (JSONObject) user.get("department");

And if you want a plain string or integer, like in this case, the department name:

    String department_name = department.get("name").toString();

# Sample usage #

    /**
     * Runs through some samples on how to use CloudCIX JAVA SDK.
     */
    import java.util.Map;
    import java.util.HashMap;
    
    import org.json.simple.JSONObject;
    
    import cloudcix.base.CloudCIXAuth;
    import cloudcix.base.Response;
    import cloudcix.base.utils;
    
    import cloudcix.api.auth;
    import cloudcix.api.membership;
    
    /**
     * @author CloudCIX developers
     *
     */
    public class HelloWorld {
    
	    public static void main(String[] args) {
	    	System.out.println("Starting hello world sample.");
	    	
		//Get an administrative token.
	    	System.out.println("Getting admin token.");
	    	utils utilities = new utils();
    		String admin_token = utilities.get_api_token();
    		System.out.println("Admin token: " + admin_token);
    		
		//Get an user token, don't forget to put proper credentials here!
    		System.out.println("Getting user token.");
    		String user_token = "";
    		auth auth_client = new auth();
    		CloudCIXAuth credentials = new CloudCIXAuth("user@mail.com", "super53cr3t", "yourIdMember", null);
    		Response response = auth_client.keystone.create(credentials);
    		if (response.code != 201) {
    			System.out.println(response.body.toJSONString());
    			System.exit(1);
    		} else {
    			user_token = response.headers.get("X-Subject-Token").get(0);
    		}
    		System.out.println("User token: " + user_token);
    		
		//Validate a token
    		System.out.println("Validate the token.");
    		response = auth_client.keystone.read(admin_token, user_token);
    		if (response.code != 200) {
    			System.out.println(response.body.toJSONString());
    			System.exit(1);
    		}
    		System.out.println("Token validated.");
    		
		//Read an address
    		System.out.println("Read the address.");
    		JSONObject token_data = (JSONObject) response.body.get("token");
    		JSONObject user_data = (JSONObject) token_data.get("user");
    		String idAddress = user_data.get("idAddress").toString();
    		membership membership_client = new membership();
    		response = membership_client.address.read(user_token, idAddress, null, null, null, null);
    		if (response.code != 200) {
    			System.out.println(response.body.toJSONString());
    			System.exit(1);
    		}
    		System.out.println("Address data: " + response.body.toJSONString());
    		
		//Create a department
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
    		
		//Update a department
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
    		
		//Read a department
    		System.out.println("Read a department.");
    		response = membership_client.department.read(user_token, idDepartment, null, null, path_params, null);
    		if (response.code != 200) {
    			System.out.println(response.body.toJSONString());
    			System.exit(1);
    		}
    		System.out.println("Department data: " + response.body.toJSONString());
    		
		List departments
    		System.out.println("List departments.");
    		response = membership_client.department.list(user_token, null, null, path_params, null);
    		if (response.code != 200) {
    			System.out.println(response.body.toJSONString());
    			System.exit(1);
    		}
    		System.out.println("Departments data: " + response.body.toJSONString());
    		
		//Delete a department
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
    		
		//Delete an user token
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

