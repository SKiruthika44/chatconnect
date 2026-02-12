import React from 'react'
import { useSelector } from 'react-redux'
import './css/MessageInput.css'
import { useState } from 'react'
const MessageInput = ({stompClient}) => {
    const selectedChat=useSelector((state)=>state.chat.selectedChat);
    const [privateInput,setPrivateInput]=useState("");

    function sendMessage(){

    if(stompClient && selectedChat){

      if(selectedChat.type=="group"){
        stompClient.publish({
          destination:"/app/group",
          body:JSON.stringify({
            content:privateInput,
            groupName:selectedChat.data.groupName


          })
        })

      }
      else{

        stompClient.publish({
          destination:"/app/private",
          body:JSON.stringify({
            content:privateInput,
            receiver:selectedChat.data.username
          })
        })

      }
      setPrivateInput("");
    }
  }
  
  return (
    <div className='input-area'>
       <input type="text" value={privateInput} placeholder='Type a message' onChange={(e)=>setPrivateInput(e.target.value)} />
        <button onClick={sendMessage}>Send</button>
    </div>
  )
}

export default MessageInput
