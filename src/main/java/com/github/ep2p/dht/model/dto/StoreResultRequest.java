package com.github.ep2p.dht.model.dto;

import com.github.ep2p.dht.model.ROWConnectionInfo;
import com.github.ep2p.kademlia.node.Node;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreResultRequest extends BasicResponse {
    private Integer key;

    public StoreResultRequest(Node<ROWConnectionInfo> node) {
        super(node);
    }

    public StoreResultRequest() {
    }
}
