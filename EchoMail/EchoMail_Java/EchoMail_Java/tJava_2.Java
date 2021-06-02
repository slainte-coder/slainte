// Schreiben der PID in das File
//***************************************************************************************************
//* $Date: 2021-03-14 09:24:41 +0100 (So, 14. Mär 2021) $
//* $Revision: 306 $
//* $Author: alfred $
//* $HeadURL: https://monitoring.slainte.at/svn/slainte/trunk/Talend/EchoMail_Java/tJava_2.Java $
//* $Id: tJava_2.Java 306 2021-03-14 08:24:41Z alfred $
//***************************************************************************************************
        BufferedWriter writer = null;
        try  {
            writer = new BufferedWriter( new FileWriter( context.TempDir+"/"+context.SQLLite_Table+".lock",true));
            writer.write(context.pid);
        } catch ( IOException e)
        {
            e.printStackTrace();
        }
        try
        {
            if ( writer != null)
                writer.close( );
        }
        catch ( IOException e)
        {
            e.printStackTrace();
        }

