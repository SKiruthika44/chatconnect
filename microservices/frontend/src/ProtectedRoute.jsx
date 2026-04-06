import React from 'react'
import { isTokenValid } from './assets/authUtils';
import {Navigate} from 'react-router-dom';
const ProtectedRoute = ({children}) => {
  const token=localStorage.getItem("token");
    if(!token || !isTokenValid(token)){
        return <Navigate to='/login'/>
    }
    console.log("token");
    return children;
  
}

export default ProtectedRoute