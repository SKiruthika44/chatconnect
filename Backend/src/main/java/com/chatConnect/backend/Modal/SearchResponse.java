package com.chatConnect.backend.Modal;

import java.util.List;

public class SearchResponse {
    private List<UserDTO> userDTOList;
    private List<GroupResponseDTO> groupResponseDTOList;

    public SearchResponse(List<UserDTO> userDTOList, List<GroupResponseDTO> groupResponseDTOList) {
        this.userDTOList = userDTOList;
        this.groupResponseDTOList = groupResponseDTOList;
    }

    public List<UserDTO> getUserDTOList() {
        return userDTOList;
    }

    public void setUserDTOList(List<UserDTO> userDTOList) {
        this.userDTOList = userDTOList;
    }

    public List<GroupResponseDTO> getGroupResponseDTOList() {
        return groupResponseDTOList;
    }

    public void setGroupResponseDTOList(List<GroupResponseDTO> groupResponseDTOList) {
        this.groupResponseDTOList = groupResponseDTOList;
    }
}
