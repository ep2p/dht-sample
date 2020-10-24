package com.github.ep2p.dht.model.dto;

import com.github.ep2p.dht.model.ROWConnectionInfo;
import com.github.ep2p.kademlia.node.Node;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreRequest extends BasicRequest {
    private Node<ROWConnectionInfo> requester;
    private Integer key;
    private String value;

    public StoreRequest(Node<ROWConnectionInfo> requester) {
        this.requester = requester;
    }

    public StoreRequest(Node<ROWConnectionInfo> caller, Node<ROWConnectionInfo> requester) {
        super(caller);
        this.requester = requester;
    }

    public StoreRequest() {
    }
}
