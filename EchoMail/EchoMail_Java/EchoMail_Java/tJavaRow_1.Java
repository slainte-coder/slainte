// Logik um zu erkennen, ob eine Nachricht beantwortet werden soll oder nicht -> DOS Vermeidung
{
//Hilfsvariable
Integer	inthelp=0;

output_row.MailFrom = input_row.MailFrom;
output_row.MailDate = input_row.MailDate;
output_row.MailSubject = input_row.MailSubject;
output_row.MailFile = input_row.MailFile;
output_row.MailDateLastTime = input_row.MailDateLastTime;
output_row.MailNo = input_row.MailNo;
output_row.MailDateCompare = output_row.MailDateLastTime;
output_row.Mailtobeskipped = input_row.Mailtobeskipped;

if (context.DoDebug) {
	System.out.println("***************************************************************************************************");
	System.out.println("* $Date: 2021-01-16 19:40:11 +0100 (Sat, 16 Jan 2021) $");
	System.out.println("* $Revision: 95 $");
	System.out.println("* $Author: alfred $");
	System.out.println("* $HeadURL: https://monitoring.slainte.at/svn/slainte/trunk/Talend/EchoMail_Java/tJavaRow_1.Java $");
	System.out.println("* $Id: tJavaRow_1.Java 95 2021-01-16 18:40:11Z alfred $");
	System.out.println("***************************************************************************************************");
	System.out.println("**** Debug Input Values ****");
	System.out.println("MailFrom         :"+output_row.MailFrom);
	System.out.println("MailDate         :"+output_row.MailDate);
	System.out.println("MailSubject      :"+output_row.MailSubject);
	System.out.println("MailNo           :"+output_row.MailNo);
	System.out.println("MailDateLastTime :"+output_row.MailDateLastTime);
	System.out.println("MailDateCompare  :"+output_row.MailDateCompare);
	System.out.println("Mailtobeskipped  :"+output_row.Mailtobeskipped);
	System.out.println("MailFile         :"+output_row.MailFile);
	System.out.println("***************************************************");
}

if (output_row.MailNo == null) { // Zählen, wie oft wir von diesem Absender schon eine Mail bekommen haben.
	output_row.MailNo = 1;
} 
else {
	output_row.MailNo = output_row.MailNo+1;
}

if (output_row.MailDateLastTime == null){ // Beim erstenmal richtig initialisieren
	if (context.DoDebug) {
		System.out.println("Initiale Mail - zum ersten Mal von diesem Empfänger"); 
	}
	inthelp=1;
} 
else {
        Calendar c = Calendar.getInstance();
        c.setTime(output_row.MailDateCompare);
        c.add(Calendar.MINUTE, context.Purge_time_Minutes);
        // Convert calendar back to Date
        output_row.MailDateCompare=c.getTime();
        
	if (output_row.MailDate.before(output_row.MailDateCompare)) { // Zeitspanne ist zu kurz
		inthelp=0; // Mail wird verworfen
	} else {
		inthelp=1; // Mail wird beantwortet
	}
	if (context.DoDebug) {
		System.out.println("Output MailDate    : "+output_row.MailDate); 
		System.out.println("Output MailCompare : "+output_row.MailDateCompare); 
		System.out.println("Vergleichsergebniss: "+inthelp); 
	}
}
if (inthelp < 1) { 
	if (context.DoDebug) {
		System.out.println("Diese Nachricht ist innerhalb der Purge-Zeit -> wird ignoriert!"); 
	}
	output_row.Mailtobeskipped=true; 
	} 
else { 
	if (context.DoDebug) {
		System.out.println("Diese Nachricht wird beantwortet."); 
	}
	output_row.MailDateLastTime = output_row.MailDate;
	output_row.Mailtobeskipped=false; 
} 
// Für die nächste Runde
output_row.MailDateLastTime=output_row.MailDate;
if (context.DoDebug) {
	System.out.println("**** Debug Output Values ****");
	System.out.println("MailFrom         :"+output_row.MailFrom);
	System.out.println("MailDate         :"+output_row.MailDate);
	System.out.println("MailSubject      :"+output_row.MailSubject);
	System.out.println("MailNo           :"+output_row.MailNo);
	System.out.println("MailDateLastTime :"+output_row.MailDateLastTime);
	System.out.println("MailDateCompare  :"+output_row.MailDateCompare);
	System.out.println("Mailtobeskipped  :"+output_row.Mailtobeskipped);
	System.out.println("MailFile         :"+output_row.MailFile);
	System.out.println("***************************************************");
}
}
