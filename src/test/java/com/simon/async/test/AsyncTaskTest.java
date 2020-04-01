package com.simon.async.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
		for (int i = 0; i < 10; i++) {
			Future<String> f = asyncTaskHandler.handle(new AsyncTask<String>() {
				public String execute() {
					try {
						return testSlowHttpRequest();
					} catch (Exception e) {
						latch.occurException(e);
						throw e;
					} finally {
						latch.countDown();
					}
				}
			}, 5, TimeUnit.SECONDS);
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
	}

	@SuppressWarnings("unused")
	private void testAsync() {
		long startTime = System.currentTimeMillis();
		List<Integer> locations = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
		locations.parallelStream().map(location -> {
			return testSlowHttpRequest();
		}).collect(Collectors.toList());
		long endTime = System.currentTimeMillis();
		// System.out.println("waste time: " + (endTime - startTime));
	}

	private static String testSlowHttpRequest() {
		try {
			long longValue = new Random().nextLong();
			if (longValue % 2 == 1) {
				Thread.sleep(10000);
			} else {
				Thread.sleep(2000);
			}
		} catch (InterruptedException e) {
			// ignore
		}
		return String.valueOf(Math.abs(new Random().nextLong()));
	}

}
