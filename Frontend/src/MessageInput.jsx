import React, { useEffect } from 'react'
import { useSelector } from 'react-redux'
import axios from 'axios'
import './css/MessageInput.css'
import { useState } from 'react'
import { sendMessage } from './app/services/MessageService'
const MessageInput = ({stompClient,editingMessage,token,onEdit}) => {
    const selectedChat=useSelector((state)=>state.chat.selectedChat);
    const [privateInput,setPrivateInput]=useState("");

    useEffect(()=>{
      if(editingMessage){
        setPrivateInput(editingMessage.msg.content);
      }
    },[editingMessage]);
    function sendMsg(){
      const type=selectedChat.type=="group"?"group":"private";
     let receiverName="";
     if(type=="group"){
      receiverName=selectedChat.data.groupName;

     }
     else{
      receiverName=selectedChat.data.username;
     }
     console.log("editing msg:",editingMessage);
     if(editingMessage){

      const editMessage=async()=>{
        const msgId=editingMessage.msg.id;
        const type=editingMessage.type;
        try{
          const resp=await axios.put(`http://localhost:8080/message/edit/${msgId}`,{},{
            params:{
               content:privateInput
            

            }
          
           
          ,
            headers:{
              Authorization:`Bearer ${token}`
            }
          }
          );
          
          console.log("edited");
          console.log(resp);
        }
        catch(error){
          console.log(error);
        }
      }
        editMessage();


     }
     else{
      sendMessage(stompClient,type,privateInput,receiverName);

     }

      
      setPrivateInput("");

      /*if(stompClient && selectedChat){

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
     
    }*/

    }

    


    /*if(stompClient && selectedChat){

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
     
    }*/
    
  
  
  return (
    <div className='input-area'>
       <input type="text" value={privateInput} placeholder='Type a message' onChange={(e)=>setPrivateInput(e.target.value)} />
        <button onClick={sendMsg}>Send</button>
    </div>
  )
}

export default MessageInput
