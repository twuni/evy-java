package org.twuni.evy;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class EventParserTest {

	private static final EventParser STATEMENT = new EventParser();

	@Test
	public void testEmptyStatement() {
		List<String []> parameters = STATEMENT.parse( "" );
		Assert.assertTrue( parameters.isEmpty() );
	}

	@Test
	public void testStatementWithNamedToken() {
		List<String []> parameters = STATEMENT.parse( "test=123" );
		Assert.assertEquals( 1, parameters.size() );
		Assert.assertEquals( "test", parameters.get( 0 )[0] );
		Assert.assertEquals( "123", parameters.get( 0 )[1] );
	}

	@Test
	public void testStatementWithNamedTokenAndUnnamedToken() {
		List<String []> parameters = STATEMENT.parse( "test again=123" );
		Assert.assertEquals( 2, parameters.size() );
		Assert.assertNull( parameters.get( 0 )[0] );
		Assert.assertEquals( "test", parameters.get( 0 )[1] );
		Assert.assertEquals( "again", parameters.get( 1 )[0] );
		Assert.assertEquals( "123", parameters.get( 1 )[1] );
	}

	@Test
	public void testStatementWithSeveralNamedTokens() {
		List<String []> parameters = STATEMENT.parse( "test=123 again=456" );
		Assert.assertEquals( 2, parameters.size() );
		Assert.assertEquals( "test", parameters.get( 0 )[0] );
		Assert.assertEquals( "123", parameters.get( 0 )[1] );
		Assert.assertEquals( "again", parameters.get( 1 )[0] );
		Assert.assertEquals( "456", parameters.get( 1 )[1] );
	}

	@Test
	public void testStatementWithSeveralUnnamedTokens() {
		List<String []> parameters = STATEMENT.parse( "test again" );
		Assert.assertEquals( 2, parameters.size() );
		Assert.assertNull( parameters.get( 0 )[0] );
		Assert.assertEquals( "test", parameters.get( 0 )[1] );
		Assert.assertNull( parameters.get( 1 )[0] );
		Assert.assertEquals( "again", parameters.get( 1 )[1] );
	}

	@Test
	public void testStatementWithSingleUnnamedToken() {
		List<String []> parameters = STATEMENT.parse( "test" );
		Assert.assertEquals( 1, parameters.size() );
		Assert.assertNull( parameters.get( 0 )[0] );
		Assert.assertEquals( "test", parameters.get( 0 )[1] );
	}
}
