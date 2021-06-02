/*
 ***************************************************************************************************
 * $Date: 2021-02-04 18:33:28 +0100 (Thu, 04 Feb 2021) $
 * $Revision: 208 $
 * $Author: alfred $
 * $HeadURL: https://monitoring.slainte.at/svn/slainte/trunk/Java/SlainteBot/src/telegram/slainte/ConectionDB.java $
 * $Id: ConectionDB.java 208 2021-02-04 17:33:28Z alfred $
 ***************************************************************************************************
 */
package telegram.slainte;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;

/**
 * @author $Author: alfred $
 * @version 1.0 $Revision: 208 $
 * @brief Slainte Bot. Connect to a maria-db Database
 * @date $Date: 2021-02-04 18:33:28 +0100 (Thu, 04 Feb 2021) $
 * @head $HeadURL: https://monitoring.slainte.at/svn/slainte/trunk/Java/SlainteBot/src/telegram/slainte/ConectionDB.java $
 */
public class ConectionDB {
    private static final String LOGTAG = "CONNECTIONDB";
    private Connection currentConection;

    public ConectionDB(String controllerDB, String linkDB, String userDB, String password) {
        this.currentConection = openConexion(controllerDB, linkDB, userDB,password);
    }

    private Connection openConexion(String controllerDB, String linkDB, String userDB, String password) {
        Connection connection = null;
        try {
            Class.forName(controllerDB).getDeclaredConstructor().newInstance();
            connection = DriverManager.getConnection(linkDB, userDB, password);
        } catch (SQLException | ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            System.out.println(LOGTAG+" "+e.toString());
        }

        return connection;
    }

    public void closeConexion() {
        try {
            this.currentConection.close();
        } catch (SQLException e) {
            System.out.println(LOGTAG+" "+e.toString());
        }

    }

    public ResultSet runSqlQuery(String query) throws SQLException {
        final Statement statement;
        statement = this.currentConection.createStatement();
        return statement.executeQuery(query);
    }

    public Boolean executeQuery(String query) throws SQLException {
        final Statement statement = this.currentConection.createStatement();
        return statement.execute(query);
    }

    public PreparedStatement getPreparedStatement(String query) throws SQLException {
        return this.currentConection.prepareStatement(query);
    }

    public PreparedStatement getPreparedStatement(String query, int flags) throws SQLException {
        return this.currentConection.prepareStatement(query, flags);
    }

    public int checkVersion() {
        int max = 0;
        try {
            final DatabaseMetaData metaData = this.currentConection.getMetaData();
            final ResultSet res = metaData.getTables(null, null, "",
                    new String[]{"TABLE"});
            while (res.next()) {
                if (res.getString("TABLE_NAME").compareTo("SlainteBot_Versions") == 0) {
                    final ResultSet result = runSqlQuery("SELECT Max(Version) FROM SlainteBot_Versions");
                    while (result.next()) {
                        max = Math.max(max, result.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(LOGTAG+" "+e.toString());
        }
        return max;
    }

    /**
     * Initilize a transaction in database
     * @throws SQLException If initialization fails
     */
    public void initTransaction() throws SQLException {
        this.currentConection.setAutoCommit(false);
    }

    /**
     * Finish a transaction in database and commit changes
     * @throws SQLException If a rollback fails
     */
    public void commitTransaction() throws SQLException {
        try {
            this.currentConection.commit();
        } catch (SQLException e) {
            if (this.currentConection != null) {
                this.currentConection.rollback();
            }
        } finally {
            if (this.currentConection != null) {
                this.currentConection.setAutoCommit(false);
            }
        }
    }
}