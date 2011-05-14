package com.twitstreet.twitter;

import java.security.MessageDigest;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.twitstreet.base.Result;

public class TwitterAnywhereImpl implements TwitterAnywhere {

	@Inject
	@Named("com.twitstreet.meta.ConsumerSecret")
	private final String consumerSecret = null;

	@Override
	public Result<String> getUserIdFromTACookie(String value) {
		// parse cookie "user_id:signature"
		int idx = value.indexOf(':');
		if (idx > 0) {
			String userid = value.substring(0, idx);
			String signature = value.substring(idx + 1, value.length());
			try {
				// SHA1.hexdigest(user_id + consumer_secret)
				MessageDigest md = MessageDigest.getInstance("SHA-1");
				md.update(userid.getBytes());
				md.update(consumerSecret.getBytes());
				byte[] digest = md.digest();

				StringBuilder hexdigest = new StringBuilder();
				for (int i = 0; i < digest.length; i++) {
					int digByte = digest[i] & 0xFF;
					if (digByte < 0x10) {
						hexdigest.append('0');
					}
					hexdigest.append(Integer.toHexString(digByte));
				}

				if (signature.equals(hexdigest.toString())) {
					return Result.success(userid);
					
				} else {
					return Result.fail(TwitterError.CookieCheckFailed);
				}

			} catch (Exception e) {
				return Result.fail(e);
			}

		} else {
			return Result.fail(TwitterError.CookieCheckFailed);
		}

	}

}
