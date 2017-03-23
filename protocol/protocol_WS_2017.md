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
A positive response has a ’+OK’ followed by the command that was successful. A negative response has a ’-ERR’ followed by the command that failed and an argument that either is a message with what went wrong or a suggestion for change that is relevant to the keyword.
If the entered command is badly formatted the Server should return:

> s: -ERR ’&lt;command&gt; is not a properly formatted command’

If the entered command does not match a valid command, the server should return:

> s: -ERR ’entered command does not exist’

AUTHORIZATION State Commands
============================

Once the session has gone in to the AUTHORIZATION state, the server will be expecting user name to identify the client by. If the server recognizes it as a user name that has already been used before and the IP address is the same as the client that last used it, it will return a positive message and all relevant information for the client. If it is a new name the server will create a new entry in the list of names. If the name is a previously used name and the IP address does not match, the server will return negative message with a suggested new name. To change a name the user can use the same command to change the old name to the new name. The same answers apply here. If the user enters the name it already had it returns a negative message.
registering a user name:

> c: uname ’&lt;name&gt;’
> s: +OK ’you are &lt;name&gt;’

recognizing client:

> c: uname ’&lt;name&gt;’
> s: +OK ’welcome back &lt;name&gt;’

username already taken:

> c: uname ’&lt;name&gt;’
> s: -ERR uname ’&lt;name\_suggestion&gt;’

own username entered:

> c: uname ’&lt;name&gt;’
> s: -ERR same username entered’

Alternatively the server could remove used names after the client disconnects. When a client connects to the server the client has to send a message for the server to identify it by. If the name is already present in the server (i.e there is already a client connected with that name), the server returns a negative message. If the name given to the server is unique the server confirms it with a positive response.
registering a name:

> c: uname ’&lt;name&gt;’
> s: +OK ’you are &lt;name&gt;’

negative response:

> c: uname ’&lt;name&gt;’
> s: -ERR uname ’&lt;name\_suggestion&gt;’

TRANSACTION State Commands
==========================

Ping/Pong
---------

in regular intervals server and client should exchange pings to make sure that they are still connected, if there is no response in a defined time the connection should be disconnected.
server sends ping:

> s: ping
> c: +OK pong

Chat
----

When chatting, the server acts as a relay between two clients. When the message arrives at the destination client, the recipient automatically sends back a message (with the chatr command) to the server that then relays a message to the sending client that the message was received.
sender:

> c: chatm ’&lt;sender\_name&gt;’ ’&lt;recipient\_name&gt;’ ’&lt;message&gt;’
> s: +OK ’message relayed’

recipient:

> s: chatm ’&lt;sender\_name&gt;’ ’&lt;recipient\_name&gt;’ ’&lt;message&gt;’
> c: chatr ’&lt;sender\_name&gt;’ ’&lt;recipient\_name&gt;’

UPDATE State Commands
=====================

Quit
----

When the client wants to terminate the connection to the server, the client uses the quit command which leads the session from the TRANSACTION state to the UPDATE state. In this state the server ends tasks related to the client in a safe manner.

> c: cquit
> s: +OK ’terminating tasks and disconnecting’
