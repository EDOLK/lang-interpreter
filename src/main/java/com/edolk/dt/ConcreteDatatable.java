package com.edolk.dt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.OptionalInt;
import java.util.function.Function;
import java.util.function.Predicate;

public class ConcreteDatatable implements Datatable{
    public Map<String, List<Object>> map = new LinkedHashMap<>();
    public int elementLengthLimit = 15;
    public List<String> groups;

    @Override
    public Row row(int index){
        return new Row(index);
    }

    @Override
    public Column column(String header){
        return new Column(header);
    }

    @Override
    public Datatable select(List<String> headers) {
        ConcreteDatatable table = new ConcreteDatatable();
        table.groups = this.groups;
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
        table.groups = this.groups;
        OptionalInt maxSize = map.values().stream().mapToInt(Collection::size).max();
        if (maxSize.isPresent()) {
            int size = maxSize.getAsInt();
            for (int i = 0; i < size; i++) {
                Row row = new Row(i);
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
        table.groups = this.groups;
        OptionalInt maxSize = map.values().stream().mapToInt(Collection::size).max();
        if (maxSize.isPresent()) {
            int size = maxSize.getAsInt();
            for (int i = 0; i < size; i++) {
                Datatable.Row row = new Row(i);
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
        if (groups != null) {
            return groupBy(groups, reducers);
        }
        ConcreteDatatable dt = new ConcreteDatatable();
        dt.groups = this.groups;
        for (Reducer reducer : reducers) {
            Column column = this.column(reducer.getHeader());
            List<Object> reduction = new ArrayList<>(List.of(reducer.reduce(column)));
            dt.map.put(reducer.getNewHeader(), reduction);
        }
        return dt;
    }

    @Override
    public Datatable groupBy(List<String> headers){
        this.groups = headers;
        return this;
    }

    @Override
    public Datatable groupBy(List<String> headers, List<Reducer> reducers) {
        ConcreteDatatable dt = new ConcreteDatatable();
        dt.groups = this.groups;
        List<List<Object>> possibleValues = new ArrayList<>();
        for (String header : headers) {
            possibleValues.add(new ArrayList<>(new HashSet<>(this.column(header).getObjects())));
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
                Datatable.Column col = filtered.column(reducer.getHeader());
                dt.map.computeIfPresent(reducer.getNewHeader(), (k,v) -> {
                    v.add(reducer.reduce(col));
                    return v;
                });
                dt.map.putIfAbsent(reducer.getNewHeader(), new ArrayList<>(List.of(reducer.reduce(col))));
            }
        }
        return dt;
    }

    @Override
    public String toString() {
        String sep = System.lineSeparator();
        String str = " | ";
        for (String header : this.map.keySet()) {
            str += stringify(header, elementLengthLimit) + " | ";
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
                    str += stringify("", elementLengthLimit) + " | ";
                    continue;
                }
                str += stringify(this.map.get(header).get(i), elementLengthLimit) + " | ";
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

    private String stringify(Object obj, int l){
        switch (obj) {
            case Number number -> {
                return String.format("%" + l + ".2f", number.doubleValue());
            }
            case String str -> {
                return String.format("%" + l + "." + l + "s", str);
            }
            default -> {
            }
        }
        return stringify(obj.toString(), l);
    }

    // private String toLength(String str, int l){
    //     if (str.length() > l) {
    //         return str.substring(0,l);
    //     } else if (str.length() < l){
    //         int extraNeeded = l - str.length();
    //         boolean odd = extraNeeded % 2 != 0;
    //         int repeats = (int)Math.floor(extraNeeded/2);
    //         str = String.valueOf(' ').repeat(repeats) + str + String.valueOf(' ').repeat(repeats);
    //         if (odd) {
    //             str += ' ';
    //         }
    //         return str;
    //     }
    //     return str;
    // }

    public class Column implements Datatable.Column {
        private String header;

        public Column(String header) {
            this.header = header;
        }

        @Override
        public String getHeader() {
            return this.header;
        }

        @Override
        public List<Object> getObjects() {
            return ConcreteDatatable.this.map.get(header);
        }

        @Override
        public Object select(int index) {
            return ConcreteDatatable.this.map.get(header).get(index);
        }

        @Override
        public Datatable getSource() {
            return ConcreteDatatable.this;
        }

    }

    public class Row implements Datatable.Row {
        private int index;

        public Row(int index) {
            this.index = index;
        }

        @Override
        public int getIndex() {
            return this.index;
        }

        @Override
        public List<Object> getObjects() {
            List<Object> objs = new ArrayList<>();
            for (Entry<String, List<Object>> entry : ConcreteDatatable.this.map.entrySet()) {
                objs.add(entry.getValue().get(index));
            }
            return objs;
        }

        @Override
        public Object select(String header) {
            return ConcreteDatatable.this.map.get(header).get(getIndex());
        }

        @Override
        public Datatable getSource() {
            return ConcreteDatatable.this;
        }
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public int entrySize() {
        return map.entrySet().stream()
            .mapToInt((e) -> e.getValue().size())
            .sum();
    }

}
