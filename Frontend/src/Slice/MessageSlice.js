import { createSlice } from "@reduxjs/toolkit"

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
        }

        
    }


})
export const {setMessages,updateMessageContent,updateMessageStatus,addMessage}=MessageSlice.actions;
export default MessageSlice.reducer;