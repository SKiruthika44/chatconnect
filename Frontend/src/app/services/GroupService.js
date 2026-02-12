import { getAllMessagesForSelectedGroupApi } from "../../api/GroupApi";
import { setMessages } from "../../Slice/MessageSlice";
export const getAllMessagesForSelectedGroup=async(token,dispatch)=>{
    try{
        const response=await getAllMessagesForSelectedGroupApi(token);
        dispatch(setMessages(response.data));
    }
    catch(error){
        console.log("groupmessages for slected group error",error);
    }
}