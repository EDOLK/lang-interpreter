package com.edolk.dt;

import java.util.Collection;
import java.util.List;
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
    public static Datatable groupBy(Datatable dt, List<String> headers, List<Reducer> reducers){
        return dt.groupBy(headers, reducers);
    }
    public static Datatable mutate(Datatable dt, List<Mutator> mutators){
        return dt.mutate(mutators);
    }
    public static Datatable sortBy(Datatable dt, String header, boolean desc){
        return dt.sortBy(header, desc);
    }
    public static Datatable sortBy(Datatable dt, String header, List<String> order, boolean desc){
        return dt.sortBy(header, order, desc);
    }
    public Row row(int index);
    public Column column(String header);
    public Datatable select(List<String> headers);
    public Datatable filter(Predicate<Row> filter);
    public Datatable map(Function<Row, Row> mapper);
    public Datatable reduce(List<Reducer> reducers);
    public Datatable groupBy(List<String> headers, List<Reducer> reducers);
    public Datatable groupBy(List<String> headers);
    public Datatable mutate(List<Mutator> mutator);
    public Datatable sortBy(String header, boolean desc);
    public Datatable sortBy(String header, List<String> order, boolean desc);
    public int size();
    public int entrySize();

    public interface Row {
        public Datatable getSource();
        public int getIndex();
        public List<Object> getObjects();
        public Object select(String header);
    }

    public interface Column {
        public Datatable getSource();
        public String getHeader();
        public List<Object> getObjects();
        public Object select(int index);
    }

    public interface Reducer {
        public String getHeader();
        public String getNewHeader();
        public Object reduce(Column column);
        public Reducer as(String newHeader);
    }

    public interface Mutator {
        public String getHeader();
        public Object mutate(Row row);
        public static Mutator create(String header, Collection<Object> collection){
            return new CollectionMutator(header, collection);
        }
        public static Mutator create(String header, Function<Row, Object> function){
            return new FunctionalMutator(header, function);
        }
        public static Mutator create(String header){
            return new Remover(header);
        }
    }

    public class CollectionMutator implements Mutator {
        private Collection<Object> collection;
        private String header;

        public CollectionMutator(String header, Collection<Object> collection) {
            this.header = header;
            this.collection = collection;
        }

        @Override
        public String getHeader() {
            return header;
        }
        @Override
        public Object mutate(Row row) {
            int index = row.getIndex();
            if (index >= 0 && index < collection.size()) {
                return collection.toArray()[index];
            }
            return null;
        }
    }

    public class ArrayMutator implements Mutator{

        private String header;
        private Object[] objects;

        public ArrayMutator(String header, Object[] objects) {
            this.header = header;
            this.objects = objects;
        }

        @Override
        public String getHeader() {
            return this.header;
        }

        @Override
        public Object mutate(Row row) {
            int index = row.getIndex();
            if (index >= 0 && index < objects.length) {
                return objects[index];
            }
            return null;
        }

    }

    public class Remover implements Mutator {
        private String header;
        public Remover(String header) {
            this.header = header;
        }

        @Override
        public String getHeader() {
            return header;
        }

        @Override
        public Object mutate(Row row) {
            return null;
        }

    }

    public class FunctionalMutator implements Mutator{

        public String header;
        public Function<Row, Object> mutationFunction;

        public FunctionalMutator(String header, Function<Row, Object> mutationFunction) {
            this.header = header;
            this.mutationFunction = mutationFunction;
        }

        @Override
        public String getHeader() {
            return header;
        }

        @Override
        public Object mutate(Row row) {
            return mutationFunction.apply(row);
        }

    }

}
