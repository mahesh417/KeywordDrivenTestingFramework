
WinWait("[class:IEFrame]","","300")
WinActivate("[class:IEFrame]","")
if WinExists("[class:IEFrame]") Then
Send("!+s")
 Sleep(8000)
EndIf