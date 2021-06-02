#!/bin/bash
############################################################################################
#    $Date: 2021-01-16 20:09:43 +0100 (Sat, 16 Jan 2021) $
#    $Revision: 100 $
#    $Author: alfred $
#    $HeadURL: https://monitoring.slainte.at/svn/slainte/trunk/Talend/EchoMail_Java/EchoMail_Java.sh $
#    $Id: EchoMail_Java.sh 100 2021-01-16 19:09:43Z alfred $
#
# Aufruf des Echo-Mail-Dämonen
#
############################################################################################
shopt -o -s errexit    #—Terminates  the shell script  if a command returns an error code.
shopt -o -s xtrace #—Displays each command before it’s executed.
shopt -o -s nounset #-No Variables without definition
IFS="
"
cd ${HOME}
chmod +x ./EchoMail_Java_0.1/EchoMail_Java/EchoMail_Java_run.sh 
./EchoMail_Java_0.1/EchoMail_Java/EchoMail_Java_run.sh 

