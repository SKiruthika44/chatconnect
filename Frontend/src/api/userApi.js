import axios from "axios";
export const getLoggedInUserApi=(token)=>{
    try{
        const response=axios.get(`https://chatconnect-8iix.onrender.com/user`,{
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

export const getAllUsersApi=(token)=>{
    try{
        const response=axios.get(`https://chatconnect-8iix.onrender.com/chat-users`,{
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

