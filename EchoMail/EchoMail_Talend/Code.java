//Code erstellt anhand des Eingabe- und Ausgabeschemas
output_row.MailFrom = input_row.MailFrom;
output_row.MailDate = input_row.MailDate;
output_row.MailNo = input_row.MailNo;
output_row.MailDateLastTime = input_row.MailDateLastTime;
//System.out.println("output_row.MailDate="+output_row.MailDate);
//System.out.println("output_row.MailDateLastTime="+output_row.MailDateLastTime);
if (output_row.MailDateLastTime  == null){ 
	output_row.MailDateLastTime = TalendDate.addDate(TalendDate.formatDate("yyyy-MM-dd HH:mm:ss",TalendDate.getCurrentDate()), "yyyy-MM-dd HH:mm:ss", -context.Purge_time_Minutes*50, "mm"); // this is the first time
}
output_row.MailDateCompare=output_row.MailDateLastTime;
//System.out.println("output_row.MailDateCompare1="+output_row.MailDateCompare);
if (TalendDate.compareDate(TalendDate.parseDateLocale("yyyy-MM-dd HH:mm:ss",output_row.MailDateCompare,"EN"),TalendDate.parseDateLocale("yyyy-MM-dd HH:mm:ss",TalendDate.addDate(TalendDate.formatDate("yyyy-MM-dd HH:mm:ss",TalendDate.getCurrentDate()), "yyyy-MM-dd HH:mm:ss", -context.Purge_time_Minutes, "mm"),"EN")) > 0) {
// Last Time was in Purge-Time-Range
	output_row.MailDateCompare=TalendDate.addDate(TalendDate.formatDate("yyyy-MM-dd HH:mm:ss",TalendDate.getCurrentDate()), "yyyy-MM-dd HH:mm:ss", -context.Purge_time_Minutes*50, "mm"); // Skip this row
	System.out.println("Message is tooo new -> will be skipped!");
	output_row.Mailtobeskipped=true;
	}
else 
{
	output_row.MailDateLastTime = input_row.MailDate; // This mail will be answered. Update it with new Date, and remmber in DB
	output_row.Mailtobeskipped=false;
}
//System.out.println("output_row.MailDateCompare2="+output_row.MailDateCompare);

