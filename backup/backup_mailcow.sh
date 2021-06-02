#!/bin/bash
############################################################################################
# $Date: 2021-01-21 20:40:37 +0100 (Do, 21. Jän 2021) $
# $Revision: 124 $
# $Author: alfred $
# $HeadURL: https://monitoring.slainte.at/svn/slainte/trunk/scripts/backup_mailcow.sh $
# $Id: backup_mailcow.sh 124 2021-01-21 19:40:37Z alfred $
# Richtiges starten des Backups für die Mailcow
############################################################################################
shopt -o -s errexit #—Terminates  the shell script if a command returns an error code.
shopt -o -s xtrace #—Displays each command before it’s executed.
shopt -o -s nounset #-No Variables withou definition
IFS="
"
# Backup all, delete backups older than 15 days
date
export MAILCOW_BACKUP_LOCATION="/opt/backup/mailcow"
cd /opt/containers/mailcow/helper-scripts/
./backup_and_restore.sh backup all --delete-days 15
date
