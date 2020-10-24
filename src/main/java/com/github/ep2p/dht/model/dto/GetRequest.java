package com.github.ep2p.dht.model.dto;

import com.github.ep2p.dht.model.NodeDtoUtil;
import com.github.ep2p.dht.model.ROWConnectionInfo;
import com.github.ep2p.kademlia.node.Node;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GetRequest extends BasicRequest {
    private Node<ROWConnectionInfo> requester;
    private Integer key;

    public GetRequest(Node<ROWConnectionInfo> requester) {
        this.requester = requester;
    }

    public GetRequest(Node<ROWConnectionInfo> node, Node<ROWConnectionInfo> requester) {
        super(node);
        this.requester = NodeDtoUtil.getCopyNode(requester);
    }

    public void setRequester(Node<ROWConnectionInfo> requester) {
        this.requester = NodeDtoUtil.getCopyNode(requester);
    }

    public GetRequest() {
    }
}
