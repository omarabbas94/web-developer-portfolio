package com.bah.na.asc.services.elasticsearch.scanner;

import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.elasticsearch.client.Client;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IdentifyNearExpiredRecords{

	private static final Logger log = LoggerFactory.getLogger(IdentifyNearExpiredRecords.class);

	private ESConnection connection;

	public IdentifyNearExpiredRecords(ESConnection connection){

		this.connection = connection;

	}

	// go through the list of all records on Elasticsearch and pull out the

	// ones that are about to expire

	public List<String> getExpiringRecords(int daysToExpire){

		ArrayList<String> expiringRecords = new ArrayList<String>();

		// Get API
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date dateobj = new Date();
		long timetoday = dateobj.getTime();
		long millisecondsToExpire = daysToExpire * 86400000L;
		long timeLater = timetoday + millisecondsToExpire;
		Date dateobjLater = new Date();
		dateobjLater.setTime(timeLater);
		String futureDate = df.format(dateobjLater);

		Client esclient = connection.getTransportClient();
		log.info("futureDate: " + futureDate);

		String[] indexList = esclient.admin().cluster().prepareState().execute().actionGet().getState().getMetaData()
				.concreteAllIndices();

		log.info("List of Indices:");

		for(String index : indexList){
			int expirationDateIndex = index.length() - 10;
			log.info("index: " + index);
			// log.info("expirationDateIndex: " + expirationDateIndex);

			if(expirationDateIndex > 0){
				String expirationDate = index.substring(expirationDateIndex);
				String datePattern = "[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]";
				// Create a Pattern object
				Pattern r = Pattern.compile(datePattern);
				// Now create matcher object.
				Matcher m = r.matcher(expirationDate);
				if(m.find()){
					log.info("expirationDate: " + expirationDate);
					if(expirationDate.equals(futureDate)){
						log.info("Adding index (" + index + ") to list of expiring indexes.");
						expiringRecords.add(index);
					}
				}else{
					log.info("expirationDate: Index has invalid format");
				}
			}else{
				log.info("expirationDate: Index has invalid format");
				// log.warn("ignoring malformed" + index);
			}

		}
		return expiringRecords;
	}

}
