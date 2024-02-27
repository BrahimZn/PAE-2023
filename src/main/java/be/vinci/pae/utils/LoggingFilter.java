package be.vinci.pae.utils;

import be.vinci.pae.main.Main;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Cette annotation indique que cette classe est un provider JAX-RS.
 */
@Provider
public class LoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

  private static final Logger LOGGER = LogManager.getLogger(Main.class.getName());

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    LOGGER.info(String.format("Request %s %s", requestContext.getMethod(),
        requestContext.getUriInfo().getRequestUri()));
  }

  @Override
  public void filter(ContainerRequestContext requestContext,
      ContainerResponseContext responseContext) throws IOException {
    LOGGER.info(String.format("Response %d %s", responseContext.getStatus(),
        responseContext.getStatusInfo().getReasonPhrase()));
  }
}
