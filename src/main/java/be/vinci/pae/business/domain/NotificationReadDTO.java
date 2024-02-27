package be.vinci.pae.business.domain;

/**
 * NotificationReadDTO represents a data transfer object for notifications that have been read.
 */
public interface NotificationReadDTO {

  /**
   * Retrieves the ID of the notification read entry.
   *
   * @return the ID of the notification read entry.
   */
  int getIdLecture();

  /**
   * Sets the ID of the notification read entry.
   *
   * @param idLecture the ID of the notification read entry.
   */
  void setIdLecture(int idLecture);

  /**
   * Checks if the notification has been read.
   *
   * @return true if the notification has been read, false otherwise.
   */
  boolean isRead();

  /**
   * Sets the read status of the notification.
   *
   * @param read the read status of the notification.
   */
  void setRead(boolean read);

  /**
   * Retrieves the ID of the notification associated with this read entry.
   *
   * @return the ID of the notification associated with this read entry.
   */
  int getIdNotification();

  /**
   * Sets the ID of the notification associated with this read entry.
   *
   * @param idNotification the ID of the notification associated with this read entry.
   */
  void setIdNotification(int idNotification);

  /**
   * Retrieves the ID of the user who has read the notification.
   *
   * @return the ID of the user who has read the notification.
   */
  int getIdUser();

  /**
   * Sets the ID of the user who has read the notification.
   *
   * @param idUser the ID of the user who has read the notification.
   */
  void setIdUser(int idUser);
}