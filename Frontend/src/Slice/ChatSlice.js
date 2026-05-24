import { createSlice } from "@reduxjs/toolkit"

const initialState={
    selectedChat:null,
    loading:false,
    forwarding:false,
    forwardingData:null
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
        setSelectedChat:(state,action)=>{
            state.selectedChat=action.payload
        },
        setLoading:(state,action)=>{
            state.loading=action.payload
        },
         setForwarding:(state,action)=>{
            state.forwarding=action.payload
        },
         setForwardingData:(state,action)=>{
            state.forwardingData=action.payload
        }


    }
})
export const {setSelectedChatAsDirect,setSelectedChatAsGroup,setLoading,setForwarding,setForwardingData,setSelectedChat}=ChatSlice.actions;
export default ChatSlice.reducer;