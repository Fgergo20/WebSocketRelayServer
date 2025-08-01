#!/bin/bash
mkdir -p out
javac -cp "Java-WebSocket-1.5.3.jar:slf4j-api-1.7.36.jar:slf4j-simple-1.7.36.jar" -d out com/example/WebSocketRelayServer.java
