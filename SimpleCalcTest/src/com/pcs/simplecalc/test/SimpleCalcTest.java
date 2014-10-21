package com.pcs.simplecalc.test;

import android.content.IntentSender.SendIntentException;

import com.pcs.simplecalc.Math;

import junit.framework.TestCase;

public class SimpleCalcTest extends TestCase{
	private int value1;
	private int value2;

	public SimpleCalcTest(String testName) {
		super(testName);
	}

	protected void setUp() throws Exception {
		super.setUp();
		value1 = 3;
		value2 = 5;
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		value1 = 0;
		value2 = 0;
	}

	public void testAdd() {
		int total = 8;
		int sum = Math.add(value1, value2);
		assertEquals(sum, total);
	}

	

	public void testSub() {
		int total = 0;
		int sub = Math.sub(4, 4);
		assertEquals(sub, total);
	}
public void testMul(){
	int total=15;
	int mul=Math.mul(value1, value2);
	assertEquals(mul, total);
}

public void testDiv(){
	int total=2;
	int mul=Math.div(14, 7);
	assertEquals(mul, total);
}
}
