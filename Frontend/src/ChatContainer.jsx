import React, { useState } from 'react'
import ChatHeader from './ChatHeader'
import MessageList from './MessageList'
import MessageInput from './MessageInput'
import {ThreeDots} from 'react-loader-spinner'
import './css/Chatcontainer.css'
import { useSelector } from 'react-redux'
const ChatContainer = ({stompClient,token,isMobile}) => {
  const [editingMessage,setEditingMessage]=useState(null);
  const loading=useSelector((state)=>state.chat.loading);
  const forwarding=useSelector((state)=>state.chat.forwarding);
  const onEdit=(msg)=>{
    setEditingMessage(msg);
    
  }
  return (
    <div className='chat-container'>
        <ChatHeader isMobile={isMobile}/>
        {
          loading?<div className="chat-loader"><ThreeDots height="80" width="80" color="white"/></div>:<MessageList token={token} stompClient={stompClient} onEdit={onEdit}/>
        }
        
        <MessageInput stompClient={stompClient} editingMessage={editingMessage} token={token} onEdit={onEdit} />
      
    </div>
  )
}

export default ChatContainer
