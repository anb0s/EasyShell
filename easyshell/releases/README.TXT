Easy Shell 1.3.6 plugin for Eclipse
-----------------------------------

This plugin allows to open a shell window or file manager from the
popup menu in the navigation tree or editor view. The current
directory of the opened shell is the directory which was selected
with the popup menu. Additionally it is possible to run selected
file in the shell and copy file or directory path. Multiple
selections are also supported. 

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

Unix / Linux:
- KDE Konsole / Konqueror and Dolphin
- Gnome Terminal / Nautilus
- CDE Xterm / Dtfile (not tested!)
- MAC OS X Terminal / Finder (not tested!)

Other:
- Default / Unknown (can be defined for all systems supported by Java)

But you can configure any shell or command you like as long as you
can figure out how to run a command to open the shell with given
parameters. The following substitution variables are available
for building the command:

{0} = The drive letter (only Windows, not used on other platforms)
{1} = The selected directory path
{2} = The selected file or directory path
{3} = The selected file name
{4} = The project name of selected directory or path (for external files it's "Easy Shell")
{5} = The line separator

Installation:
-------------
a) Extract zip file to "eclipse\dropin" folder and restart.
OR
b) Use update site: http://pluginbox.sourceforge.net
