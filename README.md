# ChatConnect – Real-Time Chat Application
## Overview

ChatConnect is a full-stack real-time chat application that supports private messaging and group chats with advanced messaging features such as delivery status tracking, emoji reactions, message translation, and message deletion.

The application is built using Spring Boot (backend) and React.js (frontend) with WebSocket-based real-time communication.

## Features
### Authentication

- User Signup
- User Login
- JWT-based secure authentication
- Protected routes after login

### Private Chat Features

- Real-time one-to-one messaging
- Message Sent status
- Message Delivered status
- Message Read status
- Online status of user
- Last seen of user
- Edit message
- Delete for me
- Delete for everyone
- Emoji reactions
- Forward message
- Message translation based on preferred language
- Unread message count display

### Group Chat Features

- Create group chats
- Real-time group messaging
- Emoji reactions with count
- Edit message
- Delete for me
- Delete for everyone
- Forward message
- Unread message count
- Message translation support
  
## UI
- Simple and clean dark theme
- Chat sidebar with personal and group chats
- Clear message bubbles
- Emoji and translation options inside messages

## Tech Stack
### Backend
- Java
- Spring Boot
- Spring Security
- JWT Authentication
- WebSocket (STOMP)
- JPA / Hibernate
- MySQL

### Frontend
- React.js
- Redux
- WebSocket (SockJS + STOMP Client)
- CSS

## How It Works 
- WebSocket is used for real-time messaging.
- Messages are stored in MySQL database.
- Separate delivery tracking is used for sent, delivered, and read status.
- JWT is used for secure login and API protection.
- Unread count is calculated based on message read status.
- Preferred language is stored for each user for translation feature.

##  How to Run Locally

### Backend

```bash
cd backend
mvn spring-boot:run
```

### Frontend

```bash
cd frontend
npm install
npm start
```
