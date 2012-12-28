package org.twuni.evy;

public class OneShotIndirectSubscriber extends IndirectSubscriber {

	private final String subscribedEventName;

	public OneShotIndirectSubscriber( Publisher publisher, Event event, String subscribedEventName, Event parent ) {
		super( publisher, event, parent );
		this.subscribedEventName = subscribedEventName;
	}

	@Override
	public void onPublish( Event triggeringEvent ) {
		try {
			super.onPublish( triggeringEvent );
			publisher.unsubscribe( subscribedEventName, this );
		} catch( MatchNotFoundException exception ) {
			// Let's just pretend this never happened and keep our subscription active until we do
			// find a match.
		}
	}

}
