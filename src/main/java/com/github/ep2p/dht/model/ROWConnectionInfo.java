package com.github.ep2p.dht.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.ep2p.kademlia.connection.ConnectionInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ROWConnectionInfo implements ConnectionInfo {
    private String address;
    private int port;

    @JsonIgnore
    public String getFullAddress(){
        return "ws://"+address+":"+port+"/ws";
    }

}
