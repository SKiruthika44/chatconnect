import { createSlice } from "@reduxjs/toolkit"


const initialState={
    allGroups:[],
    visibleGroups:[]
}
export const GroupSlice=createSlice({
    name:"group",
    initialState:initialState,
    reducers:{
        setAllGroups:(state,action)=>{
            state.allGroups=action.payload;
            console.log("all groups");
            console.log(state.allGroups);
        },
        addGroup:(state,action)=>{
            state.allGroups=[...state.allGroups,action.payload];

        },
        setVisibleGroups:(state,action)=>{
            state.visibleGroups=action.payload;
        }
    }
})
export const {setAllGroups,addGroup,setVisibleGroups}=GroupSlice.actions;
export default GroupSlice.reducer;