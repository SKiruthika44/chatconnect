import Store from "../app/Store";
import axios from "axios";
export const getAllMessagesForSelectedGroupApi=(token)=>{
    const group=Store.getState().chat.selectedChat.data;
    try{
        const response=axios.get(`http://localhost:8080/group/messages/${group.groupName}`,{
            headers:{
              Authorization:`Bearer ${token}`
            }
        });
        return response;

    }
    catch(error){
        throw error;
    }
}

export const updateGroupMessageRead=async(token,id)=>{
    try{

     const resp=await axios.put(`http://localhost:8080/group/updateGroupMessageRead/${id}`,{},{
                  headers:{
                    Authorization:`Bearer ${token}`
                  }
                })

  }
  catch(error){
   console.log(error);
  }

}