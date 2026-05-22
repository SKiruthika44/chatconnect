import React from 'react'
import { useSelector,useDispatch } from 'react-redux';
import { useState,useEffect } from 'react';
import { sendMessage } from './app/services/MessageService';
import { setForwarding, setForwardingData, setSelectedChatAsDirect, setSelectedChatAsGroup } from './Slice/ChatSlice';
import './css/ForwardList.css'
import { setMessages } from './Slice/MessageSlice';
const SendersList = ({stompClient,content,closeForward}) => {
    const allUsers=useSelector((state)=>state.user.allUsers);
    const allGroups=useSelector((state)=>state.group.allGroups);
    const [selected,setSelected]=useState(null);
    
    const loading=useSelector((state)=>state.chat.loading);
    const forwarding=useSelector((state)=>state.chat.forwarding);
    const forwardingData=useSelector((state)=>state.chat.forwardingData);
    const dispatch=useDispatch();

    useEffect(()=>{
      console.log("inside loding useeffect");
      if(!loading && forwarding && forwardingData){
        console.log("forwarding msg");
        sendMessage(stompClient,forwardingData.type,forwardingData.content,forwardingData.data);
        dispatch(setForwarding(false));
        dispatch(setForwardingData(null));
      }
    },[loading]);
    const handleForwarding=()=>{
     
      console.log("forwarding starts");
      
      if(selected && content ){
        
        const type=selected.type;
        const data=selected.data;
       dispatch(setForwarding(true));
       dispatch(setForwardingData({
        type:type,
        data:data,
        content:content
       }))
        
        closeForward();
        if(type=="user"){
          const user=allUsers.find((user)=>user.username==selected.data);
          dispatch(setMessages([]));
          dispatch(setSelectedChatAsDirect(user));
          
        }
        else{
          const group=allGroups.find((group)=>group.groupName==selected.data);
          dispatch(setMessages([]));
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
     
    
     
     <button className="forward-btn" onClick={handleForwarding}>Forward Message</button>
     <button className="close-btn" onClick={()=>closeForward()}>Close</button>
    </div>
    </div>
  )
}

export default SendersList
