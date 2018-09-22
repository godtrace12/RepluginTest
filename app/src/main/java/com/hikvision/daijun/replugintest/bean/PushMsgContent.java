package com.hikvision.daijun.replugintest.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 【说明】：
 *
 * @author daijun
 * @version 2.0
 * @date 2018/9/7 11:09
 */
public class PushMsgContent implements Parcelable{
    public MsgContent msgContent;
    public int msgType;
    public String sendTime;
    public String title;

    public PushMsgContent(){

    }

    protected PushMsgContent(Parcel in) {
        msgContent = in.readParcelable(MsgContent.class.getClassLoader());
        msgType = in.readInt();
        sendTime = in.readString();
        title = in.readString();
    }

    public static final Creator<PushMsgContent> CREATOR = new Creator<PushMsgContent>() {
        @Override
        public PushMsgContent createFromParcel(Parcel in) {
            return new PushMsgContent(in);
        }

        @Override
        public PushMsgContent[] newArray(int size) {
            return new PushMsgContent[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(msgContent, flags);
        dest.writeInt(msgType);
        dest.writeString(sendTime);
        dest.writeString(title);
    }
}
