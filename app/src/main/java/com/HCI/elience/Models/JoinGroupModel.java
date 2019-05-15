package com.HCI.elience.models;

public class JoinGroupModel {
    public String groupName;
    public Boolean isJoined;
    public String groupID;

    public JoinGroupModel(String groupName,String groupID) {
        this.groupName = groupName;
        this.groupID=groupID;
    }
}
