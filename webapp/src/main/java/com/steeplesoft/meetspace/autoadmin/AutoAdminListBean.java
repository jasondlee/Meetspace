package com.steeplesoft.meetspace.autoadmin;

import com.steeplesoft.meetspace.view.ControllerBean;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.enterprise.inject.Model;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.html.*;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.DateTimeConverter;
import javax.faces.model.DataModel;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.TimeZone;

@Model
public class AutoAdminListBean extends AutoAdminBaseBean {
}
