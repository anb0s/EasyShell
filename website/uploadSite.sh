#!/bin/sh

rm -f htdocs.tar.gz

tar cvf htdocs.tar htdocs
gzip htdocs.tar

scp htdocs.tar.gz marcelschoen@pluginbox.sourceforge.net:/home/groups/p/pl/pluginbox/Pluginbox_htdocs.tar.gz
ssh -l marcelschoen pluginbox.sourceforge.net /home/users/m/ma/marcelschoen/unpackPluginboxSite.sh