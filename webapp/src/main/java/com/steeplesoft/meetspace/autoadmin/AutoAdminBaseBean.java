package com.steeplesoft.meetspace.autoadmin;

import com.steeplesoft.meetspace.view.ControllerBean;

import javax.faces.context.FacesContext;
import javax.inject.Inject;

public abstract class AutoAdminBaseBean extends ControllerBean {
    @Inject
    private AutoAdminBean adminBean;
    private ModelMetadata modelMetadata;
    private Long id;

    private String modelClassName;
    private Class<?> modelClass;

    @Override
    public Class<?> getEntityClass() {
        return modelClass;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
        setSelected(dataAccess.find(getModelClass(), id)); 
    }

    public String getModelClassName() {
        if (modelClassName == null) {
            modelClassName = (String)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("modelClassName");
        }
        return modelClassName;
    }

    public void setModelClassName(String modelClassName) {
        this.modelClassName = modelClassName;
        loadModelMetadata();
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

    public Class<?> getModelClass() {
        return modelClass;
    }

    public void setModelClass(Class<?> modelClass) {
        this.modelClass = modelClass;
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("modelClass", modelClass);
    }

    protected void loadModelMetadata() {
        setModelClass(adminBean.getModelClass(getModelClassName()));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("modelClassName", modelClassName);
    }
}