package de.daschi.core;

import java.sql.*;

public class MySQL {
    private static MySQL mySQL;

    public static void using(final MySQL mySQL) {
        try {
            if (MySQL.mySQL != null) {
                MySQL.mySQL.closeConnection();
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

    public static StatementBuilder builder(final String table) {
        return MySQL.builder(MySQL.mySQL.database, table);
    }

    public static StatementBuilder builder(final String database, final String table) {
        return new StatementBuilder(database, table);
    }

    static MySQL getMySQL() {
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
            Class.forName("com.mysql.jdbc.Driver");
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

    void executeUpdate(final String sql) throws SQLException {
        if (this.isConnectionOpen()) {
            final Statement statement = this.connection.createStatement();
            statement.executeUpdate(sql);
            statement.close();
        }
    }

    ResultSet executeQuery(final String sql) throws SQLException {
        if (this.isConnectionOpen()) {
            final Statement statement = this.connection.createStatement();
            return statement.executeQuery(sql);
        }
        return null;
    }

}
