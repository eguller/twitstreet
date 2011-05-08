package com.twitstreet.quote.api;

import com.twitstreet.quote.data.FollowerQuoteResultData;

public interface FollowerQuote {
	public FollowerQuoteResultData getFollowerQuote(String twitteUser);
}
