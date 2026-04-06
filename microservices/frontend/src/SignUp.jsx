import React, { useState } from 'react'
import axios from 'axios'
import './SignUp.css'
import {useNavigate} from 'react-router-dom'




const SignUp = () => {

  const [password,setPassword]=useState("");
  const [username,setUsername]=useState("");
  const [confirmPassword,setConfirmPassword]=useState("");
  const [error,setError]=useState(false);
  const [errormsg,setErrorMsg]=useState("");
  const [msg,setMsg]=useState("");
  const navigate=useNavigate();
  const handleSubmit=async(e)=>{
    setMsg("");
    setError(false);
    setErrorMsg("");
    e.preventDefault();
    if(username==""){
       setError(true);
      setErrorMsg("Username shouldn't be empty");
      return;
       
    }
    if(password==""){
      setError(true);
      setErrorMsg("Passwords shouldnt be empty");
      return;


    }
    if(confirmPassword==""){
      setError(true);
      setErrorMsg("Confirm password shouldnt be empty");
      return;
    }
    if(password!=confirmPassword){
      setError(true);
      setErrorMsg("Passwords donot match");
      return;

    }
    const payload={
      username:username,
      password:password
    }
    try{
      const res=await axios.post("http://localhost:8080/register",payload);
      console.log("account created");
      setMsg("Account created successfully");
      setTimeout(()=>{
        navigate("/login");
      },[2000]);
      //navigate("/login");
    }
    catch(error){   

      if(error.response && error.response.data){
        console.log(error.response.data); 
        setError(true);
      setErrorMsg("Username already exists");

      }
      else{
        setError(true);
      setErrorMsg("Something went wrong");
        
      }
      
    }

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       
  }
  
  return (
          <div className="signup-background">
            
            <div className='signup-container'>
                  <h3>Username</h3>
            
            <input type="text" value={username} onChange={(e)=>setUsername(e.target.value)}></input>
            <h3>Password</h3>
            <input type="password" value={password} onChange={(e)=>setPassword(e.target.value)}></input>
            <h3>Confirm Password</h3>
            <input type="password" value={confirmPassword} onChange={(e)=>setConfirmPassword(e.target.value)}></input>
            {error && <p style={{ color: "red" ,textAlign:"center" }}>{errormsg}</p>}
            {msg && <p style={{ color: "green", textAlign:"center" }}>{msg}</p>}
            <button onClick={handleSubmit}>Sign up</button>
            <p className='login-link'>
              Already have an account? <a href='/login'>Login</a>
            </p>
           
              </div>

            
          </div>

    



  )
}

export default SignUp;