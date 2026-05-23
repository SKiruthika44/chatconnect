import { getAllMessagesForSelectedGroupApi } from "../../api/GroupApi";
import { setMessages } from "../../Slice/MessageSlice";
import { setLoading } from "../../Slice/ChatSlice";
import { toast } from "react-toastify";
export const getAllMessagesForSelectedGroup=async(token,dispatch)=>{
    try{
        const response=await getAllMessagesForSelectedGroupApi(token);
        dispatch(setLoading(false));
        dispatch(setMessages(response.data));
    }
    catch(error){
        toast.error(error.response.data.message);
        console.log("groupmessages for selected group error",error);
    }
}