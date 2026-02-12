import React from 'react'

const Avatar = ({user}) => {
  const colors = [
    "#FF6B6B",
    "#4ECDC4",
    "#5567FF",
    "#FFA500",
    "#00B894",
    "#6C5CE7",
    "#FD79A8",
    "#E17055",
  ];

  const getColor = (name) => {
    if (!name) return "#999";
    const charCode=name.length;
   // const charCode = name.charCodeAt(0); // get ASCII value
    return colors[charCode % colors.length];
  };
  return (
    <div>

        {
            user.profileImage? (<img src={`http://localhost:8080${user.profileImage}`} style={{
          width: "40px",
          height: "40px",
          borderRadius: "50%",
          objectFit: "cover",
        }}/>):(<div  style={{
        width: "40px",
        height: "40px",
        borderRadius: "50%",
        backgroundColor: getColor(user.username),
        color: "white",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        fontWeight: "bold",
        fontSize: "18px",
        userSelect: "none",
      }}>{user.username.charAt(0)}</div>)
        }

      
    </div>
  )
}

export default Avatar
