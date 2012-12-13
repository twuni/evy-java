package org.twuni.evy;

public class Evy extends Publisher {

	public Evy() {
		this( "" );
	}

	public Evy( String program ) {
		super( program );
		reset();
	}

	@Override
	public void reset() {

		super.reset();

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
