package org.twuni.evy;

import org.junit.Assert;
import org.junit.Test;

public class EventTest {

	@Test
	public void testEmptyStatement() {

		Event statement = new Event( "" );

		Assert.assertTrue( statement.isEmpty() );
		Assert.assertEquals( 0, statement.getDepth() );
		Assert.assertEquals( "", statement.getName() );
		Assert.assertTrue( statement.getChildren().isEmpty() );
		Assert.assertNull( statement.get( "anything" ) );
		Assert.assertNull( statement.get( null ) );
		Assert.assertNull( statement.get( 0 ) );

		statement.setSymbols( null );

	}

	@Test
	public void testSingleStatement() {

		Event statement = new Event( "This is a good statement." );

		Assert.assertFalse( statement.isEmpty() );
		Assert.assertEquals( 0, statement.getDepth() );
		Assert.assertEquals( "This is a good statement.", statement.getName() );
		Assert.assertTrue( statement.getChildren().isEmpty() );
		Assert.assertNull( statement.get( "anything" ) );
		Assert.assertNull( statement.get( null ) );
		Assert.assertNull( statement.get( 0 ) );

		statement.setSymbols( null );

	}

}
