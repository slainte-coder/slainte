#!/bin/bash
############################################################################################
# $Date: 2021-01-27 10:46:34 +0100 (Mi, 27. Jän 2021) $
# $Revision: 145 $
# $Author: alfred $
# $HeadURL: https://monitoring.slainte.at/svn/slainte/trunk/scripts/rsync_root_slainte.sh $
# $Id: rsync_root_slainte.sh 145 2021-01-27 09:46:34Z alfred $
#
# Backup Daten vom Slainte-Server auf das BackupDirectory
############################################################################################
shopt -o -s errexit	#—Terminates  the shell script if a command returns an error code.
shopt -o -s xtrace	#—Displays each command before it’s executed.
shopt -o -s nounset	#-No Variables without definition
IFS="
"
echo "Start ###################################"
date
echo "$0 $@"
BACKUPDIR=$1

/usr/bin/rsync -r -t -p -o -g -v --delete -u -s --exclude /home/sftpuser /home ${BACKUPDIR}
/usr/bin/rsync -r -t -p -o -g -v --delete -u -s /opt/icinga ${BACKUPDIR}
/usr/bin/rsync -r -t -p -o -g -v --delete -u -s /etc ${BACKUPDIR}
/usr/bin/rsync -r -t -p -o -g -v --delete -u -s /root ${BACKUPDIR}
###################/usr/bin/rsync -r -t -p -o -g -v --delete -u -s /var/www ${BACKUPDIR}/var
/usr/bin/rsync -r -t -p -o -g -v --delete -u -s /usr/lib/nagios ${BACKUPDIR}/usr/lib
/usr/bin/rsync -r -t -p -o -g -v --delete -u -s --exclude joomla/database --exclude lions/mariadb --exclude nextcloud/database --exclude nextcloud/daten /opt/containers ${BACKUPDIR}

date
cd ${BACKUPDIR}
find ./ -type d -exec chmod +rx {} \;
find ./ -type f -exec chmod +r {} \;
date
echo "Ende ###################################"
