package com.github.ep2p.dht.service;

import com.github.ep2p.dht.model.ROWConnectionInfo;
import com.github.ep2p.dht.model.dto.*;
import com.github.ep2p.kademlia.connection.NodeConnectionApi;
import com.github.ep2p.kademlia.exception.StoreException;
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
        RowRequest<BasicRequest, Void> request = new RowRequest<>(RowRequest.RowMethod.POST, "/dht/shutdown-signal", null, new BasicRequest(caller), new HashMap<>());
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
        RowRequest<FindNodeRequest, Void> request = new RowRequest<>(RowRequest.RowMethod.POST, "/dht/find", null, new FindNodeRequest(caller, nodeId), new HashMap<>());
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

    //not async yet
    @Override
    public <K, V> void storeAsync(Node<ROWConnectionInfo> caller, Node<ROWConnectionInfo> requester, Node<ROWConnectionInfo> node, K key, V value) {
        StoreRequest storeRequest = new StoreRequest(caller, requester);
        storeRequest.setKey(getKey(key));
        storeRequest.setValue(getValue(value));
        RowRequest<StoreRequest, Void> request = new RowRequest<>(RowRequest.RowMethod.POST, "/dht/store", null, storeRequest, new HashMap<>());
        try {
            rowConnectionPool.getClient(node.getConnectionInfo()).sendRequest(request, new ResponseCallback<BasicResponse>(BasicResponse.class) {
                @Override
                public void onResponse(RowResponse<BasicResponse> rowResponse) {

                }

                @Override
                public void onError(Throwable throwable) {
                    throw new RuntimeException(new StoreException(throwable.getMessage()));
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(new StoreException(e));
        }
    }

    @Override
    public <K> void getRequest(Node<ROWConnectionInfo> caller, Node<ROWConnectionInfo> requester, Node<ROWConnectionInfo> node, K key) {
        GetRequest getRequest = new GetRequest(caller, requester);
        getRequest.setKey(getKey(key));
        RowRequest<GetRequest, Void> request = new RowRequest<>(RowRequest.RowMethod.POST, "/dht/get", null, getRequest, new HashMap<>());
        try {
            rowConnectionPool.getClient(node.getConnectionInfo()).sendRequest(request, new ResponseCallback<BasicResponse>(BasicResponse.class) {
                @Override
                public void onResponse(RowResponse<BasicResponse> rowResponse) {

                }

                @Override
                public void onError(Throwable throwable) {
                    throw new RuntimeException(new StoreException(throwable.getMessage()));
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(new StoreException(e));
        }
    }

    @Override
    public <K, V> void sendGetResults(Node<ROWConnectionInfo> caller, Node<ROWConnectionInfo> requester, K key, V value) {
        GetResultRequest getResultRequest = new GetResultRequest(caller);
        getResultRequest.setKey(getKey(key));
        getResultRequest.setValue(getValue(value));
        RowRequest<GetResultRequest, Void> request = new RowRequest<>(RowRequest.RowMethod.POST, "/dht/get/results", null, getResultRequest, new HashMap<>());
        try {
            rowConnectionPool.getClient(requester.getConnectionInfo()).sendRequest(request, new ResponseCallback<BasicResponse>(BasicResponse.class) {
                @Override
                public void onResponse(RowResponse<BasicResponse> rowResponse) {

                }

                @Override
                public void onError(Throwable throwable) {

                }
            });
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public <K> void sendStoreResults(Node<ROWConnectionInfo> caller, Node<ROWConnectionInfo> requester, K key, boolean success) {
        StoreResultRequest storeResultRequest = new StoreResultRequest(caller);
        storeResultRequest.setKey(getKey(key));
        storeResultRequest.setSuccess(success);
        RowRequest<StoreResultRequest, Void> request = new RowRequest<>(RowRequest.RowMethod.POST, "/dht/store/result", null, storeResultRequest, new HashMap<>());
        try {
            rowConnectionPool.getClient(requester.getConnectionInfo()).sendRequest(request, new ResponseCallback<BasicResponse>(BasicResponse.class) {
                @Override
                public void onResponse(RowResponse<BasicResponse> rowResponse) {

                }

                @Override
                public void onError(Throwable throwable) {
                    throw new RuntimeException(new StoreException(throwable.getMessage()));
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Integer getKey(Object k){
        assert k instanceof Integer;
        return (Integer) k;
    }

    private String getValue(Object v){
        assert v instanceof String;
        return (String) v;
    }
}
