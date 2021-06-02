#!/bin/bash
############################################################################################
# $Date: 2021-02-09 09:41:30 +0100 (Di, 09. Feb 2021) $
# $Revision: 247 $
# $Author: alfred $
# $HeadURL: https://monitoring.slainte.at/svn/slainte/trunk/scripts/dump_mailcow.sh $
# $Id: dump_mailcow.sh 247 2021-02-09 08:41:30Z alfred $
# Dumpen (von aussen) der Inhalte der Maria-DB
#
# $0 /opt/containers/mailcow/mailcow.conf 
#
############################################################################################
shopt -o -s errexit #—Terminates  the shell script if a command returns an error code.
shopt -o -s xtrace #—Displays each command before it’s executed.
shopt -o -s nounset #-No Variables without definition
IFS="
"

date
BACKUP_DIR="/opt/backup/dump"
db="joomla"
yml=${1}

prefix="DBROOT="
suffix=""
MYSQL_ROOT_PASSWORD=$(grep ${prefix} ${yml})
MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD#*"$prefix"}
MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD%"$suffix"*}

prefix="SQL_PORT=127.0.0.1:"
suffix=""
MYSQL_PORT=$(grep ${prefix} ${yml})
MYSQL_PORT=${MYSQL_PORT#*"$prefix"}
MYSQL_PORT=${MYSQL_PORT%"$suffix"*}

databases=`mysql -uroot -p${MYSQL_ROOT_PASSWORD} --port=${MYSQL_PORT} --host=localhost --protocol=TCP -e "SHOW DATABASES;" | tr -d "| " | grep -v Database`

for db in $databases; do
    if [[ "$db" != "information_schema" ]] && [[ "$db" != "performance_schema" ]] && [[ "$db" != "mysql" ]] && [[ "$db" != _* ]] ; then
        echo "Dumping database: $db"
        mysqldump -uroot -p${MYSQL_ROOT_PASSWORD} --port=${MYSQL_PORT} --host=localhost --protocol=TCP --add-drop-database --databases $db > ${BACKUP_DIR}/${db}.sql
        gzip -f --rsyncable --best ${BACKUP_DIR}/${db}.sql
    fi
done

date
