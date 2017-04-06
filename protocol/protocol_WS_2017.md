Session States
==============

The session states in the current draft are directly lifted from the POP3 protocol.
The session goes through several states during it’s life time. After the TCP connection is established and the server sends a greeting and the session enters the AUTHORIZATION state. By transmitting a user name to the server with the **uname** command the client authorizes it’s self and the session can advance to the TRANSACTION state. In the TRANSACTION state the client can execute commands till the client sends the **cquit** command, that then moves the session in to the UPDATE state. In this state the server releases all resources from the TRANSACTION state and says goodbye and the TCP connection is closed.

Basic Overview
==============

The WS\_2017 service is started by the server listening to port 1030. To start a session the client establishes a TCP connection with the server and the server sends a greeting to the client. Commands are exchanged between the client and server till the connection is closed or aborted.

Commands
========

All commands are case insensitive and made up entirely of ASCII characters. Commands always have exactly one **keyword** followed by none or more **arguments**. The keyword is never longer then 5 characters. Commands are always terminated with a CRCF (Carriage Return: \\r, New Line: \\n). Commands will either be answered with a positive response confirming that the command has been understood and processed or negative response pointing out what is wrong.
A positive response has a ’+OK’ followed by the command that was successful and if required one or more argumments. A negative response has a ’-ERR’ followed by the command that failed and an argument that either is a message with what went wrong or a suggestion for change that is relevant to the keyword.
If the entered command is badly formatted the Server should return:

> s: -ERR ’&lt;command&gt; is not a properly formatted command’

If the entered command does not match a valid command, the server should return:

> s: -ERR ’entered command does not exist’

AUTHORIZATION State Commands
============================

Once the session has gone in to the AUTHORIZATION state, the server will be expecting a user name to identify the client by. This is done by sending a command with the desired user name to the server. If the name is already present in the server (i.e there is already a client connected with that name), the server returns a negative response. If the name given to the server is unique the server confirms it with a positive response. As soon as the client has entered the user name, it is broadcasted to all other connected clients.
When a user disconnects from the server the user name is removed.
registering a name:

> c: uname &lt;name&gt;
> s: +OK uname ’you are’ &lt;name&gt;

own username entered:

> c: uname &lt;name&gt;
> s: -ERR uname ’same username entered’

username already taken:

> c: uname &lt;name&gt;
> s: -ERR uname suggested &lt;name\_suggestion&gt;

broadcast new user name to other clients:

> s: nuser &lt;name&gt;

TRANSACTION State Commands
==========================

Ping/Pong
---------

Every 5 seconds server and client should exchange a ping and pong which is initialized by the server, to make sure that they are still connected. If there is no response within 15 seconds (after 3 pings) the connection should be disconnected. The server does this by starting a thread that sends out a ping to the client, which if it is not interrupted soon enough by the client with a pong, it removes the client form the user list and closes the socket.
On the client side, once the ping has been received from the server, the client starts a thread that if not interrupted by a server ping within 20 seconds, will shutdown the client
ping/pong:

> s: cping
> c: cpong

Changing user name
------------------

To change user name the same command is used as to enter the original user name in the AUTHORIZATION State.
change name:

> c: uname &lt;new\_name&gt;
> s: +OK uname ’you are’ &lt;new\_name&gt;

When the a name is changed this has to be sent to all other clients:

> s: +OK nuser &lt;old\_name&gt; &lt;new\_name&gt;

(actully part of the AUTHORIZATION State)When a user joins the server the name is announced to all users

> s: nuser &lt;newusername&gt;

Get all user names
------------------

To be able to send a message or find to an other user one needs to know the name of the other users.

> c: cgetu
> s: +OK cgetu &lt;user0&gt; &lt;user2&gt; &lt;user2&gt; ...

Main Chat
---------

When chatting, the server acts as a relay between two clients. When the message arrives at the server, the server sends the message to the recipient. To ensure that the message was sent properly, the client also sends the message to itself. The client sends the messages to every client as a separate message.
sender:

> c: chatm &lt;sender\_name&gt; &lt;recipient\_name&gt; ’&lt;message&gt;’

server to recipient:

> s: chatm &lt;sender\_name&gt; &lt;recipient\_name&gt; ’&lt;message&gt;’

Whisper Chat
------------

When sending a whisper message the server sends the message both to the recipient and the sender. The second for the sender to know the message has been delivered.
sender:

> c: chatw &lt;sender\_name&gt; &lt;recipient\_name&gt; ’&lt;message&gt;’

server to recipient and sender:

> c: chatw &lt;sender\_name&gt; &lt;recipient\_name&gt; ’&lt;message&gt;’

Games State
-----------

### Game Creation and Destruction

The Client tells the server that they created a new game.
Client to Server:

> newgm &lt;points&gt; &lt;gamename&gt;

If name is Taken, Server writes back to client. Server informs the Client that the chosen game name is already taken.
Server to Client:

> -ERR game name taken

Else the Server tells then all the Clients about the new game (including creator). The Server informs the Clients about the new game which can now be joined.
Informs all the clients that this game was deleted (because every player left it). The game could either be already playing or still be in the starting phase.
Server to all Clients:

> rmgam &lt;gamename&gt;

The Client informs the Server that they want to join the game.
Client to Server:

> joing &lt;gamename&gt; &lt;username&gt;

The Server informs all the Clients, that the client has joined this game.
The Server informs the user that there is no place left in the game.
Server to Client:

> -ERR joing game already full

The Client tells the server that it is ready and has chosen the specified characters.
Client to Server:

> ready &lt;username&gt; &lt;gamename&gt; \[&lt;characterstring&gt;\]

The Server tells the Clients (including sender) that the Client is now ready. Server to Client:

> \[&lt;characterstring&gt;\] is formated as following (with any number of characters &gt; 0):
> \[&lt;character1name&gt; ’&lt;weapon1name&gt;’ &lt;character2name&gt; ’&lt;weapon2name&gt;’\]

Client asks about all open games (similar to cgetu).
Client to Server:

> cgetg

Client asks when getting the answer for the cgetu command, after registering all usernames (to make sure they are registered). Server responds with a response for each game (waiting = not yet started; running = already playing):

> +OK cgetg waiting &lt;gameName&gt; &lt;maxPoints&gt; &lt;username1&gt; (ready \[&lt;characterstring&gt;\]|choosing) &lt;username2&gt; (ready \[&lt;characterstring&gt;\]|choosing)
> +OK cgetg running &lt;gameName&gt; &lt;username1&gt; &lt;username2&gt;

The Client informs the server that they left the game.
Client to Server:

> leavg &lt;gamename&gt; &lt;username&gt;

The Server informs all the Clients, that the Client has left the game.
Server to Client:

### Player Position

Client Sends position of children by using the chpos command. The server validates the
Client to Server:

> chpos &lt;user\_name&gt; &lt;old\_child\_position: x,y&gt; &lt;new\_child\_position: x,y&gt;

Server to Clients:

> +OK chpos &lt;user\_name&gt; &lt;old\_child\_position: x,y&gt; &lt;new\_child\_position: x,y&gt;

### Damage

When the client attacks an other user the attack intensity is transmitted to the server, which then calculates the new level of wetness of the attacked and transmittes it to all clients.
If the child has reached complete wetness the server, changes the high score of the attacking user and also sends all clients the old position and returns instead of the new position a “null”.
Client to Server:

> chatt &lt;user\_name\_attacker&gt; &lt;user\_name\_attacked&gt; &lt;attacked\_child\_position: x,y&gt; &lt;attack\_intensity&gt;

Server to Client:

> +OK chatt &lt;user\_name\_attacked&gt; &lt;attacked\_child\_position: x,y&gt; &lt;wetness\_value&gt;

If Child completely wet, Server to Clients:

> +OK chpos &lt;user\_name&gt; &lt;old\_child\_position: x,y&gt; &lt;new\_child\_position: “null”&gt;

### Weapons Change during Game

During the game a player can change their weapon, this is done by transmitting the chwea command to the server.
Client to Server:

> chwea &lt;user\_name&gt; &lt;child\_position: x,y&gt; &lt;new\_weapon&gt;

Server to sending Client:

> +OK chwea &lt;user\_name&gt; &lt;child\_position: x,y&gt; &lt;new\_weapon&gt;

### Power-Ups

When a client finds a power-up the server sends the client a chpow command, with the powered up child position and the power-up.
Server to Client:

> chpow &lt;user\_name&gt; &lt;child\_position: x,y&gt; &lt;power\_up&gt;

Client to Server:

> +OK chpow

### High Score

As soon as there is a winner, the server calculates the high score of all players and returns the high score back to all clients. The Server also saves the high score in an xml file to be able to recall the previous high score. Server to Clients:

> uhigh &lt;user\_name&gt; &lt;score&gt; &lt;user\_name&gt; &lt;score&gt; ......

UPDATE State Commands
=====================

Quit
----

When the client wants to terminate the connection to the server, the client uses the quit command which leads the session from the TRANSACTION state to the UPDATE state. In this state the server ends tasks related to the client in a safe manner.

> c: cquit
> s: +OK cquit ’terminating tasks and disconnecting’
