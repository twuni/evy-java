package org.twuni.evy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Publisher {

	private static final EventParser PARSER = new EventParser();
	private static final Pattern PUBLICATION = Pattern.compile( "^([^ ]+)(?: (.+))?$", Pattern.CASE_INSENSITIVE );

	private final Event root;
	private final Map<String, List<Subscriber>> events = new HashMap<String, List<Subscriber>>();

	public Publisher() {
		this( "" );
	}

	public Publisher( Event root ) {
		this.root = root;
		publish( root );
	}

	public Publisher( String program ) {
		this( new EventTree( program ) );
	}

	/**
	 * Gets a snapshot of all subscribers for the given event.
	 * 
	 * @param eventName
	 *            the name of the subscribed event.
	 * @return all subscribers for that event.
	 */
	protected Subscriber [] getSubscribers( String eventName ) {

		List<Subscriber> subscribers = events.get( eventName );

		if( subscribers == null ) {
			return new Subscriber [0];
		}

		Subscriber [] copy = new Subscriber [subscribers.size()];

		for( int i = 0; i < copy.length; i++ ) {
			copy[i] = subscribers.get( i );
		}

		return copy;

	}

	/**
	 * Publishes the root event.
	 */
	public void publish() {
		publish( root );
	}

	/**
	 * Publishes the given event.
	 * 
	 * @param event
	 *            The event to be published. If the event itself is empty, then its children will be
	 *            published instead. Otherwise, the event is parsed and its first token is used as
	 *            the event name, subsequent tokens are used for its parameters, and the event
	 *            itself is used as the publication context.
	 */
	public void publish( Event event ) {
		if( !event.isEmpty() ) {
			String eventName = PUBLICATION.matcher( event.getName() ).replaceAll( "$1" );
			List<String []> parameters = PARSER.parse( PUBLICATION.matcher( event.getName() ).replaceAll( "$2" ) );
			publish( eventName, parameters, event );
		} else {
			for( int i = 0; i < event.getChildren().size(); i++ ) {
				publish( event.getChildren().get( i ) );
			}
		}
	}

	/**
	 * Publishes an event with no parameters.
	 * 
	 * @param eventSource
	 *            The source of the event to publish. May be an event name, an event with
	 *            parameters, or an Evy-formatted event tree.
	 */
	public void publish( String eventSource ) {
		if( eventSource != null && eventSource.indexOf( ' ' ) > -1 ) {
			publish( eventSource.indexOf( '\n' ) > -1 ? new EventTree( eventSource ) : new Event( eventSource ) );
			return;
		}
		publish( eventSource, null );
	}

	/**
	 * Publishes an event with the given parameters.
	 * 
	 * @param eventName
	 *            The name of the event to publish.
	 * @param parameters
	 *            The list of parameters for the event.
	 */
	public void publish( String eventName, List<String []> parameters ) {
		publish( eventName, parameters, root );
	}

	/**
	 * Publishes an event with the given parameters, to be executed under the given context.
	 * 
	 * @param eventName
	 *            The name of the event to publish.
	 * @param parameters
	 *            The list of parameters for the event.
	 * @param context
	 *            The event under whose context the event will be published. This context is passed
	 *            on to subscribers. If context is null, the root context will be used instead.
	 */
	public void publish( String eventName, List<String []> parameters, Event context ) {

		if( context == null ) {
			context = root;
		}

		if( events.isEmpty() ) {
			registerDefaultSubscriptions();
		}

		Subscriber [] triggered = getSubscribers( eventName );

		for( Subscriber subscriber : triggered ) {
			context.setSymbols( parameters );
			subscriber.onPublish( context );
		}

	}

	/**
	 * Override this method to register subscribers that should always be present, regardless of
	 * program state.
	 */
	protected void registerDefaultSubscriptions() {
		// The default implementation has no inherent subscriptions.
	}

	/**
	 * Clears all subscribers, then publishes the root event (to reset this publisher to its initial
	 * state).
	 */
	public void reset() {
		events.clear();
		registerDefaultSubscriptions();
		publish( root );
	}

	/**
	 * Resets the root event to the specified source, then clears all subscribers publishes the root
	 * event.
	 * 
	 * @param source
	 *            The new source for the root event.
	 */
	public void reset( String source ) {
		root.reset( source );
		reset();
	}

	/**
	 * Subscribes the given subscriber to the given event.
	 * 
	 * @param eventName
	 *            The name of the event to which to subscribe.
	 * @param subscriber
	 *            The object to be subscribed.
	 */
	public void subscribe( String eventName, Subscriber subscriber ) {
		List<Subscriber> subscribers = events.get( eventName );
		if( subscribers == null ) {
			subscribers = new ArrayList<Subscriber>();
			events.put( eventName, subscribers );
		}
		subscribers.add( subscriber );
	}

	/**
	 * Unsubscribes the given subscriber from the given event. Counterbalance to
	 * <code>subscribe(String,Subscriber)</code>.
	 * 
	 * @param eventName
	 *            The name of the subscribed event.
	 * @param subscriber
	 *            The subscribed object to be removed.
	 */
	public void unsubscribe( String eventName, Subscriber subscriber ) {
		List<Subscriber> subscribers = events.get( eventName );
		if( subscribers == null ) {
			return;
		}
		subscribers.remove( subscriber );
	}

}
