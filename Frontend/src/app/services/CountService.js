import { setUnReadCountForGroups, setUnReadCountForUsers, updateUnReadPrivateChatCountZero,updateUnReadGroupChatCountZero} from "../../Slice/CountSlice";
import Store from "../Store";
import { getUnReadCountForGroupChatApi,getUnReadCountForPrivateChatApi } from "../../api/CountApi";
import { updateDbPrivateChatUnreadCountApi,updateDbGroupChatUnreadCountApi } from "../../api/CountApi";
export const getUnReadCountForPrivateChat=async(token,dispatch)=>{
    try{
            const response=await getUnReadCountForPrivateChatApi(token);
            dispatch(setUnReadCountForUsers(response.data));
    }
    catch(error){
        console.log("uread count  for private mssages",error);
    }
}

export const getUnReadCountForGroupChat=async(token,dispatch)=>{
    try{
            const response=await getUnReadCountForGroupChatApi(token);
            dispatch(setUnReadCountForGroups(response.data));
    }
    catch(error){
        console.log("uread count  for group mssages",error);
    }
}

export const updateUiPrivateChatUnreadCount=(dispatch)=>{
    const selectedChat=Store.getState().chat.selectedChat;
    dispatch(updateUnReadPrivateChatCountZero(selectedChat.data));
}

export const updateDbPrivateChatUnreadCount=async(token)=>{
    try{
        const response=await updateDbPrivateChatUnreadCountApi(token);
    }
    catch(error){
        console.log("update db private chat unread count",error);
    }
}

export const updateDbGroupChatUnreadCount=async(token)=>{
    try{
        const response=await updateDbGroupChatUnreadCountApi(token);
    }
    catch(error){
        console.log("update dbgroup chat unread count",error);
    }
}

export const updateUiGroupChatUnreadCount=(dispatch)=>{
    const selectedChat=Store.getState().chat.selectedChat.data;
    dispatch(updateUnReadGroupChatCountZero(selectedChat));
    
}






