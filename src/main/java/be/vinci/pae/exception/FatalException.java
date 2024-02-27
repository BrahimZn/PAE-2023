package be.vinci.pae.exception;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * This exception represents a fatal error that occurred during the execution of a web application.
 * It extends the WebApplicationException class to provide an HTTP response with a status code of
 * 500 (Internal Server Error) and an optional message that explains the error that occurred.
 */
public class FatalException extends WebApplicationException {

  /**
   * Constructs a new FatalException with a default message and a status code of 500.
   */
  public FatalException() {
    super(Response.status(Status.INTERNAL_SERVER_ERROR).build());
  }

  /**
   * Constructs a new FatalException with the specified message and a status code of 500.
   *
   * @param message the error message that explains the reason for the exception.
   */
  public FatalException(String message) {
    super(Response.status(Status.INTERNAL_SERVER_ERROR).entity(message).type("text/plain").build());
  }

  /**
   * Constructs a new FatalException with the specified cause and a status code of 500.
   *
   * @param cause the Throwable that caused this exception to be thrown.
   */
  public FatalException(Throwable cause) {
    super(
        Response.status(Status.INTERNAL_SERVER_ERROR).entity(cause.getMessage()).type("text/plain")
            .build());
  }
}

