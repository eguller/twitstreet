package com.twitstreet.base;

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
	
	public static <T> Result<T> success(T payload) {
		return new Result<T>(true, payload, null);
	}
	
	public static <T> Result<T> fail(Enum<?> errorCode) {
		return new Result<T>(false, null, errorCode);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Result<T> fail(Result<?> result) {
		return (Result<T>)result;
	}
}
