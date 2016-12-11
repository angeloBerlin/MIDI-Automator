package com.midi_automator.presenter.services.WIN32API;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;

/**
 * Represents the Windows 
 * 
 * @author aguelle
 *
 */
public interface IUser32 extends StdCallLibrary {
	
	public final IUser32 INSTANCE = (IUser32) Native.loadLibrary("user32", IUser32.class);
	
	/**
	 * Enumerates all top-level windows on the screen by passing the handle to each window, in turn, 
	 * to an application-defined callback function. EnumWindows continues until the last top-level 
	 * window is enumerated or the callback function returns FALSE. 
	 * 
	 * @param lpEnumFunc
	 * 		The object holding the function called on each found windows.
	 * @param userData
	 * 		An application-defined value to be passed to the callback function.
	 * @return <TRUE> if the function succeeded, <FALSE> if the function failed
	 */
	public boolean EnumWindows(IEnumWindowsCallback lpEnumFunc, Pointer userData);
	
	/**
	 * Copies the text of the specified window's title bar (if it has one) into a buffer. 
	 * If the specified window is a control, the text of the control is copied. However, 
	 * GetWindowText cannot retrieve the text of a control in another application.
	 * 
	 * @param hWnd
	 * 		A handle to the window or control containing the text.
	 * @param lpString
	 * 		The buffer that will receive the text. If the string is as long or longer than the buffer, 
	 * 		the string is truncated and terminated with a null character.
	 * @param nMaxCount
	 * 		The maximum number of characters to copy to the buffer, including the null character. 
	 * 		If the text exceeds this limit, it is truncated.
	 * @return If the function succeeds, the return value is the length, in characters, of the copied string, 
	 * 			not including the terminating null character. If the window has no title bar or text, if the 
	 * 			title bar is empty, or if the window or control handle is invalid, the return value is zero.
	 */
    public int GetWindowTextA(Pointer hWnd, byte[] lpString, int nMaxCount);
}
