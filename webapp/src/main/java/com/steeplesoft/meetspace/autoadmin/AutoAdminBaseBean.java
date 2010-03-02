package com.steeplesoft.meetspace.autoadmin;

import com.steeplesoft.meetspace.view.ControllerBean;

import javax.faces.context.FacesContext;
import javax.inject.Inject;

public abstract class AutoAdminBaseBean extends ControllerBean {
    @Inject
    private AutoAdminBean adminBean;
    private ModelMetadata modelMetadata;
    private Object model;

    private String modelClassName;
    private Class<?> modelClass;

    @Override
    public Class getEntityClass() {
        return getModel().getClass();
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
        if (modelClassName == null) {
            modelClassName = (String)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("modelClassName");
        }
        return modelClassName;
    }

    public void setModelClassName(String modelClassName) {
        this.modelClassName = modelClassName;
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("modelClassName", modelClassName);
    }

    public ModelMetadata getModelMetadata() throws IllegalAccessException, InstantiationException {
        if (modelMetadata == null) {
            loadModelInformation();
            this.modelMetadata = new ModelMetadata(getModelClass());
        }
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
        this.setSelected(model);
    }

    public Class<?> getModelClass() {
        return modelClass;
    }

    public void setModelClass(Class<?> modelClass) {
        this.modelClass = modelClass;
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("modelClass", modelClass);
    }

    protected void loadModelInformation() throws IllegalAccessException, InstantiationException {
        setModelClass(adminBean.getModelClass(getModelClassName()));
        setModel(modelClass.newInstance());
        this.current = model;
    }
}