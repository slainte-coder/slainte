#!/usr/bin/env bash
############################################################################################
# $Date: 2021-02-05 09:56:20 +0100 (Fr, 05. Feb 2021) $
# $Revision: 221 $
# $Author:	alfred $
# $HeadURL: https://monitoring.slainte.at/svn/slainte/trunk/scripts/mysqldump.sh $
# $Id:	backup_and_restore.sh 113 2021-01-20 16:06:37Z alfred $
# Backupscript aus der Mailcow
############################################################################################
shopt -o -s	errexit	#—Terminates  the shell	script if a	command	returns	an error code.
#shopt -o -s xtrace	#—Displays each	command	before it’s	executed.
shopt -o -s	nounset	#-No Variables without definition
IFS="
"
#Verbindung zur Joomla-DB von aussen (port ist in der docker-config eingestellt)
mariadb -uroot -pUY7LWMh8XB4UJzfDWVFa --port=14306 --host=localhost --protocol=TCP 
#Dump der Joomla-DB
mysqldump -uroot -pUY7LWMh8XB4UJzfDWVFa --port=14306 --host=localhost --protocol=TCP joomla > joomla.sql
#Ladenn in die Datenbank auf Monitoring
sudo mariadb telegram < joomla.sql
