package cloudcix.api;

import cloudcix.base.APIClient;
import cloudcix.base.Utilities;

public class Contacts {
	String _application_name = "Contacts";
	
	/**
	 * Client for Contact Campaign service.
	 */
	public APIClient campaign = new APIClient(_application_name, "Campaign/", Utilities.getContextPath());
	/**
	 * Client for Contact Group service.
	 */
	public APIClient group = new APIClient(_application_name, "Group/", Utilities.getContextPath());
	/**
	 * Client for Contact Contact service.
	 */
	public APIClient contact = new APIClient(_application_name, "Contact/", Utilities.getContextPath());
	/**
	 * Client for Contact Campaign Contact service.
	 */
	public APIClient campaign_contact = new APIClient(_application_name, "Campaign/{idCampaign}/Contact/", Utilities.getContextPath());
	/**
	 * Client for Contact Group Contact service.
	 */
	public APIClient group_contact = new APIClient(_application_name, "Group/{idGroup}/Contact/", Utilities.getContextPath());

	public Contacts() {
		
	}

	public APIClient getCampaign() {
		return campaign;
	}

	public APIClient getGroup() {
		return group;
	}

	public APIClient getContact() {
		return contact;
	}

	public APIClient getCampaign_contact() {
		return campaign_contact;
	}

	public APIClient getGroup_contact() {
		return group_contact;
	}

}
