package be.vinci.pae.api;

import be.vinci.pae.business.domain.UserDTO;
import be.vinci.pae.business.ucc.UserUCC;
import be.vinci.pae.utils.Config;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.util.Date;

/**
 * Represents a REST resource for authentication operations.
 */
@Singleton
@Path("/auths")
public class AuthsResource {

  private final ObjectMapper jsonMapper = new ObjectMapper();
  private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
  private final long expirationTime = Long.parseLong(
      Config.getProperty("TokenExpirationTime")); // 1 hour
  @Inject
  private UserUCC userUCC;

  /**
   * Handles user login requests.
   *
   * @param json containing login credentials.
   * @return a JSON object representing the public user information.
   * @throws WebApplicationException if the login credentials are invalid.
   */
  @POST
  @Path("login")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectNode login(JsonNode json) {

    if (!json.hasNonNull("login") || !json.hasNonNull("password")
        || json.get("login").asText().isBlank() || json.get("password").asText().isBlank()
        || !json.get("login").asText().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
      throw new WebApplicationException("login or password required", Status.BAD_REQUEST);
    }
    String login = json.get("login").asText();
    String password = json.get("password").asText();

    Date now = new Date();
    Date expiration = new Date(now.getTime() + expirationTime);

    int idUser = userUCC.login(login, password);

    String token;
    try {
      token = JWT.create().withIssuer("auth0").withClaim("user", idUser)
          .withExpiresAt(expiration)
          .sign(this.jwtAlgorithm);

      ObjectNode publicUser = jsonMapper.createObjectNode().put("token", token);

      return publicUser;

    } catch (Exception e) {
      throw new WebApplicationException("Unable to create token", Status.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Registers a new user in the system with the given UserDTO object.
   *
   * @param userDTO the UserDTO object representing the user to be registered.
   * @return an object representing the ID of the newly registered user.
   * @throws WebApplicationException if any of the required fields (email, password, first name,
   *                                 last name, mobile number, or profile picture) are blank.
   * @throws WebApplicationException if the email already exists in the system.
   */
  @POST
  @Path("register")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response register(UserDTO userDTO) {
    // Get and check credentials
    if (userDTO.getEmail().isBlank() || userDTO.getPassword().isBlank() || userDTO.getFirstName()
        .isBlank() || userDTO.getLastName().isBlank() || userDTO.getMobileNumber().isBlank()
        || userDTO.getProfilePicture().isBlank()) {
      throw new WebApplicationException("email and password required", Status.BAD_REQUEST);
    }

    userUCC.registerUser(userDTO);

    //status code
    return Response.ok(userDTO).build();


  }
}
