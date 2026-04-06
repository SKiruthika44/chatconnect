import React from 'react'
import { useSelector,useDispatch } from 'react-redux';
import { useState } from 'react';
import { sendMessage } from './app/services/MessageService';
import { setSelectedChatAsDirect, setSelectedChatAsGroup } from './Slice/ChatSlice';
import './css/ForwardList.css'
const SendersList = ({stompClient,content,closeForward}) => {
    const allUsers=useSelector((state)=>state.user.allUsers);
    const allGroups=useSelector((state)=>state.group.allGroups);
    const [selected,setSelected]=useState(null);
    const dispatch=useDispatch();
    const handleForwarding=()=>{
     
      console.log(selected.type);
      
      if(selected && content){
        console.log("forwarding..");
        const type=selected.type;
        const data=selected.data;
        sendMessage(stompClient,type,content,data);
        
        closeForward();
        if(type=="user"){
          const user=allUsers.find((user)=>user.username==selected.data);
          dispatch(setSelectedChatAsDirect(user));
        }
        else{
          const group=allGroups.find((group)=>group.groupName==selected.data);
          dispatch(setSelectedChatAsGroup(group));
        }
        
      }
    }
  return (
    <div className="forward-backdrop">

    
    <div className='sender-list'>
      <h3 className="forward-title">Forward To</h3>
      <div className="forward-list">
        { 
       allUsers.map((user)=>(
        <label key={user.id} className="sender-item">
            
            <input type="radio" name="forward" value={user.username} checked={selected?.data==user.username} onChange={(e)=>setSelected({type:"user",data:user.username})}/>
            <span className='sender-name'>{user.username}</span></label>
       ))
     }

      </div>

      <div className="forward-list">
        { 
       allGroups.map((group)=>(
        <label key={group.id} className='sender-item'>
            <input type="radio" name="forward" value={group.groupName} checked={selected?.data==group.groupName} onChange={(e)=>setSelected({type:"group",data:group.groupName})}/>
            <span className='sender-name'>{group.groupName}</span></label>
       ))
     }

      </div>
     
    
     
     <button className='forward-btn' onClick={handleForwarding}>Forward Message</button>
     <h3>{selected?.data}</h3>
    </div>
    </div>
  )
}

export default SendersList
