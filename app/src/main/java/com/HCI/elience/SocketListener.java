package com.HCI.elience;

public interface SocketListener {
    void onSocketConnected();
    void onSocketDisconnected();
    void onNewMessageReceived(String username, String message);
}
