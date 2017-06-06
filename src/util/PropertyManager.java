package util;

import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/** 
 * Manage properties for this application.
 * Uses static behavior and a private Properties object to manage properties.
 * To specify a properties file, the first call should be setPropertiesFile(FILENAME)
 */
public class PropertyManager {	
	/** This static block defines a custom format for Logger messages. 
	 *  I don't like the default 2-line format, so this defines a short 1-line format.  
	 *  This static block should go in the Main class so its executed once before anything else.
	 */
    static {
        // %1=datetime %3=loggername %4=level %5=message
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "%1$tF %1$tT %3$s %4$-7s %5$s%n");
    }
    
	// java.util.Properties object that will hold all properties for the app.
    // property names/values are loaded from a file.
	private static Properties properties = null;
	// Print log messages using java.util.logging.Logger
	private static final Logger logger = Logger.getLogger(PropertyManager.class.getSimpleName());	

	
	/** 
	 * Private constructor to prevent other objects from creating an instance.
	 * Use static methods to access properties.
	 */
	private PropertyManager() {
		// You could call loadProperties() here to force early failure.
	}

	/**
	 * Read properties from a file.  The property filename can be specified
	 * in either Config.java or at system level using java -Dproperties=/path/filename
	 * on by setting an environment variable.
	 */
	private static void loadProperties( ) {
		if ( properties != null ) return; // already loaded
		
		// What's the name of properties file?
		// If user specifies it on command line or as environment variable,
		// then use that value.  Otherwise, use default property file name.
		
		// For example, user can type:
		// java -Dproperties=testing.properties typingthrower.Main
		// and this class will use "testing.properties" as name of the properties file.
		//
		// If there is no environment or command-line variable for typingthrower.config
		// then the default name (also typingthrower.config) is used.
		
		String filename = System.getProperty( "properties", Property.PROPERTY_FILENAME.name);
		logger.info("loading properties from "+filename);
		
		InputStream instream = null;
		properties = new Properties();
		ClassLoader loader = PropertyManager.class.getClassLoader();
		try {
			instream = loader.getResourceAsStream( filename );
			properties.load( instream );
		} catch (java.io.IOException ex) {
			logger.log(Level.SEVERE, "couldn't load properties from " + filename, ex );
		} finally {
			if ( instream != null ) try { instream.close(); } catch(Exception e) {/* ignore it */}
		}

	}
	
	/** get a property value from the current properties.
	 *  If the property does not have a value then an empty string is returned.
	 *  @param property is the property to get, using name specified in Property enum
	 *  @return value of the property, or empty string if its not known
	 */
	public static String getProperty( Property property ) {
		if ( properties == null )  loadProperties();
		return properties.getProperty( property.name, "" /*default value*/ );
	}
	
	
	/** 
	 *  Get a property value using a String name.
	 *  This provides a way to get properties that aren't named in the Property enum.
	 *  @param property name (key) of the property to get
	 *  @return current value of the property
	 */
	public static String getProperty( String property ) {
		if ( properties == null )  loadProperties();
		return properties.getProperty( property, "" /*default value*/ );
	}
	
	/**
	 * Set the value of a property.  The value overrides the existing value,
	 * but only for duration of this process (new value not saved to a file).
	 * 
	 * @param property is the Property member to set
	 * @param newvalue is the new value for this property
	 */
	public static void putProperty( Property property, String newvalue ) {
		if ( properties == null )  loadProperties();
		properties.setProperty( property.name, newvalue );
	}

	/** save the values of Properties to a file as plain text 'key=value'
	 * @param filename is the name of the file to write properties to.
	 */
	public static void saveProperties( String filename ) {
		if ( properties == null )  loadProperties();
		try {
			java.io.FileOutputStream fout = new java.io.FileOutputStream( filename );
			// add a comment line to properties file
			properties.store(fout, 
					"properties saved on "+ java.util.Calendar.getInstance().toString() );
			fout.close();
		} catch ( java.io.IOException ex ) { 
			logger.log(Level.SEVERE, "error saving properties to " + filename, ex );
		}
	}
	
	/** 
	 * Return a reference to the Properties object.
	 * Any changes to the properties will affect values returned later,
	 * so caller should be careful not to change the properties unless
	 * its deliberate.
	 * 
	 * @return the current properties
	 */
	public static Properties getProperties() {
		if ( properties == null ) loadProperties();
		return properties;
	}
	
	/**
	 * For testing use of PropertyManager.
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Getting all properties from PropertyManager\n");
		Properties props = PropertyManager.getProperties();
		/* print them all */
		for( Object obj : props.keySet() ) {
			String key = (String)obj;
			System.out.printf("%s=%s\n", key, properties.getProperty(key) );
		}
		
		/** Get named properties using keys specified in the Property enum.
		 * This is how you would use properties in an application to avoid
		 * accidentally specifying the wrong key name.
		 */
		System.out.println("\n\nGet individual properties by name:\n");
		System.out.printf("%s = %s\n", Property.DATABASE_URL, getProperty(Property.DATABASE_URL));
		System.out.printf("%s = %s\n", Property.DATABASE_USER, getProperty(Property.DATABASE_USER));
		System.out.printf("%s = %s\n", Property.DATABASE_DRIVER, getProperty(Property.DATABASE_DRIVER));
		
	}

}
