package org.twuni.evy;

import org.junit.Assert;
import org.junit.Test;

public class StatementTreeTest {

	@Test
	public void testEmptyStatement() {

		StatementTree statement = new StatementTree( "" );

		Assert.assertTrue( statement.isEmpty() );
		Assert.assertEquals( -1, statement.getDepth() );
		Assert.assertEquals( null, statement.getName() );
		Assert.assertTrue( statement.getChildren().isEmpty() );
		Assert.assertNull( statement.lookup( "anything" ) );
		Assert.assertNull( statement.lookup( null ) );

		statement.setSymbols( null );

	}

	@Test
	public void testSingleStatement() {

		StatementTree statement = new StatementTree( "This is a good statement." );

		Assert.assertTrue( statement.isEmpty() );
		Assert.assertEquals( -1, statement.getDepth() );
		Assert.assertEquals( null, statement.getName() );
		Assert.assertEquals( 1, statement.getChildren().size() );
		Assert.assertNull( statement.lookup( "anything" ) );
		Assert.assertNull( statement.lookup( null ) );
		Assert.assertEquals( "This is a good statement.", statement.getChildren().get( 0 ).getName() );

		statement.setSymbols( null );

	}

	@Test
	public void testStatementTree() {

		StatementTree statement = new StatementTree( "go there\n  go here\n  go there again\nwe are done" );

		Assert.assertTrue( statement.isEmpty() );
		Assert.assertEquals( -1, statement.getDepth() );
		Assert.assertEquals( null, statement.getName() );

		Assert.assertNull( statement.lookup( "anything" ) );
		Assert.assertNull( statement.lookup( null ) );

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
