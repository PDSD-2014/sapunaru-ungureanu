Buddy Stalker
==================

Server
-----
We implemented an HTTP server using Spring MVC technologies. It uses an in-memory database, represented by a ConcurrentHashMap which holds all the app users.

Client
------
The Android client uses Google Maps API v2 for the map component. It connects to the HTTP server in order to retrieve other users' data or to update the current user's info.
