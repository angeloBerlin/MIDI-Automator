package com.midi_automator.presenter.services.WIN32API;

import java.util.HashSet;

import com.sun.jna.Native;
import com.sun.jna.Pointer;

public class EnumWindowsCallback implements IEnumWindowsCallback {

	@Override
	public boolean callback(Pointer hWnd, Pointer arg) {

		byte[] windowText = new byte[512];

		IUser32.INSTANCE.GetWindowTextA(hWnd, windowText, 512);

		String wText = Native.toString(windowText).trim();

		if (!wText.isEmpty()) {
			WINDOWNAMES.add(wText);
		}

		return true;
	}

	@Override
	public HashSet<String> getWINDOWNAMES() {
		return WINDOWNAMES;
	}
}
