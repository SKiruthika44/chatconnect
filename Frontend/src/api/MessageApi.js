import Store from "../app/Store";
import axios from "axios";
export const getAllMessagesBetweenApi=(token)=>{
    const selectedChat=Store.getState().chat.selectedChat;
    
    try{
        const response=axios.get(`https://chatconnect-8iix.onrender.com/messages/${selectedChat.data.username}`,{
            headers:{
                Authorization:`Bearer ${token}`
            }
        })
        return response;

    }catch(error){
        throw error;
    }
}

