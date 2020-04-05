package com.simon.async.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.simon.async.AsyncTask;
import com.simon.async.AsyncTaskHandler;
import com.simon.async.FastFailCountDownLatch;

/**
 * 异步任务测试
 */
public class AsyncTaskTest {

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		AsyncTaskHandler<String> asyncTaskHandler = new AsyncTaskHandler<String>();
		List<Future<String>> futureList = new ArrayList<Future<String>>(10);

		FastFailCountDownLatch latch = new FastFailCountDownLatch(10);
		final BizHandler bizHandler = new BizHandler();

		for (int i = 0; i < 10; i++) {
			Future<String> f = asyncTaskHandler.handle(new AsyncTask<String>() {
				public String execute() {
					try {
						return bizHandler.mockSlowHandling();
					} catch (Exception e) {
						latch.occurException(e);
						throw e;
					} finally {
						latch.countDown();
					}
				}
			}, 30, TimeUnit.SECONDS);
			futureList.add(f);
		}

		try {
			latch.await();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		if (latch.existsException()) {
			System.out.println("has exception");
			//throw new RuntimeException("occur exception.");// tx rollback
		}

		for (Future<String> future : futureList) {
			try {
				System.out.println(future.get());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		System.out.println("over");
		long end = System.currentTimeMillis();
		System.out.println("waste time: " + (end - start));
		asyncTaskHandler.shutdown();
	}

}
