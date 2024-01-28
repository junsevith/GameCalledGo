package org.theGo.database;

public abstract class Filter {
    Filter next;
    String modifier;
    public String getQuery(int page){
        return "SELECT * FROM `games` WHERE 1=1" + modifier() + " ORDER BY `date` DESC LIMIT " + (page-1)*10 + ", 10";
    }

    protected String modifier(){
        return " AND " + modifier + next.modifier();
    }

    public static class Clear extends Filter {
        @Override
        protected String modifier(){
            return "";
        }
    }

    public static class Nickname extends Filter {
        public Nickname(String user, Filter next){
            this.modifier = "`black` = '" + user + "' OR `white` = '" + user + "'";
            this.next = next;
        }
    }

    public static class Custom extends Filter {
        public Custom(String modifier, Filter next){
            this.modifier = modifier;
            this.next = next;
        }
    }
}
