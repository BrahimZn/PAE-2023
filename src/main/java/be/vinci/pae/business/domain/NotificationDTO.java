package be.vinci.pae.business.domain;

import java.time.LocalDate;

/**
 * The NotificationDTO interface represents the data transfer object of a notification.
 */
public interface NotificationDTO {

  /**
   * Retrieves the ID of the notification.
   *
   * @return the ID of the notification.
   */
  int getNotifId();

  /**
   * Sets the ID of the notification.
   *
   * @param notifId the ID of the notification.
   */
  void setNotifId(int notifId);

  /**
   * Retrieves the ID of the item.
   *
   * @return the ID of the item.
   */
  int getItemId();

  /**
   * Sets the ID of the item.
   *
   * @param itemId the ID of the item.
   */
  void setItemId(int itemId);

  /**
   * Retrieves the date on which the notification was created.
   *
   * @return the date on which the notification was created.
   */
  LocalDate getCreationDate();

  /**
   * Sets the date on which the notification was created.
   *
   * @param creationDate the date on which the notification was created.
   */
  void setCreationDate(LocalDate creationDate);

  /**
   * Retrieves the text of the notification.
   *
   * @return the text of the notification.
   */
  String getNotificationText();

  /**
   * Sets the text of the notification.
   *
   * @param notificationText the text of the notification.
   */
  void setNotificationText(String notificationText);
}