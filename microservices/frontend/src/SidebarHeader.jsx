import React from 'react'
import './css/SideBarHeader.css'
const SidebarHeader = ({setGroupCreationForm}) => {
  return (
    <div className='sidebar-header'>
        <h4>ChatConnect</h4>
        <button className='newgroup-btn' onClick={()=>setGroupCreationForm(true)}>New Group</button>
    </div>
  )
}

export default SidebarHeader
