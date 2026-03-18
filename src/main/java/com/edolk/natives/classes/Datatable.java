package com.edolk.natives.classes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Datatable {

    // private List<List<Object>> matrix;
    public Map<String, List<Object>> matrix = new LinkedHashMap<>();
    private int headerAmountLimit = 20;
    private int elementLengthLimit = 8;

    public static Datatable readCsv(String path, boolean headersInFile, String[] headers){
        Datatable dt = new Datatable();
        String line = "";
        String splitBy = ",";
        boolean headersDone = false;
        if (!headersInFile) {
            for (String string : headers) {
                dt.matrix.put(string, new ArrayList<>());
                headersDone = true;
            }
        }
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            while ((line = br.readLine()) != null) {
                String[] strVals = line.split(splitBy);
                if (headersDone) {
                    int i = 0;
                    for (Entry<String, List<Object>> entry : dt.matrix.entrySet()) {
                        entry.getValue().add(toObj(strVals[i++]));
                    }
                } else {
                    for (String string : strVals) {
                        dt.matrix.put(string, new ArrayList<>());
                    }
                    headersDone = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }

    public static Object[] toObj(String[] strVals){
        Object[] objs = new Object[strVals.length];
        for (int i = 0; i < objs.length; i++) {
            objs[i] = toObj(strVals[i]);
        }
        return objs;
    }

    public static Object toObj(String str){
        try {
            Double d = Double.valueOf(str);
            if (d % 1 == 0) {
                return (int)Math.floor(d);
            }
        } catch (Exception e) {}
        switch (str.trim().toLowerCase()) {
            case "yes":
            case "true":
                return true;
            case "no":
            case "false":
                return false;
        }
        return str;
    }

    public Column select(String columnName){
        return new Column(this, columnName);
    }

    public class Row {
        private Datatable table;
        private int rowIndex;
        public Row(Datatable table, int rowIndex) {
            this.table = table;
            this.rowIndex = rowIndex;
        }
        public List<Object> asList(){
            List<Object> row = new ArrayList<>();
            if (rowIndex == 0) {
                for (String str : table.matrix.keySet()) {
                    row.add(str);
                }
                return row;
            }
            for (List<Object> object : table.matrix.values()) {
                row.add(object.get(rowIndex-1));
            }
            return row;
        }

    }

    public class Column {
        private String columnName;
        private Datatable table;
        public Column(Datatable table, String columnName) {
            this.columnName = columnName;
            this.table = table;
        }
        public List<Object> asList(){
            return table.matrix.get(columnName);
        }
    }

    @Override
    public String toString() {
        String sep = System.lineSeparator();
        StringBuilder builder = new StringBuilder();
        List<String> h = new ArrayList<>(matrix.keySet());
        boolean skipHeaders = h.size() > headerAmountLimit;
        
        List<String> headers = new ArrayList<>();
        if (!skipHeaders) {
            headers.addAll(h);
        } else {
            for (int i = 0; i < 4; i++) {
                headers.add(h.get(i));
            }
            headers.add("...");
            for (int i = h.size() - 4; i < h.size(); i++) {
                headers.add(h.get(i));
            }
        }
        builder.append(" | ");
        for (String string : headers) {
            builder.append(toLength(string, elementLengthLimit));
            builder.append(" | ");
        }
        builder.append(sep);

        int rowFrom = 0;
        int rowTo = 10;

        for (int i = rowFrom; i < rowTo; i++) {
            builder.append(" | ");
            for (String string : headers) {
                if (string.equals("...")) {
                    builder.append(toLength("...", elementLengthLimit));
                } else {
                    List<Object> values = matrix.get(string);
                    builder.append(toLength(values.get(i).toString(), elementLengthLimit));
                }
                builder.append(" | ");
            }
            builder.append(sep);
        }

        return builder.toString();
    }

    private String toLength(String str, int l){
        if (str.length() > l) {
            return str.substring(0,l);
        } else if (str.length() < l){
            int extrNeeded = l - str.length();
            boolean odd = extrNeeded % 2 != 0;
            int repeats = (int)Math.floor(extrNeeded/2);
            str = String.valueOf(' ').repeat(repeats) + str + String.valueOf(' ').repeat(repeats);
            if (odd) {
                str += ' ';
            }
            return str;
        }
        return str;
    }

}
