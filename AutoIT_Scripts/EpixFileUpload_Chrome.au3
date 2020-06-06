#include <MsgBoxConstants.au3>
Global $fileName
 $fileName = $CmdLine[1]

WinWait("Open","","70")
If WinExists("Open") Then
    Sleep(3000)
	Send("{BACKSPACE}")
	ControlSetText("Open","","Edit1",$fileName)
	Sleep(1000)
	ControlClick("Open","","Button1")
	  Sleep(5000)
EndIf