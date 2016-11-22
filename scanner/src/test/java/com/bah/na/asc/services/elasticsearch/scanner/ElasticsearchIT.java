package com.bah.na.asc.services.elasticsearch.scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.runners.MethodSorters;
import com.bah.na.asc.core.EnvProperty;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ElasticsearchIT{
	protected static final String esIp = EnvProperty.getInstance().getEnvVar("ASC_ES_IP");
	protected static final String esPort = EnvProperty.getInstance().getEnvVar("ASC_ES_PORT");
	protected static final String esTransportPort = EnvProperty.getInstance().getEnvVar("ASC_ES_TRANSPORT_PORT");
	protected static final String esClusterName = EnvProperty.getInstance().getEnvVar("ASC_ES_CLUSTER_NAME");

	protected static final String SEARCH_QUERYSTRING = "1234567890-bob.brown@bah.com-2017-02-16/_search";
	protected static final String TEST_INDEX = "1234567890-bob.brown@bah.com-2017-02-16";
	protected static final String TEST_TYPE = "tp";
	private WebTarget target;

	/**
	 * Uses the Javax web client to prepare a WebTarget. Checks if the Docker IP
	 * and ElasticSearch ports are environmental variables. If they are, it uses
	 * the system variables instead of a default.
	 * 
	 * @throws Exception
	 */
	@Ignore
	@Before
	public void setUp() throws Exception{
		Client client = ClientBuilder.newClient();
		target = client.target(String.format("http://%s:%s", esIp, esPort));
	}

	/**
	 * Makes a request of the WebTarget, and confirms that the request was
	 * successful by checking that the response status is 200.
	 */
	@Ignore
	@Test
	public void doTest(){
		Response r = target.request().get();
		assertEquals(200, r.getStatus());
	}

	/**
	 * Ingest and Search for Data and ensure proper results returned
	 * 
	 * @throws Exception
	 */
	@Ignore
	@Test
	public void test1IngestAndSearch() throws Exception{
		URL jsonFileUrl = getClass().getResource("/data/uid-email-expirationdate.json");
		byte[] buffer = Files.readAllBytes(Paths.get(jsonFileUrl.toURI()));
		// Bulk Ingest Test Data
		try(TransportClient transportClient = new ESConnection(esIp, Integer.parseInt(esTransportPort), esClusterName)
				.getTransportClient()){
			BulkRequestBuilder bulkBuilder = transportClient.prepareBulk();
			bulkBuilder.add(buffer, 0, buffer.length, TEST_INDEX, TEST_TYPE);
			bulkBuilder.get();
		}
		// Wait a few seconds
		Thread.sleep(4000);
		// Ensure we can Query it
		WebTarget webTarget = target.path(SEARCH_QUERYSTRING).queryParam("q", "type:Feature");
		Response r = webTarget.request().get();
		assertEquals(200, r.getStatus());
		r.bufferEntity();
		String result = r.readEntity(String.class);
		System.out.println(result);
		assertTrue(result.contains("bob"));
	}
}
