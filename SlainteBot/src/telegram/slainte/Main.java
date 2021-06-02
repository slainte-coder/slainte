/*
 ***************************************************************************************************
 * $Date: 2021-02-05 09:55:03 +0100 (Fri, 05 Feb 2021) $
 * $Revision: 220 $
 * $Author: alfred $
 * $HeadURL: https://monitoring.slainte.at/svn/slainte/trunk/Java/SlainteBot/src/telegram/slainte/Main.java $
 * $Id: Main.java 220 2021-02-05 08:55:03Z alfred $
 ***************************************************************************************************
 *
 * Für ein einwandfreies funktionieren braucht es die telegram-jars telegrambots und telegambots.meta in der Version 5.x plus ein paar Adaptionen (copy) aus der 4.0.1
 *
 */

package telegram.slainte;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


//this is the Main class, which will create new instance of the bot
/**
 * @author $Author: alfred $
 * @version 1.0 $Revision: 220 $
 * @brief Slainte Bot. Verwalten der Telegram-Gruppe, sowie der Joomla-Page
 * @date $Date: 2021-02-05 09:55:03 +0100 (Fri, 05 Feb 2021) $
 * @head $HeadURL: https://monitoring.slainte.at/svn/slainte/trunk/Java/SlainteBot/src/telegram/slainte/Main.java $
 */
public class Main {
    public static String pathToLogs = "./";

    /* Das funktioniert zu Hause
     */
        public static String controllerDB = "com.mysql.cj.jdbc.Driver";
        public static String linkDB = "jdbc:mysql://monitoring:3306/telegram?useUnicode=true&characterEncoding=UTF-8";
        public static String userDB = "telegram";
        public static String password = "XQ30WWIedlHSxMHs";
        public static String arg;
        private static final String LOGTAG = "MAIN";

    /* Das funktioniert am Slainte-Server
    public static String controllerDB = "com.mysql.cj.jdbc.Driver";
    public static String linkDB = "jdbc:mysql://localhost:14306/joomla?useUnicode=true&characterEncoding=UTF-8";
    public static String userDB = "joomla";
    public static String password = "xZmNqZNws8XoBwbXFUo9";
    private static final String LOGTAG = "MAIN";
     */

    public static void main(String[] args) throws Exception {
        // no Log today
        System.out.println("no Log today");

        if (args.length >3) { // Zuweisen der Argumente
            controllerDB =args[0];
            linkDB=args[1];
            userDB=args[2];
            password=args[3];
            System.out.println("Parameter aus der Commandline!");
        }
        else if (args.length == 1) { // Parsen!
            System.out.println("Parsen aus der Commandline!"+args[0]);
            arg=args[0];
            //System.out.println("arg="+arg+" index"+arg.indexOf(" ")+" length:"+arg.length());
            controllerDB=arg.substring(0,arg.indexOf(" "));
            arg=arg.substring(arg.indexOf(" ")+1,arg.length());
            //System.out.println("arg="+arg+" index"+arg.indexOf(" ")+" length:"+arg.length());
            linkDB=arg.substring(0,arg.indexOf(" "));
            arg=arg.substring(arg.indexOf(" ")+1,arg.length());
            //System.out.println("arg="+arg+" index"+arg.indexOf(" ")+" length:"+arg.length());
            userDB=arg.substring(0,arg.indexOf(" "));
            arg=arg.substring(arg.indexOf(" ")+1,arg.length());
            //System.out.println("arg="+arg+" index"+arg.indexOf(" ")+" length:"+arg.length());
            password=arg;
        }
        else
        {
            System.out.println("DEFAULT-Parameter!");
        }
        System.out.println("controllerDB="+controllerDB);
        System.out.println("linkDB      ="+linkDB);
        System.out.println("userDB      ="+userDB);
        System.out.println("password    ="+password);


        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

/*
        DatabaseManager.getInstance(controllerDB,linkDB,userDB,password);
        System.out.println("Database connected!");
*/

        //init bot (we try to create new bot, and throw exception if creation failed)
        try {
            telegramBotsApi.registerBot(new SlainteBot(controllerDB,linkDB,userDB,password));
//            telegramBotsApi.registerBot(new CommandsHandler(BotConfig.BOT_USER));
            System.out.println("SlainteBot is online!");
        }
        catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

}
