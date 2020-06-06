Global $username
Global $password
Global $windowtitle
$username = $CmdLine[1]
$password = $CmdLine[2]
$windowtitle = $CmdLine[3]
WinWait($windowtitle,"","100")
WinWaitActive($windowtitle)
Send($username)
Send("{TAB}")
Send($password)
Send("{ENTER}")