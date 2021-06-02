#!/bin/bash
############################################################################################
# $Date: 2021-01-22 17:23:00 +0100 (Fr, 22. Jän 2021) $
# $Revision: 128 $
# $Author: alfred $
# $HeadURL: https://monitoring.slainte.at/svn/slainte/trunk/scripts/backup_mariadb.sh $
# $Id: backup_mariadb.sh 128 2021-01-22 16:23:00Z alfred $
# Backup aller Datenbanken in der MariaDB
############################################################################################
shopt -o -s errexit #—Terminates  the shell script if a command returns an error code.
shopt -o -s xtrace #—Displays each command before it’s executed.
shopt -o -s nounset #-No Variables withou definition
IFS="
"

HOLD_DAYS=14
DATE=$(date +"%Y-%m-%d-%H-%M-%S")
BACKUP_DIR="/opt/backup/mariadb"
TMPDIR="/tmp/backup"
DB=$(uname -n)
echo
echo "Starting backup ..."

if [[ -d "${TMPDIR}" ]]; then
  rm -rf ${TMPDIR}
fi
mariabackup --user root --backup --rsync --target-dir=${TMPDIR}
mariabackup --prepare --target-dir=${TMPDIR}
chown -R 999:999 "${TMPDIR}"
/bin/tar --warning='no-file-ignored' --use-compress-program='gzip --rsyncable' -Pcvpf "${BACKUP_DIR}/${DB}_${DATE}.tar.gz" ${TMPDIR}

echo
echo "Cleaning up ..."
find $BACKUP_DIR -type f -mtime +$HOLD_DAYS  -exec rm -rf {} \;

if [[ -d "${TMPDIR}" ]]; then
  rm -rf ${TMPDIR}
fi
echo "-- DONE!"
