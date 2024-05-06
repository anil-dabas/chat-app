# Chat Server

## Tech Used in the application 
- Java 17
- H2 In-memory database
- SpringBoot
- Gradle 
- Web Sockets and API Development


## Basic features of the application 

### Chat server system in Java
- User authentication with basic username/password login. The credentials are hardcoded
- Creation of a single chat room upon server startup
- Enabling users to join chat rooms
- Persistent storage of chat messages in a Database.
- Sending and receiving messages in the chat room
- The client is able to fetch the room messages

#### Additional features that I have focused on 

- WebSocket support for real-time chat communication
- The Chat Support below Actions 
  1. JOIN - chat room
  2. LEAVE - chat room
  3. DELETE - Message
  4. FETCH - all the messages from chat room
  5. HISTORY - get all the messages from the database
- CI using GitHub Actions 

#### Server scalability

I have Ideas for server scalability that I can discuss during the interview process 

1. Distributed Redis 
2. Kafka
  

## How to for the application 


1. Download the application from Git in a directory
2. Please keep the port 8080 free as the application will run on port 8080
3. Run below command from project root directory (use terminal or cmd)
   1. For Mac / Linux
      ```./gradlew bootRun```
   2. For Windows
      ```./gradlew bootRun```
   4. The websocket endpoint for different users as per the hardcoded users

      1. [ws://localhost:8080/chat](ws://localhost:8080/chat)
      2. For all the users you need to pass the Authorization key in the header
      
      User | Key           | Value 
            --- |---------------|-------
            dabas | Authorization | Basic ZGFiYXM6cGFzc3dvcmQ= 
       dummy | Authorization | Basic ZHVtbXk6cGFzc3dvcmQ=
       anil | Authorization | Basic YW5pbDpwYXNzd29yZA==

   3. For the users to join the chat room we need to pass the message as  
       ```json
       {
         "ACTION" : "join" 
       }
       ```
   4. For the users to send the messages in the chat room we need to pass the message as
    
       ```json
       {
         "MESSAGE" : "This is a demo message" 
       }
       ```
   5. For the users to leave the chat room we need to pass the message as
       ```json
       {
         "ACTION" : "leave" 
       }
       ```
   6.  For the users to Fetch all the messages from chat room we need to pass the message as
       ```json
       {
         "ACTION" : "fetch" 
       }
       ```
   7. For the users to get all the messages from database we need to pass the message as
       ```json
       {
          "ACTION" : "history"
       }
       ```
   8. For the users to delete the messages from chat room we need to pass the message as
       ```json
       {
         "ACTION" : "delete", 
         "ID" : "message_id"
       }
       ```
          
## Few things to note 

- I have covered all the features in the Web Socket implementation 
- Somehow I am not able to attach my POSTMAN Web Sockets postman collection in the project 
- For the APIs I have added postman.json in the root/postman directory
- I am adding few screenshots from my POSTMAN to show the testcases 
- I have added the test case for services layer and the chat rooms 
- I assumed there are separate chat rooms for Websockets and  REST API