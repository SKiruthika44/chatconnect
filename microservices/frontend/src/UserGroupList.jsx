import React from 'react'
import Store from './app/Store'
import UserItem from './UserItem';
import GroupItem from './GroupItem';
import { useSelector } from 'react-redux';
import './css/UserGroupList.css'
const UserGroupList = () => {
  const allUsers=useSelector((state)=>state.user.allUsers);
  const allGroups=useSelector((state)=>state.group.allGroups);
  const visibleUsers=useSelector((state)=>state.user.visibleUsers);
  const visibleGroups=useSelector((state)=>state.group.visibleGroups);

  return (
    <div className='user-group-list'>
        <h4>Personal Chats</h4>
        <div className="user-list">
                {visibleUsers.map((user) => (
                    <UserItem key={user.username} user={user} />
              
                ))}
        </div>

        <h4>Group Chats</h4>
        <div className="group-list">
              {
                visibleGroups.map((group)=>(
                <GroupItem key={group.id} group={group}/>
              ))
              }
        </div>
      
    </div>
  )
}

export default UserGroupList
