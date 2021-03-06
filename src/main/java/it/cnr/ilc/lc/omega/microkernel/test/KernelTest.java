/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.microkernel.test;

import it.cnr.ilc.lc.omega.microkernel.test.component.ClientComponent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import sirius.kernel.di.std.Part;
import sirius.kernel.di.std.Register;

/**
 *
 * @author angelo
 */

@Register(classes = KernelTest.class)
public class KernelTest {
    
   //private static final Part<ClientComponent> component = Part.of(ClientComponent.class);
    
    @Part
    private static ClientComponent component;

    private static final int DEFAULT_PORT = 7777;
    private static ClassLoader loader = ClassLoader.getSystemClassLoader();

    public static void main(String[] args) {
        String test = "una stringa di prova";
        String res = test;
        System.err.println(res);

        boolean kill = Boolean.parseBoolean(System.getProperty("kill"));
        int port = DEFAULT_PORT;
        if (System.getProperty("port") != null) {
            port = Integer.parseInt(System.getProperty("port"));
        }
        // When we're started as windows service, the start/stop command and port are passed in
        // as arguments
        if (args.length == 2) {
            if ("stop".equals(args[0])) {
                kill = true;
            }
            port = Integer.parseInt(args[1]);
        } else if (args.length == 1) {
            port = Integer.parseInt(args[0]);
        }
        if (port > 0) {
            if (kill) {
                kill(port);
            } else {
                kickstart(port);
            }
        }
    }

    /**
     * Kills an app by opening a connection to the lethal port.
     */
    private static void kill(int port) {
        try {
            System.out.println("Killing localhost: " + port);
            long now = System.currentTimeMillis();
            Socket socket = new Socket("localhost", port);
            socket.getInputStream().read();
            System.out.println("Kill succeeded after: " + (System.currentTimeMillis() - now) + " ms");
        } catch (Exception e) {
            System.out.println("Kill failed: ");
            e.printStackTrace();
        }
    }

    /*
     * Sets up a classloader and loads <tt>Sirius</tt> to initialize the framework.
     */
    private static void kickstart(int port) {
        boolean debug = true;
        //boolean debug = Boolean.parseBoolean(System.getProperty("debug"));
        boolean ide = Boolean.parseBoolean(System.getProperty("ide"));
        File home = new File(System.getProperty("user.dir"));
        System.out.println("IDE Flag: " + ide);
        System.out.println("I N I T I A L   P R O G R A M   L O A D");
        System.out.println("---------------------------------------");
        System.out.println("IPL from: " + home.getAbsolutePath());

        if (!ide) {
            List<URL> urls = new ArrayList<>();
            try {
                File jars = new File(home, "lib");
                if (jars.exists()) {
                    for (URL url : allJars(jars)) {
                        if (debug) {
                            System.out.println(" - Classpath: " + url);
                        }
                        urls.add(url);
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                File classes = new File(home, "app");
                if (classes.exists()) {
                    if (debug) {
                        System.out.println(" - Classpath: " + classes.toURI().toURL());
                    }
                    urls.add(classes.toURI().toURL());
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            loader = new URLClassLoader(urls.toArray(new URL[urls.size()]), loader);
            Thread.currentThread().setContextClassLoader(loader);
        } else {
            System.out.println("IPL from IDE: not loading any classes or jars!");
        }

        try {
            System.out.println("IPL completed - Loading Sirius as stage2...");
            System.out.println();
            System.setProperty("debug", "true");
            Class.forName("sirius.kernel.Setup", true, loader)
                    .getMethod("createAndStartEnvironment", ClassLoader.class)
                    .invoke(null, loader);
            
//            final KernelTest test = new KernelTest();
//            Thread testThread = new Thread(new Runnable() {
//
//                @Override
//                public void run() {
//                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//                    test.test();
//                }
//            });
//            testThread.start();
//            
            
            test();
            waitForLethalConnection(port);
            
            System.exit(0);
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Waits until a connection to the given port is made.
     */
    private static void waitForLethalConnection(int port) {
        try {
            System.out.printf("Opening port %d as shutdown listener%n", port);
            ServerSocket socket = new ServerSocket(port);
            try {
                Socket client = socket.accept();
                System.out.println("C L O S I N G   M I C R O K E R N E L");
                System.out.println("---------------------------------------");
                Class.forName("sirius.kernel.Sirius", true, loader).getMethod("stop").invoke(null);
                client.close();
            } finally {
                socket.close();
                System.out.println("M I C R O K E R N E L  C L O S E D");
                System.out.println("---------------------------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*
     * Enumerates all jars in the given directory
     */
    private static List<URL> allJars(File libs) throws MalformedURLException {
        List<URL> urls = new ArrayList<>();
        if (libs.listFiles() != null) {
            for (File file : libs.listFiles()) {
                if (file.getName().endsWith(".jar")) {
                    urls.add(file.toURI().toURL());
                }
            }
        }
        return urls;
    }

    private static void test() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        System.err.println("Test component in MicroKernel!");
//        Classpath cp = sirius.kernel.Sirius.getClasspath();
//        for (URL url : cp.getComponentRoots()) {
//             System.out.println(url);
//        }
        
        component.go();
    }
}
