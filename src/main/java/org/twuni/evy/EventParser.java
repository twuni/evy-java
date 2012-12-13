package org.twuni.evy;

import java.util.ArrayList;
import java.util.List;

public class EventParser {

	private static final String SPACE = " ";
	private static final String EQUALS = "=";
	private static final String QUOTE = "\"";

	public List<String []> parse( String line ) {
		return parse( line, 0 );
	}

	public List<String []> parse( String line, int start ) {
		return parse( line, start, new ArrayList<String []>() );
	}

	public List<String []> parse( String line, int start, List<String []> parameters ) {

		if( start >= line.length() ) {
			return parameters;
		}

		int space = line.indexOf( SPACE, start );
		int equals = line.indexOf( EQUALS, start );
		int quote = line.indexOf( QUOTE, start );

		if( space < 0 ) {
			space = line.length();
		}
		if( quote < 0 ) {
			quote = line.length() + 1;
		}
		if( equals < 0 ) {
			equals = line.length() + 2;
		}

		String token = line.substring( start, space );

		if( quote < space ) {
			int matchingQuote = line.indexOf( QUOTE, quote + 1 );
			token = line.substring( start, matchingQuote + 1 );
		}

		int end = start + token.length();

		String [] parameter = new String [2];

		if( equals < space ) {
			parameter[0] = line.substring( start, equals );
			parameter[1] = line.substring( equals + 1, end );
		} else {
			parameter[0] = null;
			parameter[1] = token;
		}

		parameters.add( parameter );

		return parse( line, end + 1, parameters );

	}

}
