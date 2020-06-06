#include <MsgBoxConstants.au3>
WinWait("Opening","","70")
WinActivate("Opening","")
If WinExists("Opening") Then
   Sleep(500)
	Send("!s")
	EndIf

WinWait("Opening","","70")
WinActivate("Opening","")
If WinExists("Opening") Then
    Sleep(500)
	Send("{ENTER}")
	Sleep(8000)
 EndIf
 WinWait("Opening","","7")
If WinExists("Opening") Then
	Sleep(4000)
 EndIf





