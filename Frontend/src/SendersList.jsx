import React from 'react'
import { useSelector,useDispatch } from 'react-redux';
import { useState } from 'react';
import { sendMessage } from './app/services/MessageService';
import { setSelectedChatAsDirect, setSelectedChatAsGroup } from './Slice/ChatSlice';

const SendersList = ({stompClient,content,closeForward}) => {
    const allUsers=useSelector((state)=>state.user.allUsers);
    const allGroups=useSelector((state)=>state.group.allGroups);
    const [selected,setSelected]=useState(null);
    const dispatch=useDispatch();
    const handleForwarding=()=>{
      console.log("opened method");
      console.log(selected.type);
      //console.log("forwardMessage:", forwardMessage);
      if(selected && content){
        console.log("forwarding..");
        const type=selected.type;
        const data=selected.data;
        sendMessage(stompClient,type,content,data);
        console.log("forwarding..doing");
        closeForward();
        if(type=="user"){
          const user=allUsers.find((user)=>user.username==selected.data);
          dispatch(setSelectedChatAsDirect(user));
        }
        else{
          const group=allGroups.find((group)=>group.groupName==selected.data);
          dispatch(setSelectedChatAsGroup(group));
        }
        console.log("forwarding..complete");
      }
    }
  return (
    <div>
     { 
       allUsers.map((user)=>(
        <label key={user.id}>
            
            <input type="radio" name="forward" value={user.username} checked={selected?.data==user.username} onChange={(e)=>setSelected({type:"user",data:user.username})}/>
            {user.username}</label>
       ))
     }
     <br></br>
     { 
       allGroups.map((group)=>(
        <label key={group.id}>
            <input type="radio" name="forward" value={group.groupName} checked={selected?.data==group.groupName} onChange={(e)=>setSelected({type:"group",data:group.groupName})}/>
            {group.groupName}</label>
       ))
     }
     <button onClick={handleForwarding}>Forward Message</button>
     <h3>{selected?.data}</h3>
    </div>
  )
}

export default SendersList
