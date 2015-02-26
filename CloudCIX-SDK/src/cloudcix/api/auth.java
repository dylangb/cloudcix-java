package cloudcix.api;

import cloudcix.base.KeystoneClient;

public class auth {

	/**
	 * Client for Keystone
	 */
	public KeystoneClient keystone = new KeystoneClient("/auth/tokens");

	public auth() {
		
	}

	public KeystoneClient getKeystone() {
		return keystone;
	}
}
