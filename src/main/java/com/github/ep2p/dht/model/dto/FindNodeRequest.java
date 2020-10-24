package com.github.ep2p.dht.model.dto;

import com.github.ep2p.dht.model.ROWConnectionInfo;
import com.github.ep2p.kademlia.node.Node;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindNodeRequest extends BasicRequest {
    private Integer lookupId;

    public FindNodeRequest(Node<ROWConnectionInfo> caller, Integer lookupId) {
        super(caller);
        this.lookupId = lookupId;
    }

    public FindNodeRequest(Integer lookupId) {
        this.lookupId = lookupId;
    }

    public FindNodeRequest() {
    }
}
