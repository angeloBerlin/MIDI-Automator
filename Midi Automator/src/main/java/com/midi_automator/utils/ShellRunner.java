package com.midi_automator.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Runs a command in the OS shell
 * 
 * @author aguelle
 *
 */
public class ShellRunner extends Thread {

	private String[] cmd;
	private String output;

	/**
	 * Constructor
	 * 
	 * @param cmd
	 *            command to run
	 */
	public ShellRunner(String[] cmd) {
		this.cmd = cmd;
	}

	/**
	 * Gets the output from the shell. BE AWARE: This blocks the calling thread
	 * until the shell command was finished.
	 * 
	 * @return the output
	 */
	public String getOutput() {
		while (isAlive()) {
			// Do nothing
		}
		return this.output;
	}

	@Override
	public void run() {
		StringBuffer output = new StringBuffer();

		try {
			Process p = Runtime.getRuntime().exec(cmd);

			p.getOutputStream().close();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));

			String line = "";

			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		this.output = output.toString();
	}
}
