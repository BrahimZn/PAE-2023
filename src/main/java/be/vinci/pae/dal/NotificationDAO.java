package be.vinci.pae.dal;

import be.vinci.pae.business.domain.NotificationDTO;
import be.vinci.pae.business.domain.NotificationReadDTO;
import be.vinci.pae.business.domain.UserDTO;
import java.util.List;

/**
 * This interface provides methods to interact with the persistence layer for NotificationDTO
 * and NotificationReadDTO objects.
 */
public interface NotificationDAO {

  /**
   * Retrieves all NotificationDTO objects from the database.
   *
   * @return a List of all NotificationDTO objects in the database.
   */
  List<NotificationDTO> getAllNotification();

  /**
   * Inserts a new NotificationDTO object into the database.
   *
   * @param notification the NotificationDTO object to insert.
   * @return the ID of the newly inserted NotificationDTO object.
   */
  int insert(NotificationDTO notification);

  /**
   * Inserts a new NotificationReadDTO object into the database, indicating that the
   * given user has read the notification.
   *
   * @param notification the NotificationDTO object that was read.
   * @param u            the UserDTO object indicating the user who read the notification.
   */
  void insertRead(NotificationDTO notification, UserDTO u);

  /**
   * Retrieves the NotificationReadDTO object indicating that the given user has read
   * the notification with the given ID.
   *
   * @param idNotificationDTO the ID of the NotificationDTO object that was read.
   * @param userDTO           the UserDTO object indicating the user who read the notification.
   * @return the NotificationReadDTO object indicating that the given user has read
   *         the notification with the given ID.
   */
  NotificationReadDTO readNotifBy(int idNotificationDTO, UserDTO userDTO);

  /**
   * Retrieves all NotificationDTO objects from the database that are associated
   * with the user with the given ID.
   *
   * @param id the ID of the user to retrieve notifications for.
   * @return a List of all NotificationDTO objects in the database that are associated
   *         with the user with the given ID.
   */
  List<NotificationDTO> getAllNotificationUser(int id);

  /**
   * Retrieves all NotificationReadDTO objects from the database.
   *
   * @return a List of all NotificationReadDTO objects in the database.
   */
  List<NotificationReadDTO> getAllNotificationRead();
}
