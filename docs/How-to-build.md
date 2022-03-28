# How to build

## Dependencies
* Eclipse IDE for Eclipse Committers, see `https://www.eclipse.org/downloads/packages/release/2022-03/r/eclipse-ide-eclipse-committers`
* Maven: just install  the m2e plugin, see https://www.eclipse.org/m2e
* Tycho: do not need separate installation, will be fetched by Maven during the build automattically, see `https://eclipse.org/tycho`

## Get EasyShell plugin sources
* Clone EasyShell from `https://github.com/anb0s/EasyShell.git` to new directory `EasyShell`
* switch to `main` branch
* Import all projects to Eclipse and add them to new "Working Set" **EasyShell**
  * **EasyShell**: root project, needed only for readme, headless build etc.
  * **de.anbos.eclipse.easyshell.feature**: feature project
  * **de.anbos.eclipse.easyshell.platform**: platform definition
  * **de.anbos.eclipse.easyshell.plugin**: plugin with main sources
  * **de.anbos.eclipse.easyshell.site**: update site

## Get EasyShell web-site (gh-pages)
* Clone EasyShell from `https://github.com/anb0s/EasyShell.git` to new directory `EasyShell-gh-pages`
* switch to `gh-pages` branch
* Import the project to Eclipse and add it to "Wortking Set" **EasyShell**
  * **EasyShell-gh-pages**: root project, needed for web- and update-site

## Running and debugging in Eclipse
* select target platform you want to test EasyShell against:
  * go to project **de.anbos.eclipse.easyshell.platform**
  * open wanted platform definition file, e.g. use **Eclipse-2022-03.target**
  * use `Set as Target Platform` and wait until the platform defition is loaded from Eclipse mirrors
* now there should be no build errors anymore
* use `Run | Debug As | Eclipse Application` and wait until the new Eclipse instance is opened
* use EasyShell, set breakpoint etc. :)

## Update version

!!! ATTENTION do not change other versions manually !!!

!!! all versions must be same BEFORE executiong set-version !!!

!!! e.g. in all pom.xml files: `2.3.0-SNAPSHOT` and all eclipse files `[MANIFEST.MF, feature.xml, category.xml]`: `2.3.0.qualifier` !!!

1. update the version in root `pom.xml`: e.g. `<newVersion>2.3.1-SNAPSHOT</newVersion>`
2. run `EasyShell-set-new-version.launch` or execute: `mvn clean tycho-versions:set-version` and all versions are updated now
3. build with `mvn -Dsite.dir=testing clean verify` or just launch config `EasyShell-Testing-Build.launch`

sources:
- http://codeandme.blogspot.co.at/2012/12/tycho-build-9-updating-version-numbers.html
- https://wiki.eclipse.org/Tycho/Packaging_Types#eclipse-plugin
- https://eclipse.org/tycho/sitedocs/tycho-release/tycho-versions-plugin/plugin-info.html

target defintions:
- https://wiki.eclipse.org/Eclipse_Project_Update_Sites
- http://codeandme.blogspot.de/2012/12/tycho-build-8-using-target-platform.html
- https://wiki.eclipse.org/Tycho/Packaging_Types#eclipse-target-definition
- https://bugs.eclipse.org/bugs/show_bug.cgi?id=383865
- http://blog.vogella.com/2013/01/03/tycho-advanced/

## Building EasyShell plugin with update site for testing
  * go to project **EasyShell**
  * build
    * from Eclipse: run launch configuration `EasyShell-Testing-Build`
    * from commandline / headless / CI server: `mvn clean verify` with parameter `site.dir=testing`: `mvn -Dsite.dir=testing clean verify`
  * go to project **de.anbos.eclipse.easyshell.site** and use one of:
    * directory `target\repository` OR
    * zip file e.g. `target\de.anbos.eclipse.easyshell.site-2.3.0-SNAPSHOT.zip` OR
    * last version from `updates\testing`
  * deploy
    * new automatic pre-process
      * create or reset branch `testing*` to latest from `main` and push, e.g. branch name `testing-my-new-build`
        * new PR with the new build will be automatically created for you
        * wait for PR to be merged to `main` by a maintainer
      * ask maintainer to create and push new tag at main branch for a pre-release with naming patterns
        * `v[0-9]+.[0-9]+.[0-9]+-alpha*`
        * `v[0-9]+.[0-9]+.[0-9]+-beta*`
        * `v[0-9]+.[0-9]+.[0-9]+-rc*`
      * new pre-release tag will be auomatically picked up and new pre-release will be created with changelog and new update site at gh-pages
    * old deprecated manual process as fallback
      * synchronize the directory `updates\testing` to project **EasyShell-gh-pages** directory `testing`
      * commit and push project **EasyShell-gh-pages** to GitHub and check the new version from site `http://anb0s.github.io/EasyShell/testing`

## Building EasyShell plugin with update site for releasing
  * go to project **EasyShell**
  * build
    * from Eclipse: run launch configuration `EasyShell-Release-Build`
    * from commandline / headless / CI server: `mvn clean verify` with parameter `site.dir=release`: `mvn -Dsite.dir=release clean verify`
  * go to project **de.anbos.eclipse.easyshell.site** and use one of:
    * directory `target\repository` OR
    * zip file e.g. `target\de.anbos.eclipse.easyshell.site-2.3.0-SNAPSHOT.zip` OR
    * last version from directory `updates\release`
  * deploy
    * new automatic pre-process
      * create or reset branch `release*` to latest from `main` and push, e.g. branch name `testing-my-new-release`
        * new PR with the new build will be automatically created for you
        * wait for PR to be merged to `main` by a maintainer
      * ask maintainer to create and push new tag at main branch for a release with naming patterns
        * `v[0-9]+.[0-9]+.[0-9]+`
      * new release tag will be auomatically picked up and new release will be created with changelog and new update site at gh-pages
    * old deprecated manual process as fallback
      * synchronize the directory `updates\release` to project **EasyShell-gh-pages** directory `release`
      * commit and push project **EasyShell-gh-pages** to GitHub and check the new version from site `http://anb0s.github.io/EasyShell`
