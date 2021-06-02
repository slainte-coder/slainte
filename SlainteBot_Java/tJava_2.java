/*
 ***************************************************************************************************
 * $Date: 2021-03-14 12:03:45 +0100 (So, 14. Mär 2021) $
 * $Revision: 312 $
 * $Author: alfred $
 * $HeadURL: https://monitoring.slainte.at/svn/slainte/trunk/Talend/SlainteBot_Java/tJava_2.java $
 * $Id: tJava_2.java 312 2021-03-14 11:03:45Z alfred $
 ***************************************************************************************************
 */

        BufferedWriter writer = null;
        try  {
            writer = new BufferedWriter( new FileWriter( context.TempDir+"/SlainteBot.lock",true));
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
/*
 *
 * Starten des TelegramBots
 *
 */
 {
       // final String PATH = "/home/alfred/svn/trunk/Java/SlainteBot/SlainteBot.sh";
        final String PATH = context.BotCommando;

        if (!new File(PATH).canExecute()) {
            System.out.println(PATH + " kann nicht ausgef\u00FChrt werden!");
            System.exit(-1);
        }
        String arg = context.controllerDB+" "+context.linkDB+" "+context.userDB+" "+context.password;
        ProcessBuilder builder = new ProcessBuilder(PATH, arg);
        Process process = null;
        try {
            process = builder.start();
            int status = process.waitFor();
            System.out.println("Exit status: " + status);

            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            System.out.println("Programmende");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
}
