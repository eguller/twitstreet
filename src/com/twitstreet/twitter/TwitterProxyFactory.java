package com.twitstreet.twitter;

import com.google.inject.assistedinject.Assisted;

public interface TwitterProxyFactory {
	public TwitterProxy create(@Assisted("ouathToken") String ouathToken,
			@Assisted("oauthTokenSecret") String oauthTokenSecret);
}
