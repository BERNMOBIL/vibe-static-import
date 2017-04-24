package ch.bernmobil.vibe.staticdata;

import java.util.ArrayList;
import java.util.List;

public class QueryBuilder {
    private String query;

    public QueryBuilder Select(String table) {
        return Select("*", table);
    }

    public QueryBuilder Select(String fields, String table) {
        query = "SELECT " + fields + " FROM " + table;
        return this;
    }

    public QueryBuilder Where(Predicate predicate) {
        query += " WHERE " + predicate.toString();
        return this;
    }

    public QueryBuilder Insert(String table, String[] fields, String[] values) {
        String fieldSeperator = ", ";
        query = "INSERT INTO " + table + "(";
        for(String field : fields) {
            query += field + fieldSeperator;
        }
        query = query.substring(0, query.length() - fieldSeperator.length());
        query += ") VALUES(";
        for(String value : values) {
            query += value + fieldSeperator;
        }
        query = query.substring(0, query.length() - fieldSeperator.length());
        query += ")";
        return this;
    }



    public String getQuery() {
        return query;
    }

    public static class PreparedStatement {
        public QueryBuilder Insert(String table, String... fields) {
            ArrayList<String> questionMarks = new ArrayList<>();
            for(int i = 0; i < fields.length; i++) questionMarks.add("?");
            return new QueryBuilder().Insert(table, fields, questionMarks.toArray(new String[questionMarks.size()]));
        }
    }


    public static class Predicate {
        String predicate;

        private Predicate(String predicate) {
            this.predicate = predicate;
        }

        public static Predicate equals(Object left, Object right) {
            return new Predicate(left.toString() + " = " + right.toString());
        }

        public static Predicate joinAnd(List<Predicate> predicates) {
            return Join(" AND ", predicates);
        }

        public static Predicate joinOr(List<Predicate> predicates) {
            return Join(" OR ", predicates);
        }

        public Predicate and(Predicate predicate) {
            return Join("AND", predicate);
        }

        public Predicate or(Predicate predicate) {
            return Join("AND", predicate);
        }

        public String toString() {
            return predicate;
        }

        private Predicate Join(String operator, Predicate predicate) {
            return new Predicate(toString() + " " + operator + " " + predicate.toString());
        }

        private static Predicate Join(String operator, List<Predicate> predicates) {
            String result = "";
            for(Predicate predicate : predicates) {
                result += predicate.toString() + operator;
            }
            return new Predicate(result.substring(0, result.length()-operator.length()));
        }
    }


}
