# Windows Administrator Terminal

##  Command Prompt

### Create a new command for `Command Prompt as Admin` terminal

- go to menu `Window -> Preferences -> EasyShell -> (2) Command`
- enter `prompt` in the search field
- select `Command Prompt` and use `Copy...` button
- in the new dialog
  - rename to `Command Prompt as Admin` or similar
  - select `Working directory` checkbox
  - Change command to `powershell.exe "Start-Process cmd -Verb RunAs"`
![image](https://user-images.githubusercontent.com/95811/109027578-3d0d9c80-76c1-11eb-9315-ce016be96667.png)
- accept with `OK`

### Create new menu with the command

- go to menu `Window -> Preferences -> EasyShell -> (1) Menu`
- Use `Add...` button
- enter Filter `admin`
- select `Open - Command Prompt as Admin (User)`

![image](https://user-images.githubusercontent.com/95811/109031691-3e40c880-76c5-11eb-8981-2b48fd9570c8.png)
- accept with `OK`

## Powershell Prompt

### Create a new command for `PowerShell as Admin` terminal

- go to menu `Window -> Preferences -> EasyShell -> (2) Command`
- enter `powershell` in the search field
- select `PowerShell` and use `Copy...` button
- in the new dialog
  - rename to `PowerShell as Admin` or similar
  - Change command to `powershell.exe -command "Start-Process powershell -Verb RunAs -ArgumentList '-NoExit', '-Command', 'cd ${easyshell:container_loc}'"`
![image](https://user-images.githubusercontent.com/95811/109144127-14d57a80-7761-11eb-9946-df2379afaf9a.png)
- accept with `OK`

### Create new menu with the command
- go to menu `Window -> Preferences -> EasyShell -> (1) Menu`
- Use `Add...` button
- enter Filter `powershell`
- select `Open - PowerShell as Admin (User)`

![image](https://user-images.githubusercontent.com/95811/109031898-70eac100-76c5-11eb-8b00-febe2ae5f1a3.png)
- accept with `OK`

## Arrange menu entries
Now you can select the new menu entries and use buttons `Up` and `Down` to move them to the place in the menu you want.

![image](https://user-images.githubusercontent.com/95811/109032353-dc349300-76c5-11eb-8e5f-9d98cad87904.png)

## Test via `Alt + E` or `Alt + O`
Select the resource in you Eclipse explorer and use `Alt + E` or `Alt + O` or context menu to run EasyShell

![image](https://user-images.githubusercontent.com/95811/109032748-40efed80-76c6-11eb-904c-d444ac729d21.png)