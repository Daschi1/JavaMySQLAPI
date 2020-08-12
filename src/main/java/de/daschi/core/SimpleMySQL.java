package de.daschi.core;

public class SimpleMySQL { // NOTE this class is deprecated, may be revisited at a later point

    /*public static CachedRowSet select(@NotNull final String table, @NotNull final String whereColumn, @NotNull final String whereComparator, @NotNull final Object whereValue) {
        try {
            return Objects.requireNonNull(MySQL.getMySQL()).executeQuery("SELECT * FROM `" + MySQL.getMySQL().removeSQLInjectionPossibility(table) + "` WHERE `" + MySQL.getMySQL().removeSQLInjectionPossibility(whereColumn) + "` " + MySQL.getMySQL().removeSQLInjectionPossibility(whereComparator) + " '" + MySQL.getMySQL().removeSQLInjectionPossibility(whereValue.toString()) + "';");
        } catch (final SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static void insert(@NotNull final String table, @NotNull final Object... values) {
        final StringBuilder stringBuilder = new StringBuilder("(");
        final Object[] var3 = values;
        final int var4 = values.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            final Object value = var3[var5];
            stringBuilder.append(MySQL.preventSQLInjection(value.toString())).append(", ");
        }

        MySQL.update("INSERT INTO `" + MySQL.preventSQLInjection(table) + "` VALUES " + stringBuilder.substring(0, stringBuilder.length() - 3) + ");");
    }

    public static void update(@NotNull final String table, @NotNull final String column, @NotNull final Object value, @NotNull final String whereColumn, @NotNull final String whereComparator, @NotNull final Object whereValue) {
        MySQL.update("UPDATE `" + MySQL.preventSQLInjection(table) + "` SET `" + MySQL.preventSQLInjection(column) + "` = '" + MySQL.preventSQLInjection(value.toString()) + "' WHERE `" + MySQL.preventSQLInjection(whereColumn) + "` " + MySQL.preventSQLInjection(whereComparator) + " '" + MySQL.preventSQLInjection(whereValue.toString()) + "';");
    }

    public static void delete(@NotNull final String table, @NotNull final String whereColumn, @NotNull final String whereComparator, @NotNull final Object whereValue) {
        MySQL.update("DELETE FROM `" + MySQL.preventSQLInjection(table) + "` WHERE `" + MySQL.preventSQLInjection(whereColumn) + "` " + MySQL.preventSQLInjection(whereComparator) + " '" + MySQL.preventSQLInjection(whereValue.toString()) + "';");
    }*/
}
