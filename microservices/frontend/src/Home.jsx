import React from 'react'
import { useNavigate } from 'react-router-dom'
import './Home.css'
const Home = () => {
    const navigate=useNavigate();
  return (
    <div className='home-container'>
        <div className="header">
            <div className="title">ChatConnect</div>
            <div className="btns">
                <button onClick={() => navigate("/signup")} className="signup-btn">Sign Up</button>
                <button onClick={() => navigate("/login")} className="login-btn">Login</button>
            </div>
        </div>
        <div className="content">
           <h1>ChatConnect</h1>
           <p>“Fast, secure and real-time messaging for everyone.”</p>
           <div className="features">
                <div className="feature-item">💬 Real-time Messaging</div>
                <div className="feature-item">🔒 Secure Communication</div>
                <div className="feature-item">⚡ Fast & Lightweight</div>
                <div className="feature-item">🌍 Use it anytime,anywhere</div>
                <div className="feature-item">🚀 Just Signin and start chatting</div>

            </div>

        </div>
       
      
    </div>
  )
}

export default Home
