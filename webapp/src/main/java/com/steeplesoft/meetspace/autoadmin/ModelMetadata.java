package com.steeplesoft.meetspace.autoadmin;

import com.steeplesoft.meetspace.view.util.MeetspaceUtil;

import javax.persistence.Column;
import javax.persistence.Table;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * This class will load the metadata for a specific model class.  The metadata will be extracted from either annotations
 * on the Class itself or a properties file, with the properties taking precedence.  The model-specific properties are
 * specified with the following format:
 *
 * autoadmin.model.&lt;Class name&gt;.field.&lt;Field name&gt;.&lt;Attribute&gt;=&lt;value&gt;
 *
 * The valid attributes are as follows:
 * <ul>
 *      <li>label - The label displayed in the title of the generated form.</li>
 * </ul>
 */
public class ModelMetadata {
    private Class modelClass;
    private String name;
    private String displayName;
    private List<ColumnMetadata> columns = new ArrayList<ColumnMetadata>();
    private Properties properties;

    public ModelMetadata(Class modelClass, Properties properties) {
        this.modelClass = modelClass;
        this.properties = properties;
        name = modelClass.getSimpleName();
        displayName = name;

        determineFieldInformation();
    }

    public Class<?> getModelClass() {
        return modelClass;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<ColumnMetadata> getColumns() {
        return columns;
    }

    protected void determineFieldInformation() {
        determineFieldInformationFromAnnotations();
        determineFieldInformationFromProperties();
    }

    protected void determineFieldInformationFromProperties() {
        String label = properties.getProperty("autoadmin.model." + name + ".label");
        displayName = (label != null) ? label : MeetspaceUtil.splitCamelCasedString(name);
    }

    protected void determineFieldInformationFromAnnotations() {
        Annotation[] annotations = modelClass.getAnnotations();
        for (Annotation a : annotations) {
            if (a instanceof Table) {
            }
        }

        for (Field f : modelClass.getDeclaredFields()) {
            System.out.println("Field " + f.getName() + ": " + f.getType());
            if (f.isAnnotationPresent(Column.class)){
                ColumnMetadata cmd = new ColumnMetadata(name, f, properties);
                columns.add(cmd);
            }
        }
    }
}