package com.chatConnect.backend.Controller;

import com.chatConnect.backend.Modal.GroupResponseDTO;
import com.chatConnect.backend.Modal.SearchResponse;
import com.chatConnect.backend.Modal.UserDTO;
import com.chatConnect.backend.Modal.UserPrincipal;
import com.chatConnect.backend.Service.GroupService;
import com.chatConnect.backend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController

public class SearchController {

    @Autowired
    UserService userservice;

    @Autowired
    GroupService groupService;

    @RequestMapping("/search")
    public SearchResponse search(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam(required=false) String keyword){
        List<UserDTO> userDTOList=userservice.search(userPrincipal.getUsername(),keyword);
        List<GroupResponseDTO> groupResponseDTOList=groupService.search(userPrincipal.getUsername(),keyword);
        SearchResponse searchResponse=new SearchResponse(userDTOList,groupResponseDTOList);
        return searchResponse;

    }

}
