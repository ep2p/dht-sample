package com.github.ep2p.dht.service;

import com.github.ep2p.dht.model.ROWConnectionInfo;
import lab.idioglossia.row.client.RowClient;
import lab.idioglossia.row.client.callback.RowTransportListener;
import lab.idioglossia.row.client.tyrus.RowClientConfig;
import lab.idioglossia.row.client.tyrus.TyrusRowWebsocketClient;
import lab.idioglossia.row.client.ws.RowWebsocketSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.websocket.CloseReason;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class RowConnectionPool {
    private final Map<String, RowClient> pool = new ConcurrentHashMap<>();
    private final Listener listener = new Listener();

    public synchronized RowClient getClient(ROWConnectionInfo rowConnectionInfo){
        if(pool.containsKey(rowConnectionInfo.getFullAddress())){
            return pool.get(rowConnectionInfo.getFullAddress());
        }else {
            RowClient rowClient = new TyrusRowWebsocketClient(RowClientConfig.builder()
                    .address(rowConnectionInfo.getFullAddress())
                    .rowTransportListener(listener)
                    .build());
            rowClient.open();
            pool.put(rowConnectionInfo.getFullAddress(), rowClient);
            return rowClient;
        }
    }

    private class Listener extends RowTransportListener.Default {
        @Override
        public void onOpen(RowWebsocketSession rowWebsocketSession) {

        }

        @Override
        public void onError(RowWebsocketSession rowWebsocketSession, Throwable throwable) {
            log.error("Websocket error ", throwable);
        }

        @Override
        public void onClose(RowClient rowClient, RowWebsocketSession rowWebsocketSession, CloseReason closeReason) {
            pool.remove(rowWebsocketSession.getUri().toString());
        }
    }

}
