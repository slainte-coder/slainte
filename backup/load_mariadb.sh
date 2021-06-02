#!/bin/bash
############################################################################################
# $Date: 2021-02-09 10:51:37 +0100 (Di, 09. Feb 2021) $
# $Revision: 248 $
# $Author: alfred $
# $HeadURL: https://monitoring.slainte.at/svn/slainte/trunk/scripts/load_mariadb.sh $
# $Id: load_mariadb.sh 248 2021-02-09 09:51:37Z alfred $
# Laden (von aussen) des sqldump-files in eine Maria-DB
#
# $0 dbname
#
# dbame-tag muß heissen wie das dump-file, wie der User in der DB, wie das conf
#
############################################################################################
#shopt -o -s errexit #—Terminates  the shell script if a command returns an error code.
shopt -o -s xtrace #—Displays each command before it’s executed.
shopt -o -s nounset #-No Variables without definition
IFS="
"

date
BACKUP_DIR="/mnt/rasp/slainte/backup/dump"
db=${1}
yml="/root/${db}.conf"

prefix="MYSQL_ROOT_PASSWORD="
suffix=" #SQL"
MYSQL_ROOT_PASSWORD=$(grep ${prefix} ${yml})
MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD#*"$prefix"}
MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD%"$suffix"*}

prefix="port="
suffix=":"
MYSQL_PORT=$(grep ${prefix} ${yml})
MYSQL_PORT=${MYSQL_PORT#*"$prefix"}
MYSQL_PORT=${MYSQL_PORT%"$suffix"*}

# Löschen innerhalb des Schemas
# drop database ist im dump-file enthalten.
gzip --force --keep --decompress ${BACKUP_DIR}/${db}.sql.gz # Unzip
mysql -u${db} -p${MYSQL_ROOT_PASSWORD} --port=${MYSQL_PORT} --host=localhost --protocol=TCP ${db} < ${BACKUP_DIR}/${db}.sql # Laden
# Jetzt ist alles in das Schema geladen"
date
