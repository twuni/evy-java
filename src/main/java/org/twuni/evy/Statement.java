package org.twuni.evy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Statement {

	private static final Pattern IDENTIFIER = Pattern.compile( "^[^\\d\"\\.\\+\\-'\\s][^\"\\.\\+\\-'\\s]*", Pattern.CASE_INSENSITIVE );
	private static final StatementParser PARSER = new StatementParser();
	private static final String EMPTY = "";

	private final Statement parent;
	private final String name;
	private final int depth;
	private final List<Statement> children = new ArrayList<Statement>();
	private final Map<String, String> symbols = new HashMap<String, String>();
	private final List<String> values = new ArrayList<String>();
	private final List<String []> parameters;

	public Statement( Statement parent, String source ) {
		this.parent = parent;
		if( source != null ) {
			name = source.replaceAll( "^\\s+", "" );
			depth = source.length() - name.length();
			parameters = PARSER.parse( source );
		} else {
			name = null;
			depth = -1;
			parameters = new ArrayList<String []>();
		}
	}

	public Statement( String source ) {
		this( null, source );
	}

	public void copySymbolsFrom( Statement statement ) {
		symbols.putAll( statement.symbols );
	}

	public List<Statement> getChildren() {
		return children;
	}

	public int getDepth() {
		return depth;
	}

	public String getName() {
		return name;
	}

	public List<String []> getParameters() {
		return parameters;
	}

	public boolean isEmpty() {
		return name == null || EMPTY.equals( name );
	}

	public int length() {
		return values.size();
	}

	public String lookup( int index ) {
		return values.size() > index ? values.get( index ) : null;
	}

	public String lookup( String key ) {
		String value = symbols.get( key );
		if( value == null && parent != null ) {
			value = parent.lookup( key );
		}
		return value;
	}

	public void setSymbols( List<String []> parameters ) {
		values.clear();
		if( parameters == null ) {
			return;
		}
		for( int i = 0; i < parameters.size(); i++ ) {
			String [] parameter = parameters.get( i );
			if( parameter[0] != null ) {
				symbols.put( parameter[0], parameter[1] );
			} else if( parameter[1] != null && IDENTIFIER.matcher( parameter[1] ).matches() ) {
				String value = lookup( parameter[1] );
				if( value != null ) {
					parameter[0] = parameter[1];
					parameter[1] = value;
				}
			}
			values.add( i, parameter[1] );
		}
	}

}
