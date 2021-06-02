/*
***************************************************************************************************
* $Date: 2021-01-15 12:30:57 +0100 (Fri, 15 Jan 2021) $
* $Revision: 82 $
* $Author: alfred $
* $HeadURL: https://monitoring.slainte.at/svn/slainte/trunk/Talend/EchoMail_Java/GetMail_MainCode.Java $
* $Id: GetMail_MainCode.Java 82 2021-01-15 11:30:57Z alfred $
***************************************************************************************************
*/
// here is the main part of the component,
// a piece of code executed in the row
// loop
            myMail.NextMessage();
            //System.out.println("From    :"+myMail.Getmailfrom());
            row1.mailfrom=myMail.Getmailfrom();
            //System.out.println("Date    :"+myMail.GetSentDate());
            row1.sentdate=myMail.GetSentDate();
            //System.out.println("Subject :"+myMail.GetSubject());
            row1.subject=myMail.GetSubject();
            //System.out.println("Mailfile:"+myMail.Getmailfile());
            row1.mailfile=myMail.Getmailfile();

