package com.example.pc.danmuview.DanMuView.DanMuAnimation;

/**
 * Created by ybb on 2017/9/12.
 */
public class LiveModel {

    private String sendUserId;//发送者的id
    private String sendUserName;//发送者的名字

    public LiveModel() {
    }

    public LiveModel(String sendUserId, String sendUserName) {
        this.sendUserId = sendUserId;
        this.sendUserName = sendUserName;
    }

    public String getSendUserId() {
        return sendUserId;
    }

    public LiveModel setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
        return this;
    }

    public String getSendUserName() {
        return sendUserName;
    }

    public LiveModel setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
        return this;
    }
}
