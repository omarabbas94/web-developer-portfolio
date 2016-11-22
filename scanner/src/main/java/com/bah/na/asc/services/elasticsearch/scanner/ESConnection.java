package com.bah.na.asc.services.elasticsearch.scanner;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

public class ESConnection{
	private TransportClient transportClient;

	ESConnection(String ipAddr, int port, String ASC_ES_CLUSTER_NAME) throws UnknownHostException{
		Settings settings = Settings.builder().put("cluster.name", ASC_ES_CLUSTER_NAME).build();
		transportClient = TransportClient.builder().settings(settings).build()
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ipAddr), port));

	}

	public TransportClient getTransportClient(){
		return transportClient;
	}

	public void close(){
		transportClient.close();
	}
}
