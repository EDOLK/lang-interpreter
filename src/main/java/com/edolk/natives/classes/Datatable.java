package com.edolk.natives.classes;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public interface Datatable {
    public static Row row(Datatable dt, int index){
        return dt.row(index);
    }
    public static Column column(Datatable dt, String header){
        return dt.column(header);
    }
    public static Datatable select(Datatable dt, List<String> headers){
        return dt.select(headers);
    }
    public static Datatable filter(Datatable dt, Predicate<Row> filter){
        return dt.filter(filter);
    }
    public static Datatable map(Datatable dt, Function<Row, Row> mapper){
        return dt.map(mapper);
    }
    public static Datatable reduce(Datatable dt, List<Reducer> reducers){
        return dt.reduce(reducers);
    }
    public static Datatable groupBy(Datatable dt, List<String> headers, List<Reducer> reducer){
        return dt.groupBy(headers, reducer);
    }
    public Map<String, List<Object>> getMap();
    public Row row(int index);
    public Column column(String header);
    public Datatable select(List<String> headers);
    public Datatable filter(Predicate<Row> filter);
    public Datatable map(Function<Row, Row> mapper);
    public Datatable reduce(List<Reducer> reducers);
    public Datatable groupBy(List<String> headers, List<Reducer> reducer);

    public interface Row {
        public int getIndex();
        public List<Object> getObjects();
        public Object select(String header);
    }

    public interface Column {
        public String getHeader();
        public List<Object> getObjects();
        public Object select(int index);
    }

    public class Reducer {
        public String header;
        public String newHeader;
        public Function<Column, Object> reductionFunction;
        public Reducer(String header, Function<Column, Object> reductionFunction) {
            this.header = header;
            this.newHeader = header;
            this.reductionFunction = reductionFunction;
        }
        public Reducer as(String newHeader){
            this.newHeader = newHeader;
            return this;
        }
    }
}
