import { setMessages } from "../../Slice/MessageSlice";
import { getAllMessagesBetweenApi } from "../../api/MessageApi";
export const getAllMessagesBetween=async(token,dispatch)=>{
    try{
        const response=await getAllMessagesBetweenApi(token);
        dispatch(setMessages(response.data));
    }
    catch(error){
        console.log("error getting allmessages",error);
    }

}

export const sendMessage=async (stompClient,type,content,receiverName) => {
    if(stompClient){
        if(type=="group"){
            stompClient.publish({
                destination:"/app/group",
                body:JSON.stringify({
                    content:content,
                    groupName:receiverName
                })
            })
        }
        else{
            stompClient.publish({
                destination:"/app/private",
                body:JSON.stringify({
                    content:content,
                    receiver:receiverName
                })
            })

        }
    }


    
}

