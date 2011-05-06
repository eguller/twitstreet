package com.twitstreet.base;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Semaphore;

public final class KeyLock<T> {
	
	private final ConcurrentMap<T, Semaphore> lockMap;
	
	public KeyLock() {
		lockMap = new ConcurrentHashMap<T, Semaphore>();
	}

	public final boolean tryLock(T key) {
		return lockMap.putIfAbsent(key, new Semaphore(0, true)) == null;
	}

	public final void waitAndlock(T key) {
		for(;;) {
			Semaphore lock = lockMap.putIfAbsent(key, new Semaphore(0, true));
			if(lock==null) {
				break;
			}
			
			try {
				lock.acquire();
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public final void unlock(T key) {
		Semaphore lock = lockMap.remove(key);
		if(lock!=null) {
			lock.release();
		}
	}
}
