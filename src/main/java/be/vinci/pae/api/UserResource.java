package be.vinci.pae.api;

import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.api.filters.IsHelper;
import be.vinci.pae.api.filters.IsManager;
import be.vinci.pae.business.domain.UserDTO;
import be.vinci.pae.business.ucc.UserUCC;
import be.vinci.pae.main.Main;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.server.ContainerRequest;


/**
 * The UserResource class is a resource that handles HTTP requests related to user data.
 */
@Singleton
@Path("/users")
public class UserResource {

  private final Logger logger = LogManager.getLogger(Main.class.getName());
  private final ObjectMapper jsonMapper = new ObjectMapper();
  @Inject
  private UserUCC userUCC;

  /**
   * Returns the authenticated user's data using their JWT token.
   *
   * @param request the HTTP request context containing the authenticated user's data
   * @return the authenticated user's data as a UserDTO object in JSON format
   */
  @Path("/me")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public UserDTO getUserWithToken(@Context ContainerRequest request) {

    UserDTO authenticatedUser = (UserDTO) request.getProperty("user");
    logger.info("Token de " + authenticatedUser.getFirstName());

    return authenticatedUser;
  }

  /**
   * Retrieves a list of all users.
   *
   * @param request The ContainerRequest that contains the authenticated user's information.
   * @return The List of UserDTO objects representing all users.
   * @throws WebApplicationException if the authenticated user is not authorized to access the
   *                                 resource.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  @IsHelper
  public List<UserDTO> getAllUsers(@Context ContainerRequest request) {

    UserDTO authenticatedUser = (UserDTO) request.getProperty("user");
    logger.info("Demande de voir tout les utilisateurs" + authenticatedUser.getFirstName());

    List<UserDTO> allUsers = userUCC.getUserList();

    return allUsers;
  }

  /**
   * Retrieves a list of all users who have the role of "helper".
   *
   * @param request The ContainerRequest that contains the authenticated user's information.
   * @return The List of UserDTO objects representing all users who have the role of "helper".
   * @throws WebApplicationException if the authenticated user is not authorized to access the
   *                                 resource.
   */
  @Path("/helpersList")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  @IsHelper
  public List<UserDTO> getAllUsersAsHelpers(@Context ContainerRequest request) {

    UserDTO authenticatedUser = (UserDTO) request.getProperty("user");
    logger.info("Demande de voir tout les utilisateurs" + authenticatedUser.getFirstName());

    List<UserDTO> allUsers = userUCC.getUserListAsHelpers();

    return allUsers;
  }

  /**
   * Retrieves a list of all users who have the role of "Manager".
   *
   * @param request The ContainerRequest that contains the authenticated user's information.
   * @return The List of UserDTO objects representing all users who have the role of "Manager".
   * @throws WebApplicationException if the authenticated user is not authorized to access the
   *                                 resource.
   */
  @Path("/managersList")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  @IsHelper
  public List<UserDTO> getAllUsersAsManagers(@Context ContainerRequest request) {

    UserDTO authenticatedUser = (UserDTO) request.getProperty("user");
    logger.info("Demande de voir tout les utilisateurs" + authenticatedUser.getFirstName());

    List<UserDTO> allUsers = userUCC.getUserListAsManagers();

    return allUsers;
  }

  /**
   * Updates the user's profile with the provided JSON data, after checking authorization and
   * required input fields.
   *
   * @param request      The ContainerRequest containing the authenticated user.
   * @param jsonNodeUser The JsonNode containing the user profile data.
   * @return The updated user profile as a UserDTO object.
   * @throws WebApplicationException If the user is not authorized, input data is missing or
   *                                 invalid, or if the user profile is not found.
   */
  @PATCH
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public UserDTO updateUser(@Context ContainerRequest request, JsonNode jsonNodeUser) {
    if (!jsonNodeUser.has("oldPassword") || jsonNodeUser.get("oldPassword").asText().isBlank()) {
      throw new WebApplicationException("Password required", Status.BAD_REQUEST);
    }

    String oldPassword = jsonNodeUser.get("oldPassword").asText();
    ObjectNode object = (ObjectNode) jsonNodeUser;
    object.remove("oldPassword");
    UserDTO newUser = null;
    try {
      newUser = jsonMapper.treeToValue(jsonNodeUser, UserDTO.class);
    } catch (JsonProcessingException e) {
      throw new WebApplicationException("Invalid data", Status.BAD_REQUEST);
    }
    if (newUser.getEmail().isBlank() || newUser.getFirstName()
        .isBlank() || newUser.getLastName().isBlank() || newUser.getMobileNumber().isBlank()) {
      throw new WebApplicationException("Invalid data", Status.BAD_REQUEST);
    }

    UserDTO authenticatedUser = (UserDTO) request.getProperty("user");
    if (authenticatedUser.getUserId() != newUser.getUserId()) {
      throw new WebApplicationException("You are forbidden to update this resource",
          Status.FORBIDDEN);
    }
    UserDTO userUpdated = userUCC.updateProfile(authenticatedUser, newUser, oldPassword);
    if (userUpdated == null) {
      throw new WebApplicationException("Not found", Status.NOT_FOUND);
    }
    return userUpdated;
  }

  /**
   * Retrieves information of user.
   *
   * @param id of the user to retrieve.
   * @return The UserDTO objects representing the user.
   * @throws WebApplicationException if the authenticated user is not authorized to access the
   *                                 resource.
   */
  @Path("/{id}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  @IsHelper
  public UserDTO getUserInfo(@PathParam("id") int id) {

    return userUCC.getUserById(id);
  }

  /**
   * Indicates a user as a helper.
   *
   * @param request                  The HTTP container request.
   * @param idUserToIndicateAsHelper The ID of the user to indicate as a helper.
   * @return The UserDTO of the user who has been indicated as a helper.
   * @throws WebApplicationException If the authenticated user is not a manager, returns a
   *                                 WebApplicationException with a 403 (Forbidden) HTTP status.
   */
  @Path("/indicateAsHelper/{idUser}")
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  @IsManager
  public UserDTO confirmHelperRegistration(@Context ContainerRequest request,
      @PathParam("idUser") int idUserToIndicateAsHelper) {

    UserDTO userToken = (UserDTO) request.getProperty("user");
    logger.info("userToken.getFirstName() = " + userToken.getFirstName());
    UserDTO userDTO = userUCC.indicateUserAsHelper(idUserToIndicateAsHelper);
    if (userDTO == null) {
      throw new WebApplicationException("User not found", Status.NOT_FOUND);
    }
    return userDTO;

  }

  /**
   * Indicates a manager as a helper.
   *
   * @param request                  The HTTP container request.
   * @param idUserToIndicateAsHelper The ID of the user to indicate as a helper.
   * @return The UserDTO of the user who has been indicated as a helper.
   * @throws WebApplicationException If the authenticated user is not a manager, returns a
   *                                 WebApplicationException with a 403 (Forbidden) HTTP status.
   */
  @Path("/indicateManagerAsHelper/{idUser}")
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  @IsManager
  public UserDTO indicateManagerAsHelper(@Context ContainerRequest request,
      @PathParam("idUser") int idUserToIndicateAsHelper) {

    UserDTO userToken = (UserDTO) request.getProperty("user");
    logger.info("userToken.getFirstName() = " + userToken.getFirstName());
    UserDTO userDTO = userUCC.indicateManagerAsHelper(idUserToIndicateAsHelper);
    if (userDTO == null) {
      throw new WebApplicationException("User not found", Status.NOT_FOUND);
    }
    return userDTO;

  }

  /**
   * Indicates a user as a manager.
   *
   * @param request                   The HTTP container request.
   * @param idUserToIndicateAsManager The ID of the user to indicate as a manager.
   * @return The UserDTO of the user who has been indicated as a manager.
   * @throws WebApplicationException If the authenticated user is not a manager, returns a
   *                                 WebApplicationException with a 403 (Forbidden) HTTP status.
   */
  @Path("/indicateAsManager/{idUser}")
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  @IsManager
  public UserDTO indicateMemberToManager(@Context ContainerRequest request,
      @PathParam("idUser") int idUserToIndicateAsManager) {

    UserDTO userToken = (UserDTO) request.getProperty("user");
    logger.info("userToken.getFirstName() = " + userToken.getFirstName());
    UserDTO userDTO = userUCC.indicateUserAsManager(idUserToIndicateAsManager);
    if (userDTO == null) {
      throw new WebApplicationException("User not found", Status.NOT_FOUND);
    }
    return userDTO;
  }

  /**
   * Indicates a user as a member.
   *
   * @param request                  The HTTP container request.
   * @param idUserToIndicateAsMember The ID of the user to indicate as a member.
   * @return The UserDTO of the user who has been indicated as a member.
   * @throws WebApplicationException If the authenticated user is not a manager, returns a
   *                                 WebApplicationException with a 403 (Forbidden) HTTP status.
   */
  @Path("/indicateAsMember/{idUser}")
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  @IsManager
  public UserDTO indicateUserToMember(@Context ContainerRequest request,
      @PathParam("idUser") int idUserToIndicateAsMember) {

    UserDTO userToken = (UserDTO) request.getProperty("user");
    logger.info("userToken.getFirstName() = " + userToken.getFirstName());
    UserDTO userDTO = userUCC.indicateUserAsMember(idUserToIndicateAsMember);
    if (userDTO == null) {
      throw new WebApplicationException("User not found", Status.NOT_FOUND);
    }
    return userDTO;
  }


}
