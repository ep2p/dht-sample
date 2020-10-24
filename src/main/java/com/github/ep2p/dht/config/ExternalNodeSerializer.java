package com.github.ep2p.dht.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.github.ep2p.kademlia.node.ExternalNode;

import java.io.IOException;

public class ExternalNodeSerializer extends JsonSerializer<ExternalNode> {

    @Override
    public void serialize(ExternalNode externalNode, JsonGenerator jgen, SerializerProvider serializerProvider) throws IOException {
        jgen.writeStartObject();
        jgen.writeNumberField("id", externalNode.getId());
        jgen.writeNumberField("distance", externalNode.getDistance());
        jgen.writeObjectField("connectionInfo", externalNode.getConnectionInfo());
        jgen.writeEndObject();
    }
}
