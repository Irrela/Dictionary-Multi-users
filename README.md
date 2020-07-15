# Dictionary(Multi_users)
A multi-threaded dictionary server that allows concurrent clients to search the meaning(s) of a word, add a new word, and remove an existing word.

## Introduction
This is the 1st assignment of Distributed System, the designing requirements include:
- Use C/S architecture.
- Make an explict use of Sockets and Threads.
- A Graphical User interface.

Function requirements include:
- Query the meaning(s) of a given word.
- Add a new word(A new word added by one client should be visible to all other clients of the dictionary server).
- Remove an existing word(If the word does not exist in the dictionary then no action should be taken).

Other details:
- This project communicates with Sockets(TCP protocol).
- The dictionary data is maintained by a json file, a new latest one will be generated each time server is down, and manager can load any stored version when launching the server.
- The server implements thread-per-connection model to provide service.

### Interaction diagram

## How to run

1. Download DictionaryServer.jar, DictionaryClient.jar and dict.json(you can make your own dictionary file).
2. Launch the server, text the following command in terminal:  
`> java –jar DictionaryServer.jar <port> <dictionary-file>`
3. Launch the client, text the following command in terminal:  
`> java –jar DictionaryClient.jar <server-address> <server-port>`
