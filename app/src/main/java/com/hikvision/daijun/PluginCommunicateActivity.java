package com.hikvision.daijun;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hikvision.daijun.plugin1.AlarmMessageMgr;
import com.hikvision.daijun.plugin1.bean.AlarmMessage;
import com.hikvision.daijun.replugintest.R;
import com.qihoo360.replugin.RePlugin;
import com.qihoo360.replugin.component.service.PluginServiceClient;
import com.qihoo360.replugin.component.utils.PluginClientHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class PluginCommunicateActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "PluginCommunicateActivi";
    private Button btnSendMsg;
    private Button btnBind;
    private Button btnMsgDetail;
    private ServiceConnection serviceConnection = null;
    private AlarmMessageMgr mAlarmMgr = null;
    private static final DateFormat detailFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //显示每段录像的起止时间

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin_communicate);
        findViews();
        initClickListeners();
        initServiceConnection();
    }

    private void findViews() {
        btnSendMsg = (Button) findViewById(R.id.btnSendMsg);
        btnBind = (Button) findViewById(R.id.btnBind);
        btnMsgDetail = (Button) findViewById(R.id.btnMsgDetail);
    }

    private void initClickListeners(){
        btnBind.setOnClickListener(this);
        btnSendMsg.setOnClickListener(this);
        btnMsgDetail.setOnClickListener(this);
    }

    private void initServiceConnection(){
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.e(TAG, "aidlhost onServiceConnected: ");
                mAlarmMgr = AlarmMessageMgr.Stub.asInterface(service);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.e(TAG, "aidlhost onServiceDisconnected: ");
            }
        };
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId) {
            case R.id.btnBind:
                connectToService();
                break;
            case R.id.btnSendMsg:
                addAlarmMessage();
                break;
            case R.id.btnMsgDetail:
                Log.e(TAG, "onClick: startMsgDetail Activity");
                if (RePlugin.isPluginInstalled("plugin1")) {
                    Log.e(TAG, "onClick: plugin1 installed");
                    RePlugin.startActivity(PluginCommunicateActivity.this, RePlugin.createIntent("plugin1", "com.hikvision.daijun.plugin1.AlarmMsgDetailActivity"));
                } else {
                    Toast.makeText(PluginCommunicateActivity.this,"plugi no found,you must install plugin1 first!",Toast.LENGTH_SHORT).show();
                }
                break;
        }
}

    private void connectToService(){
        Log.e(TAG, "connectToService: ");
        Intent intent1 = RePlugin.createIntent("plugin1","com.hikvision.daijun.plugin1.PluginAidlService");
        intent1.setAction("com.hikvision.daijun.replugintest");
        intent1.setPackage("com.hikvision.daijun.plugin1");
        try {
            PluginServiceClient.bindService(PluginCommunicateActivity.this, intent1, serviceConnection, BIND_AUTO_CREATE);
        } catch (PluginClientHelper.ShouldCallSystem e) {
            Log.e(TAG, "connectToService: "+e.getMessage());
        }
    }


    private void addAlarmMessage(){
        Log.e(TAG, "addAlarmMessage: ");
        if (mAlarmMgr == null) {
            return;
        }
        Random random = new Random();
        int randomId = random.nextInt();
        AlarmMessage alarmMessage = new AlarmMessage();
        alarmMessage.alarmId = randomId % 20;
        alarmMessage.alarmTitle = "来自host接收到的报警"+alarmMessage.alarmId;
        alarmMessage.alarmTime = detailFormatter.format(Calendar.getInstance().getTime());
        try {
            Log.e(TAG, "addAlarmMessage: 发送报警 "+ String.format("alarmTitle=%s alarmTime=%s",alarmMessage.alarmTitle,alarmMessage.alarmTime));
            mAlarmMgr.addAlarmMessage(alarmMessage);
        } catch (RemoteException e) {
            e.printStackTrace();
            Log.e(TAG, "addAlarmMessage: 添加报警失败");
        }
    }

    @Override
    protected void onDestroy() {
        PluginServiceClient.unbindService(PluginCommunicateActivity.this,serviceConnection);
//        unbindService(serviceConnection);
        super.onDestroy();
    }
}
