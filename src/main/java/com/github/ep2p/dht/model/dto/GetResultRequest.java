package com.github.ep2p.dht.model.dto;

import com.github.ep2p.dht.model.ROWConnectionInfo;
import com.github.ep2p.kademlia.node.Node;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetResultRequest extends BasicRequest {
    private Integer key;
    private String value;

    public GetResultRequest(Node<ROWConnectionInfo> caller) {
        super(caller);
    }

    public GetResultRequest() {
    }
}
