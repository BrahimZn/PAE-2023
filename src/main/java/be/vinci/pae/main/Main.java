package be.vinci.pae.main;

import be.vinci.pae.utils.ApplicationBinder;
import be.vinci.pae.utils.Config;
import be.vinci.pae.utils.LoggingFilter;
import java.io.IOException;
import java.net.URI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;


/**
 * Main class.
 */
public class Main {

  static {
    Config.load("dev.properties");
  }

  /**
   * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
   *
   * @return Grizzly HTTP server.
   */
  public static HttpServer startServer() {
    // Base URI the Grizzly HTTP server will listen on
    String baseUri = Config.getProperty("BaseUri");
    // create a resource config that scans for JAX-RS resources and providers
    // in vinci.be package
    final ResourceConfig rc = new ResourceConfig().packages("be.vinci.pae.api")
        .register(ApplicationBinder.class).register(MultiPartFeature.class)
        .register(LoggingFilter.class);

    // create and start a new instance of grizzly http server
    // exposing the Jersey application at BASE_URI
    return GrizzlyHttpServerFactory.createHttpServer(URI.create(baseUri), rc);
  }

  /**
   * Main method.
   *
   * @param args command line arguments
   * @throws IOException if there is an input/output error
   */
  public static void main(String[] args) throws IOException {
    System.setProperty("log4j.xml", "main/resources/log4j.xml");
    final Logger logger = LogManager.getLogger(Main.class.getName());

    logger.info("Start of server");

    String baseUri = Config.getProperty("BaseUri");
    final HttpServer server = startServer();

    System.out.println(String.format("Jersey app started with WADL available at "
        + "%sapplication.wadl\nHit enter to stop it...", baseUri));

    System.in.read();

    server.stop();
  }
}
