package com.github.ep2p.dht.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ep2p.dht.model.ROWConnectionInfo;
import lab.idioglossia.row.client.RestTemplateRowHttpClient;
import lab.idioglossia.row.client.RowClient;
import lab.idioglossia.row.client.RowClientFactory;
import lab.idioglossia.row.client.callback.RowTransportListener;
import lab.idioglossia.row.client.tyrus.RowClientConfig;
import lab.idioglossia.row.client.util.DefaultJacksonMessageConverter;
import lab.idioglossia.row.client.ws.RowWebsocketSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.websocket.CloseReason;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class RowConnectionPool {
    private final RowClientFactory rowClientFactory;
    private final ObjectMapper objectMapper;
    private final Map<String, RowClient> pool = new ConcurrentHashMap<>();
    private final Listener listener = new Listener();

    @Autowired
    public RowConnectionPool(RowClientFactory rowClientFactory, ObjectMapper objectMapper) {
        this.rowClientFactory = rowClientFactory;
        this.objectMapper = objectMapper;
    }

    public synchronized RowClient getClient(ROWConnectionInfo rowConnectionInfo){
        if(pool.containsKey(rowConnectionInfo.getFullAddress())){
            return pool.get(rowConnectionInfo.getFullAddress());
        }else {
            RowClientConfig rowClientConfig = rowClientFactory.getRowClientConfig();
            rowClientConfig.setRowTransportListener(listener);
            rowClientConfig.setMessageConverter(new DefaultJacksonMessageConverter(objectMapper));
            rowClientConfig.setAddress(rowConnectionInfo.getFullAddress());
            RestTemplateRowHttpClient restTemplateRowHttpClient = new RestTemplateRowHttpClient(rowConnectionInfo.getHttpAddress(), new RestTemplate(), objectMapper);
            RowClient rowClient = rowClientFactory.getRowClient(rowClientConfig, restTemplateRowHttpClient);
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
