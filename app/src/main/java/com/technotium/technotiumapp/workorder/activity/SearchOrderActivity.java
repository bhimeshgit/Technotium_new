dueDateSelectedPosition = position;
if (position == Constance.VALUE_NO_DUE_DATE) {
    mTvNewDueDate.setVisibility(View.GONE);
    mTvNewDueDate.setText("");
    mTvNewDueDate.setEnabled(false);
} else if (position == Constance.VALUE_CUSTOME_DUE_DATE) {
    mTvNewDueDate.setVisibility(View.VISIBLE);
    mTvNewDueDate.setEnabled(true);
    Date newDueDate = getNewDueDate();
    if (Utils.isObjNotNull(newDueDate)) {
        String strNewDueDate = DateUtil.convertDateToStringForDisplay(DATE_FORMAT, newDueDate);
        mTvNewDueDate.setText(getString(R.string.lbl_select_due_date));
    } else {
        mTvNewDueDate.setText(getString(R.string.lbl_select_due_date));
    }
} else {
    mTvNewDueDate.setVisibility(View.VISIBLE);
    mTvNewDueDate.setText("");
    mTvNewDueDate.setEnabled(false);
    String strDate = mTvCreationDate.getText().toString();
    if (Utils.isStringNotNull(strDate)) {
        Date date = DateUtil.convertStringToDateForDisplay(DATE_FORMAT, strDate);
        if (Utils.isObjNotNull(date)) {
            mTvNewDueDate.setText(DateUtil.addNoOfDays(date, DATE_FORMAT, getNoOfDates(position)));
        }
    }
}
mDueDateTV.setText(dueDateList.get(position));
setDueDateTextColor();
