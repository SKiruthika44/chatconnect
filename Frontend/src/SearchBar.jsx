import React, { useEffect, useState } from 'react'
import { useDispatch, useSelector } from 'react-redux';
import axios from "axios";
import './css/SearchBar.css';
import { setAllUsers, setVisibleUsers } from './Slice/UserSlice';
import { setAllGroups, setVisibleGroups } from './Slice/GroupSlice';
const SearchBar = ({token}) => {
  const [searchText,setSearchText]=useState("");
  const allUsers=useSelector((state)=>state.user.allUsers);
  const allGroups=useSelector((state)=>state.group.allGroups);
    const dispatch=useDispatch();
  const searchUserOrGroup=async()=>{
    try{
        const res=await axios.get(`http://localhost:8080/search`,{
            params:{
                keyword:searchText.trim() || undefined
            },
            headers:{
                Authorization:`Bearer ${token}`
            }
        });
        dispatch(setVisibleUsers(res.data.userDTOList));
        dispatch(setVisibleGroups(res.data.groupResponseDTOList));
        console.log(res.data);
    }
    catch(error){
        console.log(error);
    }
  }
  useEffect(()=>{
    
    const timer=setTimeout(()=>{
        console.log(searchText);
        if(searchText==""){
            dispatch(setVisibleUsers(allUsers));
            dispatch(setVisibleGroups(allGroups));
        }
        else{
            searchUserOrGroup();
        }
    },400);
    return ()=>clearTimeout(timer);

  },[searchText]);
  return (
    <div className='search-container'>
        <input type="text" placeholder='Search by name or group...' value={searchText} onChange={(e)=>setSearchText(e.target.value)}/>
      
    </div>
  )
}

export default SearchBar
