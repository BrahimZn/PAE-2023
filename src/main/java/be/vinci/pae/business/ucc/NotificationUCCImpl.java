package be.vinci.pae.business.ucc;

import be.vinci.pae.business.domain.NotificationDTO;
import be.vinci.pae.business.domain.NotificationReadDTO;
import be.vinci.pae.business.domain.NotificationReadImpl;
import be.vinci.pae.business.domain.UserDTO;
import be.vinci.pae.dal.DALServices;
import be.vinci.pae.dal.NotificationDAO;
import jakarta.inject.Inject;
import java.util.List;
import java.util.stream.Stream;


/**
 * Implementation of the {@link NotificationUCC} interface.
 */
public class NotificationUCCImpl implements NotificationUCC {

  @Inject
  private NotificationDAO myNotificationDAO;

  @Inject
  private UserUCC userUCC;

  @Inject
  private DALServices dalServices;

  /**
   * Returns a list of all notifications.
   *
   * @return a list of NotificationDTO objects representing the notifications
   */
  public List<NotificationDTO> getNotificationList() {
    try {
      dalServices.start();
      List<NotificationDTO> notificationDTOList = myNotificationDAO.getAllNotification();
      dalServices.closeReadTransaction();
      return notificationDTOList;
    } catch (Exception e) {
      dalServices.closeReadTransaction();
      throw e;
    }

  }


  /**
   * Returns a list of all notifications for a specific user.
   *
   * @param id the ID of the user
   * @return a list of NotificationDTO objects representing the notifications for the specified user
   */
  public List<NotificationDTO> getNotificationListUser(int id) {
    try {
      dalServices.start();
      List<NotificationDTO> notificationDTOList = myNotificationDAO.getAllNotificationUser(id);
      dalServices.closeReadTransaction();
      return notificationDTOList;
    } catch (Exception e) {
      dalServices.closeReadTransaction();
      throw e;
    }

  }

  /**
   * Adds a notification to the database and sends it to all users.
   *
   * @param notification the notification to add
   */
  @Override
  public void addNotification(NotificationDTO notification) {
    try {
      dalServices.start();
      int id = myNotificationDAO.insert(notification);
      notification.setNotifId(id);
      dalServices.commit();


    } catch (Exception e) {
      dalServices.rollback();
      throw e;
    }

    addNotificationRead(notification);

  }

  /**
   * Adds a notification read to the database for all users.
   *
   * @param notification the notification to add the read for
   */
  @Override
  public void addNotificationRead(NotificationDTO notification) {

    List<UserDTO> helperAndManager = Stream.concat(userUCC.getUserListAsHelpers().stream(),
        userUCC.getUserListAsManagers().stream()).toList();

    try {
      dalServices.start();
      for (UserDTO u : helperAndManager) {
        myNotificationDAO.insertRead(notification, u);
      }
      dalServices.commit();
    } catch (Exception e) {
      dalServices.rollback();
      throw e;
    }
  }

  /**
   * Returns the notification read by a specific user.
   *
   * @param idNotificationDTO the ID of the notification
   * @param userDTO           the user who read the notification
   * @return a NotificationReadDTO object representing the notification read by the specified user
   */

  public NotificationReadDTO read(int idNotificationDTO, UserDTO userDTO) {
    NotificationReadDTO notificationReadDTO = new NotificationReadImpl();
    try {
      dalServices.start();
      notificationReadDTO = myNotificationDAO.readNotifBy(idNotificationDTO, userDTO);
      dalServices.commit();

    } catch (Exception e) {
      dalServices.rollback();
      throw e;
    }
    return notificationReadDTO;
  }

}
