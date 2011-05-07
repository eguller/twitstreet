package com.twitstreet.base;

/**
 * A wrapper for return types similar to Option in scala.<br>
 * Prefer returning Result&lt;Object&gt; instead of void functions.<br>
 * Avoid success(null) for non-void functions.<br>
 * Avoid fail(null) for all functions.
 * @author ooktay
 *
 * @param <T> The type of payload.
 */
public final class Result<T> {
	private final T payload;
	private final boolean isSuccessful;
	private final Enum<?> errorCode;

	public final boolean isSuccessful() {
		return isSuccessful;
	}

	public final boolean isFailed() {
		return !isSuccessful;
	}
	
	public final Enum<?> getErrorCode() {
		return errorCode;
	}
	
	public final T getPayload() {
		return payload;
	}

	private Result(boolean isSuccessful, T payload, Enum<?> errorCode) {
		this.payload = payload;
		this.isSuccessful = isSuccessful;
		this.errorCode = errorCode;
	}
	
	/**
	 * create a success Result with payload.
	 * @param <T>
	 * @param payload
	 * @return
	 */
	public static <T> Result<T> success(T payload) {
		return new Result<T>(true, payload, null);
	}

	/**
	 * create a Result failed with errorCode.
	 * @param <T>
	 * @param errorCode
	 * @return
	 */
	public static <T> Result<T> fail(Enum<?> errorCode) {
		return new Result<T>(false, null, errorCode);
	}
	
	/**
	 * Convert the type of a failed Result.<br>
	 * Important: This wont work for successful Results.<p>
	 * Sometimes a function func1() calls another function func2() 
	 * and we want func1 to return the same error as func2 when func2 fails.
	 * If return types are different we can use <code>fail(Result&lt;?&gt;)</code>
	 * to convert the type.<p>
	 * Just doing a type cast works since java generic types are "erased" during compile.
	 * @param <T>
	 * @param result to convert
	 * @return result
	 */
	@SuppressWarnings("unchecked")
	public static <T> Result<T> fail(Result<?> result) {
		assert(!result.isSuccessful);
		return (Result<T>)result;
	}
}
