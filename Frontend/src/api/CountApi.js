import axios from "axios";
import Store from "../app/Store";
export const getUnReadCountForPrivateChatApi=(token)=>{
    try{
        const response=axios.get(`https://chatconnect-8iix.onrender.com/unreadCounts`,{
            headers:{
                Authorization:`Bearer ${token}`
            }
        })
        return response;
    }
    catch(error){
        throw error;
    }
}

export const getUnReadCountForGroupChatApi=(token)=>{
    try{
        const response=axios.get(`https://chatconnect-8iix.onrender.com/group/unread`,{
            headers:{
                Authorization:`Bearer ${token}`
            }
        })
        return response;
    }
    catch(error){
        throw error;
    }
}

export const updateDbPrivateChatUnreadCountApi=async(token)=>{
    const selectedChat=Store.getState().chat.selectedChat.data;
    try{
       const response=await axios.put(`https://chatconnect-8iix.onrender.com/makeRead/${selectedChat.username}`,
          {},{
        headers:{
          Authorization:`Bearer ${token}`
        }
      })
      return response;
    }
    catch(error){
        throw error;
    }

}

export const updateDbGroupChatUnreadCountApi=(token)=>{
    const selectedChat=Store.getState().chat.selectedChat;
    const group=selectedChat.data;
    try{
          const response= axios.put(`https://chatconnect-8iix.onrender.com/group/makeRead/${group.groupName}`,{},{
          headers:{authorization:`Bearer ${token}`}
        })
        return response;
        }
        catch(error){
          throw error;
    }

}
