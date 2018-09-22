package com.hikvision.daijun.aidlclient;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.hikvision.daijun.aidlservice.bean.AlarmMessage;
import com.hikvision.daijun.aidlservice.bean.AlarmMessageMgr;

import java.util.List;
import java.util.Random;

public class ClientMainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "ClientMainActivity";
    private Button btnBind;
    private Button btnAdd;
    private Button btnGetList;

    private AlarmMessageMgr mAlarmMgr = null;
    private List<AlarmMessage> mAlarmList = null;
    private ServiceConnection serviceConnection = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_main);
        findViews();
        initServiceConnection();
    }

    private void findViews() {
        btnBind = (Button) findViewById(R.id.btnBind);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnGetList = (Button) findViewById(R.id.btnGetList);
        btnBind.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnGetList.setOnClickListener(this);
    }

    private void initServiceConnection() {
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.e(TAG, "onServiceConnected: ");
                mAlarmMgr = AlarmMessageMgr.Stub.asInterface(service);
                if (mAlarmMgr != null) {
                    try {
                        mAlarmList = mAlarmMgr.getAlarmMessages();
                        Log.e(TAG, "onServiceConnected: "+mAlarmList.toString()+" size"+mAlarmList.size());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        Log.e(TAG, "onServiceConnected: 获取服务端信息列表失败");
                    }
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.e(TAG, "onServiceDisconnected: ");
            }
        };
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.btnBind) {
            connectToService();
        } else if (viewId == R.id.btnAdd) {
            addAlarmMessage();
        } else if (viewId == R.id.btnGetList) {
            getAlarmMessageList();
        }
    }

    private void connectToService(){
        Intent intent = new Intent();
        intent.setAction("com.hikvision.daijun.aidlclient");
        intent.setPackage("com.hikvision.daijun.aidlservice");
        bindService(intent,serviceConnection,BIND_AUTO_CREATE);
    }

    private void addAlarmMessage(){
        if (mAlarmMgr == null) {
            return;
        }
        Random random = new Random();
        int randomId = random.nextInt();
        AlarmMessage alarmMessage = new AlarmMessage();
        alarmMessage.alarmId = randomId % 20;
        alarmMessage.alarmTitle = "来自client的报警"+alarmMessage.alarmId;
        alarmMessage.alarmTime = "2018-9-10 10:51:45";
        try {
            mAlarmMgr.addAlarmMessage(alarmMessage);
        } catch (RemoteException e) {
            e.printStackTrace();
            Log.e(TAG, "addAlarmMessage: 添加报警失败");
        }
    }

    private void getAlarmMessageList(){
        if (mAlarmMgr == null) {
            return;
        }
        try {
            mAlarmList = mAlarmMgr.getAlarmMessages();
            AlarmMessage msgTmp = null;
            int size = mAlarmList.size();
            for (int i = 0; i < size; i++) {
                msgTmp = mAlarmList.get(i);
                Log.e(TAG, "getAlarmMessageList: "+ msgTmp.alarmId+" "+ msgTmp.alarmTitle+" "+msgTmp.alarmTime);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        if (serviceConnection != null) {
            unbindService(serviceConnection);
        }
        super.onDestroy();
    }
}
