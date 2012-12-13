package org.twuni.evy;

public class OneShotSubscription extends Subscription {

	public OneShotSubscription( Publisher root ) {
		super( root );
	}

	@Override
	protected Subscriber createExecutor( Event context, String eventName, Event parent ) {
		return new OneShotIndirectSubscriber( root, context, eventName, parent );
	}

}
