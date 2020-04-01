package com.simon.async;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 委托Future
 * @param <T>
 */
public class DelegateFuture<T> implements Future<T> {

	private Future<T> delegate;

	public DelegateFuture(Future<T> delegate) {
		this.delegate = delegate;
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return delegate.cancel(mayInterruptIfRunning);
	}

	@Override
	public boolean isCancelled() {
		return delegate.isCancelled();
	}

	@Override
	public boolean isDone() {
		return delegate.isDone();
	}

	@Override
	public T get() throws InterruptedException, ExecutionException {
		try {
			return delegate.get();
		} catch (CancellationException e) {
			if (isDone() && isCancelled()) {// 中途取消
				throw new ExecutionTimeoutException("execution timeout.");
			}
		}
		return null;
	}

	@Override
	public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		return delegate.get(timeout, unit);
	}

}
