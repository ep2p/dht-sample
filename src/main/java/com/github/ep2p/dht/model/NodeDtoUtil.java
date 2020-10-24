package com.github.ep2p.dht.model;

import com.github.ep2p.kademlia.node.Node;

public class NodeDtoUtil {

    public static Node<ROWConnectionInfo> getCopyNode(Node<ROWConnectionInfo> node){
        return new Node<ROWConnectionInfo>(node.getId(), node.getConnectionInfo(), null);
    }

}
