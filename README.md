# york-SDK
This a Java SDK to access The One API: https://the-one-api.dev/

to deploy snapshot: mvn clean deploy (check https://s01.oss.sonatype.org/content/repositories/snapshots/)
to deploy release:
mvn versions:set -DnewVersion=1.2.3
mvn clean deploy -P release
check https://s01.oss.sonatype.org/#stagingRepositories, close and release it
check maven central https://repo1.maven.org/maven2

