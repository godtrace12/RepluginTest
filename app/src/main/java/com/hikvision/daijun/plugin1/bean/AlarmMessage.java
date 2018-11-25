package com.hikvision.daijun.plugin1.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 【说明】：
 *
 * @author daijun
 * @version 2.0
 * @date 2018/11/20 10:59
 */
public class AlarmMessage implements Parcelable {
    public String alarmTitle;
    public String alarmTime;
    public int alarmId;
    public AlarmMessage(){}

    protected AlarmMessage(Parcel in) {
        alarmTitle = in.readString();
        alarmTime = in.readString();
        alarmId = in.readInt();
    }

    public static final Creator<AlarmMessage> CREATOR = new Creator<AlarmMessage>() {
        @Override
        public AlarmMessage createFromParcel(Parcel in) {
            return new AlarmMessage(in);
        }

        @Override
        public AlarmMessage[] newArray(int size) {
            return new AlarmMessage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(alarmTitle);
        dest.writeString(alarmTime);
        dest.writeInt(alarmId);
    }
}
