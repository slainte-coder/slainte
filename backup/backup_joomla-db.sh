#!/bin/bash
############################################################################################
# $Date: 2021-01-23 17:26:16 +0100 (Sa, 23. Jän 2021) $
# $Revision: 135 $
# $Author: alfred $
# $HeadURL: https://monitoring.slainte.at/svn/slainte/trunk/scripts/backup_joomla-db.sh $
# $Id: backup_joomla-db.sh 135 2021-01-23 16:26:16Z alfred $
# Richtiges starten des Backups für den Joomla-Container
############################################################################################
shopt -o -s errexit #—Terminates  the shell script if a command returns an error code.
shopt -o -s xtrace #—Displays each command before it’s executed.
shopt -o -s nounset #-No Variables withou definition
IFS="
"
# Backup all, delete backups older than 15 days
date
HOLD_DAYS=14
DATE=$(date +"%Y-%m-%d-%H-%M-%S")
BACKUP_DIR="/opt/backup/joomla-db"
TMPDIR="/opt/backup/joomla"
DB="joomla.slainte.at"

prefix="MYSQL_ROOT_PASSWORD="
suffix=" #SQL"
MYSQL_ROOT_PASSWORD=$(grep ${prefix} /opt/containers/joomla/docker-compose.yml)
MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD#*"$prefix"}
MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD%"$suffix"*}

# Vorsorgliches Bereinigen der TempDir
cd  ${TMPDIR}
rm -d -f -R *
#
# ${TMPDIR} /backup ist im docker-compose definiert
docker exec slainte-joomladb \
        /bin/bash -c "mariabackup --host localhost --user root --password ${MYSQL_ROOT_PASSWORD} --backup --rsync --target-dir=/backup ;
		mariabackup   --prepare --target-dir=/backup ;
		chown -R 999:999 /backup ;"
/bin/tar --warning='no-file-ignored' --use-compress-program='gzip --rsyncable' -Pcvpf "${BACKUP_DIR}/${DB}_${DATE}.tar.gz" ${TMPDIR}
cd  ${TMPDIR}
rm -d -f -R *

echo
echo "Cleaning up ..."
find $BACKUP_DIR -type f -mtime +$HOLD_DAYS  -exec rm -rf {} \;
date
