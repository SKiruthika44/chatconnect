import { setAllUsers, setLoggedInUser, setLoggedInUserLoading, setUsersLoading, setVisibleUsers } from "../../Slice/UserSlice";
import { getLoggedInUserApi,getAllUsersApi } from "../../api/userApi";
import { toast } from "react-toastify";
export const getLoggedInUser=async(token,dispatch)=>{
    try{
        const response=await getLoggedInUserApi(token);
        console.log("loggedinuser");
        dispatch(setLoggedInUserLoading(false));
        dispatch(setLoggedInUser(response.data));
    }
    catch(error){
        toast.error(error.response?.data?.message);
        console.log("Loggedin user error:"+error);
    }
}

export const getAllUsers=async(token,dispatch)=>{
    try{
        const response=await getAllUsersApi(token);
        console.log("users");
        dispatch(setUsersLoading(false));
        dispatch(setAllUsers(response.data));
        dispatch(setVisibleUsers(response.data));
    }
    catch(error){
        toast.error(error.response?.data?.message);
        console.log("All User error:"+error);
    }
}






