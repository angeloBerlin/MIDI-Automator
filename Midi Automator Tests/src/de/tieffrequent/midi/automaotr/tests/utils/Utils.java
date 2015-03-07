package de.tieffrequent.midi.automaotr.tests.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

	/**
	 * Replaces all system variables in a String with their corresponding value.
	 * 
	 * @param str
	 *            The string with possible system variables
	 * @return the string with substituted variables
	 */
	public static String replaceSystemVariables(String str) {

		String pattern = "%[A-Za-z0-9_]+%";
		Pattern expr = Pattern.compile(pattern);
		Matcher matcher = expr.matcher(str);
		Map<String, String> env = System.getenv();

		while (matcher.find()) {

			String sysVar = matcher.group(0);
			String sysVarValue = env.get(sysVar.replace("%", ""));

			if (sysVarValue != null) {
				str = str.replace(sysVar, sysVarValue);
			}
		}

		return str;
	}

	public static String runShellCommand(String[] cmd) {

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

		return (output.toString());
	}
}
