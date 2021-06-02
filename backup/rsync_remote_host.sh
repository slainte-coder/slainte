#!/bin/bash
############################################################################################
# $Date: 2021-04-23 07:03:59 +0200 (Fr, 23. Apr 2021) $
# $Revision: 315 $
# $Author: alfred $
# $HeadURL: https://monitoring.slainte.at/svn/slainte/trunk/scripts/rsync_remote_host.sh $
# $Id: rsync_remote_host.sh 315 2021-04-23 05:03:59Z alfred $
#
# Backup Slainte-Server
############################################################################################
shopt -o -s errexit	#—Terminates  the shell script if a command returns an error code.
shopt -o -s xtrace	#—Displays each command before it’s executed.
shopt -o -s nounset	#-No Variables without definition
IFS="
"
date
SOURCE=${1}
DEST=${2}
# Das geht mit dem aktuellen user (wenn ssh-copy-id vorher durchgeführt wurde).
##############rsync -r -t -p -o -g -v -z --delete -u -s slainte@slainte.at:/opt/backup /mnt/rasp/slainte
rsync -r -t -p -o -g -v -z --delete -u -s ${SOURCE} ${DEST}
date

