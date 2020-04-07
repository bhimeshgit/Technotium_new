package com.technotium.technotiumapp.workorder.Interface;

import com.technotium.technotiumapp.workorder.model.WorkOrderPojo;

public interface DataUpdate {
    void setData(WorkOrderPojo data);
    WorkOrderPojo getData();
}
