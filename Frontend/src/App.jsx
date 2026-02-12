import React from 'react'
import SignUp from './SignUp.jsx'
import Login from './Login.jsx'

import Chat from './Chat.jsx'
import ProtectedRoute from './ProtectedRoute.jsx'
import {Routes,Route} from 'react-router-dom';
import Home from './Home.jsx'

const App = () => {
  return (
    <div>
      <Routes>
       <Route path="/" element={<Home/>}/>
       <Route path="/signup" element={<SignUp/>}/>
       <Route path="/login" element={<Login/>}/>
       
       <Route path="/chat" element ={<ProtectedRoute children={<Chat/>}></ProtectedRoute>}/>
      </Routes>


      
    </div>
  )
}

export default App
