beta update site:
https://raw.githubusercontent.com/anb0s/EasyShell/v2_0/de.anbos.eclipse.easyshell.site/updates/testing

main update site:
https://raw.githubusercontent.com/anb0s/EasyShell/v2_0/de.anbos.eclipse.easyshell.site/updates/release

1. build the site with maven: mvn clean install
2. copy target\site to composite\site_x_y !!! Only one site with one / last plugin version !!!
3. adapt compositeArtifacts.xml and compositeContent.xml

source: http://stackoverflow.com/questions/20951842/combine-aggregate-eclipse-p2-repositories-extendable-p2-repository

24.05.2016 anb0s
