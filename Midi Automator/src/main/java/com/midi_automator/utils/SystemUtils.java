package com.midi_automator.utils;

import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class SystemUtils {

	static Logger log = Logger.getLogger(SystemUtils.class.getName());

	/**
	 * Replaces all system variables in a String with their corresponding value.
	 * 
	 * @param str
	 *            The string with possible system variables
	 * @return the string with substituted variables
	 */
	public static String replaceSystemVariables(String str) {

		log.debug("String to replace with system variables: " + str);
		if (str != null) {
			String pattern = "(%|\\$)[A-Za-z0-9_]+%*";
			Pattern expr = Pattern.compile(pattern);
			Matcher matcher = expr.matcher(str);
			TreeMap<String, String> env = new TreeMap<String, String>(
					System.getenv());

			if (System.getProperty("os.name").equals("Windows 7")) {

				TreeMap<String, String> insensitiveEnv = new TreeMap<String, String>(
						String.CASE_INSENSITIVE_ORDER);
				insensitiveEnv.putAll(env);
				env = insensitiveEnv;
			}

			while (matcher.find()) {

				String sysVar = matcher.group(0);
				String varName = sysVar.replace("%", "");
				varName = varName.replace("$", "");
				String sysVarValue = env.get(varName);

				if (sysVarValue != null) {
					str = str.replace(sysVar, sysVarValue);
				}
			}
		}

		log.debug("Replaced system variables: " + str);

		return str;
	}

	/**
	 * Prints the list of system variables to the console.
	 */
	public static void printSystemVariables() {
		Map<String, String> env = System.getenv();
		for (Map.Entry<String, String> entry : env.entrySet()) {
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}
	}

	/**
	 * Runs a shell command and returns the output as a String
	 * 
	 * @param cmd
	 *            the command
	 * @return the shell runner thread
	 */
	public static ShellRunner runShellCommand(String[] cmd) {
		ShellRunner shellRunner = new ShellRunner(cmd);
		shellRunner.start();
		return shellRunner;
	}
}
