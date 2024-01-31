package org.theGo.database;

/**
 * Decorator class responsible for constructing queries for filtering games from database.
 */
public abstract class Filter {
    Filter next;
    String modifier;

    /**
     * Returns query with all filters applied.
     *
     * @param page number of page to get
     * @return query for getting games from database
     */
    public String getQuery(int page) {
        return "SELECT * FROM `games` WHERE 1=1" + modifier() + " ORDER BY `date` DESC LIMIT " + (page - 1) * 10 + " ,10";
    }

    /**
     * Collects all modifiers from decorator type filters joins them with AND, and returns them.
     * Should not be modified
     *
     * @return modifiers from all filters
     */
    protected String modifier() {
        return " AND " + modifier + next.modifier();
    }

    /**
     * Filter that does nothing.
     */
    public static class Clear extends Filter {
        @Override
        protected String modifier() {
            return "";
        }
    }

    /**
     * Filter that filters games by user participating.
     */
    public static class Nickname extends Filter {
        /**
         * Creates filter that filters games by user participating.
         *
         * @param user nickname of user
         * @param next next filter
         */
        public Nickname(String user, Filter next) {
            this.modifier = "`black` = '" + user + "' OR `white` = '" + user + "'";
            this.next = next;
        }
    }

    /**
     * Custom filter that accepts SQL.
     */
    public static class Custom extends Filter {
        /**
         * Creates custom filter.
         *
         * @param modifier SQL to add to query
         * @param next     next filter
         */
        public Custom(String modifier, Filter next) {
            this.modifier = modifier;
            this.next = next;
        }
    }
}
