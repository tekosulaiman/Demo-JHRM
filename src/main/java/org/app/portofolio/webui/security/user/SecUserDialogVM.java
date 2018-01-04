package org.app.portofolio.webui.security.user;

import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import org.module.sysadmin.model.SecUser;
import org.module.sysadmin.service.UserService;

/**
*
* @author tekosulaiman@yahoo.com
*/
public class SecUserDialogVM {

	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Wire component
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	@Wire("#textboxPassword")
	private Textbox textboxPassword;
	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Service yang dibutuhkan sesuai bisnis proses
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	private SecUser secUser;
	private List<SecUser> secUsers;
	
	@WireVariable
	private UserService userService;
	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Inisialize Methode MVVM yang pertama kali dijalankan
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	@AfterCompose
	public void setupComponents(@ContextParam(ContextType.VIEW) Component component,
		@ExecutionArgParam("object") Object object,
		@ExecutionArgParam("secUser") SecUser secUser) {
		
		Selectors.wireComponents(component, this, false);
		
		if(secUser == null){
			this.secUser = new SecUser();
		}else{
			/*Jika Dalam Posisi Edit*/
			this.secUser = secUser; 
		}
	}
	
	@Command
	public void doClose(){
		System.out.println("doClose");
		BindUtils.postGlobalCommand(null, null, "refreshAfterClose", null);
	}
	
	@Command
	public void doSave(){
		/*Tidak Perlu Menginisialisasikan New Object, Karena sudah dilakukan di AfterCompose*/
		if(secUser.getUsrId() == null){
			System.out.println("doSave");
			
			/*String passwordMD5 = new PasswordEncoderImpl().encodePassword(textboxPassword.getValue(), 8);
			secUser.setUsrPassword(passwordMD5);*/
			secUser.setVersion(0);
			
			userService.save(secUser);
			Messagebox.show("doSave");
			
			BindUtils.postGlobalCommand(null, null, "refreshAfterSaveOrUpdate", null);
		}else{
			System.out.println("doUpdate");
			
			/*String passwordMD5 = new PasswordEncoderImpl().encodePassword(textboxPassword.getValue(), 8);		
			secUser.setUsrPassword(passwordMD5);*/
			secUser.setVersion(1);
			
			userService.saveOrUpdate(secUser);
			Messagebox.show("doUpdate");
			
			BindUtils.postGlobalCommand(null, null, "refreshAfterSaveOrUpdate", null);
		}
	}
	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Getter Setter
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	public SecUser getSecUser() {
		return secUser;
	}

	public void setSecUser(SecUser secUser) {
		this.secUser = secUser;
	}

	public List<SecUser> getSecUsers() {
		return secUsers;
	}

	public void setSecUsers(List<SecUser> secUsers) {
		this.secUsers = secUsers;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
