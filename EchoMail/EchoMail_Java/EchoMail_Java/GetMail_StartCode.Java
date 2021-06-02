/*
***************************************************************************************************
* $Date: 2021-02-10 12:32:04 +0100 (Mi, 10. Feb 2021) $
* $Revision: 256 $
* $Author: alfred $
* $HeadURL: https://monitoring.slainte.at/svn/slainte/trunk/Talend/EchoMail_Java/GetMail_StartCode.Java $
* $Id: GetMail_StartCode.Java 256 2021-02-10 11:32:04Z alfred $
***************************************************************************************************
*/
// start part of your Java code
        GetMail myMail=new GetMail();
        myMail.setPosteingang_host(context.ImapHost);
        myMail.setBenutzerName(context.ImapUser);
        myMail.setPassword(context.ImapPassword);
        myMail.setDoDebug(context.DoDebug);
        myMail.setDoDelete(true); // löschen nach dem Lesen
        myMail.setDoRead(true); // als gelesen markieren (bei Löschen eigentlich egal)
        myMail.setDoUNSEEN(false); // alle Mails
        myMail.setstarttls(true);
        myMail.setIsSSL(true);
        myMail.setTmpDir(context.TempDir);
        myMail.doConnect();
        for (int i=0;i<1;){ // Talend braucht da was

