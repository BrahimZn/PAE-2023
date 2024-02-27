package be.vinci.pae.business.domain;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a notification implementation that extends the Notification interface
 * and provides additional functionality.
 */
public class NotificationImpl implements Notification {

  private int notifId;

  private int itemId;

  private LocalDate creationDate;

  private String notificationText;

  /**
   * Constructs a new NotificationImpl object with default attribute values.
   */

  public NotificationImpl() {
  }

  public int getNotifId() {
    return notifId;
  }

  public void setNotifId(int notifId) {
    this.notifId = notifId;
  }

  public int getItemId() {
    return itemId;
  }

  public void setItemId(int itemId) {
    this.itemId = itemId;
  }

  public LocalDate getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(LocalDate creationDate) {
    this.creationDate = creationDate;
  }

  public String getNotificationText() {
    return notificationText;
  }

  public void setNotificationText(String notificationText) {
    this.notificationText = notificationText;
  }


  /**
   * Compares the specified object with this user for equality. Returns true if the object is also a
   * User object and their userIds are equal.
   *
   * @param o The object to be compared for equality with this user.
   * @return True if the specified object is equal to this user; false otherwise.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NotificationImpl notification = (NotificationImpl) o;
    return notifId == notification.notifId;
  }

  /**
   * Returns a hash code value for the user.
   *
   * @return A hash code value for the user.
   */
  @Override
  public int hashCode() {
    return Objects.hash(notifId);
  }


}
