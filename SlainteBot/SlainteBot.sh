#!/usr/bin/bash
############################################################################################
# $Date: 2021-03-14 12:12:14 +0100 (So, 14. Mär 2021) $
# $Revision: 314 $
# $Author:	alfred $
# $HeadURL: https://monitoring.slainte.at/svn/slainte/trunk/Java/SlainteBot/SlainteBot.sh $
# $Id: SlainteBot.sh 314 2021-03-14 11:12:14Z alfred $
#
# Starten des Slainte-Bots -> Achtung auf die Parameter
#
############################################################################################
shopt -o -s errexit	#—Terminates  the shell	script if a	command	returns	an error code.
shopt -o -s xtrace	#—Displays each	command	before it’s	executed.
shopt -o -s nounset	#-No Variables without definition
IFS="
"

echo $@ > /tmp/alfred.log
date > /tmp/alfred_start.log
#BOT="telegram.slainte.Test"
BOT="telegram.slainte.Main"
#BOTDIR="/home/alfred/svn/trunk/Java"	#Zu Hause
#BOTDIR="/home/slainte"			# Am Server

/usr/lib/jvm/java-1.11.0-openjdk-amd64/bin/java -Dfile.encoding=UTF-8 -classpath ${BOTDIR}/SlainteBot/out/production/SlainteBot:${BOTDIR}/SlainteBot/lib/telegrambots-5.0.1.1.jar:${BOTDIR}/SlainteBot/lib/telegrambots-meta-5.0.1.1.jar:${BOTDIR}/SlainteBot/lib/guava-30.0-jre.jar:${BOTDIR}/SlainteBot/lib/failureaccess-1.0.1.jar:${BOTDIR}/SlainteBot/lib/listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar:${BOTDIR}/SlainteBot/lib/jsr305-3.0.2.jar:${BOTDIR}/SlainteBot/lib/checker-qual-3.5.0.jar:${BOTDIR}/SlainteBot/lib/error_prone_annotations-2.3.4.jar:${BOTDIR}/SlainteBot/lib/j2objc-annotations-1.3.jar:${BOTDIR}/SlainteBot/lib/jackson-annotations-2.11.3.jar:${BOTDIR}/SlainteBot/lib/jackson-jaxrs-json-provider-2.11.3.jar:${BOTDIR}/SlainteBot/lib/jackson-jaxrs-base-2.11.3.jar:${BOTDIR}/SlainteBot/lib/jackson-module-jaxb-annotations-2.11.3.jar:${BOTDIR}/SlainteBot/lib/jackson-core-2.11.3.jar:${BOTDIR}/SlainteBot/lib/jakarta.xml.bind-api-2.3.2.jar:${BOTDIR}/SlainteBot/lib/jakarta.activation-api-1.2.1.jar:${BOTDIR}/SlainteBot/lib/jackson-databind-2.11.3.jar:${BOTDIR}/SlainteBot/lib/jersey-hk2-2.32.jar:${BOTDIR}/SlainteBot/lib/jersey-common-2.32.jar:${BOTDIR}/SlainteBot/lib/osgi-resource-locator-1.0.3.jar:${BOTDIR}/SlainteBot/lib/jakarta.activation-1.2.2.jar:${BOTDIR}/SlainteBot/lib/hk2-locator-2.6.1.jar:${BOTDIR}/SlainteBot/lib/aopalliance-repackaged-2.6.1.jar:${BOTDIR}/SlainteBot/lib/hk2-api-2.6.1.jar:${BOTDIR}/SlainteBot/lib/hk2-utils-2.6.1.jar:${BOTDIR}/SlainteBot/lib/javassist-3.25.0-GA.jar:${BOTDIR}/SlainteBot/lib/jersey-media-json-jackson-2.32.jar:${BOTDIR}/SlainteBot/lib/jersey-entity-filtering-2.32.jar:${BOTDIR}/SlainteBot/lib/jersey-container-grizzly2-http-2.32.jar:${BOTDIR}/SlainteBot/lib/jakarta.inject-2.6.1.jar:${BOTDIR}/SlainteBot/lib/grizzly-http-server-2.4.4.jar:${BOTDIR}/SlainteBot/lib/grizzly-http-2.4.4.jar:${BOTDIR}/SlainteBot/lib/grizzly-framework-2.4.4.jar:${BOTDIR}/SlainteBot/lib/jakarta.ws.rs-api-2.1.6.jar:${BOTDIR}/SlainteBot/lib/jersey-server-2.32.jar:${BOTDIR}/SlainteBot/lib/jersey-client-2.32.jar:${BOTDIR}/SlainteBot/lib/jersey-media-jaxb-2.32.jar:${BOTDIR}/SlainteBot/lib/jakarta.annotation-api-1.3.5.jar:${BOTDIR}/SlainteBot/lib/jakarta.validation-api-2.0.2.jar:${BOTDIR}/SlainteBot/lib/json-20180813.jar:${BOTDIR}/SlainteBot/lib/httpclient-4.5.13.jar:${BOTDIR}/SlainteBot/lib/httpcore-4.4.13.jar:${BOTDIR}/SlainteBot/lib/commons-logging-1.2.jar:${BOTDIR}/SlainteBot/lib/commons-codec-1.11.jar:${BOTDIR}/SlainteBot/lib/httpmime-4.5.13.jar:${BOTDIR}/SlainteBot/lib/commons-io-2.8.0.jar:${BOTDIR}/SlainteBot/lib/slf4j-api-1.7.30.jar:${BOTDIR}/SlainteBot/lib/mysql-connector-java-8.0.23.jar:${BOTDIR}/SlainteBot/lib/protobuf-java-3.11.4.jar:/snap/intellij-idea-community/273/lib/idea_rt.jar  ${BOT} $@ &
wait $!
date > /tmp/alfred_end.log

