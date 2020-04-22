package com.technotium.technotiumapp.workorder.model;

import java.io.Serializable;

public class WorkOrderPojo implements Serializable {
    String fname;
    String added_by;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    boolean  selected=false;
    public String getWo_report() {
        return wo_report;
    }

    public void setWo_report(String wo_report) {
        this.wo_report = wo_report;
    }

    String wo_report;
    public String getGst_no() {
        return gst_no;
    }

    public void setGst_no(String gst_no) {
        this.gst_no = gst_no;
    }

    String gst_no;
    public int getWo_activity() {
        return wo_activity;
    }

    public void setWo_activity(int wo_activity) {
        this.wo_activity = wo_activity;
    }

    int wo_activity=0;

    String consumer_no;

    public String getConsumer_no() {
        return consumer_no;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    int active=0;

    public String getFirm_name() {
        return firm_name;
    }

    public void setFirm_name(String firm_name) {
        this.firm_name = firm_name;
    }

    public String getContact_person_name() {
        return contact_person_name;
    }

    public void setContact_person_name(String contact_person_name) {
        this.contact_person_name = contact_person_name;
    }

    public String firm_name;
    public String contact_person_name;
    public void setConsumer_no(String consumer_no) {
        this.consumer_no = consumer_no;
    }

    public String getBu() {
        return bu;
    }

    public void setBu(String bu) {
        this.bu = bu;
    }

    String bu;

    public String getAdded_by() {
        return added_by;
    }

    public void setAdded_by(String added_by) {
        this.added_by = added_by;
    }

    public String getAdded_by_type() {
        return added_by_type;
    }

    public void setAdded_by_type(String added_by_type) {
        this.added_by_type = added_by_type;
    }

    String added_by_type;

    public WorkOrderPojo() {
    }
    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    String lname;
    String mobile;
    String Address;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    String email;

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public String getSystemdetail() {
        return systemdetail;
    }

    public void setSystemdetail(String systemdetail) {
        this.systemdetail = systemdetail;
    }

    public String getPanel() {
        return panel;
    }

    public void setPanel(String panel) {
        this.panel = panel;
    }

    public String getInverter() {
        return inverter;
    }

    public void setInverter(String inverter) {
        this.inverter = inverter;
    }

    public String getStructure() {
        return structure;
    }

    public void setStructure(String structure) {
        this.structure = structure;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getLoadextension() {
        return loadextension;
    }

    public void setLoadextension(String loadextension) {
        this.loadextension = loadextension;
    }

    public String getChangeofname() {
        return changeofname;
    }

    public void setChangeofname(String changeofname) {
        this.changeofname = changeofname;
    }

    public String getSolarsanction() {
        return solarsanction;
    }

    public void setSolarsanction(String solarsanction) {
        this.solarsanction = solarsanction;
    }

    public String getMeterinstallation() {
        return meterinstallation;
    }

    public void setMeterinstallation(String meterinstallation) {
        this.meterinstallation = meterinstallation;
    }

    public String getPanelcapacity() {
        return Panelcapacity;
    }

    public void setPanelcapacity(String panelcapacity) {
        Panelcapacity = panelcapacity;
    }

    public String getInvertercapicity() {
        return invertercapicity;
    }

    public void setInvertercapicity(String invertercapicity) {
        this.invertercapicity = invertercapicity;
    }

    public String getMedasaction() {
        return Medasaction;
    }

    public void setMedasaction(String medasaction) {
        Medasaction = medasaction;
    }

    String city="";
    String state="";
    String capacity="";
    String location="";
    String phase="";
    String systemdetail="";
    String panel="";
    String inverter="";
    String structure="";
    String Amount="0";
    String loadextension="";
    String changeofname="";
    String solarsanction="";
    String meterinstallation="";
    String Panelcapacity="";
    String invertercapicity="";
    String Medasaction="";
    String subsidy_approval="";
    String commissioning="";

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    String contactPerson="";
    String projectType="";
    String mname="";

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    String fullname="";
    String pkid="0";
    String agreement="";
    String gridType="";
    String designation="";
    String rateFromCompany="";
    String order_date="";

    public String getPkid() {
        return pkid;
    }

    public void setPkid(String pkid) {
        this.pkid = pkid;
    }
    public String getSubsidy_approval() {
        return subsidy_approval;
    }

    public void setSubsidy_approval(String subsidy_approval) {
        this.subsidy_approval = subsidy_approval;
    }

    public String getCommissioning() {
        return commissioning;
    }

    public void setCommissioning(String commissioning) {
        this.commissioning = commissioning;
    }

    public String getAgreement() {
        return agreement;
    }

    public void setAgreement(String agreement) {
        this.agreement = agreement;
    }

    public String getGridType() {
        return gridType;
    }

    public void setGridType(String gridType) {
        this.gridType = gridType;
    }

    public String getRateFromCompany() {
        return rateFromCompany;
    }

    public void setRateFromCompany(String rateFromCompany) {
        this.rateFromCompany = rateFromCompany;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

}
