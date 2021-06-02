/*
 ***************************************************************************************************
 * $Date: 2021-02-05 16:03:48 +0100 (Fri, 05 Feb 2021) $
 * $Revision: 223 $
 * $Author: alfred $
 * $HeadURL: https://monitoring.slainte.at/svn/slainte/trunk/Java/SlainteBot/src/telegram/slainte/SlainteBot.java $
 * $Id: SlainteBot.java 223 2021-02-05 15:03:48Z alfred $
 ***************************************************************************************************
 */

package telegram.slainte;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author $Author: alfred $
 * @version 1.0 $Revision: 223 $
 * @brief Slainte Bot. Ausführen der Kommandos
 * @date $Date: 2021-02-05 16:03:48 +0100 (Fri, 05 Feb 2021) $
 * @head $HeadURL: https://monitoring.slainte.at/svn/slainte/trunk/Java/SlainteBot/src/telegram/slainte/SlainteBot.java $
 */
public class SlainteBot extends TelegramLongPollingBot {
    private static final String MAINVERSION = "1.0.2";
    private static final String LOGTAG = "SLAINTEBOT";
    String string;
    String controllerDB;
    String linkDB;
    String userDB;
    String password;
    int    res=0;
    int    ReplyToId=0;


    public SlainteBot(String cDB,String lDB,String uDB,String pwd) {
        controllerDB=cDB;
        linkDB=lDB;
        userDB=uDB;
        password=pwd;
        DatabaseManager databseManager = DatabaseManager.getInstance(controllerDB,linkDB,userDB,password); // open connection
        databseManager.insertbotstart();
        System.out.println(LOGTAG+" Slainte Bot Instantiated");
    }


    @Override
    public String getBotUsername() { return "mySlaintebot"; }

    @Override
    public String getBotToken() { return "1499793371:AAFJ9MfHg9XzAaxnEY02xlDTn16lATPUpAg"; }

    @Override
    //this function is triggered everytime the bot get a new message
    //we get object from type "Update" [ read more here: https://core.telegram.org/bots/api#update ]
    public void onUpdateReceived(Update update) {
        SendMessage sendMessage=new SendMessage();
        String MId, CId, actmenu;
        ResultSet res;
        int noRead;
        DatabaseManager databseManager = DatabaseManager.getInstance(controllerDB,linkDB,userDB,password); // open connection

        //handle messages -> we look for the message we recieved
        if (update.hasMessage()) {

            //we save the message in new Message object
            Message message = update.getMessage();
            databseManager.logChat_Request(message);

            //we check that the text is not empty
            if (message != null && message.hasText()) {
                //we take the text, and convert it to lower case, so even if the user typed "/HALLO" it should work
                string = message.getText().toLowerCase();

                //Prüfen ob chat in einer Message-Group eröffnet wurde, Wenn ja, auf private wechseln
                MId=message.getFrom().getId().toString();
                CId=message.getChat().getId().toString();
                if (MId.equalsIgnoreCase(CId)) { // Das ist eine private Nachricht, direkt an den Bot
                    ReplyToId=message.getMessageId();
                }
                else { // Nachricht aus einem Chat, Sende ein Smiley
                    if (string.startsWith("/")) { // Und es ist auch wirklich ein Kommando
                        databseManager.logChat(message);
                        sendMessage.setChatId(String.valueOf(message.getChat().getId()));
                        sendMessage.setReplyToMessageId(message.getMessageId());
                        sendMessage.setText(message.getFrom().getFirstName()+" "+Emoji.FACE_WITH_STUCK_OUT_TONGUE_AND_WINKING_EYE+ " lass uns privat weiterplauden. "+Emoji.KISSING_FACE_WITH_CLOSED_EYES);
                        sendMessage.enableHtml(true);
                        SendMsg(sendMessage);
                        sendMessage.setReplyToMessageId(null); // Mach es wieder gut
                    }
                }
                sendMessage.setChatId(message.getFrom().getId().toString());
                sendMessage.enableHtml(true);

                //now we tell the bot to respond to specific cases:
                if (string.startsWith("/hallo")) {
                    sendMessage.setText("Hallo "+ message.getFrom().getFirstName()+"\n"+
                            "Was kann ich für Dich tun?"+Emoji.WINKING_FACE+"\n"+
                            "<b>/status</b>- Gibt ein paar Informationen über den Bot aus\n" +
                            "<b>/info</b>- Zeigt ein paar Informationen über Dich an\n" +
                            "<b>/hilfe</b>- Anzeige des Hilfetextes");
                    SendMsg(sendMessage);
                }
                else if (string.startsWith("/status")) {
                    //Uptime
                    //Anzahl der Nachrichten pro Benutzer gelesen und gesandt
                    sendMessage.setText("Ich zeig Dir den Status "+Emoji.EARTH_GLOBE_EUROPE_AFRICA+"\n"+
                            "bin seit "+ databseManager.selectBotUptime()+" aktiv"+Emoji.VICTORY_HAND+"\n"+
                            "habe "+databseManager.countRead()+" Nachrichten gelesen"+Emoji.SHIP+"\n"+
                            "und "+databseManager.countSend()+" Nachrichten versandt"+Emoji.ROCKET+"\n\n"+
                            "Meine Datenbank ist "+ linkDB+"\n"+
                            "Meine Version ist "+MAINVERSION);
                    SendMsg(sendMessage);
                }
                else if (string.startsWith("/info")) {
                    sendMessage.setText("Ich zeig Dir ein paar Informationen über dich"+Emoji.SUN_WITH_FACE+"\n"+
                            "ID:"+message.getFrom().getId()+"\n"+
                            "Language Code:"+message.getFrom().getLanguageCode()+"\n"+
                            "Name:"+message.getFrom().getFirstName()+" "+message.getFrom().getLastName()+"\n"+
                            "<a href=\"tg://user?id="+message.getFrom().getId()+"\"> "+message.getFrom().getUserName()+"</a>"+"\n\n"+
                            "Ich habe bereits "+databseManager.selectReadUserid(message.getFrom().getId())+" Nachrichten von Dir gelesen "+Emoji.ENVELOPE);
                    SendMsg(sendMessage);
                }
                else if (string.startsWith("/hilfe")) {
                    sendMessage.setText("Hilfe!!!!! "+Emoji.AMBULANCE+"\n" +
                            "<b>/hallo</b> - Begrüßung durch den Bot\n" +
                            "<b>/status</b>- Gibt ein paar Informationen über den Bot aus\n" +
                            "<b>/info</b>- Zeigt ein paar Informationen über Dich an\n" +
                            "<b>/ueberraschung</b>- Lass Dich überraschen\n" +
                            "<b>/hilfe</b>- Anzeige des Hilfetextes\n\n" +
                            "Wobei kann ich Dir helfen?"+Emoji.SMIRKING_FACE);
                    sendMessage.enableMarkdown(true);
                    sendMessage.enableHtml(true);
                    //sendMessage.setReplyMarkup(Helpkeyboard("egal"));
                    sendMessage.setReplyMarkup(inHelpkeyboard("egal",databseManager));
                    SendMsg(sendMessage);
                }
                else if (string.startsWith("/test")) {
                    sendMessage.setText("Test des Inline Keyboards");
                    sendMessage.enableMarkdown(true);
                    sendMessage.enableHtml(true);
                    sendMessage.setReplyMarkup(inHelpkeyboardNo(555));
                    SendMsg(sendMessage);
                }
                else if (string.startsWith("/ueberraschung")) {
                    int min=0;
                    int max=0;
                    double rand;
                    String alias=null;
                    String cat=null;
                    String Intro=null;
                    res=databseManager.doit("select MIN(id) FROM fdi67_content");
                    while (true) {
                        try {
                            if (!res.next()) break;
                            min = res.getInt(1);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                            System.out.println(LOGTAG+" "+throwables.toString());
                        }
                    }
                    res=databseManager.doit("select MAX(id) FROM fdi67_content");
                    while (true) {
                        try {
                            if (!res.next()) break;
                            max = res.getInt(1);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                            System.out.println(LOGTAG+" "+throwables.toString());
                        }
                    }
                    rand=(Math.random() * (max - min) + min);
                    res=databseManager.doit("SELECT ct.id,ct.alias,cat.alias,ct.introtext\n" +
                            "    FROM fdi67_content ct ,fdi67_categories cat\n" +
                            "    WHERE ct.id >=" +rand+"\n" +
                            "    AND ct.id <=" +max+"\n" +
                            "    AND ct.state=1 \n" +
                            "    and ct.catid = cat.id \n" +
                            "    and cat.alias not in ('root','uncategorised')\n" +
                            "    LIMIT 1;\n" +
                            "   ");
                    while (true) {
                        try {
                            if (!res.next()) break;
                            min=res.getInt(1);
                            alias = res.getString(2);
                            cat = res.getString(3);
                            Intro = res.getString(4);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                            System.out.println(LOGTAG+" "+throwables.toString());
                        }
                    }
                    Intro=removeillegalTags(Intro);
                    sendMessage.setText("Der Beitrag des Tages ist\n\n"+
                            Intro+"\n\n"+
                            "https://www.slainte.at/index.php/"+cat+"/"+min+"-"+alias+"\n");
                    sendMessage.enableMarkdown(true);
                    sendMessage.enableHtml(true);
                    SendMsg(sendMessage);
                }
                else if (string.startsWith("/")) {
                    sendMessage.setReplyToMessageId(ReplyToId);
                    sendMessage.setText(string+"\n"+
                            "Dieses Kommando kenne ich nicht! "+Emoji.HEAR_NO_EVIL_MONKEY+Emoji.SEE_NO_EVIL_MONKEY+Emoji.SPEAK_NO_EVIL_MONKEY);
                    SendMsg(sendMessage);
                }
            } // otherwise just ignore it.
        }
        else if (update.hasCallbackQuery()) { // Jetzt kommt eine Antwort, und das ist immer eine ID auf einen Text
            //we save the message in new Message object
            CallbackQuery cQ= update.getCallbackQuery();
            databseManager.logChat_Request(cQ.getMessage());
            String Intro=null;
            String Title=null;
            if (cQ.getData().equalsIgnoreCase("-1")) {
                Title=" Die Homepage ist erreichbar über";
                Intro=" <a href=\"https://www.slainte.at\">https://www.slainte.at</a>";
            }
            else {
                res=databseManager.doit("select introtext,title from fdi67_content fc where id="+cQ.getData());
                while (true) {
                    try {
                        if (!res.next()) break;
                        Intro = res.getString(1);
                        Title = res.getString(2);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                        System.out.println(LOGTAG+" "+throwables.toString());
                    }
                }
            }
            Intro=removeillegalTags(Intro);
            Title=removeillegalTags(Title);
            sendMessage.setChatId(cQ.getMessage().getChatId().toString());
            sendMessage.setText(Title+"\n\n"+Intro);
            sendMessage.enableMarkdown(true);
            sendMessage.enableHtml(true);
            sendMessage.enableWebPagePreview();
            SendMsg(sendMessage);
        }
        databseManager.close(); // close connection
    }
/*
    <p>Da gibt es mehrere hervorragende Artikel. Siehe</p>
<p><a href="index.php/faq/41-freeopt-wie-kann-ich-mich-am-system-anmelden" target="_blank" rel="noopener noreferrer">FreeOPT - Wie kann ich mich am System anmelden?</a></p>
<p><a href="index.php/faq/42-googleauthenticator-wie-kann-ich-mich-am-system-anmelden" target="_blank" rel="noopener noreferrer">GoogleAuthenticator - Wie kann ich mich am System anmelden?</a></p>
*/
    String removeillegalTags(String Html) {
        String html;
        html=Html;
        html=html.replaceAll("href=\"index.php/","href=\"https://www.slainte.at/index.php/");
        html=html.replaceAll("<p>","");
        html=html.replaceAll("</p>","");
        html=html.replaceAll("<br />","");
        return html;
    }
/*

Supported HTML-Formatting - https://core.telegram.org/bots/api#formatting-options

<b>bold</b>, <strong>bold</strong>
<i>italic</i>, <em>italic</em>
<u>underline</u>, <ins>underline</ins>
<s>strikethrough</s>, <strike>strikethrough</strike>, <del>strikethrough</del>
    <b>bold <i>italic bold <s>italic bold strikethrough</s> <u>underline italic bold</u></i> bold</b>
<a href="http://www.example.com/">inline URL</a>
<a href="tg://user?id=123456789">inline mention of a user</a>
    <code>inline fixed-width code</code>
<pre>pre-formatted fixed-width code block</pre>
<pre><code class="language-python">pre-formatted fixed-width code block written in the Python programming language</code></pre>

*/

    void sendHideKeyboard(Integer userId, Long chatId, Integer messageId)  {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.enableMarkdown(true);
        //sendMessage.setReplyToMessageId(messageId);
        //sendMessage.setText(Emoji.WAVING_HAND_SIGN.toString());

        ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove();
        replyKeyboardRemove.setSelective(true);
        replyKeyboardRemove.setRemoveKeyboard(true);
        sendMessage.setReplyMarkup(replyKeyboardRemove);

        SendMsg(sendMessage);
    }

    //@todo Scheussliche Programmierung, das muß irgendwie mit Loop gehen
    private InlineKeyboardMarkup inHelpkeyboard(String language, DatabaseManager databseManager) {

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> line1 = new ArrayList<>();
        List<InlineKeyboardButton> line2 = new ArrayList<>();
        List<InlineKeyboardButton> line3 = new ArrayList<>();
        List<InlineKeyboardButton> line4 = new ArrayList<>();
        int i=0;
        int l=1;
        String title;

        //BUTTON_DATA_INVALID - telegram limit of 64 bytes #1
        //Einlesen der Knöpfe, Nur publizierte Messages, Neueste zuerst

        ResultSet res=databseManager.doit("select title, id from fdi67_content fc where fc.state=1 and "+
                " fc.catid = (select fcat.id from fdi67_categories fcat where fcat.alias ='telegrambot')"+
                " order by fc.modified DESC");

        while (true) {
            try {
                if (!res.next()) break;
                title=res.getString(1);
                {
                    InlineKeyboardButton ikb = new InlineKeyboardButton();
                    ikb.setText(title.substring(0, Math.min(60, title.length())));
                    ikb.setCallbackData(String.valueOf(res.getInt(2)));
                    if (l == 1) {
                        line1.add(i, ikb);
                    }
                    else if (l == 2){
                        line2.add(i, ikb);
                    }
                    else if (l == 3) {
                        line3.add(i, ikb);
                    }
                    else {
                        line4.add(i, ikb);
                    }
                }
                i=i+1;
                if (i > 3){
                    i=0;
                    l=l+1;
                }
                if (l>4) {
                    break;
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                System.out.println(LOGTAG+" "+throwables.toString());
            }
        }
        if (!line1.isEmpty()) {
            rowsInline.add(line1);
        }
        if (!line2.isEmpty()) {
            rowsInline.add(line2);
        }
        if (!line3.isEmpty()) {
            rowsInline.add(line3);
        }
        if (!line4.isEmpty()) {
            rowsInline.add(line4);
        }

        if (rowsInline.isEmpty()) { //Notfallplan
            InlineKeyboardButton ikb = new InlineKeyboardButton();
            ikb.setText("Slainte Page");
            ikb.setCallbackData("-1");
            line1.add(0, ikb);
            rowsInline.add(line1);
        }
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    //@todo Scheussliche Programmierung, das muß irgendwie mit Loop gehen
    private InlineKeyboardMarkup inHelpkeyboardNo(int No) {

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> line1 = new ArrayList<>();
        List<InlineKeyboardButton> line2 = new ArrayList<>();
        List<InlineKeyboardButton> line3 = new ArrayList<>();
        List<InlineKeyboardButton> line4 = new ArrayList<>();
        int i=0;
        int l=1;
        String title;

        while (No > 0) {
                No=No-1;
                {
                    InlineKeyboardButton ikb = new InlineKeyboardButton();
                    ikb.setText(String.valueOf(No));
                    ikb.setCallbackData("-1"); // Immer Notfall hier:)
                    if (l == 1) {
                        line1.add(i, ikb);
                    }
                    else if (l == 2){
                        line2.add(i, ikb);
                    }
                    else if (l == 3) {
                        line3.add(i, ikb);
                    }
                    else {
                        line4.add(i, ikb);
                    }
                }
                i=i+1;
                if (i > 3){
                    i=0;
                    l=l+1;
                }
                if (l>4) {
                    break;
                }
        }
        if (!line1.isEmpty()) {
            rowsInline.add(line1);
        }
        if (!line2.isEmpty()) {
            rowsInline.add(line2);
        }
        if (!line3.isEmpty()) {
            rowsInline.add(line3);
        }
        if (!line4.isEmpty()) {
            rowsInline.add(line4);
        }

        if (rowsInline.isEmpty()) { //Notfallplan
            InlineKeyboardButton ikb = new InlineKeyboardButton();
            ikb.setText("Slainte Page");
            ikb.setCallbackData("-1");
            line1.add(0, ikb);
            rowsInline.add(line1);
        }
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    //this function takes a message object (that contains the sender id and all the details we need to respond)
    //and a simple string, and send a new message to the user
    public void SendMsg(SendMessage sendMessage) {
        String LOGTAG = "SLAINTEBOT/SENDMESSAGE";


        DatabaseManager databseManager = DatabaseManager.getInstance();
        databseManager.logChat_Answer(sendMessage);
        //try to send it:
        try {
            execute(sendMessage);
        }
        catch (TelegramApiException e) {
            e.printStackTrace();
            System.out.println(LOGTAG+" "+e.toString());
        }
    }
}