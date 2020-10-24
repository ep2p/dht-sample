package com.github.ep2p.dht.controller;

import com.github.ep2p.dht.model.ROWConnectionInfo;
import com.github.ep2p.dht.model.dto.*;
import com.github.ep2p.kademlia.exception.NodeIsOfflineException;
import com.github.ep2p.kademlia.model.FindNodeAnswer;
import com.github.ep2p.kademlia.node.KademliaSyncRepositoryNode;
import lab.idioglossia.row.annotations.RowController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@RowController
public class DHTController {
    private final KademliaSyncRepositoryNode<ROWConnectionInfo, Integer, String> kademliaSyncRepositoryNode;

    @Autowired
    public DHTController(KademliaSyncRepositoryNode<ROWConnectionInfo, Integer, String> kademliaSyncRepositoryNode) {
        this.kademliaSyncRepositoryNode = kademliaSyncRepositoryNode;
    }

    @PutMapping("/dht/ping")
    public @ResponseBody
    PingResponse onPing(@RequestBody BasicRequest basicRequest) throws NodeIsOfflineException {
        return new PingResponse(kademliaSyncRepositoryNode, kademliaSyncRepositoryNode.onPing(basicRequest.getCaller()));
    }

    @PostMapping("/dht/shutdown-signal")
    public @ResponseBody
    BasicResponse onShutdownSignal(@RequestBody BasicRequest basicRequest){
        kademliaSyncRepositoryNode.onShutdownSignal(basicRequest.getCaller());
        return new BasicResponse(kademliaSyncRepositoryNode);
    }

    @PostMapping("/dht/find")
    public @ResponseBody
    FindNodeResponse findNode(@RequestBody FindNodeRequest findNodeRequest) throws NodeIsOfflineException {
        FindNodeAnswer<ROWConnectionInfo> findNodeAnswer = kademliaSyncRepositoryNode.onFindNode(findNodeRequest.getLookupId());
        FindNodeResponse findNodeResponse = new FindNodeResponse(findNodeAnswer);
        findNodeResponse.setNode(kademliaSyncRepositoryNode);
        return findNodeResponse;
    }

    @PostMapping("/dht/store")
    public @ResponseBody
    BasicResponse store(@RequestBody StoreRequest storeRequest){
        kademliaSyncRepositoryNode.onStoreRequest(storeRequest.getCaller(), storeRequest.getRequester(), storeRequest.getKey(), storeRequest.getValue());
        return new BasicResponse(kademliaSyncRepositoryNode);
    }

    @PostMapping("/dht/get")
    public @ResponseBody
    BasicResponse get(@RequestBody GetRequest getRequest){
        kademliaSyncRepositoryNode.onGetRequest(getRequest.getCaller(), getRequest.getRequester(), getRequest.getKey());
        return new BasicResponse(kademliaSyncRepositoryNode);
    }

    @PostMapping("/dht/get/result")
    public @ResponseBody BasicResponse onGetResult(@RequestBody GetResultRequest getResultRequest){
        kademliaSyncRepositoryNode.onGetResult(getResultRequest.getCaller(), getResultRequest.getKey(), getResultRequest.getValue());
        return new BasicResponse(kademliaSyncRepositoryNode);
    }

    @PostMapping("/dht/store/result")
    public @ResponseBody BasicResponse onStoreResult(@RequestBody StoreResultRequest storeResultRequest){
        kademliaSyncRepositoryNode.onStoreResult(storeResultRequest.getNode(), storeResultRequest.getKey(), storeResultRequest.isSuccess());
        return new BasicResponse(kademliaSyncRepositoryNode);
    }

}
