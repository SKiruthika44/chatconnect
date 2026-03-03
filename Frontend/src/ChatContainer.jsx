import React, { useState } from 'react'
import ChatHeader from './ChatHeader'
import MessageList from './MessageList'
import MessageInput from './MessageInput'

import './css/Chatcontainer.css'
const ChatContainer = ({stompClient,token}) => {
  const [editingMessage,setEditingMessage]=useState(null);
  const onEdit=(msg)=>{
    setEditingMessage(msg);
    console.log("editng..",editingMessage);
  }
  return (
    <div className='chat-container'>
        <ChatHeader/>
        <MessageList token={token} stompClient={stompClient} onEdit={onEdit}/>
        <MessageInput stompClient={stompClient} editingMessage={editingMessage} token={token} onEdit={onEdit} />
      
    </div>
  )
}

export default ChatContainer
