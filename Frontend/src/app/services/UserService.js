import { setAllUsers, setLoggedInUser, setVisibleUsers } from "../../Slice/UserSlice";
import { getLoggedInUserApi,getAllUsersApi } from "../../api/userApi";
export const getLoggedInUser=async(token,dispatch)=>{
    try{
        const response=await getLoggedInUserApi(token);
        dispatch(setLoggedInUser(response.data));
    }
    catch(error){
        console.log("Loggedin user error:"+error);
    }
}

export const getAllUsers=async(token,dispatch)=>{
    try{
        const response=await getAllUsersApi(token);
        dispatch(setAllUsers(response.data));
        dispatch(setVisibleUsers(response.data));
    }
    catch(error){
        console.log("All User error:"+error);
    }
}






