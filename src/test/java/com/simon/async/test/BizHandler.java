package com.simon.async.test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class BizHandler {

	public void handle() {
		long startTime = System.currentTimeMillis();
		List<Integer> locations = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
		locations.parallelStream().map(location -> { return mockSlowHandling(); }).collect(Collectors.toList());
		long endTime = System.currentTimeMillis();
		System.out.println("waste time: " + (endTime - startTime));
	}

	public String mockSlowHandling() {
		try {
//			long longValue = new Random().nextLong();
//			if (longValue % 2 == 1) {
//				Thread.sleep(10000);
//			} else {
				Thread.sleep(5000);
//			}
		} catch (InterruptedException e) {
			// ignore
		}
		return String.valueOf(Math.abs(new Random().nextLong()));
	}

}