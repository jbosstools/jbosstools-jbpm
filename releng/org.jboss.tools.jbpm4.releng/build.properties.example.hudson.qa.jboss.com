## BEGIN PROJECT BUILD PROPERTIES ##

# this property allows ant-contrib and pde-svn-plugin to be fetched and installed automatically for you
thirdPartyDownloadLicenseAcceptance="I accept"

projectid=jbosstools.jbpm4
zipPrefix=jbpm4
incubation=
buildType=N
version=4.0.0

mainFeatureToBuildID=org.jboss.tools.jbpm4.sdk.feature
testFeatureToBuildID=org.jboss.tools.jbpm4.tests.feature

build.steps=buildUpdate,buildTests,generateDigests,test,publish,cleanup

# JBoss Hudson Variables defined in /home/hudson/config_repository/resources/common.variables
JAVA_HOME=${JAVA_HOME_PARENT}
JAVA14_HOME=${JAVA_HOME_PARENT}
JAVA50_HOME=${JAVA_HOME_PARENT}
JAVA60_HOME=${JAVA_HOME_PARENT}

dependencyURLs=\
http://repository.jboss.org/eclipse/galileo/GEF-runtime-3.5.1.zip,\
http://repository.jboss.org/eclipse/galileo/emf-runtime-2.5.0.zip,\
http://repository.jboss.org/eclipse/galileo/xsd-runtime-2.5.0.zip,\
http://repository.jboss.org/eclipse/galileo/wtp-R-3.1.1-20090917225226.zip,\
http://repository.jboss.org/eclipse/galileo/eclipse-SDK-3.5.1-linux-gtk-x86_64.tar.gz
#http://repository.jboss.org/eclipse/galileo/eclipse-SDK-3.5.1-linux-gtk.tar.gz

repositoryURLs=\
http://hudson.qa.jboss.com/hudson/view/DevStudio/job/jbosstools-cbi-jbpm3/lastSuccessfulBuild/artifact/build/N-SNAPSHOT/jbpm3-Update-N-SNAPSHOT.zip
featureIDsToInstall=org.jboss.tools.jbpm.common.feature,org.jboss.tools.jbpm.common.source.feature

flattenDependencies=true
parallelCompilation=true
generateFeatureVersionSuffix=true
individualSourceBundles=true

# do not sign or pack jars
#skipPack=true
skipSign=true

domainNamespace=*
projNamespace=org.jboss.tools.jbpm4
projRelengName=org.jboss.tools.jbpm4.releng

## END PROJECT BUILD PROPERTIES ##
