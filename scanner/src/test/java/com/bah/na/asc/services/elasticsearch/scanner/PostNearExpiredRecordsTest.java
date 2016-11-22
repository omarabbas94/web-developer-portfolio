package com.bah.na.asc.services.elasticsearch.scanner;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostNearExpiredRecordsTest{
	private static final Logger log = LoggerFactory.getLogger(PostNearExpiredRecordsTest.class);

	@BeforeClass
	public static void beforeClass() throws Exception{
		log.debug("Begin executing PostNearExpiredRecordsTest test suite");
	}

	@AfterClass
	public static void afterClass() throws Exception{
		log.debug("End executing PostNearExpiredRecordsTest test suite");
	}

	@Before
	public void beforeEachTest(){
		log.debug("Begin executing a PostNearExpiredRecordsTest test");
	}

	@After
	public void afterEachTest(){
		log.debug("End executing a PostNearExpiredRecordsTest test");
	}

	@Test
	public void testA(){
		assertTrue(true);
	}

}
