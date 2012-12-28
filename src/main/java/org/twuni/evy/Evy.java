package org.twuni.evy;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

/**
 * The Evy publisher is an enhancement to Publisher, providing the ability for events to subscribe
 * to other events.
 */
public class Evy extends Publisher {

	/**
	 * Constructs a new Evy publisher from the script in the specified file.
	 * 
	 * @param file
	 *            the file containing the Evy script to initially load.
	 * @return an Evy publisher initialized with the script in the specified file.
	 * @throws IOException
	 *             if there is a problem reading the file.
	 */
	public static Evy fromFile( File file ) throws IOException {

		Reader reader = new FileReader( file );
		char [] buffer = new char [4096];
		StringBuilder program = new StringBuilder();

		for( int size = reader.read( buffer, 0, buffer.length ); size > 0; size = reader.read( buffer, 0, size ) ) {
			program.append( buffer, 0, size );
		}

		reader.close();

		return new Evy( program.toString() );

	}

	/**
	 * Convenience method for <code>Evy.fromFile(File)</code>.
	 * 
	 * @param pathToFile
	 *            the path to pass into the <code>File</code> constructor.
	 * @return an Evy publisher initialized with the script at the specified filename.
	 * @throws IOException
	 *             if there is a problem reading the file.
	 */
	public static Evy fromFile( String pathToFile ) throws IOException {
		return fromFile( new File( pathToFile ) );
	}

	/**
	 * Constructs a new Evy publisher with an empty script.
	 */
	public Evy() {
		super();
	}

	/**
	 * Constructs a new Evy publisher initialized with the specified script.
	 * 
	 * @param script
	 *            this script will be run immediately after the publisher is constructed.
	 */
	public Evy( String script ) {
		super( script );
	}

	/**
	 * The Evy publisher registers two subscriptions by default: <code>@</code> and <code>@@</code>.
	 * These two subscriptions allow Evy scripts to create their own subscriptions. The
	 * <code>@</code> event specifies a single-use subscription which expires after it has been
	 * triggered once, and the <code>@@</code> event specifies a persistent subscription, which may
	 * be triggered many times.
	 */
	@Override
	protected void registerDefaultSubscriptions() {

		super.registerDefaultSubscriptions();

		Subscription oneShot = new OneShotSubscription( this );
		Subscription forever = new Subscription( this );

		subscribe( "@", oneShot );
		subscribe( "@@", forever );

	}

}
