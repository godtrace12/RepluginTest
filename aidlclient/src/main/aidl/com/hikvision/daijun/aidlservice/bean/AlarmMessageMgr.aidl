// AlarmMessageMgr.aidl
package com.hikvision.daijun.aidlservice.bean;

// Declare any non-default types here with import statements
import com.hikvision.daijun.aidlservice.bean.AlarmMessage;

interface AlarmMessageMgr {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */

    List<AlarmMessage> getAlarmMessages();
    void addAlarmMessage(in AlarmMessage msg);
}
