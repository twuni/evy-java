package org.twuni.evy;

public class Evy extends StatementTreeExecutor {

	public Evy() {
		this( "" );
	}

	public Evy( String program ) {

		super( program );

		Subscription oneShot = new OneShotSubscription( this );
		Subscription forever = new Subscription( this );

		subscribe( "on", oneShot );
		subscribe( "@", oneShot );
		subscribe( "when", oneShot );
		subscribe( "next", oneShot );

		subscribe( "@@", forever );
		subscribe( "whenever", forever );
		subscribe( "each", forever );
		subscribe( "every", forever );

	}

}
