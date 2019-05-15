package com.HCI.elience;

public class MessageModel {
    public String uid;
    public String userName;
    public String messageDate;
    public String messageTime;
    public String message;

    public MessageModel(String uid, String userName, String messageDate, String messageTime, String message) {
        this.uid = uid;
        this.userName = userName;
        this.messageDate = messageDate;
        this.messageTime = messageTime;
        this.message = message;
    }
}
