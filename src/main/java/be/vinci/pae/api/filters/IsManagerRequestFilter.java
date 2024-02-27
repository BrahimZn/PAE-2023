package be.vinci.pae.api.filters;

import be.vinci.pae.business.domain.UserDTO;
import be.vinci.pae.business.ucc.UserUCC;
import be.vinci.pae.main.Main;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class implements the JAX-RS filter interface, and is used to authorize access to a protected
 * resource. It checks whether the authenticated user has manager-level access, and returns a
 * "FORBIDDEN" response if they do not.
 */
@Singleton
@Provider
@IsManager
@Priority(2)
public class IsManagerRequestFilter implements ContainerRequestFilter {

  private final Logger logger = LogManager.getLogger(Main.class.getName());

  @Inject
  private UserUCC userUCC;

  /**
   * This is a method that filters incoming HTTP requests and checks whether the associated user has
   * manager-level access. If the user does not have manager-level access, it returns a "FORBIDDEN"
   * response. It uses the "@Context" annotation to access information about the request and the
   * "UserDTO" class to retrieve user information. This method may throw an IOException if an I/O
   * error occurs.
   *
   * @param containerRequestContext the request context for the incoming HTTP request
   * @throws IOException if an I/O error occurs while filtering the request
   */
  @Override
  public void filter(@Context ContainerRequestContext containerRequestContext) throws IOException {
    UserDTO authenticatedUser = (UserDTO) containerRequestContext.getProperty("user");
    if (authenticatedUser == null) {

      logger.info(
          "containerRequestContext = " + containerRequestContext.getProperty("user"));

      containerRequestContext.abortWith(
          Response.status(Status.FORBIDDEN).entity("You are forbidden to access this resource")
              .build());
      return;
    }

    logger.info("authenticatedUser.getFirstName() = " + authenticatedUser.getFirstName());

    if (!userUCC.userIsManager(authenticatedUser)) {
      containerRequestContext.abortWith(
          Response.status(Status.FORBIDDEN).entity("You are forbidden to access this resource")
              .build());
    }

  }
}
