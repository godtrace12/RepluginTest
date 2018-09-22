package com.hikvision.daijun.messengerservice;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;

/**
 * 【说明】：
 *
 * @author daijun
 * @version 2.0
 * @date 2018/9/6 14:57
 */
public class PluginComService extends Service {
    private static final String TAG = "PluginComService";
    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.e(TAG, "handleMessage: what="+msg.what);
            switch (msg.what) {
                case 1001:
                    Log.e(TAG, "handleMessage: 服务端收到消息"+msg.getData().getString("msg"));
                    Log.e(TAG, "handleMessage: 服务端收到消息"+msg.getData().getString("msgJson"));
                    String msgJson = msg.getData().getString("msgJson");
                    Gson gson = new Gson();
                    PushMsgContent pushMsgContent = gson.fromJson(msgJson,PushMsgContent.class);
                    if (pushMsgContent != null) {
                        Log.e(TAG, "handleMessage: PushMsgContent="+pushMsgContent.sendTime+"  "+pushMsgContent.msgContent.alarmTime);
                    }
                    Messenger mesgerReply = msg.replyTo;
                    Message msgReply = Message.obtain();
                    Bundle bundle = new Bundle();
                    bundle.putString("service_reply","hello,服务端已收到信息，想做什么");
                    msgReply.what = 2001;
                    msgReply.setData(bundle);
                    try {
                        mesgerReply.send(msgReply);
                    } catch (RemoteException e) {
                        Log.e(TAG, "handleMessage: 消息发给客户端，发送失败");
                        e.printStackTrace();
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    }
    private Messenger messenger = new Messenger(new MessengerHandler());
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind: ");
        return messenger.getBinder();
    }
}
