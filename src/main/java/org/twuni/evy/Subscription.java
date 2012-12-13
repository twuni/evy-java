package org.twuni.evy;

public class Subscription implements Subscriber {

	protected final Publisher root;

	public Subscription( Publisher root ) {
		this.root = root;
	}

	/**
	 * @param context
	 *            The context in which this executor will run.
	 * @param eventName
	 *            The event to which this executor will subscribe.
	 */
	protected Subscriber createExecutor( Event context, String eventName, Event parent ) {
		return new IndirectSubscriber( root, context, parent );
	}

	@Override
	public void onPublish( Event event ) {
		String eventName = event.get( 0 );
		for( int i = 0; i < event.getChildren().size(); i++ ) {
			Event child = event.getChildren().get( i );
			root.subscribe( eventName, createExecutor( child, eventName, event ) );
		}
	}

}
