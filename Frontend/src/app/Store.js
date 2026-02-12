import { configureStore } from "@reduxjs/toolkit";
import chatReducer from '../Slice/ChatSlice.js';
import countReducer from '../Slice/CountSlice.js';
import groupReducer from '../Slice/GroupSlice.js';
import messageReducer from '../Slice/MessageSlice.js';
import userReducer from '../Slice/UserSlice.js';


const Store=configureStore({
    reducer:{
        chat:chatReducer,
        count:countReducer,
        group:groupReducer,
        message:messageReducer,
        user:userReducer


    }
})
export default Store;