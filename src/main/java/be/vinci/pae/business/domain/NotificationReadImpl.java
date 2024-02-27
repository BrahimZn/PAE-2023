package be.vinci.pae.business.domain;

/**
 * Represents a notification read implementation that extends the NotificationRead interface.
 */
public class NotificationReadImpl implements NotificationRead {

  private int idLecture;

  private boolean isRead;

  private int idNotification;

  private int idUser;

  /**
   * Constructs a new NotificationReadImpl object with default attribute values.
   */

  public NotificationReadImpl() {

  }

  public int getIdLecture() {
    return idLecture;
  }

  public void setIdLecture(int idLecture) {
    this.idLecture = idLecture;
  }

  public boolean isRead() {
    return isRead;
  }

  public void setRead(boolean read) {
    this.isRead = read;
  }

  public int getIdNotification() {
    return idNotification;
  }

  public void setIdNotification(int idNotification) {
    this.idNotification = idNotification;
  }

  public int getIdUser() {
    return idUser;
  }

  public void setIdUser(int idUser) {
    this.idUser = idUser;
  }
}
