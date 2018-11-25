package com.hikvision.daijun.plugin1;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hikvision.daijun.plugin1.bean.AlarmMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * 【说明】：
 *
 * @author daijun
 * @version 2.0
 * @date 2018/11/20 14:33
 */
public class PluginAidlService extends Service {
    private static final String TAG = "PluginAidlService";
    private List<AlarmMessage> mMsgList = new ArrayList<>();
    private final AlarmMessageMgr.Stub mAlarmMgr = new AlarmMessageMgr.Stub() {
        @Override
        public List<AlarmMessage> getAlarmMessages() throws RemoteException {
            return mMsgList;
        }

        @Override
        public void addAlarmMessage(AlarmMessage msg) throws RemoteException {
            Log.e(TAG, "plugin service addAlarmMessage: ");
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
            int size = mMsgList.size();
            for (int i=0;i<size;i++) {
                AlarmMessage alarmMessage = mMsgList.get(i);
                Log.e(TAG, "aidl plugin1 : "+String.format("alarmTitle=%s alarmTime=%s",alarmMessage.alarmTitle,alarmMessage.alarmTime));
            }
            //启动插件界面
            Intent mainIntent = new Intent(getBaseContext(),AlarmMsgDetailActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mainIntent);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind: ");
        return mAlarmMgr;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate: ");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy: ");
        super.onDestroy();
    }
}
