package de.daschi.core;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.intellij.lang.annotations.Language;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class MySQL {

    private static final Map<String, MySQL> mySQLs = new HashMap<>();
    private static final Map<String, Boolean> autoDisconnects = new HashMap<>();

    private static final Thread shutdownHook = new Thread(() -> MySQL.autoDisconnects.forEach((s, aBoolean) -> {
        if (aBoolean) {
            try {
                MySQL.mySQLs.get(s).closeConnection();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }));

    public static void autoDisconnect(final String id, final boolean autoDisconnect) {
        if (MySQL.autoDisconnects.containsKey(id)) {
            MySQL.autoDisconnects.put(id, autoDisconnect);
        }
        if (MySQL.autoDisconnects.isEmpty()) {
            Runtime.getRuntime().removeShutdownHook(MySQL.shutdownHook);
        } else {
            Runtime.getRuntime().addShutdownHook(MySQL.shutdownHook);
        }
    }

    public static void add(final String id, final MySQL mySQL) {
        try {
            if (MySQL.mySQLs.containsKey(id)) {
                MySQL.disconnect(id);
                MySQL.mySQLs.remove(id);
                MySQL.add(id, mySQL);
            } else {
                MySQL.mySQLs.put(id, mySQL);
                MySQL.autoDisconnects.put(id, false);
                MySQL.mySQLs.get(id).openConnection();
            }
        } catch (final SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static MySQL getMySQL() {
        if (!MySQL.mySQLs.isEmpty()) {
            return (MySQL) MySQL.mySQLs.values().toArray()[0];
        }
        return null;
    }

    public static MySQL getMySQL(final String id) {
        if (MySQL.mySQLs.containsKey(id)) {
            return MySQL.mySQLs.remove(id);
        }
        return null;
    }

    public static boolean hasMySQL(final String id) {
        return MySQL.mySQLs.containsKey(id);
    }

    public static void remove(final String id) {
        if (MySQL.mySQLs.containsKey(id)) {
            MySQL.disconnect(id);
            MySQL.mySQLs.remove(id);
        }
    }

    public static void disconnect(final String id) {
        if (MySQL.mySQLs.containsKey(id)) {
            try {
                MySQL.mySQLs.get(id).closeConnection();
            } catch (final SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    public static CachedRowSet query(final String id, @Language("MySQL") final String sql) {
        try {
            return MySQL.mySQLs.get(id).executeQuery(sql);
        } catch (final SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static void update(final String id, @Language("MySQL") final String sql) {
        try {
            MySQL.mySQLs.get(id).executeUpdate(sql);
        } catch (final SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static PreparedStatement preparedStatement(final String id, @Language("MySQL") final String sql) {
        try {
            return MySQL.mySQLs.get(id).executePreparedStatement(sql);
        } catch (final SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static String preventSQLInjection(final String id, final String parameter) {
        return MySQL.mySQLs.get(id).removeSQLInjectionPossibility(parameter);
    }

    public static Map<String, MySQL> getMySQLs() {
        return MySQL.mySQLs;
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

    public void openConnection() throws SQLException {
        if (!this.isConnectionOpen()) {
            final MysqlDataSource mysqlDataSource = new MysqlDataSource();
            mysqlDataSource.setServerName(this.hostname);
            mysqlDataSource.setPort(this.port);
//            String connectionURL = "jdbc:mysql://" + this.hostname + ":" + this.port;
            if (this.database != null) {
                mysqlDataSource.setDatabaseName(this.database);
//                connectionURL = connectionURL + "/" + this.database;
            }
            mysqlDataSource.setAllowPublicKeyRetrieval(true);
            mysqlDataSource.setUseSSL(false);
//            connectionURL += "?allowPublicKeyRetrieval=true&useSSL=false";
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            this.connection = DriverManager.getConnection(connectionURL, this.username, this.password);
            this.connection = mysqlDataSource.getConnection(this.username, this.password);
        }
    }

    public boolean isConnectionOpen() throws SQLException {
        return this.connection != null && !this.connection.isClosed();
    }

    public void closeConnection() throws SQLException {
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
