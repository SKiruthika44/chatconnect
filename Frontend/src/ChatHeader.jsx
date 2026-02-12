//import { Stethoscope } from 'lucide-react'
import React from 'react'
import { useSelector } from 'react-redux'
import Avatar from './Avatar';
import GroupProfile from './assets/GroupProfile.png';
import './css/ChatHeader.css'
const ChatHeader = () => {
  const selectedChat=useSelector((state)=>state.chat.selectedChat);
  const onlineUsers=useSelector((state)=>state.user.onlineUsers);
  const allUsers=useSelector((state)=>state.user.allUsers);
  const getLastSeen=()=>{
   const selectedUser=allUsers.find((user)=>user.id==selectedChat.data.id);
   return selectedUser?.lastSeen;
  }
  return (
    <div className='chatheader'>
        {
            selectedChat.type=="direct"?  <Avatar user={selectedChat.data}/> : <img style={{width:"40px",height:"40px",borderRadius:"50%"}}src={GroupProfile}/>
        }
        
           <div className="name-status">
             <h4>{selectedChat.type=="group"?selectedChat.data.groupName:selectedChat.data.username}</h4>
          
          <h5>{selectedChat.type=="direct"?onlineUsers.includes(selectedChat.data.username)?'Online':new Date(getLastSeen()).toLocaleString():""}</h5>
           </div>

      
    </div>
  )
}

export default ChatHeader
