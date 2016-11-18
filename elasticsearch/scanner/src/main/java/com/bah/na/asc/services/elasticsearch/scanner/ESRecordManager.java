package com.bah.na.asc.services.elasticsearch.scanner;

import java.net.UnknownHostException;
import java.util.List;

//import org.elasticsearch.action.index.IndexResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bah.na.asc.core.EnvProperty;

public class ESRecordManager {
    private static final Logger log = LoggerFactory.getLogger(ESRecordManager.class);

    static String ASC_ES_IP = EnvProperty.getInstance().getEnvVar("ASC_ES_IP");
    static String ASC_ES_TRANSPORT_PORT = EnvProperty.getInstance().getEnvVar("ASC_ES_TRANSPORT_PORT");
    static String ASC_ES_CLUSTER = EnvProperty.getInstance().getEnvVar("ASC_ES_CLUSTER");

    public static void main(String[] args) {

        int daysToExpire = 2;

        if (args.length == 0) {
            log.warn("Usage: <application> [daysToExpire]");
            log.warn("Defaulting daysToExpire to " + daysToExpire);
        } else {
            try {
                // try to convert the string argument to a number
                Integer iDaysToExpire = Integer.decode(args[0]);
                daysToExpire = iDaysToExpire.intValue();
            } catch (NumberFormatException e) {
                // the argument is not a valid integer
                log.error("Argument '" + args[0] + "' is not a valid integer");
                return;
            }
        }

        ESConnection connection = null;

        try {
            connection = new ESConnection(ASC_ES_IP, Integer.parseInt(ASC_ES_TRANSPORT_PORT));
        } catch (UnknownHostException uhe) {
            System.out.println("Unknown Host!");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create about_to_expire index
        // createAboutToExpireIndex(connection, daysToExpire);

        IdentifyNearExpiredRecords identifyNearExpiredRecords = new IdentifyNearExpiredRecords(connection);
        List<String> listExpiringRecords = identifyNearExpiredRecords.getExpiringRecords(daysToExpire);

        PostNearExpiredRecords postNearExpiredRecords = new PostNearExpiredRecords(connection);
        postNearExpiredRecords.setRecordNames(listExpiringRecords);

        try {
            postNearExpiredRecords.writeRecordMetadataToElasticsearch(daysToExpire);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Create about_to_expire index
     */
    /*
     * public static void createAboutToExpireIndex(ESConnection esConnection,
     * int daysToExpire) { IndexResponse response =
     * esConnection.getTransportClient().prepareIndex("about_to_expire_" +
     * daysToExpire, "expire", "1") .setSource() .get(); }
     */
}
