<p align="center"><a href="https://anb0s.github.io/EasyShell"><img src="https://raw.githubusercontent.com/anb0s/EasyShell/master/platform/logo/horizontalversion.svg" alt="EasyShell" height="100px"></a></p>

[![Version](https://img.shields.io/github/release/anb0s/EasyShell.svg)](https://github.com/anb0s/EasyShell/releases) [![Issues](https://img.shields.io/github/issues/anb0s/EasyShell.svg)](https://github.com/anb0s/EasyShell/issues) [![Chat @ gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/anb0s/Lobby) [![Build Status](https://travis-ci.org/anb0s/EasyShell.svg?branch=master)](https://travis-ci.org/anb0s/EasyShell) [![License](https://img.shields.io/badge/License-EPL%202.0-blue.svg)](https://www.eclipse.org/legal/epl-2.0)

This Eclipse plugin allows to open a shell window or file manager from the popup menu in the navigation tree or editor view. Additionally it is possible to run selected file in the shell, copy file or directory path or run user defined external tools. Key shortcuts and multiple selections are also supported!

Installation:
-------------
Eclipse Markeplace: http://marketplace.eclipse.org/content/easyshell

<a href="http://marketplace.eclipse.org/marketplace-client-intro?mpc_install=974" class="drag" title="Drag to your running Eclipse* workspace. *Requires Eclipse Marketplace Client"><img class="img-responsive" src="https://marketplace.eclipse.org/sites/all/themes/solstice/public/images/marketplace/btn-install.png" alt="Drag to your running Eclipse* workspace. *Requires Eclipse Marketplace Client" /></a>

OR

Use update site: http://anb0s.github.io/EasyShell

OR

[Download EasyShell from GitHub](https://github.com/anb0s/EasyShell/releases) OR
[Download EasyShell from SourceForge](https://sourceforge.net/projects/pluginbox/files/latest/download) [![Download EasyShell](https://img.shields.io/sourceforge/dt/pluginbox.svg)](https://sourceforge.net/projects/pluginbox/files/latest/download)

extract it to "eclipse\dropin" folder and restart.

Features:
---------

The plugin is platform independent in principal. It just launches a (configurable) system command to open the shell, file explorer or other user defined command. It also copies path etc. to clipboard. Just open context menu for an Eclipse editor or selected resource in some view:

![context_menu_windows](https://raw.githubusercontent.com/anb0s/EasyShell/master/site/images/EasyShell_2.0_context_menu_windows.png "Context Menu @ Windows")
![context_menu_Linux](https://raw.githubusercontent.com/anb0s/EasyShell/master/site/images/EasyShell_2.1_context_menu_linux.png "Context Menu @ Linux")

Keyboard-shortcuts:
-------------------

**Alt + E**: Main popup menu (Windows)

![popup_menu_windows](https://raw.githubusercontent.com/anb0s/EasyShell/master/site/images/EasyShell_2.0_popup_menu_windows.png "Popup Menu @ Windows (Alt+E)")

**Alt + Shift + E**: execute multiple commands for selection(s)

![multi_selection_dialog_windows](https://raw.githubusercontent.com/anb0s/EasyShell/master/site/images/EasyShell_2.0_multi-selection_dialog_windows.png "Dialog for multiple tool selection @ Windows (Alt+Shift+E)")
![multi_selection_dialog_linux](https://raw.githubusercontent.com/anb0s/EasyShell/master/site/images/EasyShell_2.1_multi-selection_dialog_linux.png "Dialog for multiple tool selection @ Linux (Alt+Shift+E)")

Shortcuts that executes the command directly if only one defined or opens a popup menu like Alt + E reduced for category:

**Alt + O**: Open Here (shell)

![popup_menu_linux](https://raw.githubusercontent.com/anb0s/EasyShell/master/site/images/EasyShell_2.1_popup_menu_linux.png "Popup Menu @ Linux (Alt+O)")

**Alt + R**: Run with (shell)

**Alt + X**: Explore

**Alt + C**: Copy to Clipboard

**Alt + U**: User defined category

Special not available for all OS:

**Alt + D**: Open with Default application

Supported OS and commands:
--------------------------

The following platform, shell combinations and tools are supported as selections available in the preferences page. The user can define own commands and menues or just adapt the predefined ones!

**Windows:**
- Terminals
  - Command prompt (known as DOS shell or cmd.exe)
  - PowerShell
  - Bash
    - Cygwin: http://cygwin.com
    - Git for Windows 1.x/2.x: https://git-for-windows.github.io
    - Msys2 / MinGW32 / MinGW64: http://www.msys2.org
  - Console:
    - Console2: https://sourceforge.net/projects/console
    - ConsoleZ: https://github.com/cbucher/console
  - ConEmu: https://code.google.com/p/conemu-maximus5
  - Cmder: https://github.com/cmderdev/cmder
  - PowerCmd: http://www.powercmd.com
- File Browsers
  - Explorer
  - TotalCommander: http://www.ghisler.com
  - DoubleCommander: http://doublecmd.sourceforge.net
  - Q-Dir (Quad Explorer): http://www.softwareok.de/?seite=Freeware/Q-Dir

**Linux:**
- Terminals
  - XDG Open
  - KDE Konsole
  - Gnome Terminal
  - Xfce Terminal
  - MATE Terminal
  - LX Terminal
  - Sakura Terminal
  - ROXTerm
  - Pantheon Terminal
  - Guake
  - Enlightenment Terminology
- File Browsers
  - Konqueror
  - Pantheon
  - PCManFM
  - Nautilus
  - Dolphin
  - Nemo
  - Thunar
  - Caja
  - Krusader
  - DoubleCommander: http://doublecmd.sourceforge.net
  - Midnight Commander: http://midnight-commander.org
  - Sunflower: http://sunflower-fm.org

**MAC OS X**
- Terminals
  - Open
  - Terminal
  - iTerm
- File Browsers
  - Finder

**All OS**
- Copy to clipboard:
  - Full Path
  - Full Path Unix (@Windows)
  - Qualified Name
  - ... more configurable...

If path to your tool is not added to PATH variable, please add it or adapt the command in EasyShell!

Preferences:
------------

![preferences_general](https://raw.githubusercontent.com/anb0s/EasyShell/master/site/images/EasyShell_2.1_preferences_general.png "Preferences - General")

![preferences_menu](https://raw.githubusercontent.com/anb0s/EasyShell/master/site/images/EasyShell_2.1_preferences_menu_linux.png "Preferences - Menu")

![preferences_menu_edit](https://raw.githubusercontent.com/anb0s/EasyShell/master/site/images/EasyShell_2.1_preferences_menu_edit.png "Preferences - Menu 'Edit'")

![preferences_menu_edit_filter](https://raw.githubusercontent.com/anb0s/EasyShell/master/site/images/EasyShell_2.1_preferences_menu_edit_filter.png "Preferences - Menu 'Edit' with filter")

![preferences_menu_edit_content_assist](https://raw.githubusercontent.com/anb0s/EasyShell/master/site/images/EasyShell_2.1_preferences_menu_edit_content_assist.png "Preferences - Menu 'Edit' with content assist")

But you can configure any shell or command you like as long as you can figure out how to run a command to open the shell with given
parameters or execute the tool you want.

![preferences_command](https://raw.githubusercontent.com/anb0s/EasyShell/master/site/images/EasyShell_2.1_preferences_command_linux.png "Preferences - Command")

![preferences_command_new_content_assist](https://raw.githubusercontent.com/anb0s/EasyShell/master/site/images/EasyShell_2.1_preferences_command_new.png "Preferences - Command 'New' with content assist")

The following substitution variables are available for building the command:

- ```${easyshell:resource_loc}``` = absolute path of file or directory
- ```${easyshell:resource_name}``` = name of file or directory
- ```${easyshell:resource_basename}``` = name of file without extension
- ```${easyshell:resource_extension}``` = extension of file name (without '.')
- ```${easyshell:resource_path}``` = relative path to workspace of file or directory
- ```${easyshell:container_loc}``` = absolute path of file directory or directory itself
- ```${easyshell:container_name}``` = name of file directory or directory itself
- ```${easyshell:container_path}``` = relative path to workspace of file directory or directory itself
- ```${easyshell:parent_loc}``` = absolute path of parent directory, for files it's equal to ${easyshell:container_loc}
- ```${easyshell:parent_name}``` = name of parent directory, for files it's equal to ${easyshell:container_name}
- ```${easyshell:parent_path}``` = relative path to workspace of parent directory, for files it's equal to ${easyshell:container_path}
- ```${easyshell:project_loc}``` = absolute path of project
- ```${easyshell:project_name}``` = name of project
- ```${easyshell:project_path}``` = relative path to workspace of project
- ```${easyshell:windows_drive}``` = drive letter of file or directory on Windows
- ```${easyshell:qualified_name}``` = full qualified (class) name
- ```${easyshell:line_separator}``` = line separator, e.g. '\\n' (Unix) or '\\r\\n' (Windows)
- ```${easyshell:path_separator}``` = path separator, e.g. ':' (Unix) or ';' (Windows)
- ```${easyshell:file_separator}``` = file separator, e.g. '/' (Unix) or '\\' (Windows)
- ```${easyshell:script_bash}``` = Bash script (internal)
- and all other available variables in Eclipse

The following substitution variables are available for building the menu name:
- ```${easyshell:command_category}``` = command category
- ```${easyshell:command_type}``` = command type
- ```${easyshell:command_name}``` = command name
- ```${easyshell:command_os}``` = command operating system

<a href="http://with-eclipse.github.io/" target="_blank">
<img alt="with-Eclipse logo" src="http://with-eclipse.github.io/with-eclipse-0.jpg" />
</a>
