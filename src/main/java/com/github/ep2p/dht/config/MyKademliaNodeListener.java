package com.github.ep2p.dht.config;

import com.github.ep2p.kademlia.connection.ConnectionInfo;
import com.github.ep2p.kademlia.node.KademliaNode;
import com.github.ep2p.kademlia.node.RedistributionKademliaNodeListener;

public class MyKademliaNodeListener<C extends ConnectionInfo, K, V> extends RedistributionKademliaNodeListener<C,K,V> {

    @Override
    public void onBeforeShutdown(KademliaNode<C> kademliaNode) {
        System.out.println("onBeforeShutdown() is called");
        super.onBeforeShutdown(kademliaNode);
        System.out.println("Finished onBeforeShutdown()");
    }
}
