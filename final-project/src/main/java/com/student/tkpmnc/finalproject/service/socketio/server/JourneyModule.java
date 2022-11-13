package com.student.tkpmnc.finalproject.service.socketio.server;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.student.tkpmnc.finalproject.service.dto.DriverBroadcastMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JourneyModule {

    private static final Logger log = LoggerFactory.getLogger(JourneyModule.class);

    private final SocketIONamespace namespace;

    @Autowired
    public JourneyModule(SocketIOServer server) {
        this.namespace = server.addNamespace("/drivers");
        this.namespace.addConnectListener(onConnected());
        this.namespace.addDisconnectListener(onDisconnected());
        this.namespace.addEventListener("drivers", DriverBroadcastMessage.class, broadCast());
    }

    public DataListener<DriverBroadcastMessage> broadCast() {
        return (client, data, ackSender) -> {
            log.info("Broadcast message '{}'", data);
            namespace.getBroadcastOperations().sendEvent("drivers", data);
        };
    }

    public void sendEvent(Object message, String channelName) {
        log.info("Broadcast message '{}'", message);
        namespace.getBroadcastOperations().sendEvent(channelName, message);
    }

    private ConnectListener onConnected() {
        return client -> {
            HandshakeData handshakeData = client.getHandshakeData();
            log.info("Client[{}] - Connected to chat module through '{}'", client.getSessionId().toString(), handshakeData.getUrl());
        };
    }

    private DisconnectListener onDisconnected() {
        return client -> log.info("Client[{}] - Disconnected from chat module.", client.getSessionId().toString());
    }
}
