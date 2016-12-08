' Sets the focus to the given window name 
'
' usage: setFocus.vbs <WINDOW_NAME
' @author Angelo Guelle
' @date 07-12-16

Set objArgs = Wscript.Arguments
Set WshShell = WScript.CreateObject("WScript.Shell") 

windowName = objArgs(0)

WshShell.AppActivate(windowName)