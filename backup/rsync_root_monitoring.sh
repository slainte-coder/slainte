#!/bin/bash
############################################################################################
# $Date: 2021-01-23 09:19:30 +0100 (Sa, 23. Jän 2021) $
# $Revision: 130 $
# $Author: alfred $
# $HeadURL: https://monitoring.slainte.at/svn/slainte/trunk/scripts/rsync_root_monitoring.sh $
# $Id: rsync_root_monitoring.sh 130 2021-01-23 08:19:30Z alfred $
#
# Backup Monitoring Server von / auf den USB-Stick
############################################################################################
shopt -o -s errexit	#—Terminates  the shell script if a command returns an error code.
shopt -o -s xtrace	#—Displays each command before it’s executed.
shopt -o -s nounset	#-No Variables without definition
IFS="
"
echo date
echo "$0 $@"
BACKUPDIR=$1
/usr/bin/rsync -r -t -p -o -g -v --delete -u -s /home ${BACKUPDIR}
/usr/bin/rsync -r -t -p -o -g -v --delete -u -s /opt ${BACKUPDIR}
/usr/bin/rsync -r -t -p -o -g -v --delete -u -s /etc ${BACKUPDIR}
/usr/bin/rsync -r -t -p -o -g -v --delete -u -s /root ${BACKUPDIR}
/usr/bin/rsync -r -t -p -o -g -v --delete -u -s /var/www ${BACKUPDIR}/var
echo date
echo "Ende ###################################"

