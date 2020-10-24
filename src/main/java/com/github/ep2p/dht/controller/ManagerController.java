package com.github.ep2p.dht.controller;

import com.github.ep2p.dht.model.ROWConnectionInfo;
import com.github.ep2p.kademlia.exception.BootstrapException;
import com.github.ep2p.kademlia.node.KademliaSyncRepositoryNode;
import com.github.ep2p.kademlia.node.Node;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
}
