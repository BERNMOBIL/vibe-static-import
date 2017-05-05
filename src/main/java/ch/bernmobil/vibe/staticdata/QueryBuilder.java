package ch.bernmobil.vibe.staticdata;

import java.util.ArrayList;
import java.util.List;

public class QueryBuilder {
    private String query;

    public QueryBuilder select(String table) {
        return select("*", table);
    }

    public QueryBuilder select(String fields, String table) {
        query = "SELECT " + fields + " FROM " + table;
        return this;
    }

    public QueryBuilder where(Predicate predicate) {
        query += " WHERE " + predicate.toString();
        return this;
    }

    public QueryBuilder insert(String table, String[] fields, String[] values) {
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

    public QueryBuilder delete(String table) {
        query = "DELETE FROM " + table;
        return this;
    }

    public QueryBuilder truncate(String table) {
        query = "TRUNCATE " + table;
        return this;
    }

    public String getQuery() {
        return query;
    }

    public static class PreparedStatement {
        public QueryBuilder Insert(String table, String... fields) {
            ArrayList<String> questionMarks = new ArrayList<>();
            for (String field : fields) {
                questionMarks.add("?");
            }
            return new QueryBuilder().insert(table, fields, questionMarks.toArray(new String[questionMarks.size()]));
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

        public static Predicate notEquals(Object left, Object right) {
            return new Predicate(left.toString() + " <> " + right.toString());
        }

        public static Predicate joinAnd(List<Predicate> predicates) {
            return join(" AND ", predicates);
        }

        public static Predicate joinOr(List<Predicate> predicates) {
            return join(" OR ", predicates);
        }

        public Predicate and(Predicate predicate) {
            return join("AND", predicate);
        }

        public Predicate or(Predicate predicate) {
            return join("AND", predicate);
        }

        public String toString() {
            return predicate;
        }

        private Predicate join(String operator, Predicate predicate) {
            return new Predicate(toString() + " " + operator + " " + predicate.toString());
        }

        private static Predicate join(String operator, List<Predicate> predicates) {
            String result = "";
            for(Predicate predicate : predicates) {
                result += predicate.toString() + operator;
            }
            return new Predicate(result.substring(0, result.length()-operator.length()));
        }
    }


}
