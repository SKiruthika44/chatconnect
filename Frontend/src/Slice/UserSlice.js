import {createSlice} from "@reduxjs/toolkit"
const initialState={
    allUsers:[],
    onlineUsers:[],
    visibleUsers:[],
    loggedInUser:null
}
export const UserSlice=createSlice({
    name:"user",
    initialState:initialState,
    reducers:{
        setAllUsers:(state,action)=>{
            state.allUsers=action.payload;
            console.log("all users");
            console.log(state.allUsers);

        },
        setOnlineUsers:(state,action)=>{
            state.onlineUsers=action.payload;
            console.log("onlineusers");
            console.log(state.allUsers);

        },
        updateLastSeen:(state,action)=>{
            const updatedUser=action.payload;
            state.allUsers=state.allUsers.map((user,index)=>user.id==updatedUser.id?{...user,lastSeen:updatedUser.lastSeen}:user)

        },
        setLoggedInUser:(state,action)=>{
            state.loggedInUser=action.payload;
            console.log("loggedinuser");
            console.log(state.loggedInUser);

        }, 
        updateLoggedInUserProfile:(state,action)=>{
            state.loggedInUser={...state.loggedInUser,profileImage:action.payload}
        },
        setVisibleUsers:(state,action)=>{
            state.visibleUsers=action.payload;
            console.log("visible users");
            console.log(state.visibleUsers);

        }

    }
})
export const {setAllUsers,setLoggedInUser,setOnlineUsers,updateLastSeen,updateLoggedInUserProfile,setVisibleUsers} =UserSlice.actions;
export default  UserSlice.reducer;