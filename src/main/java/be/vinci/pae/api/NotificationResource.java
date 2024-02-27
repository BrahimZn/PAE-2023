package be.vinci.pae.api;

import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.api.filters.IsManager;
import be.vinci.pae.business.domain.NotificationDTO;
import be.vinci.pae.business.domain.NotificationReadDTO;
import be.vinci.pae.business.domain.UserDTO;
import be.vinci.pae.business.ucc.NotificationUCC;
import be.vinci.pae.main.Main;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.server.ContainerRequest;

/**
 * The NotificationResource class is a resource that handles HTTP requests related to notifications.
 */
@Singleton
@Path("/notifications")
public class NotificationResource {

  private final Logger logger = LogManager.getLogger(Main.class.getName());

  @Inject
  private NotificationUCC notificationUCC;

  /**
   * Retrieves a list of all notifications.
   *
   * @return The List of NotificationDTO objects representing all notifications.
   * @throws WebApplicationException if the authenticated user is not authorized to access the
   *                                 resource.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  @IsManager
  public List<NotificationDTO> getAllNotifications() {
    return notificationUCC.getNotificationList();
  }

  /**
   * Retrieves a list of all notifications of the user.
   *
   * @param request contains information of the requester.
   * @return The List of NotificationDTO objects representing all notifications of the user.
   * @throws WebApplicationException if the authenticated user is not authorized to access the
   *                                 resource.
   */
  @GET
  @Path("/my")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  @IsManager
  public List<NotificationDTO> getNotificationsUser(@Context ContainerRequest request) {
    UserDTO authenticatedUser = (UserDTO) request.getProperty("user");

    return notificationUCC.getNotificationListUser(authenticatedUser.getUserId());
  }


  /**
   * Retrieves a list of all notif.
   *
   * @param json a JsonNode object containing the notification's id
   * @param request    contains information of the requester.
   * @return The List of UserDTO objects representing all users.
   * @throws WebApplicationException if the authenticated user is not authorized to access the
   *                                 resource.
   */
  @POST
  @Path("/read")
  @Authorize
  @IsManager
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public NotificationReadDTO readNotifByUser(@Context ContainerRequest request, JsonNode json) {
    int id = json.get("id").asInt();

    UserDTO authenticatedUser = (UserDTO) request.getProperty("user");
    logger.info("user id : " + authenticatedUser.getUserId() + " read notif id : " + id);

    return notificationUCC.read(id, authenticatedUser);
  }


}
