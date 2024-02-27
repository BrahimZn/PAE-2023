package be.vinci.pae.utils;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Exception mapper used to handle any uncaught exceptions in the web application. If the exception
 * is a {@link WebApplicationException}, the response is returned as is. Otherwise, an internal
 * server error response is generated.
 */
@Provider
public class WebExceptionMapper implements ExceptionMapper<Throwable> {

  /**
   * Converts a Throwable exception to an HTTP response. If the exception is a
   * WebApplicationException, the response from that exception is returned. Otherwise, an internal
   * server error status code is returned with the error message from the exception.
   *
   * @param exception the Throwable exception to convert to a response
   * @return an HTTP response with an appropriate status code and error message
   */
  @Override
  public Response toResponse(Throwable exception) {
    exception.printStackTrace(); // the response is already prepared
    if (exception instanceof WebApplicationException webApplicationException) {
      return Response.status(webApplicationException.getResponse().getStatus())
          .entity(webApplicationException.getMessage()).build();
    }
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
        .entity(exception.getMessage())
        .build();
  }
}