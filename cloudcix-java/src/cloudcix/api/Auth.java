package cloudcix.api;

import cloudcix.base.KeystoneClient;
import cloudcix.base.Utilities;

public class Auth {

	/**
	 * Client for Keystone 
	 */
	public KeystoneClient keystone = new KeystoneClient("/auth/tokens", Utilities.getContextPath());

	public Auth() {
		
	}

	public KeystoneClient getKeystone() {
		return keystone;
	}
}
