package com.tony.volleydemo.http.tool;

import com.tony.volleydemo.http.core.Cache;

/**
 * @author Tony E-mail:solaris0403@gmail.com
 * @version Create Data：Aug 10, 2015 8:45:04 AM
 */
public class NoCache implements Cache {

	@Override
	public Entry get(String key) {
		return null;
	}

	@Override
	public void put(String key, Entry entry) {
	}

	@Override
	public void initialize() {
	}

	@Override
	public void invalidate(String key, boolean fullExpire) {
	}

	@Override
	public void remove(String key) {
	}

	@Override
	public void clear() {
	}
}
