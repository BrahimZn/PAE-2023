package be.vinci.pae.api.filters;

import be.vinci.pae.business.domain.UserDTO;
import be.vinci.pae.business.ucc.UserUCC;
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

/**
 * This class implements the JAX-RS filter interface, and is used to authorize access to a protected
 * resource. It checks whether the authenticated user has manager-level access, and returns a
 * "FORBIDDEN" response if they do not.
 */
@Singleton
@Provider
@IsHelper
@Priority(2)
public class IsHelperRequestFilter implements ContainerRequestFilter {

  @Inject
  private UserUCC userUCC;

  /**
   * This is a method that filters incoming HTTP requests and checks whether the associated user has
   * manager-level access or helper-level access. If the user does not have manager-level access or
   * helper-level acess, it returns a "FORBIDDEN" response. It uses the "@Context" annotation to
   * access information about the request and the "UserDTO" class to retrieve user information. This
   * method may throw an IOException if an I/O error occurs.
   *
   * @param containerRequestContext the request context for the incoming HTTP request
   * @throws IOException if an I/O error occurs while filtering the request
   */
  @Override
  public void filter(@Context ContainerRequestContext containerRequestContext) throws IOException {
    UserDTO authenticatedUser = (UserDTO) containerRequestContext.getProperty("user");
    if (!userUCC.userIsManager(authenticatedUser) && !userUCC.userIsHelper(authenticatedUser)) {
      containerRequestContext.abortWith(
          Response.status(Status.FORBIDDEN).entity("You are forbidden to access this resource")
              .build());
    }
  }
}
