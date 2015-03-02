package cloudcix.api;

import cloudcix.base.KeystoneClient;

public class Auth {

	/**
	 * Client for Keystone
	 */
	public KeystoneClient keystone = new KeystoneClient("/auth/tokens");

	public Auth() {
		
	}

	public KeystoneClient getKeystone() {
		return keystone;
	}
}
