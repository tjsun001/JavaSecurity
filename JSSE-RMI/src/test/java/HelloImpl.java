/*
 *
 * Copyright (c) 1994, 2004, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 *
 * -Redistribution of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * Redistribution in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in
 * the documentation and/or other materials provided with the
 * distribution.
 *
 * Neither the name of Oracle nor the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN MICROSYSTEMS, INC. ("SUN") AND ITS LICENSORS SHALL
 * NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT
 * OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR
 * ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 */

import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;

public class HelloImpl extends UnicastRemoteObject implements Hello {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int PORT = 2019;

    public HelloImpl() throws Exception {
        super(PORT
//        		,
//        		new RMISSLClientSocketFactory(),
//                new RMISSLServerSocketFactory()
////        		new MyClientSocket(),
//       		new MyServerSocket());
//        		new SslRMIClientSocketFactory(),
//             new SslRMIServerSocketFactory()
//        new SslRMIServerSocketFactory());
        		);
    }

    public String sayHello() {
        return "Hello World!";
    }

    public static void main(String args[]) {

        // Create and install a security manager
//        if (System.getSecurityManager() == null) {
//            System.setSecurityManager(new SecurityManager());
//        }

    	System.setProperty("javax.net.ssl.keyStore","C:\\Users\\Administrator\\Downloads\\JSSE-RMI\\src\\test\\resources\\keystore\\server\\testkeys.store");
    	System.setProperty("javax.net.ssl.keyStorePassword","passphrase");
    	System.setProperty("javax.net.ssl.trustStore","C:\\Users\\Administrator\\Downloads\\JSSE-RMI\\src\\test\\resources\\truststore\\samplecacerts.store");
    	
    	try {
            // Create SSL-based registry
            Registry registry = LocateRegistry.createRegistry(PORT
//            		,
//            		new MyClientSocket(),
//           		new MyServerSocket()
//            		new SslRMIClientSocketFactory(),
//                    new SslRMIServerSocketFactory()
//            new RMISSLClientSocketFactory(),
//            new RMISSLServerSocketFactory()
            		);

            HelloImpl obj = new HelloImpl();

            // Bind this object instance to the name "HelloServer"
            registry.rebind("HelloServer", obj);

            System.out.println("HelloServer bound in registry");
        } catch (Exception e) {
            System.out.println("HelloImpl err: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
