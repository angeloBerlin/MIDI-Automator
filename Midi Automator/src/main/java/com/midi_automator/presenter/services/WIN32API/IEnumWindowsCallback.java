package com.midi_automator.presenter.services.WIN32API;
import java.util.HashSet;

import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary.StdCallCallback;

/**
 * Interface for an object holding the callback function for 
 * the Windows API EnumWindows
 * 
 * @author aguelle
 *
 */
public interface IEnumWindowsCallback extends StdCallCallback {
	
	final HashSet<String> WINDOWNAMES = new HashSet<String>();
	
	/**
	 * The call back method run on each found window
	 * 
	 * @param hWnd
	 * 		The pointer to the window
	 * @param arg
	 * 		The pointer to further arguments for the callback function
	 * @return <TRUE> if the function succeeded, <FALSE> if the function failed
	 */
	public boolean callback(Pointer hWnd, Pointer arg);
	
	/**
	 * Returns all Window names
	 * 
	 * @return a list of open window names
	 */
	public HashSet<String> getWINDOWNAMES();
}
