package org.twuni.evy;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EvyTest {

	protected boolean success;

	@Before
	public void setUp() {
		success = false;
	}

	@Test
	public void testMultiLineProgramWithAncestralLookup() {

		StatementTreeExecutor root = new Evy( "on say message\n  print message\nsay message=\"Hello\"" );

		success = false;
		root.subscribe( "print", new StatementExecutor() {

			@Override
			public void execute( Statement statement ) {
				Assert.assertEquals( "\"Hello\"", statement.lookup( "message" ) );
				Assert.assertEquals( "\"Hello\"", statement.lookup( 0 ) );
				Assert.assertNull( statement.lookup( 1 ) );
				Assert.assertNull( statement.lookup( "print" ) );
				success = true;
			}

		} );

		root.execute();

		Assert.assertTrue( success );

	}

	@Test
	public void testMultiLineProgramWithSeveralSubscriptionsRunsAsExpected() {
		StatementTreeExecutor root = new Evy( "on error\n  print message\nerror message=\"haha u suck\"" );
		success = false;
		root.subscribe( "print", new StatementExecutor() {

			@Override
			public void execute( Statement statement ) {
				success = true;
				Assert.assertEquals( "\"haha u suck\"", statement.lookup( 0 ) );
				Assert.assertEquals( "\"haha u suck\"", statement.lookup( "message" ) );
			}

		} );
		root.execute();
		Assert.assertTrue( success );
	}

}
