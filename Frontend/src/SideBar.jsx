import React from 'react'
import SidebarHeader from './SidebarHeader'

import SidebarFooter from './SidebarFooter'
import UserGroupList from './UserGroupList'
import './css/SideBar.css'
import SearchBar from './SearchBar'
const SideBar = ({token,setShowEditForm,setGroupCreationForm}) => {
  return (
    <div className='sidebar-container'>
      <SidebarHeader setGroupCreationForm={setGroupCreationForm}/>
      <SearchBar token={token}/>
      <UserGroupList/>
      <SidebarFooter token={token} setShowEditForm={setShowEditForm}/>
      
    </div>
  )
}

export default SideBar
