package org.twuni.evy;

public class IndirectSubscriber implements Subscriber {

	protected final Publisher publisher;
	protected final Event event;
	protected final Event parent;

	public IndirectSubscriber( Publisher publisher, Event event, Event parent ) {
		this.publisher = publisher;
		this.event = event;
		this.parent = parent;
	}

	@Override
	public void onPublish( Event triggeringEvent ) {
		if( parent != null ) {
			for( int i = 2; i < parent.getParameters().size(); i++ ) {
				String [] parameter = parent.getParameters().get( i );
				if( parameter[0] == null ) {
					if( triggeringEvent.get( parameter[1] ) == null ) {
						throw new RuntimeException( "No match!" );
					}
				} else {
					if( !parameter[1].equals( triggeringEvent.get( parameter[0] ) ) ) {
						throw new RuntimeException( "No match!" );
					}
				}
			}
		}
		event.copySymbolsFrom( triggeringEvent );
		publisher.publish( event );
	}

}
