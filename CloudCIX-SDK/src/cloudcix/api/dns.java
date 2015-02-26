package cloudcix.api;

import cloudcix.base.APIClient;

public class dns {
	String _application_name = "DNS";
	
	/**
	 * Client for DNS ASN service.
	 */
	public APIClient asn = new APIClient(_application_name, "ASN/");
	/**
	 * Client for DNS Allocation service.
	 */
	public APIClient allocation = new APIClient(_application_name, "Allocation/");
	/**
	 * Client for DNS Subnet service.
	 */
	public APIClient subnet = new APIClient(_application_name, "Subnet/");
	/**
	 * Client for DNS Subnet Space service.
	 */
	public APIClient subnet_space = new APIClient(_application_name, "Allocation/{idAllocation}/Subnet_space/");
	/**
	 * Client for DNS IPAddress service.
	 */
	public APIClient ipaddress = new APIClient(_application_name, "IPAddress/");
	/**
	 * Client for DNS Record PTR service.
	 */
	public APIClient recordptr = new APIClient(_application_name, "RecordPTR/");
	/**
	 * Client for DNS Domain service.
	 */
	public APIClient domain = new APIClient(_application_name, "Domain/");
	/**
	 * Client for DNS Record service.
	 */
	public APIClient record = new APIClient(_application_name, "Record/");
	/**
	 * Client for DNS Blacklist service.
	 */
	public APIClient blacklist = new APIClient(_application_name, "Blacklist/");

	public dns() {
		
	}

	public APIClient getAsn() {
		return asn;
	}

	public APIClient getAllocation() {
		return allocation;
	}

	public APIClient getSubnet() {
		return subnet;
	}

	public APIClient getSubnet_space() {
		return subnet_space;
	}

	public APIClient getIpaddress() {
		return ipaddress;
	}

	public APIClient getRecordptr() {
		return recordptr;
	}

	public APIClient getDomain() {
		return domain;
	}

	public APIClient getRecord() {
		return record;
	}

	public APIClient getBlacklist() {
		return blacklist;
	}

}
