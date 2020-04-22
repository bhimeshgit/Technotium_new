package com.technotium.technotiumapp.workorder.Interface;

import android.content.Intent;

import com.technotium.technotiumapp.workorder.model.WorkOrderPojo;

public interface DataUpdate {
    void setData(WorkOrderPojo data);
    WorkOrderPojo getData();
    void onActivityResult(int requestCode, int resultCode, Intent data);
}
