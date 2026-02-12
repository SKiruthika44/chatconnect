import React from 'react'
import ChatHeader from './ChatHeader'
import MessageList from './MessageList'
import MessageInput from './MessageInput'
import './css/Chatcontainer.css'
const ChatContainer = ({stompClient,token}) => {
  return (
    <div className='chat-container'>
        <ChatHeader/>
        <MessageList token={token} />
        <MessageInput stompClient={stompClient}/>
      
    </div>
  )
}

export default ChatContainer
