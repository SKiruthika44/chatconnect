import React, { useEffect, useState ,useRef} from 'react'
import SockJS from "sockjs-client";
import {Client} from "@stomp/stompjs";
import {useDispatch, useSelector } from "react-redux"; 
import SideBar from './SideBar';
import GroupCreationForm from './GroupCreationForm';
import EditForm from './EditForm';
import ChatContainer from './ChatContainer';
import './css/Chat.css'
import { getAllMessagesForSelectedGroup } from './app/services/GroupService';
import { getLoggedInUser,getAllUsers } from './app/services/UserService';
import { getAllMessagesBetween } from './app/services/MessageService';
import { updateDbGroupChatUnreadCount,updateUiGroupChatUnreadCount,updateDbPrivateChatUnreadCount,updateUiPrivateChatUnreadCount } from './app/services/CountService';
import { getUnReadCountForGroupChat,getUnReadCountForPrivateChat } from './app/services/CountService';
import { subscribeOnlineUsers,subscribePrivateOnlineUsers,subscribeLastSeen,subscribeGroupMessage,subscribePrivateMessage,  subscribeToNotifyDeleteForMe, subscribeToNotifyDeleteForEveryone, subscribeToNotifyPrivateMessageEmojiCreated, subscribeToEditPrivateChat } from './app/services/SubscriptionService';
import SendersList from './SendersList';
const Chat = () => {
    const token=localStorage.getItem("token");
    const [stompClient,setStompClient]=useState(null);
    const dispatch=useDispatch();
    const subscribedGroupRef=useRef(new Set());
    const selectedChat=useSelector((state)=>state.chat.selectedChat);
    const [groupCreationForm,setGroupCreationForm]=useState(false);
    const [showEditForm,setShowEditForm]=useState(false);
    
    useEffect(()=>{
        const socket=new SockJS("http://localhost:8080/ws");
        const client=new Client({
            webSocketFactory:()=>socket,
            reconectDelay:5000
        })
        client.connectHeaders={
            Authorization:`Bearer ${token}`,
        };
        client.onConnect=(frame)=>{
            subscribedGroupRef.current.clear();
            console.log("subscribing everytime");
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
        return ()=>{
            console.log("closing the websocket connection");
            client.deactivate();
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
        console.log("subscribed group id");
        console.log(subscribedGroupRef);
    },[subscribedGroupRef])
    
  return (
    <div className="container">
       
        <SideBar token={token} setGroupCreationForm={setGroupCreationForm} setShowEditForm={setShowEditForm}/>
        { selectedChat && <ChatContainer stompClient={stompClient} token={token} />}
        {
            groupCreationForm && <GroupCreationForm token={token} setGroupCreationForm={setGroupCreationForm}/>
        }
        {
            showEditForm &&  <EditForm token={token} setShowEditForm={setShowEditForm}/>
        }
        
      
    </div>
  )
}

export default Chat
