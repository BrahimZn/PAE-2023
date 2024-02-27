package be.vinci.pae.exception;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

/**
 * Represents an exception that is thrown when a user is not authorized to access a resource. This
 * exception extends the WebApplicationException class.
 */
public class UnauthorizedException extends WebApplicationException {

  /**
   * Constructs a new UnauthorizedException with a default message. The exception's response status
   * is set to UNAUTHORIZED.
   */
  public UnauthorizedException() {
    super(Response.status(Response.Status.UNAUTHORIZED).build());
  }

  /**
   * Constructs a new UnauthorizedException with a custom message. The exception's response status
   * is set to UNAUTHORIZED.
   *
   * @param message The custom message for the exception.
   */
  public UnauthorizedException(String message) {
    super(Response.status(Response.Status.UNAUTHORIZED).entity(message).type("text/plain").build());
  }
}