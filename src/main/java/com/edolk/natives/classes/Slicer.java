package com.edolk.natives.classes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Slicer implements Iterator<List<Object>> {

    private List<List<Object>> objectLists;
    private List<Integer> places = new ArrayList<>();

    public Slicer(List<List<Object>> objectLists) {
        this.objectLists = objectLists;
        for (int i = 0; i < objectLists.size(); i++) {
            places.add(0);
        }
    }

    @Override
    public boolean hasNext() {
        for (int i = 0; i < objectLists.size(); i++) {
            if (places.get(i) >= objectLists.get(i).size()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<Object> next() {
        List<Object> objects = new ArrayList<>();
        for (int i = 0; i < objectLists.size(); i++) {
            objects.add(objectLists.get(i).get(places.get(i)));
        }
        places.set(places.size()-1, places.get(places.size()-1)+1);
        for (int i = places.size()-1; i > 0 ; i--) {
            if (places.get(i) == objectLists.get(i).size()) {
                places.set(i, 0);
                places.set(i-1, places.get(i-1)+1);
            }
        }
        return objects;
    }

    public List<List<Object>> toCartesianProduct(){
        List<List<Object>> product = new ArrayList<>();
        while (hasNext()) {
            product.add(next());
        }
        return product;
    }

}
