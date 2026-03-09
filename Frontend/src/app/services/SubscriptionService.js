import { setOnlineUsers, updateLastSeen } from "../../Slice/UserSlice";
import {setAllGroups, setVisibleGroups}from "../../Slice/GroupSlice";
import Store from '../Store'
import { addMessage, updateMessageStatus,updateMessageDeletion, removeMessage, updateGroupMessageEmoji, updatePrivateMessageEmoji, updateMessage, updateMessageContent } from "../../Slice/MessageSlice.js";
import { updateUnReadCountForGroup } from "../../Slice/CountSlice.js";
import { getUnReadCountForPrivateChat } from "./CountService.js";
import { addGroup } from "../../Slice/GroupSlice.js";
import { updateGroupMessageRead } from "../../api/GroupApi.js";
import axios from "axios";
export const subscribeOnlineUsers=(client,dispatch)=>{
    client.subscribe("/topic/online",(users)=>{
        const onlineUsers=JSON.parse(users.body);
        dispatch(setOnlineUsers(onlineUsers));
    })

}

export const subscribeToEditPrivateChat=(client,dispatch)=>{
    client.subscribe("/user/queue/message/edit",(msg)=>{
        const updatedMessage=JSON.parse(msg.body);
        console.log("uda",updateMessage);
        dispatch(updateMessage(updatedMessage));
    })
}

export const subscribePrivateOnlineUsers=(client,dispatch)=>{
    client.subscribe("/user/queue/online",(users)=>{
        const onlineUsers=JSON.parse(users.body);
        dispatch(setOnlineUsers(onlineUsers));
    })
}



export const subscribeToNotifyDeleteForEveryone=(client,dispatch)=>{
    client.subscribe("/user/queue/delete",(message)=>{
       
        const privateMessage=JSON.parse(message.body);
        console.log(privateMessage);
        const selectedChat=Store.getState().chat.selectedChat;
        if(selectedChat?.type=="direct"){
            if(privateMessage.receiverName==selectedChat.data.username){
                dispatch(updateMessageDeletion(privateMessage));
            }
            else if(privateMessage.senderName==selectedChat.data.username){
                dispatch(updateMessageDeletion(privateMessage));
            }
        }
    })
    
}

export const subscribeToNotifyGroupMessageEdited=(groupId,client,dispatch)=>{
    console.log("subscribed to groupmsg edited:",groupId);
    client.subscribe(`/topic/group-message/edit/${groupId}`,(msg)=>{
        const editedMessage=JSON.parse(msg.body);
        
        dispatch(updateMessageContent(editedMessage));
    })
}

export const subscribeToNotifyPrivateMessageEmojiCreated=(client,dispatch)=>{
    client.subscribe("/user/queue/private/emoji",(msg)=>{
        const privateMessage=JSON.parse(msg.body);
        const selectedChat=Store.getState().chat.selectedChat;
        if(selectedChat?.type=="direct"){
            const allMessages=Store.getState().message.messages;
          const message=allMessages.find((message)=>message.id==privateMessage.msgId);
            if(message){
                dispatch(updatePrivateMessageEmoji(privateMessage));
            }


        }
    });
}

export const subscribeToNotifyDeleteForMe=(client,dispatch)=>{
    client.subscribe("/user/queue/delete-for-me",(msgId)=>{
        console.log("msgid");
        const id=JSON.parse(msgId.body);
        dispatch(removeMessage(id));
    })
}



export const subscribeLastSeen=(client,dispatch)=>{
    client.subscribe("/topic/lastSeen",(user)=>{
        const updatedLastSeenUser=JSON.parse(user.body);
        dispatch(updateLastSeen(updatedLastSeenUser));
    })
}

export const subscribeGroupMessage=(client,dispatch,subscribedGroupRef,token)=>{
   
    client.subscribe("/user/queue/groupInfo",(group)=>{
         console.log("subscription called");
        const groups=JSON.parse(group.body);
        console.log("group from subscription");
        console.log(groups);
        if(Array.isArray(groups)){
            dispatch(setAllGroups(groups));
            dispatch(setVisibleGroups(groups));
        }
        else{
            dispatch(addGroup(groups));
        }
        const updatedGroups=Store.getState().group.allGroups;
        updatedGroups.forEach((group)=>{
            if(!subscribedGroupRef.current.has(group.id)){
                subscribedGroupRef.current.add(group.id);
                subscribeToGroup(group.id,client,dispatch,token);
                subscribeToPrivateGroup(group.id,client,dispatch);
                subscribeToNotifyIfGroupMessageDeletedForEveryone(group.id,client,dispatch);
                subscribeToNotifyIfGroupMessageEmojiCreated(group.id,client,dispatch);
                subscribeToNotifyGroupMessageEdited(group.id,client,dispatch);
            }
        })
    })
}
const subscribeToNotifyIfGroupMessageEmojiCreated=(groupId,client,dispatch)=>{
    console.log("rrrr");
    client.subscribe(`/topic/group/emoji/${groupId}`,(msg)=>{
        const updatedMessage=JSON.parse(msg.body);
        console.log("message from event:",updatedMessage);
        const selectedChat=Store.getState().chat.selectedChat;
        if(selectedChat?.type=="group"){
            const allMessages=Store.getState().message.messages;
            const exists=allMessages.find((message)=>message.id==updatedMessage.msgId);
            if(exists){
                dispatch(updateGroupMessageEmoji(updatedMessage));
                console.log("updated message:"+allMessages);
            }

        }
    })
}

const subscribeToGroup=(groupId,client,dispatch,token)=>{
    console.log("subscribing groupid");
    console.log(groupId);
    client.subscribe(`/topic/group/${groupId}`,(msg)=>{
        const groupMessage=JSON.parse(msg.body);
        const selectedChat=Store.getState().chat.selectedChat;
       
       
        if(selectedChat?.type=="group"){
            const group=selectedChat.data;
            if(group.groupName==groupMessage.groupName){
                dispatch(addMessage(groupMessage));
                const loggedInUser=Store.getState().user.loggedInUser;
                if(loggedInUser.username!=(groupMessage.senderName)){
                    setTimeout(()=>updateGroupMessageRead(token,groupMessage.id),200);

                }
                
            }
            else{
                //if it is an another group
                dispatch(updateUnReadCountForGroup(groupMessage));

            }

        }
        else{
             dispatch(updateUnReadCountForGroup(groupMessage));


        }
    })

}
const subscribeToNotifyIfGroupMessageDeletedForEveryone=(groupId,client,dispatch)=>{
    client.subscribe(`/topic/group/delete/${groupId}`,(groupMsg)=>{
        
        const updatedMessage=JSON.parse(groupMsg.body);
        
        dispatch(updateMessageDeletion(updatedMessage));

    })
}

const subscribeToPrivateGroup=(groupId,client,dispatch)=>{
    client.subscribe(`/user/queue/group/${groupId}`,(msg)=>{
        const message=JSON.parse(msg.body);
        const selectedChat=Store.getState().chat.selectedChat;
        
        if(selectedChat?.type=="group"){
            const group=selectedChat.data;
            if(group.groupName==message.groupName){
                dispatch(updateMessageStatus(message));
            }
        }
    })

}

export const subscribePrivateMessage=(client,dispatch,token)=>{
    client.subscribe("/user/queue/private",(msg)=>{
        const message=JSON.parse(msg.body);
        
        const senderusername = message.senderName;
        const receiverusername = message.receiverName;
        const selectedChat=Store.getState().chat.selectedChat;
        if(selectedChat?.type=="direct"){
            const currChat=selectedChat.data;
            if(currChat.username==senderusername || currChat.username==receiverusername){
                 if(currChat.username==receiverusername){
                const currMessages=Store.getState().message.messages;
                const exists=currMessages.find((m)=>m.id==message.id);
                if(exists){
                    dispatch(updateMessageStatus(message));
                }
                else{
                    dispatch(addMessage(message));
                }
            }
            else{
                dispatch(addMessage(message));
                updatePrivateMessageRead(message.id,token);

            }
            }
            else{
                getUnReadCountForPrivateChat(token,dispatch);
            }
        }
        else{
            getUnReadCountForPrivateChat(token,dispatch);
        }

    })
}

const updatePrivateMessageRead=async(msgId,token)=>{
    try {
            await axios.put(
              `http://localhost:8080/updateMessageRead/${msgId}`,
              {},
              { headers: { Authorization: `Bearer ${token}` } }
            );
            console.log("read updated");
          } catch (error) {
            console.log("Error :", error);
          }
}