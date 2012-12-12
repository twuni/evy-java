package org.twuni.evy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class StatementTreeExecutor implements StatementExecutor {

	private static final StatementParser PARSER = new StatementParser();
	private static final Pattern PUBLICATION = Pattern.compile( "^([^ ]+)(?: (.+))?$", Pattern.CASE_INSENSITIVE );

	private final StatementTree root;
	private final Map<String, List<StatementExecutor>> events = new HashMap<String, List<StatementExecutor>>();

	public StatementTreeExecutor( String program ) {
		root = new StatementTree( program );
	}

	public void execute() {
		execute( root );
	}

	@Override
	public void execute( Statement statement ) {
		if( !statement.isEmpty() ) {
			String eventName = PUBLICATION.matcher( statement.getName() ).replaceAll( "$1" );
			List<String []> parameters = PARSER.parse( PUBLICATION.matcher( statement.getName() ).replaceAll( "$2" ) );
			publish( eventName, parameters, statement );
		} else {
			for( int i = 0; i < statement.getChildren().size(); i++ ) {
				execute( statement.getChildren().get( i ) );
			}
		}
	}

	public void execute( String program ) {
		execute( new StatementTree( program ) );
	}

	public void publish( String eventName, List<String []> parameters ) {
		publish( eventName, parameters, root );
	}

	public void publish( String eventName, List<String []> parameters, Statement context ) {
		List<StatementExecutor> event = events.get( eventName );
		if( event == null ) {
			return;
		}
		if( context == null ) {
			context = root;
		}
		for( int i = 0; i < event.size(); i++ ) {
			context.setSymbols( parameters );
			event.get( i ).execute( context );
		}
	}

	public void subscribe( String eventName, StatementExecutor executor ) {
		List<StatementExecutor> event = events.get( eventName );
		if( event == null ) {
			event = new ArrayList<StatementExecutor>();
			events.put( eventName, event );
		}
		event.add( executor );
	}

	public void unsubscribe( String eventName, StatementExecutor executor ) {
		List<StatementExecutor> event = events.get( eventName );
		if( event == null ) {
			return;
		}
		event.remove( executor );
	}

}
