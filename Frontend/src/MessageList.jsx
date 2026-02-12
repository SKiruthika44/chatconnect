import React from 'react'
import { useSelector } from 'react-redux'
import MessageItem from './MessageItem';
import './css/MessageList.css'
const MessageList = ({token}) => {
    const messages=useSelector((state)=>state.message.messages);
  return (
    <div className='message-list'>
         {messages && messages.map((msg,index)=>(
        <MessageItem
                msg={msg}
                key={index}
                token={token}
                
                
        /> 

      ))}
      
    </div>
  )
}

export default MessageList
