package cloudcix.api;

import cloudcix.base.APIClient;

public class Membership {
	String _application_name = "Membership";
	
	/**
	 * Client for Membership Address service.
	 */
	public APIClient address = new APIClient(_application_name, "Address/");
	/**
	 * Client for Membership Address Link service.
	 */
	public APIClient address_link = new APIClient(_application_name, "Address/{idAddress}/Link/");
	/**
	 * Client for Membership Country service.
	 */
	public APIClient country = new APIClient(_application_name, "Country/");
	/**
	 * Client for Membership Currency service.
	 */
	public APIClient currency = new APIClient(_application_name, "Currency/");
	/**
	 * Client for Membership Department service.
	 */
	public APIClient department = new APIClient(_application_name, "Member/{idMember}/Department/");
	/**
	 * Client for Membership Language service.
	 */
	public APIClient language = new APIClient(_application_name, "Language/");
	/**
	 * Client for Membership Member service.
	 */
	public APIClient member = new APIClient(_application_name, "Member/");
	/**
	 * Client for Membership Member Link service.
	 */
	public APIClient member_link = new APIClient(_application_name, "Member/{idMember}/Link/");
	/**
	 * Client for Membership Notification service.
	 */
	public APIClient notification = new APIClient(_application_name, "Address/{idAddress}/Notification/");
	/**
	 * Client for Membership Profile service.
	 */
	public APIClient profile = new APIClient(_application_name, "Member/{idMember}/Profile/");
	/**
	 * Client for Membership Subdivision service.
	 */
	public APIClient subdivision = new APIClient(_application_name, "Country/{idCountry}/Subdivision/");
	/**
	 * Client for Membership Team service.
	 */
	public APIClient team = new APIClient(_application_name, "Member/{idMember}/Team/");
	/**
	 * Client for Membership Territory service.
	 */
	public APIClient territory = new APIClient(_application_name, "Member/{idMember}/Territory/");
	/**
	 * Client for Membership Timezone service.
	 */
	public APIClient timezone = new APIClient(_application_name, "Timezone/");
	/**
	 * Client for Membership Transaction Type service.
	 */
	public APIClient transaction_type = new APIClient(_application_name, "TransactionType/");
	/**
	 * Client for Membership User service.
	 */
	public APIClient user = new APIClient(_application_name, "User/");
	
	public Membership() {
		
	}

	public APIClient getAddress() {
		return address;
	}

	public APIClient getAddress_link() {
		return address_link;
	}

	public APIClient getCountry() {
		return country;
	}

	public APIClient getCurrency() {
		return currency;
	}

	public APIClient getDepartment() {
		return department;
	}

	public APIClient getLanguage() {
		return language;
	}

	public APIClient getMember() {
		return member;
	}

	public APIClient getMember_link() {
		return member_link;
	}

	public APIClient getNotification() {
		return notification;
	}

	public APIClient getProfile() {
		return profile;
	}

	public APIClient getSubdivision() {
		return subdivision;
	}

	public APIClient getTeam() {
		return team;
	}

	public APIClient getTerritory() {
		return territory;
	}

	public APIClient getTimezone() {
		return timezone;
	}

	public APIClient getTransaction_type() {
		return transaction_type;
	}

	public APIClient getUser() {
		return user;
	}
	
}
