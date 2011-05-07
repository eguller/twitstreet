package com.twitstreet.base;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Semaphore;

/**
 * Provides "keyed" locks of type T. Basically you can lock or unlock a key. For
 * locking, you have the option to wait until the key is available:
 * <code>waitAndlock(T key)</code>, or not to wait: <code>tryLock(T key)</code>.<p>
 * As a best practice, "locked" code should be in a try block immediately
 *  following the lock(), and unlock() should be in the finally block.<br>
 * @author ooktay
 *
 * @param <T> Type of Key
 */
public final class KeyLock<T> {
	
	private final ConcurrentMap<T, Semaphore> lockMap;
	
	public KeyLock() {
		lockMap = new ConcurrentHashMap<T, Semaphore>();
	}

	/**
	 * Try to lock the key.
	 * @param key to lock
	 * @return true if lock was acquired, false if key was already locked.
	 */
	public final boolean tryLock(T key) {
		return lockMap.putIfAbsent(key, new Semaphore(0, true)) == null;
	}

	/**
	 * Waits until the key is available and locks the key.<br>
	 * Note: If the thread is interrupted, the method will throw a RuntimeException.
	 * @param key to lock
	 */
	public final void waitAndlock(T key) {
		for(;;) {
			Semaphore lock = lockMap.putIfAbsent(key, new Semaphore(0, true));
			if(lock==null) {
				break;
			}
			
			try {
				lock.acquire();
				
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Unlocks the key if it is locked.
	 * @param key to unlock
	 */
	public final void unlock(T key) {
		Semaphore lock = lockMap.remove(key);
		if(lock!=null) {
			lock.release();
		}
	}
}
