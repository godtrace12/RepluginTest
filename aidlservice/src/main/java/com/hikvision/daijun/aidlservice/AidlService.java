package com.hikvision.daijun.aidlservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hikvision.daijun.aidlservice.bean.AlarmMessage;
import com.hikvision.daijun.aidlservice.bean.AlarmMessageMgr;

import java.util.ArrayList;
import java.util.List;

/**
 * 【说明】：
 *
 * @author daijun
 * @version 2.0
 * @date 2018/9/7 20:04
 */
public class AidlService extends Service {
    private static final String TAG = "AidlService";
    private List<AlarmMessage> mMsgList = new ArrayList<>();
    private final AlarmMessageMgr.Stub mAlarmMgr = new AlarmMessageMgr.Stub() {
        @Override
        public List<AlarmMessage> getAlarmMessages() throws RemoteException {
            Log.e(TAG, "getAlarmMessages: ");
            synchronized (this) {
                if (mMsgList != null) {
                    return mMsgList;
                }
                return new ArrayList<>();
            }
        }

        @Override
        public void addAlarmMessage(AlarmMessage msg) throws RemoteException {
            Log.e(TAG, "addAlarmMessage: ");
            synchronized (this) {
                if (mMsgList == null) {
                    mMsgList = new ArrayList<>();
                }
                if (msg == null) {
                    Log.e(TAG, "addAlarmMessage: msg to add is null");
                    msg = new AlarmMessage();
                    msg.alarmId = 0;
                    msg.alarmTime = "1997-10-7 12:00:00";
                    msg.alarmTitle = "空报警";
                }
                mMsgList.add(msg);
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        AlarmMessage msg1 = new AlarmMessage();
        msg1.alarmTitle = "默认的一个报警";
        msg1.alarmTime = "2018-9-10 10:15:20";
        msg1.alarmId = 3;
        mMsgList.add(msg1);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mAlarmMgr;
    }
}
