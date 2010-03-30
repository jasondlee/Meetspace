package com.steeplesoft.meetspace.autoadmin;

import com.steeplesoft.meetspace.service.impl.DataAccessController;
import com.steeplesoft.meetspace.view.ControllerBean;
import com.steeplesoft.meetspace.view.util.JsfUtil;
import com.steeplesoft.meetspace.view.util.Paginator;
import com.sun.mojarra.scales.component.DateSelector;
import com.sun.mojarra.scales.component.HtmlEditor;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.enterprise.inject.Model;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UISelectItem;
import javax.faces.component.html.*;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.DateTimeConverter;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.persistence.TemporalType;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Model
public class AutoAdminBean {
    public static final String NAV_BASE = "/autoAdmin";
    public static final String NAV_REDIRECT = "&faces-redirect=true";
    public static final String NAV_ADD = "/autoAdmin/form";
    public static final String NAV_EDIT = "/autoAdmin/form";
    public static final String NAV_LIST = "/autoAdmin/list";
    public static final String NAV_VIEW = "/autoAdmin/view";

//    private Set<String> modelPackages = new TreeSet<String>();
//    private Set<String> modelClasses = new TreeSet<String>();

    @Inject
    private MetaDataBuilder metaDataBuilder;

    private ModelMetadata modelMetadata;
    private Long id;

    @Inject
    protected DataAccessController dataAccess;
    protected Object current;
    protected Paginator paginator;
    protected int rowsPerPage = 5;
    protected DataModel dataModel;

    private String modelClassName;
    private Class<?> modelClass;
    private HtmlDataTable table = null;
    private HtmlPanelGrid grid = null;
    private HtmlCommandButton commandButton;

    private final FacesContext facesContext = FacesContext.getCurrentInstance();
    private final Application application = facesContext.getApplication();
    private final ELContext elContext = facesContext.getELContext();
    private Properties modelProperties;

    public AutoAdminBean() {
    }

    public Class<?> getEntityClass() {
        return modelMetadata.getModelClass();
    }

    public Set<String> getModelClasses() {
        return metaDataBuilder.getModelClasses();
    }

    public Paginator getPaginator() {
        if (paginator == null) {
            paginator = new Paginator(rowsPerPage) {

                @Override
                public int getItemsCount() {
                    return dataAccess.count(getEntityClass());
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(dataAccess.findRange(getEntityClass(), getPageFirstItem(), getPageFirstItem()+getPageSize()));
                }
            };
        }

        return paginator;

    }

    public void next() {
        getPaginator().nextPage();
        resetList();
    }

    public void previous() {
        getPaginator().previousPage();
        resetList();
    }

    public int getRowsPerPage() {
        return rowsPerPage;
    }

    public void setRowsPerPage(int rowsPerPage) {
        this.rowsPerPage = rowsPerPage;
        resetList();
    }

    public DataModel getList() {
        if (dataModel == null) {
            dataModel = getPaginator().createPageDataModel();
        }

        return dataModel;
    }

    public void resetList() {
        dataModel = null;
    }

    public void resetPagination(ValueChangeEvent vce) {
        paginator = null;
    }

    public String save() {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("modelClassName", modelClassName);
        String ret = NAV_LIST + "?model=" + getModelClassName() + NAV_REDIRECT;
        try {
            if (id == null) {
                dataAccess.create(getSelected());
//            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("GroupMemberCreated")); // Left as an example :)
                JsfUtil.addSuccessMessage("Group member created");
                return NAV_LIST + NAV_REDIRECT;
            } else {
                dataAccess.edit(getSelected());
                JsfUtil.addSuccessMessage("Group member updated");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.addErrorMessage(e, "A persistence error occurred.");
            ret = null;
        }

        return ret;
    }

    public Object getSelected() {
        if (current == null) {
            setSelected(newEntityInstance());
//            selectedItemIndex = -1;
        }
        return current;
    }

    public void setSelected(Object selected) {
        this.current = selected;
    }

    protected Object newEntityInstance() {
        Object obj = null;
        try {
            obj = getEntityClass().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    public void setModelClasses(Set<String> modelClasses) {
//        this.modelClasses = modelClasses;
    }

    public List getModelList() {
        return new ArrayList(metaDataBuilder.getModelClasses());
    }

    public Class<?> getModelClass() {
        if (modelClass == null) {
            modelClass = metaDataBuilder.getModelMetadata(getModelClassName()).getModelClass();
//            modelClass = loadModelClass(getModelClassName());
        }
        return modelClass;
    }

    public void setModelClass(Class<?> modelClass) {
        this.modelClass = modelClass;
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("modelClass", modelClass);
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
        this.current = dataAccess.find(getModelClass(), id);
    }

    public String getModelClassName() {
        if (modelClassName == null) {
            modelClassName = (String) facesContext.getExternalContext().getRequestParameterMap().get("autoAdminForm:modelClassName");
            if (modelClassName != null) {
                modelMetadata = metaDataBuilder.getModelMetadata(modelClassName);
                setModelClass(modelMetadata.getModelClass());
            }
        }
        return modelClassName;
    }

    public void setModelClassName(String modelClassName) {
        this.modelClassName = modelClassName;
        modelMetadata = metaDataBuilder.getModelMetadata(modelClassName);
//        setModelClass(loadModelClass(modelClassName));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("modelClassName", modelClassName);
    }

    public ModelMetadata getModelMetadata() {
        return this.metaDataBuilder.getModelMetadata(getModelClassName());
    }

    public void setModelMetadata(ModelMetadata modelMetadata) {
//        this.modelMetadata = modelMetadata;
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
                        application.getExpressionFactory().createValueExpression(elContext, "#{item." + name + "}", cmd.getType()));
                determineNecessaryConverters(output, cmd);
                column.getChildren().add(output);

                table.getChildren().add(column);
            }
            addCommandColumn(table, this.getModelClassName());

        }

        return table;
    }

    public void setDataTable(UIComponent table) {
        this.table = (HtmlDataTable) table;
    }

    public HtmlPanelGrid getPanelGrid() throws IllegalAccessException, InstantiationException {
        if (grid == null) {
            final FacesContext facesContext = FacesContext.getCurrentInstance();
            final Application application = facesContext.getApplication();
            final ELContext elContext = facesContext.getELContext();
            grid = (HtmlPanelGrid) application.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
            grid.setColumns(2);

            final ModelMetadata modelMetadata = getModelMetadata();
            for (ColumnMetadata cmd : modelMetadata.getColumns()) {
                String name = cmd.getName();
                HtmlOutputLabel label = (HtmlOutputLabel) application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
                label.setId("autoAdmin_label_" + name);
                label.setFor("autoAdmin_" + name);
                label.setValue(cmd.getLabel() + ":"); //getLabel(modelMetadata.getName(), name) + ":");
                label.setTitle("Label for " + name);

                UIComponent field = null;
                if (cmd.isSkipDisplay()) {//skipDisplay(modelMetadata.getName(), cmd.getName())) {
                    continue;
                }
                if (cmd.getIsPrimaryKey()) {
                    HtmlPanelGroup group = (HtmlPanelGroup) application.createComponent(HtmlPanelGroup.COMPONENT_TYPE);

                    ValueExpression pkve = application.getExpressionFactory().createValueExpression(elContext, "#{autoAdminBean.selected." + name + "}", cmd.getType());
                    Object value = pkve.getValue(elContext);
                    HtmlOutputText output = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
                    output.setValue(pkve.getValue(elContext));
                    output.setStyle("font-weight: bold;");

                    HtmlInputHidden hidden = (HtmlInputHidden) application.createComponent(HtmlInputHidden.COMPONENT_TYPE);
                    hidden.setId("autoAdmin_hidden_" + name);
                    hidden.setValue(value);

                    group.getChildren().add(output);
                    group.getChildren().add(hidden);

                    field = group;
                } else {
                    UIOutput component = createAppropriateComponent(modelMetadata, cmd);
                    //(HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
                    ValueExpression ve = application.getExpressionFactory().createValueExpression(elContext, "#{autoAdminBean.selected." + name + "}", cmd.getType());
                    component.setValueExpression("value", ve);
                    final String simpleName = cmd.getType().getSimpleName();
                    determineNecessaryConverters(component, cmd);
                    field = component;
                }

                field.setId("autoAdmin_" + name);
                grid.getChildren().add(label);
                grid.getChildren().add(field);
            }
        }

        return grid;
    }

    private UIOutput createAppropriateComponent(ModelMetadata md, ColumnMetadata cmd) {
        UIOutput comp = null;

        if (facesContext.getViewRoot().getViewId().endsWith("form.xhtml")) {
            String componentType = cmd.getComponentType();//getComponentType(modelMetadata.getName(), cmd.getName());
            if (componentType == null) {
                if (cmd.getType().equals(String.class)) {
                    if (cmd.getLength() <= 255) {
                        comp = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
                        comp.getAttributes().put("style", "width: 100%");
                    } else {
                        HtmlEditor he = (HtmlEditor) application.createComponent(HtmlEditor.COMPONENT_TYPE);
                        he.getAttributes().put("width", "100%");

                        comp = he;
                    }
                } else if (cmd.getType().equals(Date.class)) {
                    if (cmd.getTemporalType().equals(TemporalType.DATE)) {
                        DateSelector ds = (DateSelector) application.createComponent(DateSelector.COMPONENT_TYPE);
                        ds.setFormat("yyyy-MM-dd");
                        comp = ds;
                    } else {
                        comp = //(Combo) application.createComponent(Combo.COMPONENT_TYPE);
                                (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
                    }

                } else if (cmd.getType().equals(Boolean.class)) {
                    HtmlSelectOneMenu menu = (HtmlSelectOneMenu) application.createComponent(HtmlSelectOneMenu.COMPONENT_TYPE);
                    UISelectItem siFalse = (UISelectItem) application.createComponent(UISelectItem.COMPONENT_TYPE);
                    siFalse.setItemValue(Boolean.FALSE);
                    UISelectItem siTrue = (UISelectItem) application.createComponent(UISelectItem.COMPONENT_TYPE);
                    siTrue.setItemValue(Boolean.TRUE);
                    menu.getChildren().add(siFalse);
                    menu.getChildren().add(siTrue);
                    comp = menu;
                } else {
                    comp = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
                }
            } else {
                comp = (UIOutput) application.createComponent(componentType);
            }
        } else {
            comp = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
            if (cmd.isHtml()) {//isHtmlEnabledField(modelMetadata.getName(), cmd.getName())) {
                ((HtmlOutputText) comp).setEscape(false);
            }
        }

        return comp;
    }

    public void setPanelGrid(UIComponent grid) {
        this.grid = (HtmlPanelGrid) grid;
    }

    protected void determineNecessaryConverters(UIOutput component, ColumnMetadata cmd) {
        final String simpleName = cmd.getType().getSimpleName();
        if (!"String".equals(simpleName)) {
            Converter converter = null;
            if (simpleName.equals("Date")) {
                DateTimeConverter dtc = (DateTimeConverter) application.createConverter("javax.faces.DateTime");
                dtc.setLocale(facesContext.getViewRoot().getLocale());
                dtc.setTimeZone(TimeZone.getDefault());
                if (TemporalType.DATE.equals(cmd.getTemporalType())) {
                    dtc.setType("date");
                    dtc.setPattern("yyyy-MM-dd");
                } else if (TemporalType.TIME.equals(cmd.getTemporalType())) {
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
                application.getExpressionFactory().createValueExpression(elContext, "#{request.contextPath}" + AutoAdminBean.NAV_BASE + "/view.jsf?model=" + model + "&id=#{item.id}", Object.class));
        addOutputText(viewLink, "View");
        column.getChildren().add(viewLink);
        addOutputText(column, "&#160;");

        HtmlOutputLink editLink = (HtmlOutputLink) application.createComponent(HtmlOutputLink.COMPONENT_TYPE);
        editLink.setValueExpression("value",
                application.getExpressionFactory().createValueExpression(elContext, "#{request.contextPath}" + AutoAdminBean.NAV_BASE + "/form.jsf?model=" + model + "&id=#{item.id}", Object.class));
        addOutputText(editLink, "Edit");
        column.getChildren().add(editLink);
        addOutputText(column, "&#160;");

        HtmlOutputLink deleteLink = (HtmlOutputLink) application.createComponent(HtmlOutputLink.COMPONENT_TYPE);
        deleteLink.setValue(request.getContextPath() + AutoAdminBean.NAV_BASE + "/form.jsf?model=" + model + "&id=" + id);
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