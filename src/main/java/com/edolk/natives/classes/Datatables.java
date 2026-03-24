package com.edolk.natives.classes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class Datatables {
    private Datatables() {}
    
    public static Datatable readCsv(String path, boolean headersInFile, String[] headers){
        ConcreteDatatable dt = new ConcreteDatatable();
        String line = "";
        String splitBy = ",";
        boolean headersDone = false;
        if (!headersInFile) {
            for (String string : headers) {
                dt.map.put(string, new ArrayList<>());
                headersDone = true;
            }
        }
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            while ((line = br.readLine()) != null) {
                String[] strVals = line.split(splitBy);
                if (headersDone) {
                    int i = 0;
                    for (Entry<String, List<Object>> entry : dt.map.entrySet()) {
                        entry.getValue().add(toObj(strVals[i++]));
                    }
                } else {
                    for (String string : strVals) {
                        dt.map.put(string, new ArrayList<>());
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

}
