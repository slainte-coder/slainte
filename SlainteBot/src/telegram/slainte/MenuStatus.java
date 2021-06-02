/*
 ***************************************************************************************************
 * $Date: 2021-02-02 18:40:45 +0100 (Tue, 02 Feb 2021) $
 * $Revision: 184 $
 * $Author: alfred $
 * $HeadURL: https://monitoring.slainte.at/svn/slainte/trunk/Java/SlainteBot/src/telegram/slainte/MenuStatus.java $
 * $Id: MenuStatus.java 184 2021-02-02 17:40:45Z alfred $
 ***************************************************************************************************
 */
package telegram.slainte;

/**
 * @author $Author: alfred $
 * @version 1.0 $Revision: 184 $
 * @brief Slainte Bot. Verwalten der Menustati pro Usersession
 * @date $Date: 2021-02-02 18:40:45 +0100 (Tue, 02 Feb 2021) $
 * @head $HeadURL: https://monitoring.slainte.at/svn/slainte/trunk/Java/SlainteBot/src/telegram/slainte/MenuStatus.java $
 */

public enum MenuStatus {
    KEINMENU("KEINMENU"),
    HILFEMENU("HILFEMENU");

    String menu;
    MenuStatus(String Menu) {
        this.menu=Menu;
    }

    @Override
    public String toString() {
        return this.menu;
    }
}
