package org.twuni.evy;

import org.junit.Assert;
import org.junit.Test;

public class EventTreeTest {

	@Test
	public void testEmptyStatement() {

		EventTree statement = new EventTree( "" );

		Assert.assertTrue( statement.isEmpty() );
		Assert.assertEquals( -1, statement.getDepth() );
		Assert.assertEquals( null, statement.getName() );
		Assert.assertTrue( statement.getChildren().isEmpty() );
		Assert.assertNull( statement.get( "anything" ) );
		Assert.assertNull( statement.get( null ) );

		statement.setSymbols( null );

	}

	@Test
	public void testSingleStatement() {

		EventTree statement = new EventTree( "This is a good statement." );

		Assert.assertTrue( statement.isEmpty() );
		Assert.assertEquals( -1, statement.getDepth() );
		Assert.assertEquals( null, statement.getName() );
		Assert.assertEquals( 1, statement.getChildren().size() );
		Assert.assertNull( statement.get( "anything" ) );
		Assert.assertNull( statement.get( null ) );
		Assert.assertEquals( "This is a good statement.", statement.getChildren().get( 0 ).getName() );

		statement.setSymbols( null );

	}

	@Test
	public void testStatementTree() {

		EventTree statement = new EventTree( "go there\n  go here\n  go there again\nwe are done" );

		Assert.assertTrue( statement.isEmpty() );
		Assert.assertEquals( -1, statement.getDepth() );
		Assert.assertEquals( null, statement.getName() );

		Assert.assertNull( statement.get( "anything" ) );
		Assert.assertNull( statement.get( null ) );

		Assert.assertEquals( 2, statement.getChildren().size() );

		Assert.assertEquals( "go there", statement.getChildren().get( 0 ).getName() );

		Assert.assertEquals( 2, statement.getChildren().get( 0 ).getChildren().size() );
		Assert.assertEquals( "go here", statement.getChildren().get( 0 ).getChildren().get( 0 ).getName() );
		Assert.assertEquals( "go there again", statement.getChildren().get( 0 ).getChildren().get( 1 ).getName() );

		Assert.assertEquals( "we are done", statement.getChildren().get( 1 ).getName() );

		Assert.assertTrue( statement.getChildren().get( 1 ).getChildren().isEmpty() );

		statement.setSymbols( null );

	}

}
