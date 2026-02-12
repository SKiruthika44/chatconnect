import { setMessages } from "../../Slice/MessageSlice";
import { getAllMessagesBetweenApi } from "../../api/MessageApi";
export const getAllMessagesBetween=async(token,dispatch)=>{
    try{
        const response=await getAllMessagesBetweenApi(token);
        dispatch(setMessages(response.data));
    }
    catch(error){
        console.log("error getting allmessages",error);
    }

}

