Easy Shell 1.3.0 plugin for Eclipse
-----------------------------------

This plugin allows to open a shell window or file manager from the
popup menu in the navigation tree or editor view.
The current directory of the opened shell is the directory which
was selected with the popup menu.
Additionally it is possible to run selected file in the shell.

The plugin is platform-independent in principal. It just launches
a (configurable) system command to open the shell. The following
platform / shell combinations are supported as selections
available from the drop-down-list in the preferences page:

Windows
- DOS-Shell / Explorer
- Cygwin-Shell / Explorer (Check path of Cygwin executable!)

Linux (and other)
- KDE Konsole / Konqueror
- Gnome Terminal / Nautilus (not tested!)
- CDE Xterm / Dtfile (not tested!)

But you can configure any shell you like as long as you can figure
out how to run a command to open the shell with a given working
directory. The following substitution variables are available
for building the exec command:

{0} = The drive letter (only Windows, not used on other platforms)
{1} = The selected directory path
{2} = The selected file or directory path
{3} = The selected file name
{4} = The project name of selected directory or path (if file is external it's "Easy Shell")
