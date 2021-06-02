/*
 ***************************************************************************************************
 * $Date: 2021-02-05 16:03:48 +0100 (Fri, 05 Feb 2021) $
 * $Revision: 223 $
 * $Author: alfred $
 * $HeadURL: https://monitoring.slainte.at/svn/slainte/trunk/Java/SlainteBot/src/telegram/slainte/DatabaseManager.java $
 * $Id: DatabaseManager.java 223 2021-02-05 15:03:48Z alfred $
 ***************************************************************************************************
 */
package telegram.slainte;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * @author $Author: alfred $
 * @version 1.0 $Revision: 223 $
 * @brief Slainte Bot. Verwalten der Datenbank
 * @date $Date: 2021-02-05 16:03:48 +0100 (Fri, 05 Feb 2021) $
 * @head $HeadURL: https://monitoring.slainte.at/svn/slainte/trunk/Java/SlainteBot/src/telegram/slainte/DatabaseManager.java $
 */
public class DatabaseManager {
    private static final String LOGTAG = "DATABASEMANAGER";

    private static volatile DatabaseManager instance;
    private static volatile ConectionDB connection;

    public static final int version = 1;

    public static final String createBotTable = "CREATE TABLE IF NOT EXISTS SlainteBot (\n" +
            "  ID int(11) NOT NULL AUTO_INCREMENT COMMENT 'Interne ID',\n" +
            "  Datum datetime NOT NULL DEFAULT current_timestamp() COMMENT 'Zeitpunt an dem der Bot gestartet wurde',\n" +
            "  PRIMARY KEY (ID)\n" +
            ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='Startzeitpunkte des Bots'";

    public static final String createVersionTable = "CREATE TABLE IF NOT EXISTS SlainteBot_Versions (\n" +
            "  ID int(11) NOT NULL AUTO_INCREMENT COMMENT 'Interne ID',\n" +
            "  Version int(11) DEFAULT NULL COMMENT 'Versionsnummer des DB-Schemas',\n" +
            "  VersionsDatum datetime DEFAULT current_timestamp() COMMENT 'Zeitpunt an dem die Verssion angelegt wurde',\n" +
            "  PRIMARY KEY (ID)\n" +
            ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='Installierte Datenbankversion'";

    public static final String insertBotStart = "INSERT IGNORE INTO SlainteBot (Datum) VALUES (NOW())";

    public static final String insertCurrentVersion = "INSERT IGNORE INTO SlainteBot_Versions (Version,Versionsdatum) VALUES(%d,NOW())";

    public static final String createUsersTable = "CREATE TABLE IF NOT EXISTS SlainteBot_Users (\n" +
            "  userId int(11) NOT NULL COMMENT 'UserID aus Telegram',\n" +
            "  status int(11) NOT NULL COMMENT 'Aktueller Status des Users im aktuellen Chat, 0=angelegt, 1=aktiv',\n" +
            "  Firstname varchar(100) DEFAULT NULL COMMENT 'Vorname',\n" +
            "  Lastname varchar(100) DEFAULT NULL COMMENT 'Nachname',\n" +
            "  Username varchar(100) DEFAULT NULL COMMENT 'Benutzername in Telegram',\n" +
            "  Phonenumber varchar(100) DEFAULT NULL COMMENT 'Telephonnummer in Telegram',\n" +
            "  VCARD mediumtext DEFAULT NULL COMMENT 'Gespeicherte VCARD in Telegram',\n" +
            "  Bot tinyint(1) DEFAULT NULL COMMENT 'Ist das ein Bot?',\n" +
            "  LanguageCode varchar(10) DEFAULT NULL COMMENT 'Spracheinstellungen des Users',\n" +
            "  CreationDate datetime DEFAULT current_timestamp() COMMENT 'Zeitpunkt des erstmaligen Kontaktes',\n"+
            "  PRIMARY KEY (userId)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Users aus dem TelegramChat mit aktuellem Chatstatus'";

    public static final String createUserStatus = "CREATE TABLE SlainteBot_UserStatus (\n" +
            "  userId int(11) NOT NULL COMMENT 'UserID aus SlainteBot_Users',\n" +
            "  UserMenu varchar(10) NOT NULL DEFAULT 'NOMENU' COMMENT 'Aktuelles Menu des Benutzers ',\n" +
            "  PRIMARY KEY (userid),\n" +
            "  KEY `UserStatus_FK` (userId),\n" +
            "  CONSTRAINT UserStatus_Read_FK FOREIGN KEY (userId) REFERENCES SlainteBot_Users (userId) ON DELETE CASCADE ON UPDATE CASCADE\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Aktueller Status des Benutzers'";

    public static final String createCommandRead = "CREATE TABLE IF NOT EXISTS SlainteBot_Command_Read (\n" +
            "  id int(11) NOT NULL AUTO_INCREMENT COMMENT 'Interne ID',\n" +
            "  userId int(11) NOT NULL COMMENT 'UserID aus SlainteBot_Users',\n" +
            "  ChatID int(11) NOT NULL COMMENT 'ID der Gruppe (spreche ich mit einer Gruppe oder einem User)',\n" +
            "  Message mediumtext DEFAULT NULL COMMENT 'Message aus Telegram',\n" +
            "  Datum datetime DEFAULT current_timestamp() COMMENT 'Zeitpunkt der Nachricht',\n" +
            "  Description varchar(256) default Null comment 'Beschreibung des Chats',\n" +
            "  AllMembersAreAdministrators tinyint(1) DEFAULT NULL COMMENT 'Sind alle Admins?',\n" +
            "  CanSetStickerSet tinyint(1) DEFAULT NULL COMMENT 'Stickerset?',\n" +
            "  InviteLink varchar(256) default Null comment 'Link der Einladung',\n" +
            "  Photo varchar(256) default Null comment 'Photo',\n" +
            "  PinnedMessage varchar(256) default Null comment 'PinnedMessage',\n" +
            "  StickerSetName varchar(256) default Null comment 'StickerSetName',\n" +
            "  Title varchar(256) default Null comment 'Title',\n" +
            "  ChannelChat tinyint(1) DEFAULT NULL COMMENT 'ChannelChat?',\n" +
            "  GroupChat tinyint(1) DEFAULT NULL COMMENT 'GroupChat?',\n" +
            "  SuperGroupChat tinyint(1) DEFAULT NULL COMMENT 'SuperGroupChat?',\n" +
            "  UserChat tinyint(1) DEFAULT NULL COMMENT 'UserChat?',\n" +
            "  PRIMARY KEY (id),\n" +
            "  KEY CommandHistory_FK (userId),\n" +
            "  CONSTRAINT Command_Read_FK FOREIGN KEY (userId) REFERENCES SlainteBot_Users (userId) ON DELETE CASCADE ON UPDATE CASCADE\n" +
            ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='Alle Kommandos, die der Bot bekommt'";

    public static final String createCommandSend = "CREATE TABLE IF NOT EXISTS SlainteBot_Command_Send (\n" +
            "  id int(11) NOT NULL AUTO_INCREMENT COMMENT 'Interne ID',\n" +
            "  userId int(11) NOT NULL COMMENT 'UserID aus SlainteBot_Users',\n" +
            "  Message mediumtext DEFAULT NULL COMMENT 'Message aus Telegram',\n" +
            "  Datum datetime DEFAULT current_timestamp() COMMENT 'Zeitpunkt der Nachricht',\n" +
            "  PRIMARY KEY (id),\n" +
            "  KEY CommandHistory_FK (userId),\n" +
            "  CONSTRAINT Command_Send_FK FOREIGN KEY (userId) REFERENCES SlainteBot_Users (userId) ON DELETE CASCADE ON UPDATE CASCADE\n" +
            ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='Alle Kommandos, die der Bot bekommt'";

    public static final String selectContent = "select title,introtext, `fulltext` from fdi67_content fc where state=1 and \n" +
            "catid = (select fcat.id from fdi67_categories fcat where fcat.alias ='telegrambot')\n" +
            "order by modified DESC";



    /**
     * no singleton anymore, immer brav auf und zu
     */
    public DatabaseManager(String controllerDB,String linkDB,String userDB,String password) {
        String LOGTAG = "DATABASEMANAGER/INIT";
        connection = new ConectionDB(controllerDB, linkDB, userDB, password);
        final int currentVersion = connection.checkVersion();
        //System.out.println(LOGTAG+"Current db version: " + currentVersion);
        if (currentVersion < version) {
            recreateTable(currentVersion);
        }
    }

    public void insertbotstart() {
        try {
            connection.executeQuery(insertBotStart);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println(LOGTAG + " " + throwables.toString());
        }
    }

    /**
     * Get Singleton instance
     *
     * @return instance of the class
     */
    public static DatabaseManager getInstance(String controllerDB,String linkDB,String userDB,String password) {
        String LOGTAG = "DATABASEMANAGER/GETINSTANCE";
        final DatabaseManager currentInstance;
        if (instance == null) {
            synchronized (DatabaseManager.class) {
                if (instance == null) {
                    instance = new DatabaseManager(controllerDB,linkDB,userDB,password);
                }
                currentInstance = instance;
            }
        } else {
            currentInstance = instance;
        }
        return currentInstance;
    }

    // Jetzt sollte es bereits eine geben!
    public static DatabaseManager getInstance()  {
        final DatabaseManager currentInstance;
        currentInstance = instance;
        if (instance == null) {
            System.out.println("Datenbankinstanz wurde noch nicht verbunden!");
        }
        return currentInstance;
    }

    /**
     * Close connection
     */
    public void close() {
        String LOGTAG = "DATABASEMANAGER/RECREATETABLE";
        try {
            connection.commitTransaction();
            connection.closeConexion();
            instance=null;
        } catch (SQLException e) {
            System.out.println(LOGTAG+" "+ e.toString());
        }
    }

    private void recreateTable(int currentVersion) {
        String LOGTAG = "DATABASEMANAGER/RECREATETABLE";
        try {
            connection.initTransaction();
            if (currentVersion == 0) {
                createNewTables();
            }
            connection.commitTransaction();
        } catch (SQLException e) {
            System.out.println(LOGTAG+" "+ e.toString());
        }
    }

    private int createNewTables() throws SQLException {
        connection.executeQuery(createBotTable);
        connection.executeQuery(createVersionTable);
        connection.executeQuery(String.format(insertCurrentVersion, version));
        connection.executeQuery(createUsersTable);
        connection.executeQuery(createUserStatus);
        connection.executeQuery(createCommandRead);
        connection.executeQuery(createCommandSend);
        return version;
    }

    public void logUser(Message message) {
        String LOGTAG = "DATABASEMANAGER/LOGUSER";
        int updatedRows;
        try {
            connection.initTransaction();
            final PreparedStatement preparedStatement = connection.getPreparedStatement("Insert into SlainteBot_Users (userId, status, Firstname, Lastname, Username, Bot, LanguageCode, Phonenumber, VCARD) VALUES \n"+
                    "(?, ?, ?, ?, ?, ?, ?,?,?)\n"+
                    "ON DUPLICATE KEY UPDATE status=?,Firstname=?, Lastname=?, Username=?, Bot=?, LanguageCode=?, Phonenumber=?, VCARD=?");

            preparedStatement.setInt(1, message.getFrom().getId());
            preparedStatement.setInt(2, 1);
            preparedStatement.setString(3, message.getFrom().getFirstName());
            preparedStatement.setString(4, message.getFrom().getLastName());
            preparedStatement.setString(5, message.getFrom().getUserName());
            preparedStatement.setBoolean(6, message.getFrom().getIsBot());
            preparedStatement.setString(7, message.getFrom().getLanguageCode());
            if (message.getContact() == null) {
                preparedStatement.setString(8, null);
                preparedStatement.setString(9, null);
            }
            else {
                preparedStatement.setString(8, message.getContact().getPhoneNumber());
                preparedStatement.setString(9, message.getContact().getVCard());
            }

            preparedStatement.setInt(10, 1);
            preparedStatement.setString(11, message.getFrom().getFirstName());
            preparedStatement.setString(12, message.getFrom().getLastName());
            preparedStatement.setString(13, message.getFrom().getUserName());
            preparedStatement.setBoolean(14, message.getFrom().getIsBot());
            preparedStatement.setString(15, message.getFrom().getLanguageCode());
            if (message.getContact() == null) {
                preparedStatement.setString(16, null);
                preparedStatement.setString(17, null);
            }
            else {
                preparedStatement.setString(16, message.getContact().getPhoneNumber());
                preparedStatement.setString(17, message.getContact().getVCard());
            }

            preparedStatement.executeUpdate();
            connection.commitTransaction();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(LOGTAG+" "+e.toString());
        }
    }

    public void logChat(Message message) {
        String LOGTAG = "DATABASEMANAGER/LOGCHAT";
        int updatedRows;
        try {
            connection.initTransaction();
            final PreparedStatement preparedStatement = connection.getPreparedStatement("Insert into SlainteBot_Users (userId, status, Firstname, Lastname, Username, Bot, LanguageCode, Phonenumber, VCARD) VALUES \n"+
                    "(?, ?, ?, ?, ?, ?, ?,?,?)\n"+
                    "ON DUPLICATE KEY UPDATE status=?,Firstname=?, Lastname=?, Username=?, Bot=?, LanguageCode=?, Phonenumber=?, VCARD=?");

            preparedStatement.setInt(1, Math.toIntExact(message.getChat().getId()));
            preparedStatement.setInt(2, 1);
            preparedStatement.setString(3, message.getChat().getTitle());
            preparedStatement.setString(4, message.getChat().getTitle());
            preparedStatement.setString(5, message.getChat().getTitle());
            preparedStatement.setBoolean(6, message.getFrom().getIsBot());
            preparedStatement.setString(7, message.getFrom().getLanguageCode());
            if (message.getContact() == null) {
                preparedStatement.setString(8, null);
                preparedStatement.setString(9, null);
            }
            else {
                preparedStatement.setString(8, message.getContact().getPhoneNumber());
                preparedStatement.setString(9, message.getContact().getVCard());
            }

            preparedStatement.setInt(10, 1);
            preparedStatement.setString(11, message.getChat().getTitle());
            preparedStatement.setString(12, message.getChat().getTitle());
            preparedStatement.setString(13, message.getChat().getTitle());
            preparedStatement.setBoolean(14, message.getFrom().getIsBot());
            preparedStatement.setString(15, message.getFrom().getLanguageCode());
            if (message.getContact() == null) {
                preparedStatement.setString(16, null);
                preparedStatement.setString(17, null);
            }
            else {
                preparedStatement.setString(16, message.getContact().getPhoneNumber());
                preparedStatement.setString(17, message.getContact().getVCard());
            }

            preparedStatement.executeUpdate();
            connection.commitTransaction();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(LOGTAG+" "+e.toString());
        }
    }

    public void logChat_Request(Message message) {
        String LOGTAG = "DATABASEMANAGER/LOGCHATREQUEST";
        int updatedRows;

        logUser(message);
        try {
            connection.initTransaction();
            final PreparedStatement preparedStatement = connection.getPreparedStatement("INSERT INTO SlainteBot_Command_Read\n" +
                    "(userId, ChatID, Description, AllMembersAreAdministrators, CanSetStickerSet, InviteLink, Photo, PinnedMessage, StickerSetName, Title, ChannelChat, GroupChat, SuperGroupChat, UserChat, Message)\n" +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            preparedStatement.setInt(1, message.getFrom().getId());
            preparedStatement.setLong(2, message.getChatId());
            preparedStatement.setString(3, message.getChat().getDescription());
            if (message.getChat().getAllMembersAreAdministrators() == null) {
                preparedStatement.setBoolean(4, false);
            }
            else {
                preparedStatement.setBoolean(4, message.getChat().getAllMembersAreAdministrators());
            }
            preparedStatement.setString(5, null); //chat.getCanSetStickerSet()
            preparedStatement.setString(6, message.getChat().getInviteLink());
            preparedStatement.setString(7, null); //chat.getPhoto()
            preparedStatement.setString(8, null); //chat.getPinnedMessage()
            preparedStatement.setString(9, message.getChat().getStickerSetName());
            preparedStatement.setString(10, message.getChat().getTitle());
            preparedStatement.setBoolean(11, message.getChat().isChannelChat());
            preparedStatement.setBoolean(12, message.getChat().isGroupChat());
            preparedStatement.setBoolean(13, message.getChat().isSuperGroupChat());
            preparedStatement.setBoolean(14, message.getChat().isUserChat());
            preparedStatement.setString(15, message.toString());

            preparedStatement.executeUpdate();
            connection.commitTransaction();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(LOGTAG+" "+e.toString());
        }
    }

    public void logChat_Answer(SendMessage sendmessage) {
        String LOGTAG = "DATABASEMANAGER/LOGCHATANSWER";
        int updatedRows;

        try {
            connection.initTransaction();
            final PreparedStatement preparedStatement = connection.getPreparedStatement("INSERT INTO SlainteBot_Command_Send\n" +
                    "(userId, Message)\n" +
                    "VALUES(?, ?)");

            preparedStatement.setString(1, sendmessage.getChatId());
            preparedStatement.setString(2, sendmessage.toString());

            preparedStatement.executeUpdate();
            connection.commitTransaction();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(LOGTAG+" "+e.toString());
        }
    }

    public ResultSet doit(String sqlstring) {
        String LOGTAG = "DATABASEMANAGER/DOIT";
        ResultSet res=null;
        try {
            res= connection.runSqlQuery(sqlstring);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println(LOGTAG+" "+throwables.toString());
        }
        return res;
    }

    public int selectReadUserid(int userid) {
        String LOGTAG = "DATABASEMANAGER/SELECTREADUSERID";
        ResultSet res;
        int nores=0;
        try {
            res= connection.runSqlQuery("select count(*) from SlainteBot_Command_Read where userId="+userid);
            while (res.next()) {
                nores = Math.max(nores, res.getInt(1));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println(LOGTAG+" "+throwables.toString());
        }
        return nores;
    }

    public String selectBotUptime() {
        String LOGTAG = "DATABASEMANAGER/SELECTBOTUPTIME";
        ResultSet res;
        String tres=null;
        try {
            res= connection.runSqlQuery("select max(DATE_FORMAT(Datum,'%Y-%m-%d %T')) from SlainteBot");
            while (res.next()) {
                tres = res.getString(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println(LOGTAG+" "+throwables.toString());
        }
        return tres;
    }

    public int countRead() {
        String LOGTAG = "DATABASEMANAGER/SELECTCOUNTREAD";
        ResultSet res;
        int nores=0;
        try {
            res= connection.runSqlQuery("select count(*) from SlainteBot_Command_Read");
            while (res.next()) {
                nores = Math.max(nores, res.getInt(1));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println(LOGTAG+" "+throwables.toString());
        }
        return nores;
    }

    public int countSend() {
        String LOGTAG = "DATABASEMANAGER/SELECTCOUNTSEND";
        ResultSet res;
        int nores=0;
        try {
            res= connection.runSqlQuery("select count(*) from SlainteBot_Command_Send");
            while (res.next()) {
                nores = Math.max(nores, res.getInt(1));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println(LOGTAG+" "+throwables.toString());
        }
        return nores;
    }
    public void logUserStatus(int UserId, String Menu) {
        String LOGTAG = "DATABASEMANAGER/LOGUSERSTATUS";
        int updatedRows;

        try {
            connection.initTransaction();
            final PreparedStatement preparedStatement = connection.getPreparedStatement("INSERT INTO SlainteBot_UserStatus (userId,UserMenu)\n" +
                    "VALUES (?,?)\n"+
                    "ON DUPLICATE KEY UPDATE UserMenu=?");
            preparedStatement.setInt(1, UserId);
            preparedStatement.setString(2, Menu);
            preparedStatement.setString(3, Menu);

            preparedStatement.executeUpdate();
            connection.commitTransaction();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(LOGTAG+" "+e.toString());
        }
    }

    public void logUserStatus(String chatId, String menu) {
        int ci=Integer.parseInt(chatId);
        logUserStatus(ci,menu);
    }

    public ResultSet getUserStatus(int UserId) {
        return doit("select UserMenu,userid from SlainteBot_UserStatus where userid="+UserId);
    }

}