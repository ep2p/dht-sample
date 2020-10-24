package com.github.ep2p.dht.config;

import com.github.ep2p.dht.model.ROWConnectionInfo;
import com.github.ep2p.dht.service.DHTRepository;
import com.github.ep2p.dht.service.ROWNodeConnectionApi;
import com.github.ep2p.kademlia.node.KademliaRepository;
import com.github.ep2p.kademlia.node.KademliaSyncRepositoryNode;
import com.github.ep2p.kademlia.node.RedistributionKademliaNodeListener;
import com.github.ep2p.kademlia.table.RoutingTable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class KademliaConfig {
    private final int nodeId;
    private final int port;
    private final String address;

    public KademliaConfig(@Value("${nodeId}") int nodeId, @Value("${server.port}") int port, @Value("${server.address}") String address) {
        this.nodeId = nodeId;
        this.port = port;
        this.address = address;
    }

    @Bean
    public ROWConnectionInfo rowConnectionInfo(){
        return new ROWConnectionInfo(address, port);
    }

    @Bean
    public KademliaRepository<Integer, String> kademliaRepository(){
        return new DHTRepository();
    }

    @Bean
    @DependsOn({"rowNodeConnectionApi", "kademliaRepository", "rowConnectionInfo"})
    public KademliaSyncRepositoryNode<ROWConnectionInfo, Integer, String> kademliaSyncRepositoryNode(ROWNodeConnectionApi rowNodeConnectionApi, ROWConnectionInfo rowConnectionInfo, KademliaRepository<Integer, String> kademliaRepository){
        KademliaSyncRepositoryNode<ROWConnectionInfo, Integer, String> node = new KademliaSyncRepositoryNode<>(nodeId, new RoutingTable<>(nodeId), rowNodeConnectionApi, rowConnectionInfo, kademliaRepository);
        node.setKademliaNodeListener(new RedistributionKademliaNodeListener<>());
        return node;
    }

}
