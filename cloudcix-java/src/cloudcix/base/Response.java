package cloudcix.base;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

public class Response {
	
	public Map<String, List<String>> headers;
	public int code;
	public String message;
	public JSONObject body;

	public Response() {
		
	}
	
	public Response(Map<String, List<String>> start_headers, int start_code, String start_message, JSONObject start_body) {
		headers = start_headers;
		code = start_code;
		message = start_message;
		body = start_body;
	}
}
