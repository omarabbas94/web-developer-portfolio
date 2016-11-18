package com.bah.na.asc.services.elasticsearch.scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.bah.na.asc.core.EnvProperty;

public class IdentifyNearExpiredRecordsTest
{
	protected static final String esIp = EnvProperty.getInstance().getEnvVar("ASC_ES_IP");
	protected static final String esPort = EnvProperty.getInstance().getEnvVar("ASC_ES_PORT");
	protected static final String esTransportPort = EnvProperty.getInstance().getEnvVar("ASC_ES_TRANSPORT_PORT");
	protected static final String SEARCH_QUERYSTRING = "1234567890-bob.brown@bah.com-2017-02-16/_search";
	protected static final String TEST_TYPE = "tp";
	ESConnection connection = null;
	/**
	 * Ingest
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp()
			throws Exception {
		
		try {
			connection = new ESConnection(esIp, Integer.parseInt(esTransportPort));
		} catch (UnknownHostException uhe) {
			System.out.println("Unknown Host!" );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	/*URL jsonFileUrl = getClass().getResource("/data/uid-email-expirationdate.json");
		byte[] buffer = Files.readAllBytes(Paths.get(jsonFileUrl.toURI()));
		// Bulk Ingest Test Data
		try (TransportClient transportClient = connection.getTransportClient()) {
			BulkRequestBuilder bulkBuilder = transportClient.prepareBulk();
			bulkBuilder.add(buffer, 0, buffer.length);
			bulkBuilder.get();
			// Wait a few seconds
			Thread.sleep(4000);*/
	}
	/**
	 * Ingest and Search for Data and ensure proper results returned
	 * 
	 * @throws Exception
	 */
	@Test
	public void testIngestAndIdentify()
			throws Exception {
		IdentifyNearExpiredRecords i= new IdentifyNearExpiredRecords(connection);
		List<String> recordsList = i.getExpiringRecords(2);
		assertTrue(recordsList.contains("1234567892-rachel.red@bah.com-2016-11-18"));
	}
}
