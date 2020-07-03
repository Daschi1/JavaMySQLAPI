package de.daschi.core;

import org.intellij.lang.annotations.Language;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;

public class MySQL {
    private static MySQL mySQL;

    private static boolean autoDisconnect = false;
    private static final Thread shutdownHook = new Thread(MySQL::disconnect);

    public static void autoDisconnect(final boolean autoDisconnect) {
        if (autoDisconnect) {
            if (MySQL.autoDisconnect) {
                MySQL.autoDisconnect = false;
                Runtime.getRuntime().removeShutdownHook(MySQL.shutdownHook);
            }
        } else {
            if (!MySQL.autoDisconnect) {
                MySQL.autoDisconnect = true;
                Runtime.getRuntime().addShutdownHook(MySQL.shutdownHook);
            }
        }
    }

    public static void using(final MySQL mySQL) {
        try {
            if (MySQL.mySQL != null) {
                MySQL.disconnect();
                MySQL.mySQL = null;
                MySQL.using(mySQL);
            } else {
                MySQL.mySQL = mySQL;
                MySQL.mySQL.openConnection();
            }
        } catch (final SQLException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    public static void disconnect() {
        if (MySQL.mySQL != null) {
            try {
                MySQL.getMySQL().closeConnection();
            } catch (final SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    public static CachedRowSet query(@Language("MySQL") final String sql) {
        try {
            return MySQL.getMySQL().executeQuery(sql);
        } catch (final SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static void update(@Language("MySQL") final String sql) {
        try {
            MySQL.getMySQL().executeUpdate(sql);
        } catch (final SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static PreparedStatement preparedStatement(@Language("MySQL") final String sql) {
        try {
            return MySQL.getMySQL().executePreparedStatement(sql);
        } catch (final SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static String preventSQLInjection(final String parameter) {
        return MySQL.getMySQL().removeSQLInjectionPossibility(parameter);
    }

    public static MySQL getMySQL() {
        return MySQL.mySQL;
    }

    private final String hostname;
    private final int port;
    private final String username;
    private final String password;
    private final String database;

    private Connection connection;

    public MySQL(final String hostname, final String username, final String password) {
        this(hostname, 3306, username, password, null);
    }

    public MySQL(final String hostname, final int port, final String username, final String password) {
        this(hostname, port, username, password, null);
    }

    public MySQL(final String hostname, final String username, final String password, final String database) {
        this(hostname, 3306, username, password, database);
    }

    public MySQL(final String hostname, final int port, final String username, final String password, final String database) {
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.password = password;
        this.database = database;
    }

    void openConnection() throws SQLException, ClassNotFoundException {
        if (!this.isConnectionOpen()) {
            String connectionURL = "jdbc:mysql://" + this.hostname + ":" + this.port;
            if (this.database != null) {
                connectionURL = connectionURL + "/" + this.database;
            }
            connectionURL += "?useSSL=false";
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(connectionURL, this.username, this.password);
        }
    }

    boolean isConnectionOpen() throws SQLException {
        return this.connection != null && !this.connection.isClosed();
    }

    void closeConnection() throws SQLException {
        if (this.isConnectionOpen()) {
            this.connection.close();
        }
    }

    public String removeSQLInjectionPossibility(final String sqlInjectionPossibility) {
        return sqlInjectionPossibility.replaceAll("[']", "\\\\'").replaceAll("[`]", "\\\\`");
    }

    public void executeUpdate(final String sql) throws SQLException {
        if (this.isConnectionOpen()) {
            final Statement statement = this.connection.createStatement();
            statement.executeUpdate(sql);
            statement.close();
        }
    }

    public CachedRowSet executeQuery(final String sql) throws SQLException {
        if (this.isConnectionOpen()) {
            final Statement statement = this.connection.createStatement();
            final ResultSet resultSet = statement.executeQuery(sql);
            final CachedRowSet cachedRowSet = RowSetProvider.newFactory().createCachedRowSet();
            cachedRowSet.populate(resultSet);
            statement.close();
            return cachedRowSet;
        }
        return null;
    }

    public PreparedStatement executePreparedStatement(final String sql) throws SQLException {
        if (this.isConnectionOpen()) {
            return this.connection.prepareStatement(sql);
        }
        return null;
    }

}
