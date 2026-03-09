import React from 'react'
import { useSelector } from 'react-redux'
import { Check,CheckCheck ,Edit,Trash,Forward,CornerUpRight, PlusCircle, Smile} from 'lucide-react';
import { useDispatch } from 'react-redux';
import { updateMessageContent } from './Slice/MessageSlice';
import { useState } from 'react';
import './css/MessageItem.css'
import axios from 'axios';
import { getAllMessagesBetween } from './app/services/MessageService';
import { getAllMessagesForSelectedGroup } from './app/services/GroupService';
import DeleteForm from './DeleteForm';
import SendersList from './SendersList';
const MessageItem = ({msg,token,stompClient,onEdit}) => {
  const selectedChat=useSelector((state)=>state.chat.selectedChat);
  const loggedInUser=useSelector((state)=>state.user.loggedInUser);
  const [showDeleteForm,setShowDeleteForm]=useState(false);
  const [showSenderList,setShowSenderList]=useState(false);
  const [showEmojis,setShowEmojis]=useState(false);
 
  const reactionEmojis=["👍", "❤️", "😂", "🔥", "😮", "😢"];
   const isSender=(msg.senderName==loggedInUser.username);
   const isGroupMessage=(selectedChat.type=="group");
    const dispatch=useDispatch();
   const handleTranslation=async(msgId)=>{
      try{
        const response=await axios.get("http://localhost:8080/translate",{
          params:{
            msgId:msgId
            
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
        
       
      }
       catch(error){
        console.log(error);
      }
      
    }

    const handleEmojiApi=async(emoji)=>{
      try{
        const response=await axios.put(`http://localhost:8080/message/emoji`,{},{
          params:{
            msgId:msg.id,
          
          emoji:emoji
          },

        
          headers:{

            Authorization:`Bearer ${token}`

          }
        }
        );
        
        setShowEmojis(false);


      }
      catch(error){
        console.log(error.response);
      }
    }

    const handleDelete=async(scope)=>{
      try{
        //const type=isGroupMessage==true?"group":"direct";
        const response=await axios.delete(`http://localhost:8080/delete`,{
          params:{
            msgId:msg.id,
            
            scope:scope
          },
            headers:{
              Authorization:`Bearer ${token}`
            }
          
        }) 
        console.log(response);
        setShowDeleteForm(false);
       
        
      }
      catch(error){
        console.log("error",error);
      }
    }

    const handleForward=()=>{
      console.log(msg.content);
      
      setShowSenderList(true);
    }

    const closeForward=()=>{
      
      setShowSenderList(false);
    }

  return (
    <div className={`message-item ${isSender ? "sender" : "receiver"}`}>
        {
          msg.deletedForEveryone ? <div className="deleted-bubble">
            <div className="deleted-text">This message was deleted</div>
            <span className="time">
                {new Date(msg.createdAt).toLocaleString()}
            </span>
          </div> :
        
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
        <div className="message-actions">
          <div className='translate-btn' onClick={()=>{handleTranslation(msg.id,isGroupMessage==true?"group":"direct")}}>🌐 Translate</div>
          <div className="delete-icon" onClick={() => setShowDeleteForm(true)}>
            <Trash size={16} />
          </div>
          <div className="forward">
           
            <CornerUpRight size={16} onClick={handleForward}/>
          </div>
          {
            isSender &&  <div className="edit-button">
            <Edit size={16}onClick={()=>onEdit({
              msg:msg,
              type:isGroupMessage?"group":"private"
            })}/>
          </div>
          }
         
          <div className="emoji-reaction">
            <Smile size={16} onClick={()=>setShowEmojis(true)}/>
            
          </div>
          
          
          {
            isGroupMessage?(
            msg.emojisCount &&
  Object.entries(msg.emojisCount).map(([emoji, count]) => (
    <span key={emoji} className='chat-emoji'>
      {emoji} {count}
    </span>
))
            ):(
              
            msg.emojis?.map((emoji,index)=><span key={index} className='chat-emoji'>{emoji}</span>)
          

        )
          }
          

        </div>
        

       
        
       
        
       

      </div>
      }
      {
        showDeleteForm && <DeleteForm isSender={isSender} handleDelete={handleDelete} setShowDeleteForm={setShowDeleteForm}></DeleteForm>
      }
      {
        showSenderList  && <SendersList stompClient={stompClient} content={msg.content} closeForward={closeForward}/>
      }
      {
        showEmojis && 
         <div className="emoji-picker">
         {
          reactionEmojis.map((emoji,index)=><span key={index} onClick={()=>handleEmojiApi(emoji)}>{emoji}</span>)
         }
         </div>

        
      }
      
    </div>
  )
}

export default MessageItem
