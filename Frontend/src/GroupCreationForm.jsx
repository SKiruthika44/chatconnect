import React from 'react'
import axios from "axios";
import { useSelector } from 'react-redux';
import './css/GroupCreationForm.css'
import { useState } from 'react';
const GroupCreationForm = ({token,setGroupCreationForm}) => {
    const [groupNameInput,setGroupNameInput]=useState("");
    const [groupMembersInput,setGroupMembersInput]=useState([]);
    const [successMsg,setSuccessMsg]=useState("");
    const allUsers=useSelector((state)=>state.user.allUsers);

     const handleGroupCreation=async()=>{
        const groupMembers={
          groupName:groupNameInput,
          groupMembers:groupMembersInput
        }
        try{
    
          const response=await axios.post(`http://localhost:8080/group/create-group`,groupMembers,{
            headers:{Authorization:`Bearer ${token}`}
          })
          console.log(response);
          setSuccessMsg(` ${response.data.groupName} Group has been successfully created....`);
          setTimeout(()=>{
            setGroupCreationForm(false);
          },2000);
          
        }
        catch(error){
            console.log(error);
          console.log("error creating the group");
        }
    
      }
      function addGroupMembers(e){
         const { checked, value } = e.target;
    
        setGroupMembersInput(prev =>
          checked
          ? [...prev, value]               // add user
          : prev.filter(member => member !== value)  // remove user
        );
    
      }
  return (
    <div className='modal-backdrop'>
        <div className="groupcreate-form">

      <label>Group Name:</label>
      <input
        type="text"
        value={groupNameInput}
        onChange={(e) => setGroupNameInput(e.target.value)}
      />

      <label>Add Users</label>

      <div className="user-checkboxes">
        {allUsers.map((user, i) => (
          <div key={i}>
            <input
              type="checkbox"
              value={user.username}
              checked={groupMembersInput.includes(user.username)}
              onChange={addGroupMembers}
            />
            <label>{user.username}</label>
          </div>
        ))}
      </div>

      
      <div className="button-row">
        <button onClick={handleGroupCreation}>Create Group</button>
        <button onClick={() => setGroupCreationForm(false)}>Close</button>
      </div>
      {
        successMsg && <div className="success-msg">
         <p>{successMsg}</p>
        
      </div>
      }

    </div>
      
    </div>
  )
}

export default GroupCreationForm
