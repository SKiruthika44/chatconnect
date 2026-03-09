import { createSlice } from "@reduxjs/toolkit"
import { CitrusIcon, Stethoscope } from "lucide-react";

const initialState={
    messages:[]
}
export const MessageSlice=createSlice({
    name:"message",
    initialState:initialState,
    reducers:{
        setMessages:(state,action)=>{
            state.messages=action.payload;
        },
        addMessage:(state,action)=>{
            //dd a msg to previous mes if the same window opens
            state.messages=[...state.messages,action.payload];
        },
        updateMessageStatus:(state,action)=>{
            //update msg status as read,delivered,sent
            const updatedMessage=action.payload;
            state.messages=state.messages.map((message,index)=>message.id==updatedMessage.id?{...message,status:updatedMessage.status}:message);
        },
        updateMessageContent:(state,action)=>{
            //update msg content to translatedcontent
            const updatedMessage=action.payload;
            
            state.messages=state.messages.map((message,index)=>message.id==updatedMessage.id?{...message,content:updatedMessage.content}:message);
        },
        updateMessageDeletion:(state,action)=>{
            const updatedMessage=action.payload;
            state.messages=state.messages.map((message)=>message.id==updatedMessage.id?{...message,content:null,deletedForEveryone:updatedMessage.deletedForEveryone}:message);
        },
        removeMessage:(state,action)=>{
            const msgId=action.payload;
            state.messages=state.messages.filter((message)=>message.id!=msgId);
        },
        updatePrivateMessageEmoji:(state,action)=>{
            const updatedMessage=action.payload;
            state.messages=state.messages.map((message)=>message.id==updatedMessage.msgId?{...message,emojis:updatedMessage.emojis}:message);
        },
        updateGroupMessageEmoji:(state,action)=>{
            const updatedMessage=action.payload;
            state.messages=state.messages.map((message)=>message.id==updatedMessage.msgId?{...message,emojisCount:updatedMessage.emojisMap}:message);

        },
        updateMessage:(state,action)=>{
            const updatedMessage=action.payload;
            state.messages=state.messages.map((message)=>message.id==updatedMessage.id?updatedMessage:message);
        }

        
    }


})
export const {setMessages,updateMessageContent,updateMessageStatus,addMessage,updateMessageDeletion,removeMessage,updatePrivateMessageEmoji,updateGroupMessageEmoji,updateMessage}=MessageSlice.actions;
export default MessageSlice.reducer;