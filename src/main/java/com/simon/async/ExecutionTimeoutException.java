package com.simon.async;

/**
 * 执行超时异常
 */
public class ExecutionTimeoutException extends RuntimeException {
	private static final long serialVersionUID = -3697051242734276916L;

	public ExecutionTimeoutException() {}

	public ExecutionTimeoutException(String message) {
		super(message);
	}

}