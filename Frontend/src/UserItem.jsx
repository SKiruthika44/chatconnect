import React from 'react'
import { useSelector } from 'react-redux'
import { useDispatch } from 'react-redux';
import { setSelectedChatAsDirect } from './Slice/ChatSlice';
import './css/UserItem.css'
import Avatar from './Avatar';
const UserItem = ({user}) => {
  
    const dispatch=useDispatch();
    const selectedChat=useSelector((state)=>state.chat.selectedChat);
    const onlineUsers=useSelector((state)=>state.user.onlineUsers);
    const unReadCounts=useSelector((state)=>state.count.unReadCountForUsers);
    
    const handleSelectedChat=(user)=>{
        dispatch(setSelectedChatAsDirect(user));
    }
  return (
    <div className={`user-item ${selectedChat && selectedChat.type=="direct" && selectedChat.data.username==user.username ? "selected":""}`} onClick={()=>
       handleSelectedChat(user)}>
        
        <Avatar user={user}/>
        <div className="name-status">
            <h4>{user.username}</h4>
            {onlineUsers.includes(user.username) && <h6>Online</h6>}
        </div>
         <div className="unread">
                        {unReadCounts[user.username] > 0 && (
                          <span className="unread-badge">{unReadCounts[user.username]}</span>
                        )}
                      </div>
      
    </div>
  )
}

export default UserItem
