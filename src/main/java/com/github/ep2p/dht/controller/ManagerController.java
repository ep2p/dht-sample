package com.github.ep2p.dht.controller;

import com.github.ep2p.dht.model.ManagerStore;
import com.github.ep2p.dht.model.ROWConnectionInfo;
import com.github.ep2p.kademlia.exception.BootstrapException;
import com.github.ep2p.kademlia.exception.GetException;
import com.github.ep2p.kademlia.exception.StoreException;
import com.github.ep2p.kademlia.model.GetAnswer;
import com.github.ep2p.kademlia.model.StoreAnswer;
import com.github.ep2p.kademlia.node.KademliaSyncRepositoryNode;
import com.github.ep2p.kademlia.node.Node;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class ManagerController {
    private final KademliaSyncRepositoryNode<ROWConnectionInfo, Integer, String> kademliaSyncRepositoryNode;

    public ManagerController(KademliaSyncRepositoryNode<ROWConnectionInfo, Integer, String> kademliaSyncRepositoryNode) {
        this.kademliaSyncRepositoryNode = kademliaSyncRepositoryNode;
    }

    @GetMapping("/manager/start")
    public @ResponseBody String start(){
        kademliaSyncRepositoryNode.start();
        return "OK";
    }

    @PostMapping("/manager/bootstrap")
    public @ResponseBody String bootstrap(@RequestBody Node<ROWConnectionInfo> bootstrapNode){
        try {
            this.kademliaSyncRepositoryNode.bootstrap(bootstrapNode);
            return "OK!";
        } catch (BootstrapException e) {
            log.error("Failed to bootstrap node", e);
            return "FAILED";
        }
    }

    @PostMapping("/manager/store")
    public @ResponseBody String store(@RequestBody ManagerStore managerStore){
        try {
            StoreAnswer<Integer> storeAnswer = this.kademliaSyncRepositoryNode.store(managerStore.getKey(), managerStore.getValue(), 10, TimeUnit.SECONDS);
            return "Node #"+ storeAnswer.getNodeId() + " STORED DATA";
        } catch (StoreException | InterruptedException e) {
            e.printStackTrace();
            return "Failed to store: " + e.getMessage();
        }
    }

    @GetMapping("/manager/get/{key}")
    public @ResponseBody String get(@PathVariable Integer key){
        try {
            GetAnswer<Integer, String> getAnswer = this.kademliaSyncRepositoryNode.get(key, 10, TimeUnit.SECONDS);
            return "Node #"+getAnswer.getNodeId() + " GOT DATA: " + getAnswer.getValue();
        } catch (GetException e) {
            e.printStackTrace();
            return "Failed to get: " + e.getMessage();
        }
    }
}
