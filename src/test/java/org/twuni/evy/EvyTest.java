package org.twuni.evy;

import java.io.File;
import java.io.IOException;

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
	public void testSecondChoiceOfLikeEventNameIsTraversible() {
		success = false;
		Publisher root = new Evy( "@ start a=789\n  @ test a=123\n    FAIL\n  @ test a=456\n    PASS\nstart a=789\ntest a=456" ) {

			@Override
			protected void registerDefaultSubscriptions() {

				super.registerDefaultSubscriptions();

				subscribe( "PASS", new Subscriber() {

					@Override
					public void onPublish( Event event ) {
						success = true;
					}

				} );

				subscribe( "FAIL", new Subscriber() {

					@Override
					public void onPublish( Event event ) {
						Assert.fail();
					}

				} );

			}

		};
		Assert.assertNotNull( root );
		Assert.assertTrue( success );
	}

	@Test
	public void testSecondChoiceWhenNestingIsTraversible() {
		success = false;
		Publisher root = new Evy( "@ a\n @ b1\n  @ c1\n   d1\n @ b2\n  @ c2\n   d2\n  @ c3\n   d3\na\nb2\nc3" ) {

			@Override
			protected void registerDefaultSubscriptions() {
				super.registerDefaultSubscriptions();
				subscribe( "d3", new Subscriber() {

					@Override
					public void onPublish( Event event ) {
						success = true;
					}

				} );
			}

		};
		Assert.assertNotNull( root );
		Assert.assertTrue( success );
	}

	@Test
	public void testSubscribersShouldNotInterruptTheirSiblings() throws IOException {

		success = false;

		Evy evy = Evy.fromFile( new File( "src/test/resources/test_sibling_interruption.ev" ) );

		evy.subscribe( "PASS", new Subscriber() {

			@Override
			public void onPublish( Event event ) {
				success = true;
			}

		} );
		evy.publish( "START" );
		Assert.assertTrue( "Subscriber prevented its next sibling from being consulted.", success );
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
