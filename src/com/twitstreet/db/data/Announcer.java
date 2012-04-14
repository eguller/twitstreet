package com.twitstreet.db.data;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Announcer implements DataObjectIF {
	
	long id;
	String name;
	String consumerKey;
	String consumerSecret;
	String accessToken;
	String accessTokenSecret;

	@Override
	public void getDataFromResultSet(ResultSet rs) throws SQLException {
		this.setId(rs.getLong("id"));
		this.setName(rs.getString("name"));
		this.setConsumerKey(rs.getString("consumerKey"));
		this.setConsumerSecret(rs.getString("consumerSecret"));
		this.setAccessToken(rs.getString("accessToken"));
		this.setAccessTokenSecret(rs.getString("accessTokenSecret"));
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getConsumerKey() {
		return consumerKey;
	}

	public void setConsumerKey(String consumerKey) {
		this.consumerKey = consumerKey;
	}

	public String getConsumerSecret() {
		return consumerSecret;
	}

	public void setConsumerSecret(String consumerSecret) {
		this.consumerSecret = consumerSecret;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getAccessTokenSecret() {
		return accessTokenSecret;
	}

	public void setAccessTokenSecret(String accessTokenSecret) {
		this.accessTokenSecret = accessTokenSecret;
	}

}
