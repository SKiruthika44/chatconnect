import {jwtDecode} from "jwt-decode";
export function isTokenValid(token){
try{
    const obj=jwtDecode(token);
    console.log(obj.sub);
    console.log(new Date(obj.iat*1000).toLocaleString());
    console.log(new Date(obj.exp*1000).toLocaleString());
    return obj.exp*1000>Date.now();
}
catch(error){
    return false;
}
}