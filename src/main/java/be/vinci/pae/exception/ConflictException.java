package be.vinci.pae.exception;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Represents an exception that is thrown when a user is not authorized to access a resource. This
 * exception extends the WebApplicationException class.
 */
public class ConflictException extends WebApplicationException {

  /**
   * Constructs a new exception with {@code null} as its detail message. The cause is not
   * initialized, and may subsequently be initialized by a call to {@link #initCause}.
   */
  public ConflictException() {
    super(Response.status(Status.CONFLICT).build());

  }

  /**
   * Constructs a new exception with the specified detail message.  The cause is not initialized,
   * and may subsequently be initialized by a call to {@link #initCause}.
   *
   * @param message the detail message. The detail message is saved for later retrieval by the
   *                {@link #getMessage()} method.
   */
  public ConflictException(String message) {
    super(Response.status(Status.CONFLICT).entity(message).type("text/plain").build());
  }


}
