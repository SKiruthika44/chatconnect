import React from 'react'
import { useSelector } from 'react-redux'
import { useDispatch } from 'react-redux';
import { setLoggedInUser, updateLoggedInUserProfile } from './Slice/UserSlice';
import { useState,useEffect } from 'react';
import './css/EditForm.css'
import axios from 'axios';
const EditForm = ({token,setShowEditForm}) => {
    const dispatch=useDispatch();
    const loggedInUser=useSelector((state)=>state.user.loggedInUser);
    const [language,setLanguage]=useState("");
    const [imageUploadSuccess,setImageUploadSuccess]=useState(false);

    const updateLanguage=async()=>{
    console.log("called function");
    console.log(language);
    try{
        const resp=await axios.get(`http://localhost:8080/change-lang/${language}`,{
            headers:{
                Authorization:`Bearer ${token}`
            }
        })
        dispatch(setLoggedInUser(resp.data));
        
        console.log(loggedInUser);
        console.log(resp.data);
        

    }
    catch(error){
        console.log(error);
    }

  }

  useEffect(()=>{
    if(language){
      updateLanguage();
    }
  },[language]);

  const callImageUploadAPI=async(file)=>{
      if(!file){
       
        return;
      }
      try{
        const formdata=new FormData();
      
        formdata.append("image",file);
        const res=await axios.post(`http://localhost:8080/upload-image`,formdata,{
            headers:{
              Authorization:`Bearer ${token}`,
              "Content-Type":"multipart/form-data",
          
          }
        })
      //console.log(res.data);
      dispatch(updateLoggedInUserProfile(res.data));
      /*setLoggedInUser((prevUser)=>{
      return {
        ...prevUser,
        profileImage:res.data
      }});*/
      
      setImageUploadSuccess(true);
  
      setTimeout(() => {
          // close after 2 sec
        setImageUploadSuccess(false);
        setShowEditForm(false);
      }, 2000);
      
      }
      catch(error){
        console.log(error);
      }
    }

    function UploadProfileImage(e){
  
      console.log(e.target.files[0]);
      const file=e.target.files[0];
      
      callImageUploadAPI(file);
  
    
    //console.log(showFileInput);
  
  
    }

  return (
    <div className='editform-backdrop'>
        <div className="editform">
             <div className="select-language">
              <h4>Choose PreferredLanguage</h4>
               <select value={language} onChange={(e) => setLanguage(e.target.value)}>
        <option value="">Select language</option>
        <option value="English">English</option>
        <option value="Tamil">Tamil</option>
        <option value="Hindi">Hindi</option>
        <option value="Malayalam">Malayalam</option>
        <option value="Kannada">Kannada</option>
      </select>
             </div>
             <div className='select-profile'>
                <h4>Choose file to upload</h4>
                <input type="file" onChange={UploadProfileImage}></input>
                {imageUploadSuccess && <h5>profile uploaded</h5>}
                <button onClick={()=>setShowEditForm(false)}>Close</button>
             </div>
        </div>
        
      
    </div>
  )
}

export default EditForm
