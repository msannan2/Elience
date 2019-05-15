package com.HCI.elience;

public class GroupsModel {
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String groupName;
    public int unread;
    public String date;
    public String lastMessage;
    public Long timestamp;
    public Long lastseen;
    public String groupID;


    public GroupsModel(String groupName, int unread, String date, String lastMessage,long timestamp) {
        this.groupName = groupName;
        this.unread = unread;
        this.date = date;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
    }

    public GroupsModel() {
    }
}
