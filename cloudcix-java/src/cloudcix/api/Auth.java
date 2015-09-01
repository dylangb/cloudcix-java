package cloudcix.api;

import cloudcix.base.KeystoneClient;
import cloudcix.base.Utilities;

public class Auth {

	/**
	 * Client for Keystone 
	 */
	public static final KeystoneClient keystone;
	static{
		keystone= new KeystoneClient("/auth/tokens", Utilities.getContextPath());
	}

	public Auth() {
		
	}

	public static KeystoneClient getKeystone() {
		return keystone;
	}
}
