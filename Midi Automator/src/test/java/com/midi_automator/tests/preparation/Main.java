package com.midi_automator.tests.preparation;

import org.junit.runner.JUnitCore;

/**
 * Runs preparations for integration tests
 * 
 * @author aguelle
 *
 */
public class Main {

	public static void main(String[] args) {
		JUnitCore junit = new JUnitCore();
		junit.run(Installer.class);
	}

}
