package org.app.portofolio.webui.security.role.model;

import org.zkoss.bind.BindUtils;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import org.module.sysadmin.model.SecRole;
import org.module.sysadmin.service.SecurityService;

/**
*
* @author tekosulaiman@yahoo.com
*/
public class SecRoleListModelItemRenderer implements ListitemRenderer<SecRole>{

	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Service yang dibutuhkan sesuai bisnis proses
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	private SecurityService securityService = (SecurityService) SpringUtil.getBean("securityService");
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void render(Listitem item, final SecRole secRole, int index) throws Exception {
		Listcell listcell;
		
		/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		 * Wire component
		 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
		final Button buttonSave = new Button();
    	buttonSave.setImage("/images/icons/btn_save.gif");
		
		final Button buttonEdit = new Button();
			buttonEdit.setImage("/images/icons/btn_edit.gif");
			
		final Button buttonDelete = new Button();
        	buttonDelete.setImage("/images/icons/btn_delete.gif");
        
        final Button buttonCancel = new Button();
        	buttonCancel.setImage("/images/icons/btn_cancel.gif");

    	final Label labelRoleName = new Label();			        
        final Label labelDescription = new Label();
        
        final Textbox textboxRoleName = new Textbox();
        final Textbox textboxDescription = new Textbox();
        
        listcell = new Listcell();
        	buttonEdit.setParent(listcell); 
        	buttonSave.setParent(listcell); 
        	buttonCancel.setParent(listcell); 
        	buttonDelete.setParent(listcell); 
		listcell.setParent(item);
		
		listcell = new Listcell(String.valueOf(secRole.getRolId() != null ? secRole.getRolId() : ""));
		listcell.setParent(item);
        
		listcell = new Listcell();
			textboxRoleName.setParent(listcell); 
			labelRoleName.setParent(listcell); 
		listcell.setParent(item);

		listcell = new Listcell();
			textboxDescription.setParent(listcell); 
			labelDescription.setParent(listcell);
		listcell.setParent(item);

		if(secRole.getRolId() == null){
			buttonEdit.setVisible(false);
			buttonDelete.setVisible(false);
			
		}else{
        	buttonSave.setVisible(false);
        	buttonCancel.setVisible(false);
        	buttonDelete.setVisible(false);

        	labelRoleName.setValue(secRole.getRolShortdescription());
        	labelDescription.setValue(secRole.getRolLongdescription());
        	
        	textboxRoleName.setVisible(false);
        	textboxDescription.setVisible(false);
		}
		
		/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		 * Function CRUD Event
		 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
		buttonSave.addEventListener(Events.ON_CLICK, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if(secRole.getRolId() == null){
					secRole.setRolShortdescription(textboxRoleName.getValue());
					secRole.setRolLongdescription(textboxDescription.getValue());
					
					securityService.save(secRole);
					
					BindUtils.postGlobalCommand(null, null, "refreshAfterSaveOrUpdate", null);
				}else{
					secRole.setRolShortdescription(textboxRoleName.getValue());
					secRole.setRolLongdescription(textboxDescription.getValue());
					
					securityService.update(secRole);
					
					BindUtils.postGlobalCommand(null, null, "refreshAfterSaveOrUpdate", null);
				}
			}
		});
		
		buttonEdit.addEventListener(Events.ON_CLICK, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				buttonEdit.setVisible(false);
				buttonSave.setVisible(true);
				buttonDelete.setVisible(true);
				
				textboxRoleName.setVisible(true);
				textboxDescription.setVisible(true);
				
				labelRoleName.setVisible(false);
				labelDescription.setVisible(false);
				
				textboxRoleName.setValue(secRole.getRolShortdescription());
				textboxDescription.setValue(secRole.getRolLongdescription());
			}
		});
		
		buttonDelete.addEventListener(Events.ON_CLICK, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				System.out.println("ID >>>"+secRole.getRolId());
				Messagebox.show("Do you really want to remove item?", "Confirm", Messagebox.OK | Messagebox.CANCEL, Messagebox.EXCLAMATION, new EventListener() {
				    public void onEvent(Event event) throws Exception {    	
				 		if (((Integer) event.getData()).intValue() == Messagebox.OK) {

				 			securityService.delete(secRole);
				 			
				 			BindUtils.postGlobalCommand(null, null, "refreshAfterSave", null);
				 		}else{
				 			return;
				 		}
				 	}
				});
			}
		});
		
		buttonCancel.addEventListener(Events.ON_CLICK, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				BindUtils.postGlobalCommand(null, null, "refreshAfterSaveOrUpdate", null);
			}
		});
	}
}