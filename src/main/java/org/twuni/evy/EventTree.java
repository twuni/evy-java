package org.twuni.evy;

import java.util.ArrayList;
import java.util.List;

public class EventTree extends Event {

	private final List<Event> pathCache = new ArrayList<Event>();

	public EventTree( String source ) {
		super( null );
		reset( source );
	}

	private List<Event> newPath() {
		pathCache.clear();
		return pathCache;
	}

	@Override
	public void reset( String source ) {

		super.reset( source );

		if( source == null ) {
			return;
		}

		List<Event> path = newPath();

		path.add( this );

		String [] lines = source.split( "\n" );

		for( int i = 0; i < lines.length; i++ ) {

			String line = lines[i];

			Event parent = path.get( 0 );
			Event event = new Event( parent, line );

			if( event.isEmpty() ) {
				continue;
			}

			if( event.getDepth() > parent.getDepth() ) {
				parent.getChildren().add( event );
				path.add( 0, event );
			} else {
				path.remove( 0 );
				i--;
			}

		}

		path.clear();

	}

}
