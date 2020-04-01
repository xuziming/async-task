package com.simon.async;

import java.util.concurrent.Callable;

/**
 * 异步任务
 * @param <T>
 */
public abstract class AsyncTask<T> implements Callable<T> {

	@Override
	public T call() throws Exception {
		return execute();
	}

	protected abstract T execute();

}
