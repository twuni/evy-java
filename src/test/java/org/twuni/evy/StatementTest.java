package org.twuni.evy;

import org.junit.Assert;
import org.junit.Test;

public class StatementTest {

	@Test
	public void testEmptyStatement() {

		Statement statement = new Statement( "" );

		Assert.assertTrue( statement.isEmpty() );
		Assert.assertEquals( 0, statement.getDepth() );
		Assert.assertEquals( "", statement.getName() );
		Assert.assertTrue( statement.getChildren().isEmpty() );
		Assert.assertNull( statement.lookup( "anything" ) );
		Assert.assertNull( statement.lookup( null ) );
		Assert.assertNull( statement.lookup( 0 ) );

		statement.setSymbols( null );

	}

	@Test
	public void testSingleStatement() {

		Statement statement = new Statement( "This is a good statement." );

		Assert.assertFalse( statement.isEmpty() );
		Assert.assertEquals( 0, statement.getDepth() );
		Assert.assertEquals( "This is a good statement.", statement.getName() );
		Assert.assertTrue( statement.getChildren().isEmpty() );
		Assert.assertNull( statement.lookup( "anything" ) );
		Assert.assertNull( statement.lookup( null ) );
		Assert.assertNull( statement.lookup( 0 ) );

		statement.setSymbols( null );

	}

}
