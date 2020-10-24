package com.github.ep2p.dht.model.dto;

import com.github.ep2p.dht.model.ROWConnectionInfo;
import com.github.ep2p.kademlia.model.FindNodeAnswer;
import com.github.ep2p.kademlia.node.Node;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindNodeResponse extends BasicResponse {
    private FindNodeAnswer<ROWConnectionInfo> answer;

    public FindNodeResponse(Node<ROWConnectionInfo> node, FindNodeAnswer<ROWConnectionInfo> answer) {
        super(node);
        this.answer = answer;
    }

    public FindNodeResponse(FindNodeAnswer<ROWConnectionInfo> answer) {
        this.answer = answer;
    }

    public FindNodeResponse() {
    }
}
