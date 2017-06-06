package util;

/** 
 * Names of properties used in this project.
 * The strings are the <b>names</b> of properties (the keys) that
 * will be read from a properties file.
 * 
 * The enum member PROPERTY_FILENAME contains the name of the properties file.
 * 
 * @see util.PropertyManager
 */
public enum Property {
	
	/** The name of the properties file. 
	 * The properties file should be on the CLASSPATH.
	 * This value is used by PropertyManager to load properties from a file.
	 */
	PROPERTY_FILENAME("typingthrower.config"),
	
	/** Property names for JDBC properties. */
	DATABASE_URL("jdbc.url"),
	DATABASE_USER("jdbc.user"),
	DATABASE_PASSWORD("jdbc.password"),
	/** property name for JDBC Driver class. "jdbc.drivers" is the standard name. */
	DATABASE_DRIVER("jdbc.drivers"),
	/** characterEncoding is a MySQL property: it sets default character encoding. */
	DATABASE_CHARSET("characterEncoding"),
	/** property names for IP address and port of game server. */
	SERVER_ADDRESS("server.addr"),
	SERVER_PORT("server.port");
	
	
	private Property(String propname) {
		this.name = propname;
	}
	/** String name (key) of the property as in the properties file. */
	public final String name;
	
	@Override
	public String toString() {
		return name;
	}
}
