import axios from "axios";
export const getLoggedInUserApi=(token)=>{
    try{
        const response=axios.get(`http://localhost:8080/user`,{
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
        const response=axios.get(`http://localhost:8080/all-users`,{
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

