import { getAllMessagesForSelectedGroupApi } from "../../api/GroupApi";
import { setMessages } from "../../Slice/MessageSlice";
import { toast } from "react-toastify";
export const getAllMessagesForSelectedGroup=async(token,dispatch)=>{
    try{
        const response=await getAllMessagesForSelectedGroupApi(token);
        dispatch(setMessages(response.data));
    }
    catch(error){
        toast.error(error.response.data.message);
        console.log("groupmessages for slected group error",error);
    }
}