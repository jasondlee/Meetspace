/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.steeplesoft.meetspace.view;

import com.steeplesoft.meetspace.model.Attachment;
import com.steeplesoft.meetspace.util.upload.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

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

    @Inject
    private MeetSpaceBean meetspaceBean;

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
            //HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            MultipartRequest request = (MultipartRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String uploadPath = meetspaceBean.getHome() + File.separator + "uploads";

            Attachment attachment = (Attachment) getSelected();
            FileHolder item = request.getFile("contents");

            attachment.setFilename(item.getFileName());
            attachment.setMimeType(item.getMimeType());
            attachment.setPath(uploadPath + item.getFileName());
            copyFile(item.getFile(), uploadPath, item.getFileName());
//            attachment.setContent(getFileContents(item.getFile()));

            return super.create();
        } catch (Exception ex) {
            Logger.getLogger(AttachmentBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    @Override
    public String edit() {
        System.out.println("edit");
        return super.edit();
    }

    protected void copyFile(File source, String dir, String fileName) {
        final File targetDir = new File(dir);
        targetDir.mkdirs();
        final File target = new File(targetDir, fileName);
        source.renameTo(target);
    }

    protected byte[] getFileContents(File file) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            long length = file.length();
            byte[] bytes = new byte[8192];
            // Read in the bytes
            int numRead = is.read(bytes);
            while (numRead >= 0) {
                baos.write(bytes);
                bytes = new byte[8192];
                numRead = is.read(bytes);
            }
        } catch (Exception ex) {
            Logger.getLogger(AttachmentBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                //
            }
        }
        return baos.toByteArray();
    }
}
