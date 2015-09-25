# EasyShell [![Build Status](https://travis-ci.org/anb0s/EasyShell.svg?branch=v1_5)](https://travis-ci.org/anb0s/EasyShell?branch=v1_5) [![Build Status](https://travis-ci.org/anb0s/EasyShell.svg?branch=master)](https://travis-ci.org/anb0s/EasyShell?branch=master)   [![Download EasyShell](https://img.shields.io/sourceforge/dt/pluginbox.svg)](https://sourceforge.net/projects/pluginbox/files/latest/download)
Eclipse Plugin - This plugin allows to open a shell window or file manager from the popup menu in the navigation tree or editor view. The current directory of the opened shell is the directory which was selected with the popup menu. Additionally it is possible to run selected file in the shell and copy file or directory path.
Multiple selections are also supported.

Installation:
-------------
Use update site: http://anbos.de/eclipse/pluginbox-updatesite/

OR

[Download EasyShell] (https://sourceforge.net/projects/pluginbox/files/latest/download), extract it to "eclipse\dropin" folder and restart.

Eclipse Markeplace: http://marketplace.eclipse.org/content/easyshell

Features:
---------

EasyShell does not support virtual folders, because they are not
present in the file system!

The plugin is platform-independent in principal. It just launches
a (configurable) system command to open the shell. The following
platform and shell combinations are supported as selections
available from the drop-down-list in the preferences page:

Windows:
- DOS-Shell / Explorer
- PowerShell / Explorer (check path of PowerShell executable!)
- Cygwin (Bash) / Explorer (check path of Cygwin-bash executable!)
  http://cygwin.com
- Console / Explorer
  https://sourceforge.net/projects/console
- DOS-Shell / TotalCommander
  http://www.ghisler.com
- Git (Bash) / Explorer (check path of Git-bash executable!)
  http://msysgit.github.io
- ConEmu / Explorer (check path of ConEmu executable!)
  https://code.google.com/p/conemu-maximus5/

Unix / Linux:
- KDE Konsole / Konqueror and Dolphin
- Gnome Terminal / Nautilus
- MAC OS X Terminal / Finder
- CDE Xterm / Dtfile (not tested!)

Other:
- Default / Unknown (can be defined for all systems supported by Java)

But you can configure any shell or command you like as long as you
can figure out how to run a command to open the shell with given
parameters. The following substitution variables are available
for building the command:

{0} = The drive letter (only Windows, not used on other platforms)<br/>
{1} = The selected directory path<br/>
{2} = The selected file or directory path<br/>
{3} = The selected file name<br/>
{4} = The project name of selected directory or path (for external files it's "Easy Shell")<br/>
{5} = The line separator<br/>

Option "Enable string tokenizer" with values "Yes, No" and default value "Yes".<br/>
The whole command line string will be splitted (tokenized) to single strings before expansion
of parameters {0} - {4} and passing to command line. With this solution there is no need to
surround parameters {0} - {4} with quotes when space character is possible between command
line option and parameter, e.g.: /D {1}. Otherwise maybe quotes are still needed,
e.g.: --working-directory="{1}".

Option "Add quotes to {1}, {2}, {3}*" with values "No, Single, Double, Automatic" and default
value "No". <br/>
If changed to other values, parameters will be eclosed with quotes.
ATTENTION: use it carefully with "string tokenizer" and remove quotes from commands.

Option "Enable debug output" with values "Yes, No" and default value "No".<br/>
If changed to "Yes", debug messages will be printed to error console.
