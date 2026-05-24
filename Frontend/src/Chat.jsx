import React, { useEffect, useState ,useRef} from 'react'
import SockJS from "sockjs-client";
import {Client} from "@stomp/stompjs";
import {useDispatch, useSelector } from "react-redux"; 
import SideBar from './SideBar';
import GroupCreationForm from './GroupCreationForm';
import EditForm from './EditForm';
import ChatContainer from './ChatContainer';
import { ToastContainer } from 'react-toastify';
import './css/Chat.css'
import { getAllMessagesForSelectedGroup } from './app/services/GroupService';
import { getLoggedInUser,getAllUsers } from './app/services/UserService';
import { getAllMessagesBetween } from './app/services/MessageService';
import { updateDbGroupChatUnreadCount,updateUiGroupChatUnreadCount,updateDbPrivateChatUnreadCount,updateUiPrivateChatUnreadCount } from './app/services/CountService';
import { getUnReadCountForGroupChat,getUnReadCountForPrivateChat } from './app/services/CountService';
import { subscribeOnlineUsers,subscribePrivateOnlineUsers,subscribeLastSeen,subscribeGroupMessage,subscribePrivateMessage,  subscribeToNotifyDeleteForMe, subscribeToNotifyDeleteForEveryone, subscribeToNotifyPrivateMessageEmojiCreated, subscribeToEditPrivateChat } from './app/services/SubscriptionService';
import SendersList from './SendersList';
import { setMessages } from './Slice/MessageSlice';
import { sendMessage } from './app/services/MessageService';
import { setLoading,setForwarding,setForwardingData} from './Slice/ChatSlice';
import axios from 'axios';
const Chat = () => {
    const token=localStorage.getItem("token");
    const [stompClient,setStompClient]=useState(null);
    const dispatch=useDispatch();
    const subscribedGroupRef=useRef(new Set());
    const selectedChat=useSelector((state)=>state.chat.selectedChat);
    const [groupCreationForm,setGroupCreationForm]=useState(false);
    const [showEditForm,setShowEditForm]=useState(false);
    const forwarding=useSelector((state)=>state.chat.forwarding);
    const forwardingData=useSelector((state)=>state.chat.forwardingData);
    const loading=useSelector((state)=>state.chat.loading);
    const isMobile=window.innerWidth<=768;
    useEffect(()=>{
        let client=null;
        const initializeWebsocket=async()=>{
             try{
                await axios.get("https://chatconnect-8iix.onrender.com/ping");
                console.log("Backend awake");
                const socket=new SockJS("https://chatconnect-8iix.onrender.com/ws");
                client=new Client({
                        webSocketFactory:()=>socket,
                        reconnectDelay:5000
                })
                client.connectHeaders={
                        Authorization:`Bearer ${token}`,
                 };
                client.onConnect=(frame)=>{
                        subscribedGroupRef.current.clear();
            
                        subscribeOnlineUsers(client,dispatch);
                        subscribePrivateOnlineUsers(client,dispatch);
                        subscribeLastSeen(client,dispatch);
                        subscribeGroupMessage(client,dispatch,subscribedGroupRef,token);
                        subscribePrivateMessage(client,dispatch,token);
                        subscribeToNotifyDeleteForEveryone(client,dispatch);
                        subscribeToNotifyDeleteForMe(client,dispatch);
                        subscribeToEditPrivateChat(client,dispatch);
                         subscribeToNotifyPrivateMessageEmojiCreated(client,dispatch);
                client.publish({
                    destination:"/app/ready",
                    body:"{}"
                });

        }
        client.onStompError=(frame)=>{
            console.log(frame);
        }
        client.activate();
        setStompClient(client);


            }catch(error){
             console.log("Backend still waking up", error);

            
            setTimeout(() => {
                initializeWebsocket();
            }, 3000);
        }
        }

        initializeWebsocket();
        return () => {

        console.log("closing websocket connection");

        if (client) {
            client.deactivate();
        }
    };

    },[token]);
  

    useEffect(()=>{
        getLoggedInUser(token,dispatch);

        getAllUsers(token,dispatch);
        getUnReadCountForPrivateChat(token,dispatch);
        getUnReadCountForGroupChat(token,dispatch);

    },[token]);

    useEffect(()=>{

        if(selectedChat){
            dispatch(setLoading(true));
            dispatch(setMessages([]));
            if(selectedChat.type=="direct"){
                
                getAllMessagesBetween(token,dispatch);
            
                updateDbPrivateChatUnreadCount(token);
                updateUiPrivateChatUnreadCount(dispatch);
            }
        else{

                getAllMessagesForSelectedGroup(token,dispatch);
                updateDbGroupChatUnreadCount(token);
                updateUiGroupChatUnreadCount(dispatch);
            
            }
        }

    },[selectedChat]);

   

    useEffect(()=>{
        
    },[subscribedGroupRef])

    useEffect(()=>{
      console.log("inside loading useeffect");
      console.log(loading);
      if(!loading && forwarding && forwardingData){
        console.log("forwarding msg");
        sendMessage(stompClient,forwardingData.type,forwardingData.content,forwardingData.data);
        dispatch(setForwarding(false));
        dispatch(setForwardingData(null));
      } 
    },[loading]);
    useEffect(()=>{
        console.log("forwarding",forwarding);
    },[forwarding]);
    useEffect(()=>{
        console.log("forwardingData",forwardingData);
    },[forwardingData]);
  return (
    <div className="container">
        {
            (!isMobile || selectedChat) && selectedChat && 
            <ChatContainer stompClient={stompClient} token={token} isMobile={isMobile}/>
        }

        {
            (!isMobile || !selectedChat) && 
            <SideBar token={token} setGroupCreationForm={setGroupCreationForm} setShowEditForm={setShowEditForm}/>
        }
        
       

        {
            groupCreationForm && <GroupCreationForm token={token} setGroupCreationForm={setGroupCreationForm}/>
        }
        {
            showEditForm &&  <EditForm token={token} setShowEditForm={setShowEditForm}/>
        }
        <ToastContainer/>
        
      
    </div>
  )
}

export default Chat
