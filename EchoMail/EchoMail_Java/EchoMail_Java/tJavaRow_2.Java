/*
***************************************************************************************************
* $Date: 2021-01-16 19:42:27 +0100 (Sat, 16 Jan 2021) $
* $Revision: 96 $
* $Author: alfred $
* $HeadURL: https://monitoring.slainte.at/svn/slainte/trunk/Talend/EchoMail_Java/tJavaRow_2.Java $
* $Id: tJavaRow_2.Java 96 2021-01-16 18:42:27Z alfred $
***************************************************************************************************
*/
{
SendMail mySendMail=new SendMail();
String MsgText=null;

output_row.MailFrom = input_row.MailFrom;
output_row.MailDate = input_row.MailDate;
output_row.MailSubject = input_row.MailSubject;
output_row.MailFile = input_row.MailFile;
output_row.MailDateLastTime = input_row.MailDateLastTime;
output_row.MailNo = input_row.MailNo;
output_row.MailDateCompare = input_row.MailDateCompare;
output_row.Mailtobeskipped = input_row.Mailtobeskipped;

if (output_row.Mailtobeskipped){
	if (context.DoDebug) { // Show the context values	System.out.println("Mail wird nicht versandt")
		System.out.println("output_row.MailDate ="+ input_row.MailDate);
		System.out.println("output_row.MailDateCompare ="+ input_row.MailDateCompare);
		System.out.println("output_row.Mailtobeskipped ="+ input_row.Mailtobeskipped);
	}
} else {
	mySendMail.setPostausgang_host(context.SMTPHost);
	mySendMail.setBenutzerName(context.SmtpUser);
	mySendMail.setPassword(context.SmtpPassword);
	mySendMail.setstarttls(true);
	mySendMail.doConnect();
	// Zusammenstellen der Mail
	mySendMail.setmailfrom(context.SmtpUser);
	mySendMail.setmailto(input_row.MailFrom);
	mySendMail.setSubject(input_row.MailSubject);
	MsgText="Hallo\n";
	MsgText=MsgText+"\n";
	MsgText=MsgText+"Das ist der automatische Mailer-Dämon auf "+context.ImapHost+".\n";
	MsgText=MsgText+"\n";
	MsgText=MsgText+"Wiederholte Nachrichten innerhalb von "+context.Purge_time_Minutes+" Minute(n) werden ignoriert.\n";
	MsgText=MsgText+"Sie haben bereits "+row8.MailNo+" Emails an mich gesandt.\n";
	MsgText=MsgText+"\n";
	MsgText=MsgText+"Ihre Email-Daten finden Sie im Attachment.\n";
	MsgText=MsgText+"\n";
	MsgText=MsgText+"Viel Spaß\n";
	mySendMail.setmailmessage(MsgText);
	mySendMail.setfilename(input_row.MailFile);
	mySendMail.doSendMail();
	mySendMail.doDisConnect();
}
}
