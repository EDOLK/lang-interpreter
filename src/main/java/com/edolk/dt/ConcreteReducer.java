package com.edolk.dt;

import java.util.function.Function;

import com.edolk.dt.Datatable.Column;
import com.edolk.dt.Datatable.Reducer;

public class ConcreteReducer implements Reducer{
    public String header;
    public String newHeader;
    public Function<Column, Object> reductionFunction;
    public ConcreteReducer(String header, Function<Column, Object> reductionFunction) {
        this.header = header;
        this.newHeader = header;
        this.reductionFunction = reductionFunction;
    }
    @Override
    public Reducer as(String newHeader){
        this.newHeader = newHeader;
        return this;
    }
    @Override
    public String getHeader() {
        return this.header;
    }
    @Override
    public String getNewHeader() {
        return this.newHeader;
    }
    @Override
    public Object reduce(Column column) {
        return reductionFunction.apply(column);
    }
}
