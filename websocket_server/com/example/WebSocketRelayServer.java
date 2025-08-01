package com.example;

import org.java_websocket.server.WebSocketServer;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class WebSocketRelayServer extends WebSocketServer {

    private final Set<WebSocket> clients = Collections.synchronizedSet(new HashSet<>());

    public WebSocketRelayServer(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        clients.add(conn);
        System.out.println("✅ Új kapcsolat: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        clients.remove(conn);
        System.out.println("❌ Kapcsolat bontva: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onMessage(WebSocket sender, String message) {
        System.out.println("📨 Üzenet: " + message + " | Küldő: " + sender.getRemoteSocketAddress());

        synchronized (clients) {
            for (WebSocket client : clients) {
                if (!client.equals(sender)) {
                    client.send(message);
                    System.out.println("➡️  Továbbítva: " + client.getRemoteSocketAddress());
                }
            }
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println("⚠️ Hiba: " + ex.getMessage());
    }

    @Override
    public void onStart() {
        System.out.println("🚀 WebSocket szerver elindult a porton: " + getPort());
    }

    public static void main(String[] args) {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "12345"));
        WebSocketRelayServer server = new WebSocketRelayServer(port);
        server.start();
    }
}
