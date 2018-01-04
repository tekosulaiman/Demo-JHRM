package org.app.portofolio.webui.hr.transaction.employee;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.module.hr.model.TrsEmployee;
import org.module.hr.service.EmployeeService;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Listbox;

public class EmployeeListVM {
	
	@WireVariable
	private EmployeeService employeeService;
	
	@Wire("#listBoxEmployee")
	private Listbox listBoxEmployee;
	
	private List<TrsEmployee> trsEmployees;
	private TrsEmployee trsEmployee;
	
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Function Custom sesuai kebutuhan
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	public void doPrepareList(){
		listBoxEmployee.setCheckmark(true);
		listBoxEmployee.setMultiple(true);
		listBoxEmployee.setRows(5);
		listBoxEmployee.setStyle("border-style: none;");
		listBoxEmployee.setMold("paging");
		listBoxEmployee.setAutopaging(true);
	}

	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Inisialize Methode MVVM yang pertama kali dijalankan
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	@AfterCompose
	public void setupComponents(@ContextParam(ContextType.VIEW) Component component,
		@ExecutionArgParam("object") Object object) {		
		Selectors.wireComponents(component, this, false);
		
		trsEmployees = employeeService.getAllTrsEmployee();
		doPrepareList();
	}
	
	@Command
	public void doNew(){
		Executions.createComponents("/WEB-INF/pages/module_hr/transaction/employee/employeeDialog.zul", null, null);
	}
	
	@Command
	public void doDetail(){
		HashMap<String, Object> arg = new HashMap<>();
		arg.put("trsEmployee", trsEmployee);
		Executions.createComponents("/WEB-INF/pages/module_hr/transaction/employee/employeeDetailDialog.zul", null, arg);
	}


	public EmployeeService getEmployeeService() {
		return employeeService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public Listbox getListBoxEmployee() {
		return listBoxEmployee;
	}

	public void setListBoxEmployee(Listbox listBoxEmployee) {
		this.listBoxEmployee = listBoxEmployee;
	}

	public List<TrsEmployee> getTrsEmployees() {
		return trsEmployees;
	}

	public void setTrsEmployees(List<TrsEmployee> trsEmployees) {
		this.trsEmployees = trsEmployees;
	}

	public TrsEmployee getTrsEmployee() {
		return trsEmployee;
	}

	public void setTrsEmployee(TrsEmployee trsEmployee) {
		this.trsEmployee = trsEmployee;
	}
	
	
	
	
}