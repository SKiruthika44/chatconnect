import {jwtDecode} from "jwt-decode";
export function isTokenValid(token){
try{
    const obj=jwtDecode(token);
    
    return obj.exp*1000>Date.now();
}
catch(error){
    return false;
}
}