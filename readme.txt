!!! ATTENTION do not change other versions manually !!!
!!! all versions must be same BEFORE executiong set-version !!!
!!! e.g. in all pom.xml files: 2.0.0-SNAPSHOT and all eclipse files [MANIFEST.MF, feature.xml, category.xml]: 2.0.0.qualifier !!!

1. update the version in pom.xml: e.g. <newVersion>2.0.1-SNAPSHOT</newVersion>
2. run: mvn 'clean tycho-versions:set-version' all versions are updated now
3. build with 'mvn verify' or 'mvn install' or just launch config: EasyShell-Testing-Build.launch

sources:
http://codeandme.blogspot.co.at/2012/12/tycho-build-9-updating-version-numbers.html
https://wiki.eclipse.org/Tycho/Packaging_Types#eclipse-plugin
https://eclipse.org/tycho/sitedocs/tycho-release/tycho-versions-plugin/plugin-info.html

target defintions
https://wiki.eclipse.org/Eclipse_Project_Update_Sites
http://codeandme.blogspot.de/2012/12/tycho-build-8-using-target-platform.html
https://wiki.eclipse.org/Tycho/Packaging_Types#eclipse-target-definition

24.05.2016 anb0s
