# EasyShell [![Build Status](https://travis-ci.org/anb0s/EasyShell.svg)](https://travis-ci.org/anb0s/EasyShell) [![Download EasyShell](https://img.shields.io/sourceforge/dt/pluginbox.svg)](https://sourceforge.net/projects/pluginbox/files/latest/download)
This Eclipse plugin allows to open a shell window or file manager from the popup menu in the navigation tree or editor view. Additionally it is possible to run selected file in the shell, copy file or directory path or run user defined external tools. Key shortcuts and multiple selections are also supported!

Installation:
-------------
Use update site: http://anb0s.github.io/EasyShell

OR

Eclipse Markeplace: http://marketplace.eclipse.org/content/easyshell

OR

[Download EasyShell] (https://sourceforge.net/projects/pluginbox/files/latest/download), extract it to "eclipse\dropin" folder and restart.

Features:
---------

The plugin is platform independent in principal. It just launches a (configurable) system command to open the shell, file explorer or other user defined command. It also copies path etc. to clipboard.

![context_menu_windows](https://raw.githubusercontent.com/anb0s/EasyShell/master/site/images/EasyShell_2.0_context_menu_windows.png "Context Menu @ Windows")

![popup_menu_windows](https://raw.githubusercontent.com/anb0s/EasyShell/master/site/images/EasyShell_2.0_popup_menu_windows.png "Popup Menu @ Windows (Alt+E)")

![multi_selection_dialog_windows](https://raw.githubusercontent.com/anb0s/EasyShell/master/site/images/EasyShell_2.0_multi-selection_dialog_windows.png "Dialog for multiple tool selection @ Windows (Alt+Shift+E)")

The following platform, shell combinations and tools are supported as selections available in the preferences page. The user can define own commands and menues or just adapt the predefined ones!

Windows:
- Command prompt (known as DOS shell or cmd.exe)
- PowerShell
- Bash (Cygwin, Git for Windows, etc.)
  http://cygwin.com, https://git-for-windows.github.io
- Console
  https://sourceforge.net/projects/console
- ConEmu 
  https://code.google.com/p/conemu-maximus5/
- Cmder
  https://github.com/cmderdev/cmder
- Explorer
- TotalCommander
  http://www.ghisler.com  

If path to your tool is not added to PATH variable, please add it or adapt the command in EasyShell!

Unix (BSD, Mac OS X) or Linux:
- XDG Open
- KDE Konsole
- Konqueror
- Gnome Terminal
- Xfce Terminal
- Sakura Terminal
- ROXTerm
- Pantheon Terminal
- Dolphin
- Nautilus
- Nemo
- Thunar
- Pantheon
- PCManFM

MAC OS X
 - Terminal
 - Finder

Copy to clipboard
 - Full Path
 - Full Path Unix (@Windows)
 - Qualified Name
 - ... more configurable...

![preferences_menu](https://raw.githubusercontent.com/anb0s/EasyShell/master/site/images/EasyShell_2.0_preferences_menu.png "Preferences - Menu")

![preferences_menu_new](https://raw.githubusercontent.com/anb0s/EasyShell/master/site/images/EasyShell_2.0_preferences_menu_new.png "Preferences - Menu 'New'")

![preferences_menu_new_content_assist](https://raw.githubusercontent.com/anb0s/EasyShell/master/site/images/EasyShell_2.0_preferences_menu_new_content_assist.png "Preferences - Menu 'New' with content assist")

But you can configure any shell or command you like as long as you can figure out how to run a command to open the shell with given
parameters or execute the tool you want.

![preferences_command](https://raw.githubusercontent.com/anb0s/EasyShell/master/site/images/EasyShell_2.0_preferences_command.png "Preferences - Command")

![preferences_command_new_content_assist](https://raw.githubusercontent.com/anb0s/EasyShell/master/site/images/EasyShell_2.0_preferences_command_new.png "Preferences - Command 'New' with content assist")

The following substitution variables are available for building the command:

- ${easyshell:resource_loc} = absolute path of file or directory
- ${easyshell:resource_name} = name of file or directory
- ${easyshell:resource_basename} = name of file without extension
- ${easyshell:resource_extension} = extension of file name (without '.')
- ${easyshell:resource_path} = relative path to workspace of file or directory
- ${easyshell:container_loc} = absolute path of file directory or directory itself
- ${easyshell:container_name} = name of file directory or directory itself
- ${easyshell:container_path} = relative path to workspace of file directory or directory itself
- ${easyshell:project_loc} = absolute path of project
- ${easyshell:project_name} = name of project
- ${easyshell:project_path} = relative path to workspace of project
- ${easyshell:windows_drive} = drive letter of file or directory on Windows
- ${easyshell:qualified_name} = full qualified (class) name
- ${easyshell:line_separator} = line separator, e.g. '\\n' (Unix) or '\\r\\n' (Windows)
- ${easyshell:path_separator} = path separator, e.g. ':' (Unix) or ';' (Windows)
- ${easyshell:file_separator} = file separator, e.g. '/' (Unix) or '\\' (Windows)
- and all other available variables in Eclipse

License:
--------
https://eclipse.org/org/documents/epl-v10.html
