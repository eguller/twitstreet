package com.twitstreet.twitter;

import com.twitstreet.base.Result;

public interface TwitterAnywhere {
	
	Result<String> getUserIdFromTACookie(String value);

}
