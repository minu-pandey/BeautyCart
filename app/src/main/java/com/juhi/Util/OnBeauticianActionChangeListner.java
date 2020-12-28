package com.juhi.Util;

public interface OnBeauticianActionChangeListner {
    void onBeauticianAccept(boolean state,int adapterPosition);
    void onBeauticianUpdateStatus(int  statusCode,int adapterPosition);

}
