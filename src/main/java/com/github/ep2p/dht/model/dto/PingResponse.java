package com.github.ep2p.dht.model.dto;

import com.github.ep2p.dht.model.ROWConnectionInfo;
import com.github.ep2p.kademlia.model.PingAnswer;
import com.github.ep2p.kademlia.node.Node;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PingResponse extends BasicResponse {
    private PingAnswer pingAnswer;

    public PingResponse(PingAnswer pingAnswer) {
        this.pingAnswer = pingAnswer;
    }

    public PingResponse(Node<ROWConnectionInfo> node, PingAnswer pingAnswer) {
        super(node);
        this.pingAnswer = pingAnswer;
    }

    public PingResponse() {
    }
}
