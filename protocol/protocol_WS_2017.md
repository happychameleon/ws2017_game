The WS\_2017 protocol
=====================

Session States
--------------

The session states in the current draft are directly lifted from the POP3 protocol.
The session goes through several states during it’s life time. After the TCP connection is established and the server sends a greeting and the session enters the AUTHORIZATION state. By transmitting a user name to the server with the **uname** command the client authorizes it’s self and the session can advance to the TRANSACTION state. The client can execute commands till the client sends the QUIT command, that then moves the session in to the UPDATE state. In this state the server releases all resources from the TRANSACTION state and says goodbye and the TCP connection is closed.

basic overview
--------------

The WS\_2017 service is started by the server listening to port 1030. To start a session the client establishes a TCP connection with the server and the server sends a greeting to the client. Commands are exchanged between the client and server till the connection is closed or aborted.

Commands
--------

All commands are case insensitive and made up entirely of ASCII characters. Commands always have exactly one **keyword** followed by none or more **arguments**. The keyword is never longer then 5 characters. Commands are always terminated with a CR (Carriage Return: \\r).

AUTHORIZATION state commands
----------------------------

Once the session has gone in to the AUTHORIZATION state, the server will be expecting user name to identify the client by. If the server recognizes it as a user name that has already been used before and the IP address is the same as the client that last used it, it will return a positive message and all relevant information for the client. If it is a new name the server will create a new entry in the list of names. If the name is a previously used name and the IP address does not match, the server will return negative message.
registering a user name:

> c: uname &lt;name&gt;
> s: +OK you are &lt;name&gt;

recognizing client:

> c: uname &lt;name&gt;
> s: +OK welcome back &lt;name&gt;

negative response:

> c: uname &lt;name&gt;
> s: -ERR &lt;name&gt; is already taken by another client

Alternatively the server could remove used names after the client disconnects. When a client connects to the server the client has to send a message for the server to identify it by. If the name is already present in the server (i.e there is already a client connected with that name), the server returns a negative message. If the name given to the server is unique the server confirms it with a positive response.
registering a name:

> c: uname &lt;name&gt;
> s: +OK ’you are &lt;name&gt;’

negative response:

> c: uname &lt;name&gt;
> s: -ERR ’&lt;name&gt; is already taken by another client’

Transaction state commands
--------------------------

### ping/pong

in regular intervals server and client should exchange pings to make sure that they are still connected, if there is no response in a defined time the connection should be disconnected.
client sends ping:

> c: ping
> s: +OK pong

server sends ping:

> s: ping
> c: +OK pong

### change name

> c: cname &lt;new\_name&gt;
> s: +OK ’name changed from &lt;old\_name&gt; to &lt;new\_name&gt;’

### chat room

### chat

When chatting, the server acts as a relay between two clients. When the message arrives at the destination client, the recipient automatically sends back a message (with the chatr command) to the server that then relays a message to the sending client that the message was received.
sender:

> c: chatm &lt;sender\_name&gt; &lt;recipient\_name&gt; &lt;message&gt;
> s: +OK ’message relayed’

recipient:

> s: chatm &lt;sender\_name&gt; &lt;recipient\_name&gt; &lt;message&gt;
> c: chatr &lt;sender\_name&gt; &lt;recipient\_name&gt;

UPDATE commands
---------------

### quit

When the client wants to terminate the connection to the server, the client uses the quit command which leads the session from the TRANSACTION state to the UPDATE state. In this state the server ends tasks related to the client in a safe manner.

> c: quit
> s: +OK ’terminating tasks and disconnecting’
