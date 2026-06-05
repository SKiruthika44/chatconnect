import React, { useEffect } from 'react'
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
  const groupLoading=useSelector((state)=>state.group.groupLoading);
  const usersLoading=useSelector((state)=>state.user.usersLoading);
  const loggedInUserLoading=useSelector((state)=>state.user.loggedInUserLoading);

  useEffect(()=>{
    console.log(visibleUsers);
  },[visibleUsers]);
  return (
    
    <div className='user-group-list'>
      {
        (groupLoading || usersLoading || loggedInUserLoading) ? (
          <div className="chat-loading">
            <p>Loading your chats and groups....</p>
            <p>Please wait while we connect to the server.</p>
          </div>
        ):(visibleUsers.length==0 && visibleGroups.length==0 ? (
          <div className="empty-chat-list">
               <p>No chats yet.</p>
              <p>Search users to start chatting.</p>
          </div>
        ):(
          <div className="list">
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
      )
      }
      
      
      
    </div>
  )
}

export default UserGroupList
