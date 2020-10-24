package com.github.ep2p.dht.model.dto;

import com.github.ep2p.dht.model.NodeDtoUtil;
import com.github.ep2p.dht.model.ROWConnectionInfo;
import com.github.ep2p.kademlia.node.Node;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BasicResponse {
    private Node<ROWConnectionInfo> node;

    public BasicResponse(Node<ROWConnectionInfo> node) {
        this.node = NodeDtoUtil.getCopyNode(node);
    }

    public void setCaller(Node<ROWConnectionInfo> node) {
        this.node = NodeDtoUtil.getCopyNode(node);
    }
}
