#include <MsgBoxConstants.au3>
Global $fileName
 $fileName = $CmdLine[1]

WinWait("File Upload","","70")
If WinExists("File Upload") Then
    Sleep(3000)
	Send("{BACKSPACE}")
	ControlSetText("File Upload","","Edit1",$fileName)
	Sleep(1000)
	ControlClick("File Upload","","Button1")
	  Sleep(3000)
EndIf