package com.steeplesoft.meetspace.autoadmin;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: jasonlee
 * Date: Feb 28, 2010
 * Time: 2:06:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class ModelMetadata {
    private Class clazz;
    private String name;
    private String displayName;
    private List<ColumnMetadata> columns = new ArrayList<ColumnMetadata>();

    public ModelMetadata(Class clazz) {
        this.clazz = clazz;
        name = clazz.getName();
        displayName = name;

        determineFieldInformation();
    }

    public List<ColumnMetadata> getColumns() {
        return columns;
    }

    protected void determineFieldInformation() {
        Annotation[] annotations = clazz.getAnnotations();
        for (Annotation a : annotations) {
            if (a instanceof Table) {
            }
        }

        for (Field f : clazz.getDeclaredFields()) {
            System.out.println("Field " + f.getName() + ": " + f.getType());
            for (Annotation a : f.getAnnotations()) {
                if (a instanceof Column) {
                    addColumn(f, (Column) a);
                }
            }
        }
    }

    protected void addColumn(Field field, Column column) {
        final ColumnMetadata cmd = new ColumnMetadata(field.getName(), field.getType(), column.length());
        columns.add(cmd);
        Temporal t = field.getAnnotation(Temporal.class);
        if (t != null) {
            cmd.setTemporalType(t.value());
        }

        cmd.setPrimaryKey(field.getAnnotation(Id.class) != null);
    }
}
