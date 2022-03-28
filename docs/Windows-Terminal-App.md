# Windows Terminal App

## Open Terminal
See: https://docs.microsoft.com/de-de/windows/terminal/command-line-arguments?tabs=windows

## Create a new command for `Windows Terminal`
Open EasyShell preferences go to `(2) Command` and add new command:

![image](https://user-images.githubusercontent.com/95811/112133452-047fb680-8bcc-11eb-8d31-a41e0a1ffdb1.png)

* Category `Open`
* Name: `Windows Terminal`
* Command: `wt new-tab -d "${easyshell:container_loc}" --title "${easyshell:project_name}"`

Press `OK `and then `Apply and Close` (because of reloading command issues => will fix this later)

Reopen EasyShell preferences go to `(1) Menu` and create new menu and select new command:

![image](https://user-images.githubusercontent.com/95811/112134165-b919d800-8bcc-11eb-8fa0-4434a5b032e5.png)
Filter: `terminal`
Press `OK ` and then move the new menu entry with `Up` and `Down` to wanted place:

![image](https://user-images.githubusercontent.com/95811/112134334-ecf4fd80-8bcc-11eb-8fe9-37f43c712cce.png)
then `Apply and Close`

Now it should open the folder:

![image](https://user-images.githubusercontent.com/95811/112134839-6d1b6300-8bcd-11eb-836e-22e139805d98.png)

![image](https://user-images.githubusercontent.com/95811/112134954-8f14e580-8bcd-11eb-821e-dcc2c51348bc.png)
