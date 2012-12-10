package org.twuni.evy;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StatementTreeExecutorTest {

	protected boolean success;

	@Before
	public void setUp() {
		success = false;
	}

	@Test
	public void testEmptyProgramWithNoSubscriptionsExecutesWithoutFailure() {
		StatementTreeExecutor evy = new StatementTreeExecutor( "" );
		evy.execute();
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
	public void testSingleLineProgramWithMatchingSubscriptionRunsSubscription() {

		StatementTreeExecutor root = new StatementTreeExecutor( "test" );

		root.subscribe( "test", new StatementExecutor() {

			@Override
			public void execute( Statement statement ) {
				success = true;
			}

		} );

		root.execute();

		Assert.assertTrue( success );

	}

	@Test
	public void testSingleLineProgramWithNamedParametersAndMatchingSubscription() {

		StatementTreeExecutor root = new StatementTreeExecutor( "print message=\"Hello, world!\"" );

		root.subscribe( "print", new StatementExecutor() {

			@Override
			public void execute( Statement statement ) {
				Assert.assertEquals( "\"Hello, world!\"", statement.lookup( "message" ) );
				Assert.assertEquals( "\"Hello, world!\"", statement.lookup( 0 ) );
				Assert.assertNull( statement.lookup( 1 ) );
				Assert.assertNull( statement.lookup( "print" ) );
			}

		} );

		root.execute();

	}

	@Test
	public void testSingleLineProgramWithNoSubscriptionsExecutesWithoutFailure() {
		StatementTreeExecutor root = new StatementTreeExecutor( "test" );
		root.execute();
	}

	@Test
	public void testSingleLineProgramWithUnmatchingSubscriptionDoesNotRunSubscription() {

		StatementTreeExecutor root = new StatementTreeExecutor( "foo" );

		success = true;
		root.subscribe( "bar", new StatementExecutor() {

			@Override
			public void execute( Statement statement ) {
				success = false;
			}

		} );

		root.execute();

		Assert.assertTrue( success );

	}

}
