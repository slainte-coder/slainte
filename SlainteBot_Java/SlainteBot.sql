------------------------------------------------------------------------------------------
-- $Date: 2021-02-09 21:39:16 +0100 (Di, 09. Feb 2021) $
-- $Revision: 254 $
-- $Author:	alfred $
-- $HeadURL: https://monitoring.slainte.at/svn/slainte/trunk/Talend/SlainteBot_Java/SlainteBot.sql $
-- $Id: SlainteBot.sql 254 2021-02-09 20:39:16Z alfred $
-- Schema für den TelegramBot
------------------------------------------------------------------------------------------

-- joomla.SlainteBot definition

CREATE TABLE SlainteBot (
  ID int(11) NOT NULL AUTO_INCREMENT COMMENT 'Interne ID',
  Datum datetime NOT NULL DEFAULT current_timestamp() COMMENT 'Zeitpunt an dem der Bot gestartet wurde',
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='Startzeitpunkte des Bots';


-- joomla.SlainteBot_Users definition

CREATE TABLE SlainteBot_Users (
  userId int(11) NOT NULL COMMENT 'UserID aus Telegram',
  status int(11) NOT NULL COMMENT 'Aktueller Status des Users im aktuellen Chat, 0=angelegt, 1=aktiv',
  Firstname varchar(100) DEFAULT NULL COMMENT 'Vorname',
  Lastname varchar(100) DEFAULT NULL COMMENT 'Nachname',
  Username varchar(100) DEFAULT NULL COMMENT 'Benutzername in Telegram',
  Phonenumber varchar(100) DEFAULT NULL COMMENT 'Telephonnummer in Telegram',
  VCARD mediumtext DEFAULT NULL COMMENT 'Gespeicherte VCARD in Telegram',
  Bot tinyint(1) DEFAULT NULL COMMENT 'Ist das ein Bot?',
  LanguageCode varchar(10) DEFAULT NULL COMMENT 'Spracheinstellungen des Users',
  CreationDate datetime DEFAULT current_timestamp() COMMENT 'Zeitpunkt des erstmaligen Kontaktes',
  PRIMARY KEY (userId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Users aus dem TelegramChat mit aktuellem Chatstatus';


-- joomla.SlainteBot_Versions definition

CREATE TABLE SlainteBot_Versions (
  ID int(11) NOT NULL AUTO_INCREMENT COMMENT 'Interne ID',
  Version int(11) DEFAULT NULL COMMENT 'Versionsnummer des DB-Schemas',
  VersionsDatum datetime DEFAULT current_timestamp() COMMENT 'Zeitpunt an dem die Version angelegt wurde',
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='Installierte Datenbankversion';


-- joomla.SlainteBot_Command_Read definition

CREATE TABLE SlainteBot_Command_Read (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT 'Interne ID',
  userId int(11) NOT NULL COMMENT 'UserID aus SlainteBot_Users',
  ChatID int(11) NOT NULL COMMENT 'ID der Gruppe (spreche ich mit einer Gruppe oder einem User)',
  Message mediumtext DEFAULT NULL COMMENT 'Nachricht aus Telegram',
  Datum datetime DEFAULT current_timestamp() COMMENT 'Zeitpunkt der Nachricht',
  Description varchar(256) DEFAULT NULL COMMENT 'Beschreibung des Chats',
  AllMembersAreAdministrators tinyint(1) DEFAULT NULL COMMENT 'Sind alle Admins?',
  CanSetStickerSet tinyint(1) DEFAULT NULL COMMENT 'Stickerset?',
  InviteLink varchar(256) DEFAULT NULL COMMENT 'Link der Einladung',
  Photo varchar(256) DEFAULT NULL COMMENT 'Photo',
  PinnedMessage varchar(256) DEFAULT NULL COMMENT 'PinnedMessage',
  StickerSetName varchar(256) DEFAULT NULL COMMENT 'StickerSetName',
  Title varchar(256) DEFAULT NULL COMMENT 'Title',
  ChannelChat tinyint(1) DEFAULT NULL COMMENT 'ChannelChat?',
  GroupChat tinyint(1) DEFAULT NULL COMMENT 'GroupChat?',
  SuperGroupChat tinyint(1) DEFAULT NULL COMMENT 'SuperGroupChat?',
  UserChat tinyint(1) DEFAULT NULL COMMENT 'UserChat?',
  PRIMARY KEY (id),
  KEY CommandHistory_FK (userId),
  CONSTRAINT Command_Read_FK FOREIGN KEY (userId) REFERENCES SlainteBot_Users (userId) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='Alle Nachrichten, die der Bot bekommt';


-- joomla.SlainteBot_Command_Send definition

CREATE TABLE SlainteBot_Command_Send (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT 'Interne ID',
  userId int(11) NOT NULL COMMENT 'UserID aus SlainteBot_Users',
  Message mediumtext DEFAULT NULL COMMENT 'Zu versendende Nachricht',
  Datum datetime DEFAULT current_timestamp() COMMENT 'Zeitpunkt der Nachricht',
  PRIMARY KEY (id),
  KEY CommandHistory_FK (userId),
  CONSTRAINT Command_Send_FK FOREIGN KEY (userId) REFERENCES SlainteBot_Users (userId) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='Alle Nachrichten, die der Bot versendet';


-- joomla.SlainteBot_UserStatus definition

CREATE TABLE SlainteBot_UserStatus (
  userId int(11) NOT NULL COMMENT 'UserID aus SlainteBot_Users',
  UserMenu varchar(10) NOT NULL DEFAULT 'NOMENU' COMMENT 'Aktuelles Menu des Benutzers ',
  PRIMARY KEY (userId),
  KEY UserStatus_FK (userId),
  CONSTRAINT UserStatus_Read_FK FOREIGN KEY (userId) REFERENCES SlainteBot_Users (userId) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Aktuelles Menu des Benutzers';


-- joomla.SlainteBot_User_Gelesen source

CREATE OR REPLACE
ALGORITHM = UNDEFINED VIEW SlainteBot_User_Gelesen AS
select
    u.Firstname AS Firstname,
    count(cr.id) AS val
from
    (SlainteBot_Users u
join SlainteBot_Command_Read cr)
where
    cr.userId = u.userId
group by
    u.Firstname
order by
    u.Firstname;


-- joomla.SlainteBot_User_Gesendet source

CREATE OR REPLACE
ALGORITHM = UNDEFINED VIEW SlainteBot_User_Gesendet AS
select
    u.Firstname AS Firstname,
    count(cs.id) AS val
from
    (SlainteBot_Users u
join SlainteBot_Command_Send cs)
where
    cs.userId = u.userId
group by
    u.Firstname
order by
    u.Firstname;

