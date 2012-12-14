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

		Publisher root = new Evy( "@ say message\n  print message\nsay message=\"Hello\"" );

		success = false;
		root.subscribe( "print", new Subscriber() {

			@Override
			public void onPublish( Event statement ) {
				Assert.assertEquals( "\"Hello\"", statement.get( "message" ) );
				Assert.assertEquals( "\"Hello\"", statement.get( 0 ) );
				Assert.assertNull( statement.get( 1 ) );
				Assert.assertNull( statement.get( "print" ) );
				success = true;
			}

		} );

		root.publish();

		Assert.assertTrue( success );

	}

	@Test
	public void testMultiLineProgramWithSeveralSubscriptionsRunsAsExpected() {
		Publisher root = new Evy( "@ error\n  print message\nerror message=\"haha u suck\"" );
		success = false;
		root.subscribe( "print", new Subscriber() {

			@Override
			public void onPublish( Event statement ) {
				success = true;
				Assert.assertEquals( "\"haha u suck\"", statement.get( 0 ) );
				Assert.assertEquals( "\"haha u suck\"", statement.get( "message" ) );
			}

		} );
		root.publish();
		Assert.assertTrue( success );
	}

	@Test
	public void testMultipleExecutions() {

		Evy evy = new Evy( "@ happens\n  test\n@@ happening\n  else" );

		success = true;

		evy.subscribe( "test", new Subscriber() {

			@Override
			public void onPublish( Event statement ) {
				success = false;
			}

		} );

		evy.subscribe( "else", new Subscriber() {

			@Override
			public void onPublish( Event statement ) {
				success = false;
			}

		} );

		Assert.assertTrue( success );

		evy.publish();

		success = true;
		evy.publish( new Event( "happens" ) );
		Assert.assertFalse( success );

		success = true;
		evy.publish( new Event( "happens" ) );
		Assert.assertTrue( success );

		success = true;
		evy.publish( "happening" );
		Assert.assertFalse( success );

		success = true;
		evy.publish( "happening" );
		Assert.assertFalse( success );

	}

	@Test
	public void testSubscriptionWithComplicatedStuff() {
		Evy root = new Evy( "@ Greet\n  NPC says=\"Hi\"\n  Consider saying=\"Sup\"\n  Consider saying=\"Yo\"\n  @ Player says=\"Yo\"\n    Smack\n  @ Player says=\"Sup\"\n    Nod\n" );
		success = false;
		root.subscribe( "Nod", new Subscriber() {

			@Override
			public void onPublish( Event statement ) {
				success = true;
			}

		} );
		root.subscribe( "Smack", new Subscriber() {

			@Override
			public void onPublish( Event statement ) {
				Assert.fail( "Should NOT get smacked in this scenario." );
			}

		} );
		root.publish();
		root.publish( "Greet" );
		root.publish( new Event( "Player says=\"Sup\"" ) );
		Assert.assertTrue( success );
		success = false;
		root.publish( new Event( "Player says=\"Sup\"" ) );
		Assert.assertFalse( success );
	}

	@Test
	public void testSubscriptionWithParameterQualifierDoesNotTriggerWhenEventIsPublishedWithoutThoseParameters() {
		Evy root = new Evy( "@ taste chicken\n  say \"Yum!\"\n@ taste cauliflower\n  say \"Eww, nasty!\"" );
		root.subscribe( "say", new Subscriber() {

			@Override
			public void onPublish( Event statement ) {
				if( !"Yum!".equals( statement.get( 0 ).split( "\"" )[1] ) ) {
					Assert.fail( "Should never say anything except Yum! when tasting chicken." );
				}
			}

		} );
		root.publish( "taste chicken" );
	}

}
