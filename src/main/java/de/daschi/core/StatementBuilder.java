package de.daschi.core;

class StatementBuilder {
    private final String database;
    private final String table;

    StatementBuilder(final String database, final String table) {
        this.database = database;
        this.table = table;
    }

    public SelectBuilder select() {
        return new SelectBuilder(this.database, this.table);
    }

    void query() {

    }

    String getDatabase() {
        return this.database;
    }

    String getTable() {
        return this.table;
    }
}
