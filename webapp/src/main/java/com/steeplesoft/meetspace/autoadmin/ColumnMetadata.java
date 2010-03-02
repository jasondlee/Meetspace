package com.steeplesoft.meetspace.autoadmin;

import javax.persistence.TemporalType;

/**
 * Created by IntelliJ IDEA.
 * User: jasonlee
 * Date: Feb 28, 2010
 * Time: 2:36:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class ColumnMetadata {
    private String name;
    private Class type;
    private int length;
    private TemporalType temporalType;
    private boolean isPrimaryKey;
    private Object value;

    public ColumnMetadata(String name, Class type, int length) {
        this.name = name;
        this.type = type;
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public Class getType() {
        return type;
    }

    public int getLength() {
        return length;
    }

    public String getTemporalType() {
        return temporalType.name();
    }

    public void setTemporalType(TemporalType temporalType) {
        this.temporalType = temporalType;
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        isPrimaryKey = primaryKey;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}