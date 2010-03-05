package com.steeplesoft.meetspace.autoadmin;

import com.steeplesoft.meetspace.view.ControllerBean;
import org.osgi.service.blueprint.reflect.ComponentMetadata;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.enterprise.inject.Model;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.UIOutput;
import javax.faces.component.html.*;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.DateTimeConverter;
import javax.faces.model.DataModel;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: jasonlee
 * Date: Feb 27, 2010
 * Time: 8:22:51 AM
 * To change this template use File | Settings | File Templates.
 */
//@Named
//@SessionScoped
@Model
public class AutoAdminBean extends ControllerBean {
    public static final String NAV_BASE = "/autoAdmin";
    public static final String NAV_ADD = "/autoAdmin/form";
    public static final String NAV_EDIT = "/autoAdmin/form";
    public static final String NAV_LIST = "/autoAdmin/list";
    public static final String NAV_VIEW = "/autoAdmin/view";

    private Set<String> modelPackages = new TreeSet<String>();
    private Set<String> modelClasses = new TreeSet<String>();
    private ModelMetadata modelMetadata;
    private Long id;

    private String modelClassName;
    private Class<?> modelClass;
    private HtmlDataTable table = null;
    private HtmlPanelGrid grid = null;
    private HtmlCommandButton commandButton;

    private final FacesContext facesContext = FacesContext.getCurrentInstance();
    private final Application application = facesContext.getApplication();
    private final ELContext elContext = facesContext.getELContext();

    public AutoAdminBean() {
        modelClasses.add("Meeting");
        modelClasses.add("GroupMember");
        modelClasses.add("Sponsor");

        modelPackages.add("com.steeplesoft.meetspace.model");
    }

    @Override
    public Class<?> getEntityClass() {
        return modelClass;
    }

    public String getListViewId() {
        return NAV_LIST + "?model="+getModelClassName();
    }

    public String getAddViewId() {
        return NAV_ADD;
    }

    public String getEditViewId() {
        return NAV_EDIT;
    }

    public String getViewViewId() {
        return NAV_VIEW;
    }

    public Set<String> getModelClasses() {
        return modelClasses;
    }

    public String save() {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("modelClassName", modelClassName);
        String ret = null;
        if (id == null) {
            ret = create();
        } else {
            ret = edit();
        }

        return ret;
    }

    public void setModelClasses(Set<String> modelClasses) {
        this.modelClasses = modelClasses;
    }

    public List getModelList() {
        return new ArrayList(modelClasses);
    }

    public Class<?> getModelClass() {
        if (modelClass == null) {
            modelClass = loadModelClass(getModelClassName());
        }
        return modelClass;
    }

    public void setModelClass(Class<?> modelClass) {
        this.modelClass = modelClass;
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("modelClass", modelClass);
    }

    public Class loadModelClass(String className) {
        Class clazz = null;

        for (String pkg : modelPackages) {
            try {
                clazz = Class.forName(pkg + "." + className);
                break;
            } catch (ClassNotFoundException e) {
                //
            }
        }

        if (clazz == null) {
            throw new RuntimeException("The class '" + className + "' could not be found in any of the configured packages.");
        }

        return clazz;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
        setSelected(dataAccess.find(getModelClass(), id));
    }

    public String getModelClassName() {
        if (modelClassName == null) {
            modelClassName = (String)facesContext.getExternalContext().getRequestParameterMap().get("autoAdminForm:modelClassName");
            if (modelClassName != null) {
                setModelClass(loadModelClass(modelClassName));
            }
        }
        return modelClassName;
    }

    public void setModelClassName(String modelClassName) {
        this.modelClassName = modelClassName;
        setModelClass(loadModelClass(modelClassName));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("modelClassName", modelClassName);
    }

    public ModelMetadata getModelMetadata() throws IllegalAccessException, InstantiationException {
        if (modelMetadata == null) {
            this.modelMetadata = new ModelMetadata(getModelClass());
        }
        return modelMetadata;
    }

    public void setModelMetadata(ModelMetadata modelMetadata) {
        this.modelMetadata = modelMetadata;
    }


    // Component-binding methods
    public UIComponent getDataTable() throws IllegalAccessException, InstantiationException {
        if (table == null) {
            table = (HtmlDataTable) application.createComponent(HtmlDataTable.COMPONENT_TYPE);
            table.setValueExpression("value",
                    application.getExpressionFactory().createValueExpression(elContext, "#{autoAdminBean.list}", DataModel.class));
            table.setVar("item");
            table.setStyle("border-collapse: collapse;");
            table.setBorder(1);
            table.setWidth("100%");

            for (ColumnMetadata cmd : getModelMetadata().getColumns()) {
                String name = cmd.getName();
                if (cmd.getLength() > 255) {
                    continue;
                }
                HtmlColumn column = (HtmlColumn) application.createComponent(HtmlColumn.COMPONENT_TYPE);
                addHeaderFacet(column, name);

                HtmlOutputText output = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
                output.setValueExpression("value",
                        application.getExpressionFactory().createValueExpression(elContext, "#{item." + name +"}", cmd.getType()));
                determineNecessaryConverters(output, cmd);
                column.getChildren().add(output);

                table.getChildren().add(column);
            }
            addCommandColumn(table, this.getModelClassName());

        }

        return table;
    }

    public void setDataTable(UIComponent table) {
        this.table = (HtmlDataTable)table;
    }

    public HtmlPanelGrid getPanelGrid() throws IllegalAccessException, InstantiationException {
        if (grid == null) {
            final FacesContext facesContext = FacesContext.getCurrentInstance();
            final Application application = facesContext.getApplication();
            final ELContext elContext = facesContext.getELContext();
            grid = (HtmlPanelGrid) application.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
            grid.setColumns(2);

            for (ColumnMetadata cmd : getModelMetadata().getColumns()) {
                String name = cmd.getName();
                HtmlOutputLabel label = (HtmlOutputLabel) application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
                label.setId("autoAdmin_label_" + name);
                label.setFor("autoAdmin_" + name);
                label.setValue(name + ":");
                label.setTitle("Label for " + name);

                UIComponent field = null;
                if (cmd.getIsPrimaryKey()) {
                    HtmlPanelGroup group = (HtmlPanelGroup) application.createComponent(HtmlPanelGroup.COMPONENT_TYPE);

                    ValueExpression pkve = application.getExpressionFactory().createValueExpression(elContext, "#{autoAdminBean.selected." + name + "}", cmd.getType());
                    Object value = pkve.getValue(elContext);
                    HtmlOutputText output = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
                    output.setValue(pkve.getValue(elContext));
                    output.setStyle("font-weight: bold;");

                    HtmlInputHidden hidden = (HtmlInputHidden) application.createComponent(HtmlInputHidden.COMPONENT_TYPE);
                    hidden.setId("autoAdmin_hidden_"+name);
                    hidden.setValue(value);

                    group.getChildren().add(output);
                    group.getChildren().add(hidden);

                    field = group;
                } else {
                    HtmlInputText input = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
                    ValueExpression ve = application.getExpressionFactory().createValueExpression(elContext, "#{autoAdminBean.selected." + name + "}", cmd.getType());
                    input.setValueExpression("value", ve);
                    final String simpleName = cmd.getType().getSimpleName();
                    determineNecessaryConverters(input, cmd);
                    field = input;
                }

                field.setId("autoAdmin_" + name);
                grid.getChildren().add(label);
                grid.getChildren().add(field);
            }
        }

        return grid;
    }

    public void setPanelGrid(UIComponent grid) {
        this.grid = (HtmlPanelGrid)grid;
    }

    protected void determineNecessaryConverters(UIOutput component, ColumnMetadata cmd) {
        final String simpleName = cmd.getType().getSimpleName();
        if (!"String".equals(simpleName)) {
            Converter converter = null;
            if (simpleName.equals("Date")) {
                DateTimeConverter dtc = (DateTimeConverter) application.createConverter("javax.faces.DateTime");
                dtc.setLocale(facesContext.getViewRoot().getLocale());
                dtc.setTimeZone(TimeZone.getDefault());
                if ("DATE".equals(cmd.getTemporalType())) {
                    dtc.setType("date");
                    dtc.setPattern("yyyy-MM-dd");
                } else if ("TIME".equals(cmd.getTemporalType())) {
                    dtc.setType("time");
                    dtc.setPattern("h:mm a");
                } else {
                    dtc.setType("both");
                    dtc.setPattern("yyyy-MM-dd HH:mm");
                }
                converter = dtc;
            } else {
                converter = application.createConverter("javax.faces." + simpleName);
            }
            component.setConverter(converter);
        }
    }

    protected void addHeaderFacet(HtmlColumn column, Object value) {
        HtmlOutputText header = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        header.setStyle("font-weight: bold;");
        header.setValue(value);

        column.getFacets().put("header", header);
    }

    protected void addCommandColumn(HtmlDataTable table, String model) {
        HtmlColumn column = (HtmlColumn) application.createComponent(HtmlColumn.COMPONENT_TYPE);
        ValueExpression id = application.getExpressionFactory().createValueExpression(elContext, "#{item.id}", Object.class);

        addHeaderFacet(column, "&#160;");
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();

        HtmlOutputLink viewLink = (HtmlOutputLink) application.createComponent(HtmlOutputLink.COMPONENT_TYPE);
        viewLink.setValueExpression("value",
                application.getExpressionFactory().createValueExpression(elContext, "#{request.contextPath}"+AutoAdminBean.NAV_BASE + "/view.jsf?model=" + model + "&id=#{item.id}", Object.class));
        addOutputText(viewLink, "View");
        column.getChildren().add(viewLink);
        addOutputText(column, "&#160;");

        HtmlOutputLink editLink = (HtmlOutputLink) application.createComponent(HtmlOutputLink.COMPONENT_TYPE);
        editLink.setValueExpression("value",
                application.getExpressionFactory().createValueExpression(elContext, "#{request.contextPath}"+AutoAdminBean.NAV_BASE + "/form.jsf?model=" + model + "&id=#{item.id}", Object.class));
        addOutputText(editLink, "Edit");
        column.getChildren().add(editLink);
        addOutputText(column, "&#160;");

        HtmlOutputLink deleteLink = (HtmlOutputLink) application.createComponent(HtmlOutputLink.COMPONENT_TYPE);
        deleteLink.setValue(request.getContextPath() + AutoAdminBean.NAV_BASE + "/form.jsf?model=" + model + "&id="+id);
        addOutputText(deleteLink, "Delete");
        column.getChildren().add(deleteLink);

        table.getChildren().add(column);

//        <h:column>
//            <f:facet name="header">
//                <h:outputText value="&nbsp;"/>
//            </f:facet>
//            <h:commandLink action="#{autoAdmin.prepareView}" value="View"/>
//            &nbsp;
//            <h:commandLink action="#{autoAdmin.prepareEdit}" value="Edit"/>
//            &nbsp;
//            <h:commandLink action="#{autoAdmin.delete}" value="Delete"
//                           onclick="return confirm('Are you sure you want to delete #{meeting.name}?');" >
//                <f:ajax execute="@this" render="@form"/>
//            </h:commandLink>
//        </h:column>
    }

    protected void addOutputText(UIComponent parent, String text) {
        HtmlOutputText outputText = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        outputText.setValue(text);
        outputText.setEscape(false);
        parent.getChildren().add(outputText);
    }
}