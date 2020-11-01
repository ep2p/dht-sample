package com.github.ep2p.dht.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.ep2p.kademlia.connection.ConnectionInfo;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ROWConnectionInfo implements ConnectionInfo {
    private String address;
    private int port;

    @JsonIgnore
    public String getHttpAddress() {
        return "http://"+ address + ":" + port;
    }

    @JsonIgnore
    public String getFullAddress(){
        return "ws://"+address+":"+port+"/ws";
    }

}
