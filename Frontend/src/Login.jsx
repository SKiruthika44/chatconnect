import React from 'react'
import './Login.css';
import {useState,useEffect} from 'react'
import {isTokenValid} from './assets/authUtils';
import {useNavigate} from 'react-router-dom';
import axios from 'axios';

const Login = () => {

  const [username,setUsername]=useState("");
  const [password,setPassword]=useState("");
  const [successMsg,setSuccessMsg]=useState(false);
  const [errormsg,setErrormsg]=useState("");

  const navigate=useNavigate();
  useEffect(()=>{
    const token=localStorage.getItem("token");
    if(token && isTokenValid(token)){
      navigate("/chat");
    }
  },[]);

  const handleSubmit=async(e)=>{
    e.preventDefault();
    setErrormsg("");
    setSuccessMsg("");
    const payload={
        username:username,
        password:password
    }
    try{
        const res=await axios.post("http://localhost:8080/log-in",payload);
        console.log("Logged in successfully");
        console.log(res);
        setSuccessMsg("logging in....");
        localStorage.setItem("token",res.data);
        
        navigate("/chat");
        



    }catch(error){
        if(error.response && error.response.data){
            setErrormsg(error.response.data);
        }
        else{
          setErrormsg("Something went wrong");
        }
        
        
    }
  }
  return (
    <div className="login-container">
            <h3>Username</h3>
            
            <input type="text" value={username} onChange={(e)=>setUsername(e.target.value)}></input>
            <h3>Password</h3>
            <input type="password" value={password} onChange={(e)=>setPassword(e.target.value)}></input>
            {successMsg && <p style={{color:"green",textAlign:"center"}}>{successMsg}</p>}
            {errormsg && <p style={{ color: "red",textAlign:"center"}}>{errormsg}</p>}
            <button onClick={handleSubmit}>Login</button>
            <p className='signup-link'>
              Doesn't have an account? <a href='/signup'>Signup</a>
            </p>
            
            
          </div>
  )
}

export default Login