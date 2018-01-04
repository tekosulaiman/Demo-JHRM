package org.app.portofolio.webui.hr.transaction.employee;

import java.util.HashMap;
import java.util.List;

import org.app.portofolio.webui.hr.transaction.employee.model.SalaryListItemRenderer;
import org.module.hr.model.TrsEmployee;
import org.module.hr.model.TrsEmployeeSalary;
import org.module.hr.service.EmployeeService;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Listbox;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class Salary {
	
	@Wire("#listBoxSalary")
	private Listbox listBoxSalary;
	
	@WireVariable
	private EmployeeService employeeService;
	
	private TrsEmployee trsEmployee;
	
	private SalaryListItemRenderer salaryListItemRenderer;
	
	private List<TrsEmployeeSalary> trsEmployeeSalaries;
	
	@AfterCompose
	public void setuoComponents(@ContextParam(ContextType.VIEW) Component component, 
			@ExecutionArgParam("type") TrsEmployee trsEmployee){
		Selectors.wireComponents(component, this, false);
		this.trsEmployee = trsEmployee;
		HashMap< String, Object> requestMap = new HashMap<>();
		requestMap.put("trsEmployee", trsEmployee);
		trsEmployeeSalaries = employeeService.getTrsEmployeeSalaryByTrsEmployeeSalaryRequestMap(requestMap);
	}
	
}
