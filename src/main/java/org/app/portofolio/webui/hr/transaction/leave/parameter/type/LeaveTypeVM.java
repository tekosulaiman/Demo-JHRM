package org.app.portofolio.webui.hr.transaction.leave.parameter.type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sf.jasperreports.engine.JRException;
import org.app.portofolio.webui.hr.transaction.leave.model.LeaveTypeListitemRenderer;
import org.module.hr.model.MstLeaveType;
import org.module.hr.service.LeaveService;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.event.PagingEvent;

public class LeaveTypeVM {

    private List<MstLeaveType> leaveTypeList, selectedTypeList;
    private Set<MstLeaveType> tempSelectedTypeList;
    private ListitemRenderer<MstLeaveType> leaveTypeItemRenderer;
    private MstLeaveType mstLeaveTypeForFilter;
    private Integer pagingCount, pageSize;
    private String itemNameFilter;

    @WireVariable
    private LeaveService leaveService;

    @AfterCompose
    public void setupComponents(@ContextParam(ContextType.VIEW) Component component) throws Exception {

        Selectors.wireComponents(component, this, false);

        mstLeaveTypeForFilter = new MstLeaveType();
        selectedTypeList = new ArrayList<>();
        tempSelectedTypeList = new HashSet<>();
        
        pageSize = 10;
        refreshPageList(0);

    }

    @NotifyChange("*")
    private void refreshPageList(int refreshActivePage) throws Exception {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        if (mstLeaveTypeForFilter.getLeaveTypeName() != null
                && mstLeaveTypeForFilter.getLeaveTypeName().equals("")) {
            hashMap.put("leaveTypeName", mstLeaveTypeForFilter.getLeaveTypeName());
        }
        pagingCount = leaveService.getCountMstLeaveTypeWithFilter(hashMap);

        //postgres offset start from 0
        //refreshActivePage += 1;

        Map<String, Object> sortBy = new LinkedHashMap<>();
        sortBy.put("leaveTypeName","ASC");
        
        HashMap<String, Object> hashMapMstLeaveType = new HashMap<String, Object>();
        hashMapMstLeaveType.put("map", hashMap);
        hashMapMstLeaveType.put("sortBy", sortBy);
        hashMapMstLeaveType.put("offset", refreshActivePage);
        hashMapMstLeaveType.put("limit", pageSize);

        leaveTypeList = leaveService.getMstLeaveTypePagingWithFilter(hashMapMstLeaveType);
        leaveTypeItemRenderer = new LeaveTypeListitemRenderer();
        
        setSelectedFromTemp();
    }

    @Command
    @NotifyChange("leaveTypeList")
    public void onPaging(@ContextParam(ContextType.TRIGGER_EVENT) PagingEvent pagingEvent) throws Exception {
        selectedToTemp();
        refreshPageList(pagingEvent.getActivePage() * pageSize);
    }

    @Command
    @NotifyChange({"leaveTypeList", "leaveTypeItemRenderer", "pagingCount"})
    public void doFilter() throws Exception {
        leaveTypeList.clear();
        refreshPageList(0);
    }

    @Command
    @NotifyChange("leaveTypeList")
    public void doNew() {
        leaveTypeList.add(0, new MstLeaveType());
    }

    @GlobalCommand
    @NotifyChange("*")
    public void refreshMstLeaveTypeAfterSaveOrUpdate() throws Exception {
        refreshPageList(0);
    }

    public void selectedToTemp(){
        for(MstLeaveType obj : leaveTypeList){
            if(tempSelectedTypeList.contains(obj)
                    && !selectedTypeList.contains(obj)){
                tempSelectedTypeList.remove(obj);
            }
        }
        tempSelectedTypeList.addAll(selectedTypeList);
    }
    
    public void setSelectedFromTemp(){
        selectedTypeList = new ArrayList<>();
        for(MstLeaveType obj : leaveTypeList){
            if(tempSelectedTypeList.contains(obj)){
                selectedTypeList.add(obj);
            }
        }
    }
    
    @Command
    @NotifyChange("*")
    public void doDelete() {
        selectedToTemp();
        if (tempSelectedTypeList.isEmpty()) {
            Messagebox.show("There is no selected record?", "Confirm", Messagebox.OK, Messagebox.ERROR);
        } else {
            Messagebox.show("Do you really want to remove item?", "Confirm", Messagebox.OK | Messagebox.CANCEL, Messagebox.EXCLAMATION, new EventListener<Event>() {
                public void onEvent(Event event) throws Exception {
                    if (((Integer) event.getData()).intValue() == Messagebox.OK) {
                        leaveService.deleteListMstLeaveType(new ArrayList<MstLeaveType>(tempSelectedTypeList));
                        tempSelectedTypeList.clear();
                        BindUtils.postGlobalCommand(null, null, "refreshMstLeaveTypeAfterSaveOrUpdate", null);
                    } else {
                        return;
                    }
                }
            });
        }
    }

    @Command
    @NotifyChange("*")
    public void doRefresh() throws Exception {
        tempSelectedTypeList.clear();
        refreshPageList(0);
    }

    @Command
    public void doPrint() throws JRException {

    }

    
    
    
    
    /////////// setter getter ////////////////
    
   
    public List<MstLeaveType> getLeaveTypeList() {
        return leaveTypeList;
    }

    public void setLeaveTypeList(List<MstLeaveType> leaveTypeList) {
        this.leaveTypeList = leaveTypeList;
    }

    public ListitemRenderer<MstLeaveType> getLeaveTypeItemRenderer() {
        return leaveTypeItemRenderer;
    }

    public void setLeaveTypeItemRenderer(ListitemRenderer<MstLeaveType> leaveTypeItemRenderer) {
        this.leaveTypeItemRenderer = leaveTypeItemRenderer;
    }

    public Integer getPagingCount() {
        return pagingCount;
    }

    public void setPagingCount(Integer pagingCount) {
        this.pagingCount = pagingCount;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getItemNameFilter() {
        return itemNameFilter;
    }

    public void setItemNameFilter(String itemNameFilter) {
        this.itemNameFilter = itemNameFilter;
    }

    public MstLeaveType getMstLeaveTypeForFilter() {
        return mstLeaveTypeForFilter;
    }

    public void setMstLeaveTypeForFilter(MstLeaveType mstLeaveTypeForFilter) {
        this.mstLeaveTypeForFilter = mstLeaveTypeForFilter;
    }

    public List<MstLeaveType> getSelectedTypeList() {
        return selectedTypeList;
    }

    public void setSelectedTypeList(List<MstLeaveType> selectedTypeList) {
        this.selectedTypeList = selectedTypeList;
    }

    public Set<MstLeaveType> getTempSelectedTypeList() {
        return tempSelectedTypeList;
    }

    public void setTempSelectedTypeList(Set<MstLeaveType> tempSelectedTypeList) {
        this.tempSelectedTypeList = tempSelectedTypeList;
    }

}
