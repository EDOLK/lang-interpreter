package com.edolk.natives.classes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.OptionalInt;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public class ConcreteDatatable implements Datatable{
    public Map<String, List<Object>> map = new LinkedHashMap<>();
    private int elementLengthLimit = 10;

    @Override
    public Row row(int index){
        return new Row(this, index);
    }

    @Override
    public Column column(String header){
        return new Column(this, header);
    }

    @Override
    public Map<String, List<Object>> getMap() {
        return this.map;
    }

    @Override
    public Datatable select(List<String> headers) {
        ConcreteDatatable table = new ConcreteDatatable();
        for (String header : headers) {
            if (map.containsKey(header)) {
                table.map.put(header, map.get(header));
            }
        }
        return table;
    }

    @Override
    public Datatable filter(Predicate<Datatable.Row> filter) {
        ConcreteDatatable table = new ConcreteDatatable();
        OptionalInt maxSize = map.values().stream().mapToInt(Collection::size).max();
        if (maxSize.isPresent()) {
            int size = maxSize.getAsInt();
            for (int i = 0; i < size; i++) {
                Row row = new Row(this, i);
                if (filter.test(row)) {
                    for (String header : this.map.keySet()) {
                        table.map.computeIfPresent(
                            header, 
                            (k,v) -> {
                                v.add(row.select(header));
                                return v;
                            }
                        );
                        table.map.putIfAbsent(
                            header, 
                            new ArrayList<>(List.of(row.select(header)))
                        );
                    }
                }
            }
        }
        return table;
    }

    @Override
    public Datatable map(Function<Datatable.Row, Datatable.Row> mapper) {
        ConcreteDatatable table = new ConcreteDatatable();
        OptionalInt maxSize = map.values().stream().mapToInt(Collection::size).max();
        if (maxSize.isPresent()) {
            int size = maxSize.getAsInt();
            for (int i = 0; i < size; i++) {
                Datatable.Row row = new Row(this, i);
                Datatable.Row newRow = mapper.apply(row);
                for (String header : this.map.keySet()) {
                    table.map.computeIfPresent(
                        header, 
                        (k,v) -> {
                            v.add(newRow.select(header));
                            return v;
                        }
                    );
                    table.map.putIfAbsent(
                        header, 
                        new ArrayList<>(List.of(newRow.select(header)))
                    );
                }
            }
        }
        return table;
    }

    @Override
    public Datatable reduce(List<Reducer> reducers) {
        ConcreteDatatable table = new ConcreteDatatable();
        for (Reducer reducer : reducers) {
            Column column = this.column(reducer.header);
            table.map.put(reducer.newHeader, new ArrayList<>(List.of(reducer.reductionFunction.apply(column))));
        }
        return table;
    }

    @Override
    public Datatable groupBy(List<String> headers, List<Reducer> reducers) {
        ConcreteDatatable dt = new ConcreteDatatable();
        List<List<Object>> possibleValues = new ArrayList<>();
        for (String header : headers) {
            Column column = this.column(header);
            Set<Object> possibleForHeader = new HashSet<>();
            possibleForHeader.addAll(column.getObjects());
            possibleValues.add(new ArrayList<>(possibleForHeader));
        }
        Slicer slicer = new Slicer(possibleValues);
        while (slicer.hasNext()) {
            List<Object> values = slicer.next();
            Datatable filtered = this.filter((Datatable.Row row) -> {
                for (int i = 0; i < headers.size(); i++) {
                    if (!row.select(headers.get(i)).equals(values.get(i))) {
                        return false;
                    }
                }
                return true;
            });
            for (int i = 0; i < headers.size(); i++) {
                final int j = i;
                dt.map.computeIfPresent(headers.get(i), (k,v) -> {
                    v.add(values.get(j));
                    return v;
                });
                dt.map.putIfAbsent(headers.get(i), new ArrayList<>(List.of(values.get(j))));
            }
            for (Reducer reducer : reducers) {
                Datatable.Column col = filtered.column(reducer.header);
                dt.map.computeIfPresent(reducer.newHeader, (k,v) -> {
                    v.add(reducer.reductionFunction.apply(col));
                    return v;
                });
                dt.map.putIfAbsent(reducer.newHeader, new ArrayList<>(List.of(reducer.reductionFunction.apply(col))));
            }
        }
        return dt;
    }

    @Override
    public String toString() {
        String sep = System.lineSeparator();
        String str = " | ";
        for (String header : this.map.keySet()) {
            str += toLength(header, elementLengthLimit) + " | ";
        }
        str += sep;
        str += String.valueOf('-').repeat(str.length());
        str += sep;
        str += " | ";
        int i = 0;
        boolean added;
        int lines = 0;
        do {
            added = false;
            for (String header : this.map.keySet()) {
                if (i >= this.map.get(header).size()) {
                    str += toLength("", elementLengthLimit) + " | ";
                    continue;
                }
                str += toLength(this.map.get(header).get(i).toString(), elementLengthLimit) + " | ";
                added = true;
            }
            if (added) {
                str += sep;
                str += " | ";
            }
            i++;
        } while (added && lines++ < 10);
        return str;
    }

    private String toLength(String str, int l){
        if (str.length() > l) {
            return str.substring(0,l);
        } else if (str.length() < l){
            int extraNeeded = l - str.length();
            boolean odd = extraNeeded % 2 != 0;
            int repeats = (int)Math.floor(extraNeeded/2);
            str = String.valueOf(' ').repeat(repeats) + str + String.valueOf(' ').repeat(repeats);
            if (odd) {
                str += ' ';
            }
            return str;
        }
        return str;
    }

    public class Column implements Datatable.Column {
        private Datatable table;
        private String header;

        public Column(Datatable table, String header) {
            this.table = table;
            this.header = header;
        }

        @Override
        public String getHeader() {
            return this.header;
        }

        @Override
        public List<Object> getObjects() {
            return table.getMap().get(getHeader());
        }

        @Override
        public Object select(int index) {
            return map.get(header).get(index);
        }

    }

    public class Row implements Datatable.Row {
        private Datatable table;
        private int index;

        public Row(Datatable table, int index) {
            this.table = table;
            this.index = index;
        }

        @Override
        public int getIndex() {
            return this.index;
        }

        @Override
        public List<Object> getObjects() {
            List<Object> objs = new ArrayList<>();
            for (Entry<String, List<Object>> entry : table.getMap().entrySet()) {
                objs.add(entry.getValue().get(index));
            }
            return objs;
        }

        @Override
        public Object select(String header) {
            return map.get(header).get(getIndex());
        }
    }

}
