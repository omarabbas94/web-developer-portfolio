package com.bah.na.asc.services.elasticsearch.scanner;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

public class ESConnection {
    private TransportClient transportClient;
    
    ESConnection(String ipAddr, int port) throws UnknownHostException {
    	transportClient = TransportClient.builder().build()
            .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ipAddr), port));
    }
    
    public TransportClient getTransportClient() {
        return transportClient;
    }
}
