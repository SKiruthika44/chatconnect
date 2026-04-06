import React from 'react'
import { useSelector } from 'react-redux'
import { useDispatch } from 'react-redux';
import { setSelectedChatAsGroup } from './Slice/ChatSlice';
import GroupProfile from './assets/GroupProfile.png';
import './css/GroupItem.css'
const GroupItem = ({group}) => {
    const selectedChat=useSelector((state)=>state.chat.selectedChat);
    const unReadCountsForGroup=useSelector((state)=>state.count.unReadCountForGroups);
    const dispatch=useDispatch();
    const handleSelectedChat=(group)=>{
        dispatch(setSelectedChatAsGroup(group));
    }
  return (
    <div className={`group-item ${selectedChat && selectedChat.type=="group" && selectedChat.data.groupName==group.groupName?"selected" : ""}`} onClick={()=>handleSelectedChat(group)}>
        <img src={GroupProfile} />
       <h4>{group.groupName}</h4>
       <div className="unread">
                        {unReadCountsForGroup[group.groupName] > 0 && (
                          <span className="unread-badge">{unReadCountsForGroup[group.groupName]}</span>
                        )}
                      </div>
      
    </div>
  )
}

export default GroupItem
