package com.github.ep2p.dht.model.dto;

import com.github.ep2p.dht.model.NodeDtoUtil;
import com.github.ep2p.dht.model.ROWConnectionInfo;
import com.github.ep2p.kademlia.node.Node;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BasicRequest {
    private Node<ROWConnectionInfo> caller;

    public BasicRequest(Node<ROWConnectionInfo> caller) {
        this.caller = NodeDtoUtil.getCopyNode(caller);
    }

    public void setCaller(Node<ROWConnectionInfo> caller) {
        this.caller = NodeDtoUtil.getCopyNode(caller);
    }
}
