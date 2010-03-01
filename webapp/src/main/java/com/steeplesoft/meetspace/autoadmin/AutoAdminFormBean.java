package com.steeplesoft.meetspace.autoadmin;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.enterprise.inject.Model;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.html.*;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.DateTimeConverter;
import javax.persistence.TemporalType;
import java.util.TimeZone;

@Model
public class AutoAdminFormBean extends AutoAdminBaseBean {
    HtmlPanelGrid grid;

    public UIComponent getPanelGrid() {
        if (grid == null) {
            final FacesContext facesContext = FacesContext.getCurrentInstance();
            final Application application = facesContext.getApplication();
            final ELContext elContext = facesContext.getELContext();
            grid = (HtmlPanelGrid) application.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
            grid.setColumns(2);

            for (ColumnMetadata cmd : getModelMetadata().getColumns()) {
                String name = cmd.getName();
                HtmlOutputLabel label = (HtmlOutputLabel) application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
                label.setFor("autoAdmin_" + name);
                label.setValue(name + ":");
                label.setTitle("Label for " + name);

                UIComponent field = null;
                if (cmd.isPrimaryKey()) {
                    HtmlPanelGroup group = (HtmlPanelGroup) application.createComponent(HtmlPanelGroup.COMPONENT_TYPE);

                    ValueExpression pkve = application.getExpressionFactory().createValueExpression(elContext, "#{autoAdminFormBean.model." + name + "}", cmd.getType());
                    Object value = pkve.getValue(elContext);
                    HtmlOutputText output = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
                    output.setValue(pkve.getValue(elContext));
                    output.setStyle("font-weight: bold;");

                    HtmlInputHidden hidden = (HtmlInputHidden) application.createComponent(HtmlInputHidden.COMPONENT_TYPE);
                    hidden.setValue(value);

                    group.getChildren().add(output);
                    group.getChildren().add(hidden);

                    field = group;
                } else {
                    HtmlInputText input = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
                    ValueExpression ve = application.getExpressionFactory().createValueExpression(elContext, "#{autoAdminFormBean.model." + name + "}", cmd.getType());
                    input.setValueExpression("value", ve);
                    final String simpleName = cmd.getType().getSimpleName();
                    if (!simpleName.equals("String")) {
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
                        input.setConverter(converter);

                    }
                    field = input;
                }

                field.setId("autoAdmin_" + name);
                grid.getChildren().add(label);
                grid.getChildren().add(field);
            }
        }

        return grid;
    }

    public String save() {
        System.out.println(getModel());

        return "";
    }
}