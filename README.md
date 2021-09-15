## WxCyberark README

This package allows credentials to be replaced at runtime with values from your cyberark vault. Credentials are persisted locally and connections are pooled. Cyberark is queried if a new adapter connection cannot be created locally due to an "Access Denied" error. In which case any services trying to use a related adapter service are blocked until the fetched credentials from Cyberark are updated locally and the connection pool flushed.

# Setup

You must first restart the server after installing this package. To configure go to the package's home page e.g.

http://localhost:5555/WxCyberark

Edit the connection properties to provide the https endpoint of your cyberark server, ensuring you include the port, but not HTTPS.

Fill out the app-id that you have given to identify your webMethods platform with Cyberark. You will need to have created a certificate/private key and have the serial number assigned to the application in Cyberark. You will need to add the same certificate to your webMethods environment, which is explained in the section below.

Enter the Safe ID's that you want to use adapter connections and then local users.


# Configuring SSL authentication with Cyberark

Connections to Cyberark are secured via a certificate that you will need to have register in Cyberark. Create a keystore with the name "cyberark" for the given certificate and private key. You will also need to ensure that the CA you used to sign your certificate is managed by a truststore called "cyberark". You will need to register these stores with webMethods via Security -> Keystore.

**Extended Settings**

Set the following extended setting if cyberark specified a self-signed certificate or unrecognised CA, otherwise the connection will be refused

		watt.security.ssl.client.ignoreEmptyAuthoritiesList=true

# Specify connections to be managed

You need to specify a complete list of all the adapter connections that you want to have credentials synced with Cyberark. Click on the "edit connections" button and paste the full namespace of the adapter connection followed by a semicolon ';' and then the name of the Cyberark account object name. Make sure each connection is on a new line.

# Specify local users to be managed

You need to specify a complete list of all the local users that you want passwords to be synced from Cyberark. Click on the "edit users" button and add a list of the local users per line.

A scheduled service will be automatically set up to periodically sync local users with updated credentials in cyberark, based on the "Sync interval (Users)" property.

**WARNING** local users will be disabled if the sync with Cyberark fails for any reason, if the property "Disable users on error" is set to true.

# Making changes

Any changes that you make will not be used until after you reload this package.

# Trouble-shooting

Refer to the server log for any detailed error messages.
