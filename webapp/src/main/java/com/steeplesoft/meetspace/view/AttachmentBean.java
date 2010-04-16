/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steeplesoft.meetspace.view;

import com.steeplesoft.meetspace.model.Attachment;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author jasonlee
 */
@Named
@SessionScoped
public class AttachmentBean extends ControllerBean {

    public static final String NAV_ADD = "/admin/attachments/form";

    public static final String NAV_EDIT = "/admin/attachments/form";

    public static final String NAV_LIST = "/admin/attachments/list";

    public static final String NAV_VIEW = "/admin/attachments/view";

    public AttachmentBean() {
        setNavigationIds(NAV_ADD, NAV_EDIT, NAV_LIST, NAV_VIEW);
    }

    @Override
    public Class getEntityClass() {
        return Attachment.class;
    }

    @Override
    public String create() {
        try {
            System.out.println("create");
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            // Create a factory for disk-based file items
            DiskFileItemFactory factory = new DiskFileItemFactory();
            // Set factory constraints
            //        factory.setSizeThreshold(yourMaxMemorySize);
            //        factory.setRepository(yourTempDirectory);
            // Create a new file upload handler
            ServletFileUpload upload = new ServletFileUpload(factory);
            // Set overall request size constraint
            upload.setSizeMax(49152);
            // Parse the request
            /* FileItem */
            List items = upload.parseRequest(request);
            Iterator iter = items.iterator();
            if (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();

                if (!item.isFormField()) {
                    Attachment attachment = (Attachment)getSelected();

                    attachment.setFilename(item.getName());
                    attachment.setMimeType(item.getContentType());
                    attachment.setContent(item.get());
                }
            }
        return super.create();
        } catch (FileUploadException ex) {
            Logger.getLogger(AttachmentBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    @Override
    public String edit() {
        System.out.println("edit");
        return super.edit();
    }
}
