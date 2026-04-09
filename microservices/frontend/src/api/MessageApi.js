import Store from "../app/Store";
import axios from "axios";
export const getAllMessagesBetweenApi=(token)=>{
    const selectedChat=Store.getState().chat.selectedChat;
    console.log(selectedChat.data);
    try{
        const response=axios.get(`http://localhost:8080/message/direct/all-messages/${selectedChat.data.username}`,{
            headers:{
                Authorization:`Bearer ${token}`
            }
        })
        return response;

    }catch(error){
        throw error;
    }
}

