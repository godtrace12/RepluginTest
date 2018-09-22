package com.hikvision.daijun.replugintest.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 【说明】：
 *
 * @author daijun
 * @version 2.0
 * @date 2018/9/7 11:06
 */
public class MsgContent implements Parcelable{
    public int alarmId = -1;
    public String alarmTime;
    public int alarmTypeCode;
    public String alarmTypeName;
    public int channelNo;

    public MsgContent(){

    }

    protected MsgContent(Parcel in) {
        alarmId = in.readInt();
        alarmTime = in.readString();
        alarmTypeCode = in.readInt();
        alarmTypeName = in.readString();
        channelNo = in.readInt();
    }

    public static final Creator<MsgContent> CREATOR = new Creator<MsgContent>() {
        @Override
        public MsgContent createFromParcel(Parcel in) {
            return new MsgContent(in);
        }

        @Override
        public MsgContent[] newArray(int size) {
            return new MsgContent[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(alarmId);
        dest.writeString(alarmTime);
        dest.writeInt(alarmTypeCode);
        dest.writeString(alarmTypeName);
        dest.writeInt(channelNo);
    }
}
