package be.vinci.pae.api.filters;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

/**
 * Exception to be thrown when a JWT token cannot be decoded due to invalid format or signature.
 * Extends WebApplicationException to return an HTTP 401 Unauthorized response to the client.
 */
public class TokenDecodingException extends WebApplicationException {

  /**
   * Constructs a TokenDecodingException with a default HTTP 401 Unauthorized response.
   */
  public TokenDecodingException() {
    super(Response.status(Response.Status.UNAUTHORIZED).build());
  }


  /**
   * Constructs a TokenDecodingException with a custom error message and an HTTP 401 Unauthorized
   * response.
   *
   * @param message The error message to include in the response body.
   */
  public TokenDecodingException(String message) {
    super(Response.status(Response.Status.UNAUTHORIZED).entity(message).type("text/plain").build());
  }

  /**
   * Constructs a TokenDecodingException with a custom error message obtained from a Throwable and
   * an HTTP 401 Unauthorized response.
   *
   * @param cause The Throwable that caused the exception to be thrown.
   */
  public TokenDecodingException(Throwable cause) {
    super(
        Response.status(Response.Status.UNAUTHORIZED).entity(cause.getMessage()).type("text/plain")
            .build());
  }
}

