#!/bin/bash
############################################################################################
# $Date: 2021-02-07 16:39:31 +0100 (So, 07. Feb 2021) $
# $Revision: 231 $
# $Author: alfred $
# $HeadURL: https://monitoring.slainte.at/svn/slainte/trunk/scripts/load_joomla_files.sh $
# $Id: load_joomla_files.sh 231 2021-02-07 15:39:31Z alfred $
# Syncen der File-Inhalte
############################################################################################
shopt -o -s errexit #—Terminates  the shell script if a command returns an error code.
shopt -o -s xtrace #—Displays each command before it’s executed.
shopt -o -s nounset #-No Variables without definition
IFS="
"

date
# Sichern der configuration
#
# Am besten nur images kopieren!
#
mv -f /var/www/html/joomla/configuration.php /root/joomla_configuration.php
/usr/bin/rsync -r -t -p -o -g -v --delete -u -s --exclude /mnt/rasp/slainte/backup/containers/joomla/app/libraries  /mnt/rasp/slainte/backup/containers/joomla/app/ /var/www/html/joomla
cp -f /root/joomla_configuration.php /var/www/html/joomla/configuration.php 
date
