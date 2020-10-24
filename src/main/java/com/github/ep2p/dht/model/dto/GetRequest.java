package com.github.ep2p.dht.model.dto;

import com.github.ep2p.dht.model.ROWConnectionInfo;
import com.github.ep2p.kademlia.node.Node;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetRequest extends BasicResponse {
    private Node<ROWConnectionInfo> requester;
    private Integer key;

    public GetRequest(Node<ROWConnectionInfo> requester) {
        this.requester = requester;
    }

    public GetRequest(Node<ROWConnectionInfo> node, Node<ROWConnectionInfo> requester) {
        super(node);
        this.requester = requester;
    }

    public GetRequest() {
    }
}
