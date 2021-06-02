// Richtige Werte für den Homepath und das Temporary Verzeichnis
context.SQLLite = System.getProperty("user.home")+"/"+context.SQLLite;
context.TempDir = System.getProperty("java.io.tmpdir")+System.getProperty("user.home");
String processName = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
context.pid = processName.split("@")[0];
if (context.DoDebug) { // Show the context values
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
	Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	System.out.println(sdf.format(timestamp));
	System.out.println("***************************************************************************************************");
	System.out.println("* $Date: 2021-03-14 09:24:50 +0100 (So, 14. Mär 2021) $");
	System.out.println("* $Revision: 307 $");
	System.out.println("* $Author: alfred $");
	System.out.println("* $HeadURL: https://monitoring.slainte.at/svn/slainte/trunk/Talend/EchoMail_Java/tJava_1.Java $");
	System.out.println("* $Id: tJava_1.Java 307 2021-03-14 08:24:50Z alfred $");
	System.out.println("***************************************************************************************************");
	System.out.println("**** Debug ist eingeschalten. Bitte ausschalten ****");
	System.out.println("OS Name           :"+System.getProperty("os.name"));
	System.out.println("OS Version        :"+System.getProperty("os.version"));
	System.out.println("ImapUser          :"+context.ImapUser);
	System.out.println("ImapPassword      :"+context.ImapPassword);
	System.out.println("ImapHost          :"+context.ImapHost);
	System.out.println("ImapPort          :"+context.ImapPort);
	System.out.println("SmtpUser          :"+context.SmtpUser);
	System.out.println("SmtpPassword      :"+context.SmtpPassword);
	System.out.println("SMTPHost          :"+context.SMTPHost);
	System.out.println("SMTPPort          :"+context.SMTPPort);
	System.out.println("Purge_time_Minutes:"+context.Purge_time_Minutes);
	System.out.println("SQLLite           :"+context.SQLLite);
	System.out.println("SQLLite_Table     :"+context.SQLLite_Table);
	System.out.println("TempDir           :"+context.TempDir);
	System.out.println("Pid               :"+context.pid);
	System.out.println("***************************************************");
	System.out.println(sdf.format(timestamp));
}
