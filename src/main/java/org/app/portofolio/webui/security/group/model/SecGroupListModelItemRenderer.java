package org.app.portofolio.webui.security.group.model;

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

import org.module.sysadmin.model.SecGroup;
import org.module.sysadmin.service.SecurityService;

/**
*
* @author tekosulaiman@yahoo.com
*/
public class SecGroupListModelItemRenderer implements ListitemRenderer<SecGroup> {
	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Service yang dibutuhkan sesuai bisnis proses
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	private SecurityService securityService = (SecurityService) SpringUtil.getBean("securityService");		
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void render(Listitem item, final SecGroup secGroup, int index) throws Exception {		
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
        	
    	final Label labelGroupName = new Label();			        
        final Label labelDescription = new Label();
        
        final Textbox textboxGroupName = new Textbox();
        final Textbox textboxDescription = new Textbox();
        
        listcell = new Listcell();
        	buttonEdit.setParent(listcell); 
        	buttonSave.setParent(listcell); 
        	buttonCancel.setParent(listcell); 
        	buttonDelete.setParent(listcell); 
		listcell.setParent(item);
		
		listcell = new Listcell(String.valueOf(secGroup.getGrpId() != null ? secGroup.getGrpId() : ""));
		listcell.setParent(item);
        
		listcell = new Listcell();
			textboxGroupName.setParent(listcell); 
			labelGroupName.setParent(listcell); 
		listcell.setParent(item);

		listcell = new Listcell();
			textboxDescription.setParent(listcell); 
			labelDescription.setParent(listcell);
		listcell.setParent(item);

		if(secGroup.getGrpId() == null){
			buttonEdit.setVisible(false);
			buttonDelete.setVisible(false);
			
		}else{
        	buttonSave.setVisible(false);
        	buttonCancel.setVisible(false);
        	buttonDelete.setVisible(false);

        	labelGroupName.setValue(secGroup.getGrpShortdescription());
        	labelDescription.setValue(secGroup.getGrpLongdescription());
        	
        	textboxGroupName.setVisible(false);
        	textboxDescription.setVisible(false);
		}
		
		/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		 * Function CRUD Event
		 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
		buttonSave.addEventListener(Events.ON_CLICK, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if(secGroup.getGrpId() == null){
					secGroup.setGrpShortdescription(textboxGroupName.getValue());
					secGroup.setGrpLongdescription(textboxDescription.getValue());
					
					securityService.save(secGroup);
					
					BindUtils.postGlobalCommand(null, null, "refreshAfterSaveOrUpdate", null);
				}else{
					secGroup.setGrpShortdescription(textboxGroupName.getValue());
					secGroup.setGrpLongdescription(textboxDescription.getValue());
					
					securityService.update(secGroup);
					
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
				
				textboxGroupName.setVisible(true);
				textboxDescription.setVisible(true);
				
				labelGroupName.setVisible(false);
				labelDescription.setVisible(false);
				
				textboxGroupName.setValue(secGroup.getGrpShortdescription());
				textboxDescription.setValue(secGroup.getGrpLongdescription());
			}
		});
		
		buttonDelete.addEventListener(Events.ON_CLICK, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				System.out.println("ID >>>"+secGroup.getGrpId());
				Messagebox.show("Do you really want to remove item?", "Confirm", Messagebox.OK | Messagebox.CANCEL, Messagebox.EXCLAMATION, new EventListener() {
				    public void onEvent(Event event) throws Exception {    	
				 		if (((Integer) event.getData()).intValue() == Messagebox.OK) {

				 			securityService.delete(secGroup);
				 			
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