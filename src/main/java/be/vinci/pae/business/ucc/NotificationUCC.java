package be.vinci.pae.business.ucc;

import be.vinci.pae.business.domain.NotificationDTO;
import be.vinci.pae.business.domain.NotificationReadDTO;
import be.vinci.pae.business.domain.UserDTO;
import java.util.List;


/**
 * The NotificationUCC interface represents the business logic for managing notifications.
 */

public interface NotificationUCC {
  /**
   * Retrieves a list of all notifications.
   *
   * @return a list of NotificationDTO objects representing all notifications.
   */
  List<NotificationDTO> getNotificationList();

  /**
   * Adds a new notification.
   *
   * @param notification a NotificationDTO object representing the notification to add.
   */
  void addNotification(NotificationDTO notification);

  /**
   * Adds a notification to the list of notifications read by a user.
   *
   * @param notification a NotificationDTO object representing the notification to add to
   *                     the list of notifications read by a user.
   */
  void addNotificationRead(NotificationDTO notification);

  /**
   * Marks a notification as read by a specific user.
   *
   * @param idNotificationDTO the ID of the notification to mark as read.
   * @param userDTO           a UserDTO object representing the user that read the notification.
   * @return a NotificationReadDTO object representing the updated notification.
   */
  NotificationReadDTO read(int idNotificationDTO, UserDTO userDTO);

  /**
   * Retrieves a list of all notifications for a specific user.
   *
   * @param id the ID of the user.
   * @return a list of NotificationDTO objects representing all notifications
   *         for the specified user.
   */
  List<NotificationDTO> getNotificationListUser(int id);
}