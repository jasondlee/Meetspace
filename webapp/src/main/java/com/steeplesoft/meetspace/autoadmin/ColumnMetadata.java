package com.steeplesoft.meetspace.autoadmin;

import com.steeplesoft.meetspace.view.util.MeetspaceUtil;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.lang.reflect.Field;
import java.util.Properties;

/**
 * This class will load the metadata for a specific model field.  The metadata will be extracted from either annotations
 * on the Field itself or a properties file, with the properties taking precedence.  The field-specific properties are
 * specified with the following format:
 * <p/>
 * autoadmin.model.&lt;Class name&gt;.field.&lt;Field name&gt;.&lt;Attribute&gt;=&lt;value&gt;
 * <p/>
 * The valid attributes are as follows:
 * <ul>
 *      <li>label - The label displayed in the left column of the generated form.  For example, <code>.foo.label=Some field</code>
 *          would generated "Some field:" followed by an input field.</li>
 *      <li>html - Whether or not the field takes HTML input.  False by defult, but, if set to true, the field will rendered
 *          as a rich text component</li>
 *      <li>skipDisplay - If set to false, this field will not be added to the generated form</li>
 *      <li>type - The COMPONENT_TYPE to use when rendering this component.
 * </ul>
 */
public class ColumnMetadata {
    private String model;
    private String name;
    private Class type;
    private int length;
    private TemporalType temporalType;
    private boolean isPrimaryKey;
    private Object value;

    private String label;
    private Boolean html = Boolean.FALSE;
    private Boolean skipDisplay = Boolean.FALSE;
    private String componentType;
    private Properties properties;

    public ColumnMetadata(String model, Field field, Properties properties) {
        this.properties = properties;
        Column column = field.getAnnotation(Column.class);

        this.model = model;
        this.name = field.getName();
        this.type = field.getType();
        this.length = column.length();

        Temporal t = field.getAnnotation(Temporal.class);
        if (t != null) {
            setTemporalType(t.value());
        }

        setPrimaryKey(field.getAnnotation(Id.class) != null);

        readPropertiesData();
    }

    protected void readPropertiesData() {
        String property = properties.getProperty("autoadmin.model." + model + ".field." + name + ".label");
        label = (property != null) ? property : MeetspaceUtil.splitCamelCasedString(name);

        componentType = properties.getProperty("autoadmin.model." + model + ".field." + name + ".inputType");
        html = Boolean.parseBoolean(properties.getProperty("autoadmin.model." + model + ".field." + name + ".html"));

        String prop = properties.getProperty("autoadmin.model." + model + ".field." + name + ".skipDisplay");
        skipDisplay = (prop != null) ? !Boolean.parseBoolean(prop) : Boolean.FALSE;
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

    public TemporalType getTemporalType() {
        return temporalType;
    }

    public void setTemporalType(TemporalType temporalType) {
        this.temporalType = temporalType;
    }

    public boolean getIsPrimaryKey() {
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Boolean isHtml() {
        return html;
    }

    public void setHtml(Boolean html) {
        this.html = html;
    }

    public Boolean isSkipDisplay() {
        return skipDisplay;
    }

    public void setSkipDisplay(Boolean skipDisplay) {
        this.skipDisplay = skipDisplay;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }
}