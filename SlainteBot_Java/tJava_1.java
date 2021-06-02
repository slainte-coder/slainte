// Richtige Werte für das Temporary Verzeichnis
context.TempDir = System.getProperty("java.io.tmpdir")+System.getProperty("user.home");
String processName = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
context.pid = processName.split("@")[0];
if (context.DoDebug) { // Show the context values
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
	Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	System.out.println(sdf.format(timestamp));
	System.out.println("***************************************************************************************************");
	System.out.println("* $Date: 2021-03-14 11:56:57 +0100 (So, 14. Mär 2021) $");
	System.out.println("* $Revision: 311 $");
	System.out.println("* $Author: alfred $");
	System.out.println("* $HeadURL: https://monitoring.slainte.at/svn/slainte/trunk/Talend/SlainteBot_Java/tJava_1.java $");
	System.out.println("* $Id: tJava_1.java 311 2021-03-14 10:56:57Z alfred $");
	System.out.println("***************************************************************************************************");
	System.out.println("**** Debug ist eingeschalten. Bitte ausschalten ****");
	System.out.println("OS Name           :"+System.getProperty("os.name"));
	System.out.println("OS Version        :"+System.getProperty("os.version"));
	System.out.println("controllerDB      :"+context.controllerDB);
	System.out.println("linkDB            :"+context.linkDB);
	System.out.println("userDB            :"+context.userDB);
	System.out.println("password          :"+context.password );
	System.out.println("TempDir           :"+context.TempDir);
	System.out.println("BotCommand        :"+context.BotCommando);
	System.out.println("Pid               :"+context.pid);
	System.out.println("***************************************************");
	System.out.println(sdf.format(timestamp));
}
