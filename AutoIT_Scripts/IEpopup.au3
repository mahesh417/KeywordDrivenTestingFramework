
WinWait("Message from webpage","","300")
WinActivate("Message from webpage","")
if WinExists("Message from webpage") Then
   ControlClick("Message from webpage","","Button1")
   Sleep(1000)
EndIf