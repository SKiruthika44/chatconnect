import React from 'react'
import { useSelector } from 'react-redux'
import { useEffect } from 'react';
import MessageItem from './MessageItem';
import './css/MessageList.css'
const MessageList = ({token,stompClient,onEdit}) => {
    const messages=useSelector((state)=>state.message.messages);
    useEffect(()=>{
      console.log(messages);
    },[messages]);
  return (
    <div className='message-list'>
         {messages && messages.map((msg,index)=>(
        <MessageItem
                msg={msg}
                key={index}
                token={token}
                stompClient={stompClient}
                onEdit={onEdit}
                
                
        /> 

      ))}
      
    </div>
  )
}

export default MessageList
