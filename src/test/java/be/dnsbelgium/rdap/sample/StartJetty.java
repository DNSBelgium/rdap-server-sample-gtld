package be.dnsbelgium.rdap.sample;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Date;

public class StartJetty {

  private final static Logger logger = LoggerFactory.getLogger(StartJetty.class);

  public static void main(String[] args) {
    startWebApp();
  }

  private static void startWebApp() {
    Server server = new Server();
    ServerConnector connector = new ServerConnector(server);

    connector.setIdleTimeout(1000 * 60 * 60);
    connector.setSoLingerTime(-1);
    connector.setPort(3030);
    server.setConnectors(new Connector[]{connector});

    WebAppContext webAppContext = new WebAppContext();
    webAppContext.setServer(server);

    String path = "sample/src/main/webapp";
    path = findWarPath(path);
    System.out.println("path = " + path);

    webAppContext.setWar(path);

    server.setHandler(webAppContext);

    try {
      String msg = "http://localhost:3030/";
      log("Starting " + msg + " (please wait)...");
      server.start();
      log("Started " + msg);
      System.out.println("PRESS ANY KEY TO STOP");
      while (System.in.available() == 0) {
        Thread.sleep(5000);
      }
      log("Stopping the webapp...");
      webAppContext.stop();
      log("The webapp was stopped.");

      server.stop();
      server.join();
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(100);
    }

  }

  private static void log(String message) {
    String msg = String.format("%tT : %s", new Date(), message);
    logger.info(msg);
    System.out.println(msg);
  }

  private static String findWarPath(String warPath) {
    if (warPath == null) {
      return null;
    }
    if (!new File(warPath).exists()) {
      return findWarPath(shortenPath(warPath));
    } else {
      return warPath;
    }
  }

  private static String shortenPath(String path) {
    int indexOfSlash = path.indexOf("/");
    if (indexOfSlash != -1) {
      return path.substring(indexOfSlash + 1);
    } else {
      return null;
    }
  }

}
