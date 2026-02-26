import React from 'react'
import './css//DeleteForm.css';
const DeleteForm = ({isSender,handleDelete,setShowDeleteForm}) => {
  
  return (
    <div className="delete-modal-backdrop">
      <div className="delete-modal-box">
        <h4>Are you sure you want to delete?</h4>

        {
          isSender ? (
            <div className="delete-modal-buttons">
              <button className="danger" onClick={() => handleDelete("me")}>
                Delete for me
              </button>

              <button className="danger" onClick={() => handleDelete("everyone")}>
                Delete for everyone
              </button>

              <button className="cancel" onClick={() => setShowDeleteForm(false)}>
                Cancel
              </button>
            </div>
          ) : (
            <div className="delete-modal-buttons">
              <button className="danger" onClick={() => handleDelete("me")}>
                Delete
              </button>

              <button className="cancel" onClick={() => setShowDeleteForm(false)}>
                Cancel
              </button>
            </div>
          )
        }
      </div>
    </div>
  )
}

export default DeleteForm
