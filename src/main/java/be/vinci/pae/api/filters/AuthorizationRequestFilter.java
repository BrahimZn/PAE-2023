package be.vinci.pae.api.filters;

import be.vinci.pae.business.domain.UserDTO;
import be.vinci.pae.business.ucc.UserUCC;
import be.vinci.pae.main.Main;
import be.vinci.pae.utils.Config;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The AuthorizationRequestFilter class is a filter that intercepts incoming requests and validates
 * the JWT token in the "Authorization" header to authenticate the user and authorize access to
 * protected resources.
 */
@Singleton
@Provider
@Authorize
@Priority(1)
public class AuthorizationRequestFilter implements ContainerRequestFilter {

  private final Logger logger = LogManager.getLogger(Main.class.getName());
  private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
  private final JWTVerifier jwtVerifier = JWT.require(this.jwtAlgorithm).withIssuer("auth0")
      .build();
  @Inject
  private UserUCC userUCC;

  /**
   * Filters incoming requests to authenticate and authorize users based on their JWT token.
   *
   * @param requestContext the incoming request context
   * @throws IOException if an I/O error occurs while filtering the request
   */
  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    String token = requestContext.getHeaderString("Authorization");
    if (token == null) {
      requestContext.abortWith(
          Response.status(Status.UNAUTHORIZED).entity("A token is needed to access this resource")
              .build());
    } else {
      DecodedJWT decodedToken = null;
      try {
        decodedToken = this.jwtVerifier.verify(token);
      } catch (Exception e) {
        throw new TokenDecodingException(e);
      }

      logger.info(
              "decodedToken.getClaim(\"user\").asInt() = " + decodedToken.getClaim("user").asInt());

      UserDTO authenticatedUser = userUCC.getUserById(decodedToken.getClaim("user").asInt());

      logger.info("authenticatedUser.getEmail() = " + authenticatedUser.getEmail());

      if (authenticatedUser == null) {
        requestContext.abortWith(
            Response.status(Status.FORBIDDEN).entity("You are forbidden to access this resource")
                .build());
      }
      requestContext.setProperty("user", authenticatedUser);
    }
  }

}

