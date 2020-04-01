package com.simon.async;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 异步任务处理器
 * @param <T>
 */
public class AsyncTaskHandler<T> {

	// private static final ForkJoinPool EXECUTOR = new ForkJoinPool(20);

	// 线程池里有很多线程需要同时执行，旧的可用线程将被新的任务触发重新执行，
	// 如果线程超过60秒内没执行，那么将被终止并从池中删除
	private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();

    private static final Object PRESENT = new Object();

	private static final long DEFAULT_TIMEOUT = 60;// 默认超时时间: 60秒

	private static final TimeUnit DEFAULT_TIMEOUT_UNIT = TimeUnit.SECONDS;

	private TimeCache<Future<T>, Object> timeCache = new TimeCache<Future<T>, Object>();

	public Future<T> handle(AsyncTask<T> task) {
		return handle(task, DEFAULT_TIMEOUT, DEFAULT_TIMEOUT_UNIT);
	}

	public Future<T> handle(AsyncTask<T> task, long timeout, TimeUnit timeUnit) {
		Future<T> future = new DelegateFuture<T>(EXECUTOR.submit(task));
		timeCache.put(future, PRESENT, timeout, timeUnit);
		return future;
	}

}