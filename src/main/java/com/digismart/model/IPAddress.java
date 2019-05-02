package com.digismart.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "ipaddress_master")
public class IPAddress {

	private String id;
	private String ipaddress;
	private String domain_id;
	private String type;
	private boolean is_deleted;
	private boolean status;
	private String host_name;
	private long max_volume_engagement;
	private long max_volume_promo;
	private long maximumVolume;
	private String reply_email;
	@Field("from_email_id")
	private String from_email;
	private int port;
	private String infra;
	
	private long count;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public String getDomain_id() {
		return domain_id;
	}

	public void setDomain_id(String domain_id) {
		this.domain_id = domain_id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isIs_deleted() {
		return is_deleted;
	}

	public void setIs_deleted(boolean is_deleted) {
		this.is_deleted = is_deleted;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public long getMax_volume_engagement() {
		return max_volume_engagement;
	}

	public void setMax_volume_engagement(long max_volume_engagement) {
		this.max_volume_engagement = max_volume_engagement;
	}

	public long getMax_volume_promo() {
		return max_volume_promo;
	}

	public void setMax_volume_promo(long max_volume_promo) {
		this.max_volume_promo = max_volume_promo;
	}

	public long getMaximumVolume() {
		return maximumVolume;
	}

	public void setMaximumVolume(long maximumVolume) {
		this.maximumVolume = maximumVolume;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public String getHost_name() {
		return host_name;
	}

	public void setHost_name(String host_name) {
		this.host_name = host_name;
	}

	public String getReply_email() {
		return reply_email;
	}

	public void setReply_email(String reply_email) {
		this.reply_email = reply_email;
	}

	public String getFrom_email() {
		return from_email;
	}

	public void setFrom_email(String from_email) {
		this.from_email = from_email;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	

	public String getInfra() {
		return infra;
	}

	public void setInfra(String infra) {
		this.infra = infra;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ipaddress == null) ? 0 : ipaddress.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IPAddress other = (IPAddress) obj;
		if (ipaddress == null) {
			if (other.ipaddress != null)
				return false;
		} else if (!ipaddress.equals(other.ipaddress))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("IPAddress [id=");
		builder.append(id);
		builder.append(", ipaddress=");
		builder.append(ipaddress);
		builder.append(", domain_id=");
		builder.append(domain_id);
		builder.append(", type=");
		builder.append(type);
		builder.append(", is_deleted=");
		builder.append(is_deleted);
		builder.append(", status=");
		builder.append(status);
		builder.append(", host_name=");
		builder.append(host_name);
		builder.append(", max_volume_engagement=");
		builder.append(max_volume_engagement);
		builder.append(", max_volume_promo=");
		builder.append(max_volume_promo);
		builder.append(", maximumVolume=");
		builder.append(maximumVolume);
		builder.append(", reply_email=");
		builder.append(reply_email);
		builder.append(", from_email=");
		builder.append(from_email);
		builder.append(", port=");
		builder.append(port);
		builder.append(", count=");
		builder.append(count);
		builder.append("]");
		return builder.toString();
	}

}
