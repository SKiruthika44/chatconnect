import { createSlice } from "@reduxjs/toolkit"

const initialState={
    selectedChat:null,
    loading:false
}
export  const ChatSlice=createSlice({
    name:"chat",
    initialState:initialState,
    reducers:{
        setSelectedChatAsDirect:(state,action)=>{
            state.selectedChat={
                type:"direct",
                data:action.payload
            }
        },
        setSelectedChatAsGroup:(state,action)=>{
            state.selectedChat={
                type:"group",
                data:action.payload
            }
        },
        setLoading:(state,action)=>{
            state.loading=action.payload
        }


    }
})
export const {setSelectedChatAsDirect,setSelectedChatAsGroup,setLoading}=ChatSlice.actions;
export default ChatSlice.reducer;