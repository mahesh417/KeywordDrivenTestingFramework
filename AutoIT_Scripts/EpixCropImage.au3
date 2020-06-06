#include <MsgBoxConstants.au3>
Global $itemId
 $itemId = $CmdLine[1]

WinWait($itemId,"","200")
WinActivate($itemId,"")
If WinExists($itemId) Then
    Sleep(2000)
	Send("^w")

	WinWait("Resize and Skew","","100")
   If WinExists("Resize and Skew") Then
    Sleep(500)
   ControlClick("Resize and Skew","","Button3")
   Sleep(500)
   ControlSetText("Resize and Skew","","Edit1","2000")
   Sleep(500)
   ControlClick("Resize and Skew","","Button6")
   EndIf

EndIf

WinActivate($itemId,"")
If WinExists($itemId) Then
    Sleep(1000)
	Send("^s")
	Sleep(2000)
EndIf

WinActivate($itemId,"")
If WinExists($itemId) Then
	Sleep(5000)
   Send("!{F4}")
EndIf