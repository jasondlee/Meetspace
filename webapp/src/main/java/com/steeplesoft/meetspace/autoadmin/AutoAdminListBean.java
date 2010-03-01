package com.steeplesoft.meetspace.autoadmin;

import com.steeplesoft.meetspace.view.ControllerBean;

import javax.el.ELContext;
import javax.enterprise.inject.Model;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlColumn;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.DateTimeConverter;
import javax.faces.model.DataModel;
import javax.inject.Inject;
import java.util.Date;
import java.util.TimeZone;

@Model
public class AutoAdminListBean extends AutoAdminBaseBean {
    private HtmlDataTable table = null;
    private final FacesContext facesContext = FacesContext.getCurrentInstance();
    private final Application application = facesContext.getApplication();
    private final ELContext elContext = facesContext.getELContext();
    public UIComponent getDataTable() {
        if (table == null) {
            table = (HtmlDataTable) application.createComponent(HtmlDataTable.COMPONENT_TYPE);
//            table.setValueExpression("#{autoAdmin.list};" +
            table.setValueExpression("value",
                    application.getExpressionFactory().createValueExpression(elContext, "#{autoAdminListBean.list}", DataModel.class));
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
//                <h:column>
//                    <f:facet name="header"><h:outputText style="font-weight: bold;" value="Name"/></f:facet>
//                    #{meeting.name}
//                </h:column>
                //column.getFacets().put("header", createOutputText(name, application, ));
                addHeaderFacet(application, column, name);

                HtmlOutputText output = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
                output.setValueExpression("value",
                        application.getExpressionFactory().createValueExpression(elContext, "#{item." + name +"}", cmd.getType()));
                determineNecessaryConverters(output, cmd.getType());
                column.getChildren().add(output);
                table.getChildren().add(column);
            }

            addCommandColumn(table);
        }

        return table;
    }

    protected void determineNecessaryConverters(HtmlOutputText output, Class type) {
        if (Date.class.equals(type)) {
            DateTimeConverter dtc = (DateTimeConverter) application.createConverter("javax.faces.DateTime");
            dtc.setDateStyle("short");
            dtc.setTimeStyle("short");
            dtc.setLocale(facesContext.getViewRoot().getLocale());
            dtc.setTimeZone(TimeZone.getDefault());
            output.setConverter(dtc);
        }
    }

    protected void addHeaderFacet(Application application, HtmlColumn column, Object value) {
        HtmlOutputText header = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        header.setStyle("font-weight: bold;");
        header.setValue(value);

        column.getFacets().put("header", header);
    }

    protected void addCommandColumn(HtmlDataTable table) {
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
}
