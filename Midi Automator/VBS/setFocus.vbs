' Sets the focus to the given PID
'
' usage: setFocus.vbs <PID>
' @author Angelo Guelle
' @date 18-12-16

Set objArgs = Wscript.Arguments
Set WshShell = WScript.CreateObject("WScript.Shell") 

pid = objArgs(0)

WshShell.AppActivate(pid)