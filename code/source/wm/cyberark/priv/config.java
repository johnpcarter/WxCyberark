package wm.cyberark.priv;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.jc.cyberark.VaultChecker;
import com.jc.invoke.credentials.CredentialsInjector;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
// --- <<IS-END-IMPORTS>> ---

public final class config

{
	// ---( internal utility methods )---

	final static config _instance = new config();

	static config _newInstance() { return new config(); }

	static config _cast(Object o) { return (config)o; }

	// ---( server methods )---




	public static final void getConnections (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getConnections)>> ---
		// @sigtype java 3.5
		// [o] field:1:required connections
		IDataCursor c = pipeline.getCursor();
		IDataUtil.put(c, "connections", readFileAsStringList(SERVICES_FILE));
		c.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void getConnectionsFile (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getConnectionsFile)>> ---
		// @sigtype java 3.5
		// [o] field:0:required content
		IDataCursor c = pipeline.getCursor();
		IDataUtil.put(c, "content", new String(readFile(SERVICES_FILE)));
		c.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void getCyberarkProperties (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getCyberarkProperties)>> ---
		// @sigtype java 3.5
		// [o] field:0:required endpoint
		// [o] field:0:required AppID
		// [o] field:0:required Safe
		// [o] field:0:required SafeForUsers
		// [o] object:0:required suspendUsers
		// [o] object:0:required throwError
		// [o] field:0:required syncInterval
		IDataCursor c = pipeline.getCursor();
		
		String[] props = readFileAsStringList(PROPS_FILE);
		
		for (String p : props) {
			
			if (p.indexOf("=") != -1) {
				int i = p.indexOf("=");
				String k = p.substring(0, i);
				String v = p.substring(i+1);
				
				if (k.contentEquals("throwError"))
					IDataUtil.put(c, k, Boolean.parseBoolean(v));
				else
					IDataUtil.put(c, k, v);
			}
		}
		
		if (IDataUtil.get(c, "syncInterval") == null) {
			IDataUtil.put(c, "syncInterval", "0");
		}
		
		c.destroy();
			
		// --- <<IS-END>> ---

                
	}



	public static final void getUsers (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getUsers)>> ---
		// @sigtype java 3.5
		// [o] field:1:required users
		IDataCursor c = pipeline.getCursor();
		IDataUtil.put(c, "users", readFileAsStringList(USERS_FILE));
		c.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void getUsersFile (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getUsersFile)>> ---
		// @sigtype java 3.5
		// [o] field:0:required content
		IDataCursor c = pipeline.getCursor();
		IDataUtil.put(c, "content", new String(readFile(USERS_FILE)));
		c.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void recordTaskId (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(recordTaskId)>> ---
		// @sigtype java 3.5
		// [i] field:0:required id
		
		// pipeline in
		
		IDataCursor pipelineCursor = pipeline.getCursor();
		String id = IDataUtil.getString(pipelineCursor, "id");
		pipelineCursor.destroy();
		
		// process
		
		_taskId = id;
		// --- <<IS-END>> ---

                
	}



	public static final void registerInvokeInterceptor (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(registerInvokeInterceptor)>> ---
		// @sigtype java 3.5
		// [i] field:0:required endpoint
		// [i] field:0:required AppID
		// [i] field:0:required Safe
		// [i] object:0:required throwError
		// [i] field:1:required connections
		// pipeline in
		IDataCursor pipelineCursor = pipeline.getCursor();
		String endpoint = IDataUtil.getString(pipelineCursor, "endpoint");
		String appId = IDataUtil.getString(pipelineCursor, "AppID");
		String safe = IDataUtil.getString(pipelineCursor, "Safe");
		Boolean throwError = (Boolean) IDataUtil.get(pipelineCursor, "throwError");
		String[] connections = IDataUtil.getStringArray(pipelineCursor, "connections");
		pipelineCursor.destroy();
		
		// process
		
		ArrayList<String> allowed = new ArrayList<String>();
		HashMap<String, String> aliases = new HashMap<String, String>();
		
		if (throwError == null)
			throwError = true;
		
		if (connections != null) {
			for (String c : connections) {
				if (c.contains(";")) {
					int i = c.indexOf(";");
					String k = c.substring(0, i);
					String v = c.substring(i+1);
					allowed.add(k);
					
					System.out.println("setting up alias " + k + " = " + v);
					aliases.put(k, v);
				} else {
					allowed.add(c);
				}
			}
		}
		
		if (allowed.size() > 0) {
			CredentialsInjector.register(allowed.toArray(new String[allowed.size()]), new VaultChecker(endpoint, appId, safe, aliases), throwError);
		} else {
			CredentialsInjector.register(null, new VaultChecker(endpoint, appId, safe, aliases), throwError);
		}
		// --- <<IS-END>> ---

                
	}



	public static final void restoreTaskId (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(restoreTaskId)>> ---
		// @sigtype java 3.5
		// [o] field:0:required taskId
		// pipeline out
		
		IDataCursor pipelineCursor = pipeline.getCursor();
		IDataUtil.put(pipelineCursor, "taskId", _taskId);
		pipelineCursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void saveConnectionsFile (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(saveConnectionsFile)>> ---
		// @sigtype java 3.5
		// [i] field:0:required content
		IDataCursor c = pipeline.getCursor();
		String content = IDataUtil.getString(c, "content");
		
		writeFile(SERVICES_FILE, content.getBytes());
		c.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void saveCyberarkProperties (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(saveCyberarkProperties)>> ---
		// @sigtype java 3.5
		// [i] field:0:required endpoint
		// [i] field:0:required AppID
		// [i] field:0:required Safe
		// [i] field:0:required SafeForUsers
		// [i] field:0:required suspendUsers
		// [i] field:0:required throwError
		// [i] field:0:required syncInterval
		// pipeline in
		
		IDataCursor pipelineCursor = pipeline.getCursor();
		String endpoint = IDataUtil.getString(pipelineCursor, "endpoint");
		String appId = IDataUtil.getString(pipelineCursor, "AppID");
		String safe = IDataUtil.getString(pipelineCursor, "Safe");
		String safeForUsers = IDataUtil.getString(pipelineCursor, "SafeForUsers");
		String sync = IDataUtil.getString(pipelineCursor, "syncInterval");
		String suspendUsers =  IDataUtil.getString(pipelineCursor, "suspendUsers");
		String throwError = IDataUtil.getString(pipelineCursor, "throwError");
		pipelineCursor.destroy();
		
		// process
		
		String content = 	"endpoint=" + endpoint + System.lineSeparator() +
							"AppID=" + appId + System.lineSeparator() +
							"Safe=" + safe + System.lineSeparator() +
							"SafeForUsers=" + safeForUsers + System.lineSeparator() +
							"syncInterval=" + sync + System.lineSeparator();
		
		if (suspendUsers != null) 
			content += "suspendUsers=true" + System.lineSeparator();
		
		if (throwError != null)
			content += "throwError=true";
		
		writeFile(PROPS_FILE, content.getBytes());
		// pipeline out
		// --- <<IS-END>> ---

                
	}



	public static final void saveUsersFile (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(saveUsersFile)>> ---
		// @sigtype java 3.5
		// [i] field:0:required content
		IDataCursor c = pipeline.getCursor();
		String content = IDataUtil.getString(c, "content");
		
		writeFile(USERS_FILE, content.getBytes());
		c.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void tokenize (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(tokenize)>> ---
		// @sigtype java 3.5
		// [i] field:0:required instring
		// [i] field:0:required delimiter
		// [o] field:0:required string1
		// [o] field:0:required string2
		// pipeline in
		
		IDataCursor pipelineCursor = pipeline.getCursor();
		String instring = IDataUtil.getString(pipelineCursor, "instring");
		String delimiter = IDataUtil.getString(pipelineCursor, "delimiter");
		pipelineCursor.destroy();
		
		// process
		
		String string1 = instring;
		String string2 = instring;
		
		int i = instring.indexOf(delimiter != null ? delimiter : ";");
		
		if (i != -1) {
			string1 = instring.substring(0, i);
			string2 = instring.substring(i+1);
		}
		
		// pipeline out
		
		IDataUtil.put(pipelineCursor, "string1", string1);
		IDataUtil.put(pipelineCursor, "string2", string2);
		pipelineCursor.destroy();
		
			
		// --- <<IS-END>> ---

                
	}



	public static final void unregister (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(unregister)>> ---
		// @sigtype java 3.5
		CredentialsInjector.unregister();
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	
	private static String _taskId;
	
	private static final String PROPS_FILE = "./packages/WxCyberark/config/properties.cnf";
	private static final String SERVICES_FILE = "./packages/WxCyberark/config/connections.cnf";
	private static final String USERS_FILE = "./packages/WxCyberark/config/users.cnf";
	
	private static String[] readFileAsStringList(String fname) throws ServiceException {
				
		InputStream is = new ByteArrayInputStream(readFile(fname));
		
		List<String> lines = new ArrayList<String>();
		String line = null;
		
		try (
			BufferedReader rdr = new BufferedReader(new InputStreamReader(is));
		)
		{
			while ((line=rdr.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException e) {
			throw new ServiceException(e);
		}
			
		return lines.toArray(new String[lines.size()]);
	}
	
	private static byte[] readFile(String fname) throws ServiceException {
				
		if (fname == null)
			throw new ServiceException("provide file name please");
		
		// process
		
		byte[] data = null;
		
		try {
			data = Files.readAllBytes(Paths.get(fname));
		} catch(NoSuchFileException e) {
			
			data = new byte[0];
		} catch (IOException e) {
			
			throw new ServiceException(e);
		}
				
		return data;
	}
	
	private static void writeFile(String fname, byte[] content) throws ServiceException {
		
		if (fname == null)
			throw new ServiceException("provide file name please");
		
		// process
				
		try {
			Files.write(Paths.get(fname), content);
		} catch (NoSuchFileException e ) {
			throw new ServiceException(e);
		} catch (IOException e) {
			
			throw new ServiceException(e);
		}
	
	}
	// --- <<IS-END-SHARED>> ---
}

