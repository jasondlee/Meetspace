package com.steeplesoft.meetspace.autoadmin;

import com.steeplesoft.meetspace.view.ControllerBean;

import javax.faces.application.Application;
import javax.faces.component.html.HtmlOutputText;
import javax.inject.Inject;

/**
 * Created by IntelliJ IDEA.
 * User: jasonlee
 * Date: Feb 28, 2010
 * Time: 10:38:49 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AutoAdminBaseBean extends ControllerBean {
    @Inject
    private AutoAdminBean adminBean;
    private ModelMetadata modelMetadata;
    private Object model;

    private String modelClassName;

    @Override
    public Class getEntityClass() {
        return model.getClass();
    }

    @Override
    public String getListViewId() {
        return adminBean.getListViewId();
    }

    @Override
    public String getAddViewId() {
        return adminBean.getAddViewId();
    }

    @Override
    public String getEditViewId() {
        return adminBean.getEditViewId();
    }

    @Override
    public String getViewViewId() {
        return adminBean.getViewViewId();
    }

    public String getModelClassName() {
        return modelClassName;
    }

    public void setModelClassName(String modelClassName) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        final Class<?> clazz = adminBean.getModelClass(modelClassName);
        this.modelClassName = modelClassName;
        this.model = clazz.newInstance();
        this.current = model;
        this.modelMetadata = new ModelMetadata(clazz);
    }

    public ModelMetadata getModelMetadata() {
        return modelMetadata;
    }

    public void setModelMetadata(ModelMetadata modelMetadata) {
        this.modelMetadata = modelMetadata;
    }

    public Object getModel() {
        return model;
    }

    public void setModel(Object model) {
        this.model = model;
    }
}
