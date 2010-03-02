package com.steeplesoft.meetspace.view;

import com.steeplesoft.meetspace.service.impl.DataAccessController;
import com.steeplesoft.meetspace.view.util.JsfUtil;
import com.steeplesoft.meetspace.view.util.Paginator;
import java.io.Serializable;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;

public abstract class ControllerBean implements Serializable {
    public static String NAV_REDIRECT = "?faces-redirect=true";

    @Inject
    protected DataAccessController dataAccess;
    protected DataModel dataModel;
    protected int rowsPerPage = 5;
    protected int selectedItemIndex = -1;
    protected Object current;
    protected Paginator paginator;

    //**************************************************************************
    // Abstract methods

    public abstract String getListViewId();
    public abstract String getAddViewId();
    public abstract String getEditViewId();
    public abstract String getViewViewId(); // ick

    public abstract Class getEntityClass();


    public int getRowsPerPage() {
        return rowsPerPage;
    }

    public void setRowsPerPage(int rowsPerPage) {
        this.rowsPerPage = rowsPerPage;
        resetList();
    }

    public void resetList() {
        dataModel = null;
    }

    public void resetPagination(ValueChangeEvent vce) {
        paginator = null;
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



    //**************************************************************************
    // prepareFoo and Foo action methods
    public String prepareList() {
        resetList();
        return getListViewId() + NAV_REDIRECT;
    }

    public DataModel getList() {
        if (dataModel == null) {
            dataModel = getPaginator().createPageDataModel();
        }

        return dataModel;
    }

    public String prepareCreate() {
        current = newEntityInstance();
        selectedItemIndex = -1;
        return getAddViewId() + NAV_REDIRECT;
    }

    public String create() {
        try {
            dataAccess.create(current);
//            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("GroupMemberCreated")); // Left as an example :)
            JsfUtil.addSuccessMessage("Group member created");
            return getListViewId() + NAV_REDIRECT;
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.addErrorMessage(e, "A persistence error occurred.");
            return null;
        }
    }

    public String prepareEdit() {
        current = getList().getRowData();
        selectedItemIndex = paginator.getPageFirstItem() + getList().getRowIndex();
        return getEditViewId() + NAV_REDIRECT;
    }

    public String edit() {
        try {
            dataAccess.edit(current);
            JsfUtil.addSuccessMessage("Group member updated");
            return getListViewId() + NAV_REDIRECT;
        } catch (Exception e) {
           JsfUtil.addErrorMessage(e, "A persistence error occurred.");
            e.printStackTrace();
            return null;
        }
    }

    public String delete() {
        current = getList().getRowData();
        selectedItemIndex = paginator.getPageFirstItem() + getList().getRowIndex();
        try {
            dataAccess.remove(current);
            JsfUtil.addSuccessMessage("Group member deleted");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "A persistence error occurred.");
        }
        resetList();
        return getListViewId() + NAV_REDIRECT;
    }

    public String prepareView() {
        current = getList().getRowData();
        selectedItemIndex = paginator.getPageFirstItem() + getList().getRowIndex();
        return getViewViewId() + NAV_REDIRECT;
    }

    public String checkViewData() {
        // If an ID is in the URL, that overrides what might be in the session state
        String id = JsfUtil.getRequestParameter("id");
        if (id != null) {
            current = dataAccess.find(getEntityClass(), Long.valueOf(id));
        }

        if (current != null) {
            return getViewViewId() + NAV_REDIRECT;
        } else {
            return getListViewId() + NAV_REDIRECT;
        }
    }

    public Object getSelected() {
        if (current == null) {
            current = newEntityInstance();
            selectedItemIndex = -1;
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
}