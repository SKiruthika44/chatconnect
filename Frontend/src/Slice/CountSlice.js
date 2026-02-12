import { createSlice } from "@reduxjs/toolkit"

const initialState={
    unReadCountForUsers:{},
    unReadCountForGroups:{}

}
export const CountSlice=createSlice({
    name:"count",
    initialState:initialState,
    reducers:{
        setUnReadCountForUsers:(state,action)=>{
            state.unReadCountForUsers=action.payload;
            console.log("unread counts for users")
            console.log(state.unReadCountForUsers);
        },
        setUnReadCountForGroups:(state,action)=>{
            state.unReadCountForGroups=action.payload;

        },
        updateUnReadCountForGroup:(state,action)=>{
            console.log("action payload");
            console.log(action.payload);
            state.unReadCountForGroups={...state.unReadCountForGroups,[action.payload.groupName]:(state.unReadCountForGroups?.[action.payload.groupName]|| 0)+1}
        },
        
        updateUnReadPrivateChatCountZero:(state,action)=>{
            state.unReadCountForUsers={...state.unReadCountForUsers,[action.payload.username]:0}
        },
        updateUnReadGroupChatCountZero:(state,action)=>{
            state.unReadCountForGroups={...state.unReadCountForGroups,[action.payload.groupName]:0}
        }


    }
})
export const {setUnReadCountForGroups,setUnReadCountForUsers,updateUnReadCountForGroup,updateUnReadPrivateChatCountZero,updateUnReadGroupChatCountZero}=CountSlice.actions;
export default CountSlice.reducer;