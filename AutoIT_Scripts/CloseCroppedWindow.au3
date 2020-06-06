#include <MsgBoxConstants.au3>
Global $itemId
 $itemId = $CmdLine[1]

WinWait($itemId,"","70")
WinActivate($itemId,"")
If WinExists($itemId) Then
	Sleep(5000)
   Send("!{F4}")
EndIf






