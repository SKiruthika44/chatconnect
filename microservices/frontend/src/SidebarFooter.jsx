import React from 'react'
import { useSelector } from 'react-redux'
import {Pencil,LogOut} from "lucide-react"
import {useNavigate} from "react-router-dom";
import './css/SideBarFooter.css'
import Avatar from './Avatar';
const SidebarFooter = ({setShowEditForm}) => {
    const loggedInUser=useSelector((state)=>state.user.loggedInUser);
    const navigate=useNavigate();
    const handleLogout=()=>{
        localStorage.removeItem("token");
        console.log("logging out");
        navigate("/login");
    }
  return (
    <div className='sidebar-footer'>
        {loggedInUser && <Avatar user={loggedInUser} />}

        <div className="user-info">
            <h4>{loggedInUser ? loggedInUser.username:""}</h4>
        </div>

        <div className="edit-icons">
                <Pencil className="icon edit-icon" onClick={() => setShowEditForm(true)}/>
                <LogOut className="icon logout-icon" onClick={handleLogout} />
        </div>
      
    </div>
  )
}

export default SidebarFooter
