package org.twuni.evy;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PublisherTest {

	protected boolean success;

	@Before
	public void setUp() {
		success = false;
	}

	@Test
	public void testEmptyProgramWithNoSubscriptionsExecutesWithoutFailure() {
		Publisher publisher = new Publisher();
		publisher.reset();
	}

	@Test
	public void testMultiLineProgramWithAncestralLookup() {

		Publisher publisher = new Evy( "@ say message\n  print message\nsay message=\"Hello\"" );

		success = false;

		publisher.subscribe( "print", new Subscriber() {

			@Override
			public void onPublish( Event event ) {
				Assert.assertEquals( "\"Hello\"", event.get( "message" ) );
				Assert.assertEquals( "\"Hello\"", event.get( 0 ) );
				Assert.assertNull( event.get( 1 ) );
				Assert.assertNull( event.get( "print" ) );
				success = true;
			}

		} );

		publisher.publish();

		Assert.assertTrue( success );

	}

	@Test
	public void testSingleLineProgramWithMatchingSubscriptionRunsSubscription() {

		Publisher root = new Publisher();

		root.subscribe( "test", new Subscriber() {

			@Override
			public void onPublish( Event statement ) {
				success = true;
			}

		} );

		root.publish( "test" );

		Assert.assertTrue( success );

	}

	@Test
	public void testSingleLineProgramWithNamedParametersAndMatchingSubscription() {

		Publisher root = new Publisher();

		root.subscribe( "print", new Subscriber() {

			@Override
			public void onPublish( Event event ) {
				Assert.assertEquals( "\"Hello, world!\"", event.get( "message" ) );
				Assert.assertEquals( "\"Hello, world!\"", event.get( 0 ) );
				Assert.assertNull( event.get( 1 ) );
				Assert.assertNull( event.get( "print" ) );
			}

		} );

		root.publish( "print message=\"Hello, world!\"" );

	}

	@Test
	public void testSingleLineProgramWithNoSubscriptionsExecutesWithoutFailure() {
		Publisher root = new Publisher();
		root.publish( "test" );
	}

	@Test
	public void testSingleLineProgramWithUnmatchingSubscriptionDoesNotRunSubscription() {

		Publisher root = new Publisher();

		success = true;
		root.subscribe( "bar", new Subscriber() {

			@Override
			public void onPublish( Event event ) {
				success = false;
			}

		} );

		root.publish( "foo" );

		Assert.assertTrue( success );

	}

}
