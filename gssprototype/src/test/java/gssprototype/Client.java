package gssprototype;

import java.io.*;
import java.net.*;
import java.security.PrivilegedAction;
import java.util.Properties;
import javax.security.auth.Subject;
import javax.security.auth.login.*;
import org.ietf.jgss.*;
import sun.misc.BASE64Encoder;
 
@SuppressWarnings("restriction")
public class Client {
	static Oid KRB5_PRINCIPAL_NAME_OID;
	static {
        try
        {
            Oid KERB_V5_OID = new Oid("1.2.840.113554.1.2.2");
            
            KRB5_PRINCIPAL_NAME_OID = new Oid("1.2.840.113554.1.2.2.1");

        } catch (final GSSException ex)
        {
            throw new Error(ex);
        }
    }

 

public static void main( String[] args) {
	  
    try {
      // Setup up the Kerberos properties.
      Properties props = new Properties();
      //props.load( new FileInputStream( "/client.properties"));
      InputStream fis = ClassLoader.getSystemResourceAsStream("client.properties");
      props.load(new FileInputStream("C:\\Users\\Administrator\\Java_Projects\\gssprototype\\src\\test\\resources\\client.properties"));
//      props.load( fis);
      System.setProperty( "sun.security.krb5.debug", "true");
      System.setProperty( "java.security.krb5.realm", props.getProperty( "realm"));
      System.out.println(props.getProperty("realm"));
      System.setProperty( "java.security.krb5.kdc", props.getProperty( "kdc"));
      System.setProperty( "java.security.auth.login.config", "C:\\Users\\Administrator\\Java_Projects\\gssprototype\\src\\test\\resources\\jass.conf");
      System.setProperty( "javax.security.auth.useSubjectCredsOnly", "true");
      String username = props.getProperty( "client.principal.name");
      String password = props.getProperty( "client.password");
      // Oid mechanism = use Kerberos V5 as the security mechanism.
      krb5Oid = new Oid( "1.2.840.113554.1.2.2");
      final  Oid KRB5_PRINCIPAL_NAME_OID = new Oid("1.2.840.113554.1.2.2.1");
      Client client = new Client();
      // Login to the KDC.
      client.login( username, password);
      // Request the service ticket.
      String spn = props.getProperty( "service.principal.name");
      client.initiateSecurityContext(spn );
      // Write the ticket to disk for the server to read.
      String tmp = encodeAndWriteTicketToDisk( client.serviceTicket, "./security.token");
      Socket s = new Socket("localhost",7777);
      PrintWriter out = new PrintWriter(s.getOutputStream());
      out.println(tmp);
//      out.print(tmp);
      out.flush();
      out.close();
      s.close();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
 
  public Client() {
    super();
  }
  private static Oid krb5Oid;
  
  private Subject subject;
  private byte[] serviceTicket;
 
  // Authenticate against the KDC using JAAS.
  private void login( String username, String password) throws LoginException {
    LoginContext loginCtx = null;
    // "Client" references the JAAS configuration in the jaas.conf file.
    loginCtx = new LoginContext( "Client", new LoginCallbackHandler( username, password));
    System.out.println("trying to login");
    loginCtx.login();
    this.subject = loginCtx.getSubject();
  }
 
  // Begin the initiation of a security context with the target service.
  private void initiateSecurityContext( String servicePrincipalName)
      throws GSSException {
    GSSManager manager = GSSManager.getInstance();
    GSSName serverName = manager.createName( servicePrincipalName,
    		KRB5_PRINCIPAL_NAME_OID);
    final GSSContext context = manager.createContext( serverName, krb5Oid, null,
        GSSContext.DEFAULT_LIFETIME);
    // The GSS context initiation has to be performed as a privileged action.
    this.serviceTicket = Subject.doAs( subject, new PrivilegedAction<byte[]>() {
      public byte[] run() {
        try {
          byte[] token = new byte[0];
          // This is a one pass context initialisation.
          context.requestMutualAuth( false);
          context.requestCredDeleg( false);
          return context.initSecContext( token, 0, token.length);
        }
        catch ( GSSException e) {
          e.printStackTrace();
          return null;
        }
      }
    });
 
  }
//Base64 encode the raw ticket and write it to the given file.
 private static String encodeAndWriteTicketToDisk( byte[] ticket, String filepath)
     throws IOException {
   BASE64Encoder encoder = new BASE64Encoder();    
   FileWriter writer = new FileWriter( new File( filepath));
   String encodedToken = encoder.encode( ticket);
   writer.write( encodedToken);
   writer.close();
   return encodedToken;
 }
}