package com.chatConnect.backend.Modal;

import java.util.List;

public class GroupDTO {

    private String groupName;


    private List<String> groupMembers;

    public GroupDTO( String groupName, List<String> groupMembers) {



        this.groupName = groupName;
        this.groupMembers = groupMembers;

    }





    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<String> getGroupMembers() {
        return groupMembers;
    }

    public void setGroupMembers(List<String> groupMembers) {
        this.groupMembers = groupMembers;
    }

    @Override
    public String toString() {
        return "GroupDTO{" +
                "groupName='" + groupName + '\'' +
                ", groupMembers=" + groupMembers +
                '}';
    }
}
