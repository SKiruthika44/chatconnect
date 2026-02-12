import React from 'react'
import { useSelector } from 'react-redux'
import { Check,CheckCheck } from 'lucide-react';
import { useDispatch } from 'react-redux';
import { updateMessageContent } from './Slice/MessageSlice';
import './css/MessageItem.css'
import axios from 'axios';
import { getAllMessagesBetween } from './app/services/MessageService';
const MessageItem = ({msg,token}) => {
  const selectedChat=useSelector((state)=>state.chat.selectedChat);
  const loggedInUser=useSelector((state)=>state.user.loggedInUser);

   const isSender=(msg.senderName==loggedInUser.username);
   const isGroupMessage=(selectedChat.type=="group");
    const dispatch=useDispatch();
   const handleTranslation=async(msgId,type)=>{
      try{
        const response=await axios.get("http://localhost:8080/translate",{
          params:{
            msgId:msgId,
            type:type
          },
            headers:{
              Authorization:`Bearer ${token}`
            }
          
        });
        console.log(response);

        dispatch(updateMessageContent({
            id:msgId,
            content:response.data

        }));
        /*setMessages((prevMessages)=>{
          return prevMessages.map((message)=>message.id==msgId?{...message,content:response.data}:message)
        })*/
       
      }
       catch(error){
        console.log(error);
      }
      
    }

    const handleDelete=async(scope)=>{
      try{
        const type=isGroupMessage==true?"group":"direct";
        const response=await axios.delete(`http://localhost:8080/delete`,{
          params:{
            msgId:msg.id,
            type:type,
            scope:scope
          },
            headers:{
              Authorization:`Bearer ${token}`
            }
          
        }) 
        console.log(response);
        getAllMessagesBetween(token,dispatch);
      }
      catch(error){
        console.log("error",error);
      }
    }


  return (
    <div className={`message-item ${isSender ? "sender" : "receiver"}`}>
        <div className="message-bubble">
        {isGroupMessage && !isSender &&  <div className='sender-name'>{msg.senderName}</div>}

        
        <div className="msg-and-ticks">
          
          <div className="message-content">{msg.content}</div>

          {isSender && (
            <span className="ticks">
              {msg.status === "READ" ? (
                <CheckCheck className="tick-read" size={16} />
              ) : msg.status === "DELIVERED" ? (
                <CheckCheck className="tick-delivered" size={16} />
              ) : (
                <Check className="tick-sent" size={16} />
              )}
            </span>
          )}
        </div>

        <span className="time">
          {new Date(msg.createdAt).toLocaleString()}
        </span>
        <div className='translate-btn' onClick={()=>{handleTranslation(msg.id,isGroupMessage==true?"group":"direct")}}>🌐 Translate</div>
        
       
        {
                isSender==true? (
                  <div className="dd">
                    <button onClick={()=>handleDelete("me")}>Delete for me</button>
                    <button onClick={()=>handleDelete("everyone")}>Delete for everyone</button>
                    <button>Cancel</button>
                  </div>
                ):(
                  <div className="ddee">
                    <button onClick={()=>handleDelete("me")}>Delete</button>
                    <button>Cancel</button>
                  </div>
                )
              }
       
        
       

      </div>
      
    </div>
  )
}

export default MessageItem
