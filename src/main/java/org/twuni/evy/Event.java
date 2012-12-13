package org.twuni.evy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Event {

	private static final Pattern IDENTIFIER = Pattern.compile( "^[^\\d\"\\.\\+\\-'\\s][^\"\\.\\+\\-'\\s]*", Pattern.CASE_INSENSITIVE );
	private static final EventParser PARSER = new EventParser();
	private static final String EMPTY = "";

	private final Event parent;
	private final String name;
	private final int depth;
	private final List<Event> children = new ArrayList<Event>();
	private final Map<String, String> symbols = new HashMap<String, String>();
	private final List<String> values = new ArrayList<String>();
	private final List<String []> parameters;

	public Event( Event parent, String source ) {
		this.parent = parent;
		if( source != null ) {
			name = source.replaceAll( "^\\s+", "" );
			depth = source.length() - name.length();
			parameters = PARSER.parse( name );
		} else {
			name = null;
			depth = -1;
			parameters = new ArrayList<String []>();
		}
	}

	public Event( String source ) {
		this( null, source );
	}

	public void copySymbolsFrom( Event statement ) {
		symbols.putAll( statement.symbols );
	}

	public List<Event> getChildren() {
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

	public Event getParent() {
		return parent;
	}

	public boolean isEmpty() {
		return name == null || EMPTY.equals( name );
	}

	public int length() {
		return values.size();
	}

	public String get( int index ) {
		return values.size() > index ? values.get( index ) : null;
	}

	public String get( String key ) {
		String value = symbols.get( key );
		if( value == null && parent != null ) {
			value = parent.get( key );
		}
		return value;
	}

	/**
	 * Discards the event state.
	 */
	public void reset() {
		symbols.clear();
		values.clear();
		parameters.clear();
		children.clear();
	}

	/**
	 * Discards the event state.
	 * 
	 * @param source
	 *            If applicable, this specifies a new event state.
	 */
	public void reset( String source ) {
		reset();
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
				String value = get( parameter[1] );
				if( value != null ) {
					parameter[0] = parameter[1];
					parameter[1] = value;
				}
			}
			values.add( i, parameter[1] );
		}
	}

}
