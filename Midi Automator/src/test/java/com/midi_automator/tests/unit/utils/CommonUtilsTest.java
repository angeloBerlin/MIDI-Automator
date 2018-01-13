package com.midi_automator.tests.unit.utils;

import static org.junit.Assert.*;

import org.junit.Test;

import com.midi_automator.utils.CommonUtils;

public class CommonUtilsTest {

	@Test
	public void testIsInteger() {
		int radix;

		radix = 10;
		assertTrue(CommonUtils.isInteger("1", radix));
		assertTrue(CommonUtils.isInteger("2", radix));
		assertTrue(CommonUtils.isInteger("3", radix));
		assertTrue(CommonUtils.isInteger("10", radix));
		assertTrue(CommonUtils.isInteger("127", radix));
		assertFalse(CommonUtils.isInteger("A", radix));

		radix = 16;
		assertTrue(CommonUtils.isInteger("1", radix));
		assertTrue(CommonUtils.isInteger("A", radix));
		assertTrue(CommonUtils.isInteger("F", radix));
		assertTrue(CommonUtils.isInteger("FF", radix));
		assertFalse(CommonUtils.isInteger("G", radix));

	}
}
