package com.github.ep2p.dht.service;

import com.github.ep2p.dht.model.ROWConnectionInfo;
import com.github.ep2p.dht.model.dto.BasicRequest;
import com.github.ep2p.dht.model.dto.BasicResponse;
import com.github.ep2p.dht.model.dto.FindNodeRequest;
import com.github.ep2p.dht.model.dto.FindNodeResponse;
import com.github.ep2p.kademlia.connection.NodeConnectionApi;
import com.github.ep2p.kademlia.model.FindNodeAnswer;
import com.github.ep2p.kademlia.model.PingAnswer;
import com.github.ep2p.kademlia.node.Node;
import lab.idioglossia.row.client.RowClient;
import lab.idioglossia.row.client.callback.ResponseCallback;
import lab.idioglossia.row.client.model.RowRequest;
import lab.idioglossia.row.client.model.RowResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ROWNodeConnectionApi implements NodeConnectionApi<ROWConnectionInfo> {
    private final RowConnectionPool rowConnectionPool;

    @Autowired
    public ROWNodeConnectionApi(RowConnectionPool rowConnectionPool) {
        this.rowConnectionPool = rowConnectionPool;
    }

    @Override
    public PingAnswer ping(Node<ROWConnectionInfo> caller, Node<ROWConnectionInfo> node) {
        RowRequest<BasicRequest, Void> request = new RowRequest<>(RowRequest.RowMethod.PUT, "/dht/ping", null, new BasicRequest(caller), new HashMap<>());
        AtomicReference<PingAnswer> responseAtomicAnswer = new AtomicReference<>(new PingAnswer(node.getId(), false));
        CountDownLatch latch = new CountDownLatch(1);
        try {
            RowClient client = rowConnectionPool.getClient(node.getConnectionInfo());
            client.sendRequest(request, new ResponseCallback<BasicRequest>(BasicRequest.class) {
                @Override
                public void onResponse(RowResponse<BasicRequest> rowResponse) {
                    responseAtomicAnswer.set(new PingAnswer(node.getId(), true));
                    latch.countDown();
                }

                @Override
                public void onError(Throwable throwable) {
                    latch.countDown();
                }
            });
        } catch (Exception e) {
            latch.countDown();
            e.printStackTrace();
        }
        try {
            latch.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return responseAtomicAnswer.get();
    }

    @Override
    public void shutdownSignal(Node<ROWConnectionInfo> caller, Node<ROWConnectionInfo> node) {
        RowRequest<BasicRequest, Void> request = new RowRequest<>(RowRequest.RowMethod.GET, "/dht/shutdown-signal", null, new BasicRequest(caller), new HashMap<>());
        try {
            rowConnectionPool.getClient(node.getConnectionInfo()).sendRequest(request, new ResponseCallback<BasicResponse>(BasicResponse.class) {
                @Override
                public void onResponse(RowResponse<BasicResponse> rowResponse) {

                }

                @Override
                public void onError(Throwable throwable) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public FindNodeAnswer<ROWConnectionInfo> findNode(Node<ROWConnectionInfo> caller, Node<ROWConnectionInfo> node, Integer nodeId) {
        RowRequest<FindNodeRequest, Void> request = new RowRequest<>(RowRequest.RowMethod.PUT, "/dht/ping", null, new FindNodeRequest(caller, nodeId), new HashMap<>());
        FindNodeAnswer<ROWConnectionInfo> defaultAnswer = new FindNodeAnswer<ROWConnectionInfo>(0);
        defaultAnswer.setAlive(false);
        AtomicReference<FindNodeAnswer<ROWConnectionInfo>> responseAtomicAnswer = new AtomicReference<>(defaultAnswer);
        CountDownLatch latch = new CountDownLatch(1);
        try {
            rowConnectionPool.getClient(node.getConnectionInfo()).sendRequest(request, new ResponseCallback<FindNodeResponse>(FindNodeResponse.class) {
                @Override
                public void onResponse(RowResponse<FindNodeResponse> rowResponse) {
                    responseAtomicAnswer.set(rowResponse.getBody().getAnswer());
                    latch.countDown();
                }

                @Override
                public void onError(Throwable throwable) {
                    latch.countDown();

                }
            });
        } catch (IOException e) {
            latch.countDown();
            e.printStackTrace();
        }
        try {
            latch.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return responseAtomicAnswer.get();
    }

    @Override
    public <K, V> void storeAsync(Node<ROWConnectionInfo> caller, Node<ROWConnectionInfo> requester, Node<ROWConnectionInfo> node, K key, V value) {

    }

    @Override
    public <K> void getRequest(Node<ROWConnectionInfo> caller, Node<ROWConnectionInfo> requester, Node<ROWConnectionInfo> node, K key) {

    }

    @Override
    public <K, V> void sendGetResults(Node<ROWConnectionInfo> caller, Node<ROWConnectionInfo> requester, K key, V value) {

    }

    @Override
    public <K> void sendStoreResults(Node<ROWConnectionInfo> caller, Node<ROWConnectionInfo> requester, K key, boolean success) {

    }
}
