package be.vinci.pae.dal;

import be.vinci.pae.business.domain.DomainFactory;
import be.vinci.pae.business.domain.NotificationDTO;
import be.vinci.pae.business.domain.NotificationReadDTO;
import be.vinci.pae.business.domain.NotificationReadImpl;
import be.vinci.pae.business.domain.UserDTO;
import be.vinci.pae.exception.FatalException;
import jakarta.inject.Inject;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


/**
 * Implementation of the NotificationDAO interface that handles the communication with the database
 * to retrieve information about notifications.
 */
public class NotificationDAOImpl implements NotificationDAO {

  @Inject
  private DALBackendServices dalServices;
  @Inject
  private DomainFactory myDomainFactory;


  /**
   * Retrieves all NotificationDTO objects from the database.
   *
   * @return a List of all NotificationDTO objects in the database.
   */
  @Override
  public List<NotificationDTO> getAllNotification() {
    try {
      String query = """
             SELECT n.id_notification,
                    n.item,
                    n.creation_date,
                    n.notification_text
             FROM pae.notifications n

          """;
      List<NotificationDTO> notifDTOList = new ArrayList<>();
      try (PreparedStatement ps = dalServices.getPreparedStatement(query)) {
        try (ResultSet rs = ps.executeQuery()) {
          while (rs.next()) {
            NotificationDTO notificationDTO = createNotif(rs);
            notifDTOList.add(notificationDTO);
          }
        }
      }
      return notifDTOList;
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  /**
   * Retrieves all NotificationDTO objects from the database that are associated
   * with the user with the given ID.
   *
   * @param id the ID of the user to retrieve notifications for.
   * @return a List of all NotificationDTO objects in the database that are associated
   *         with the user with the given ID.
   */
  public List<NotificationDTO> getAllNotificationUser(int id) {
    try {
      String query = """
             SELECT n.id_notification,
                    n.item,
                    n.creation_date,
                    n.notification_text
             FROM pae.notifications n, pae.notification_reads nr
             WHERE nr.notification = n.id_notification AND
             nr.is_read = false AND
             nr.notified_user = ?

          """;
      List<NotificationDTO> notifDTOList = new ArrayList<>();
      try (PreparedStatement ps = dalServices.getPreparedStatement(query)) {
        ps.setInt(1, id);
        try (ResultSet rs = ps.executeQuery()) {
          while (rs.next()) {
            NotificationDTO notificationDTO = createNotif(rs);
            notifDTOList.add(notificationDTO);
          }
        }
      }
      return notifDTOList;
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  /**
   * Retrieves all NotificationReadDTO objects from the database.
   *
   * @return a List of all NotificationReadDTO objects in the database.
   */
  public List<NotificationReadDTO> getAllNotificationRead() {
    try {
      String query = """
             SELECT n.id_lecture,
                    n.is_read,
                    n.notification,
                    n.notified_user
             FROM pae.notification_read n

          """;
      List<NotificationReadDTO> notifReadDTOList = new ArrayList<>();
      try (PreparedStatement ps = dalServices.getPreparedStatement(query)) {
        try (ResultSet rs = ps.executeQuery()) {
          while (rs.next()) {
            NotificationReadDTO notificationReadDTODTO = createNotifRead(rs);
            notifReadDTOList.add(notificationReadDTODTO);
          }
        }
      }
      return notifReadDTOList;
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }


  /**
   * Inserts a new NotificationDTO object into the database.
   *
   * @param notification the NotificationDTO object to insert.
   * @return the ID of the newly inserted NotificationDTO object.
   */
  public int insert(NotificationDTO notification) {
    int generatedId = 0;
    try {
      String query = """
              INSERT INTO pae.notifications (
              item,
              creation_date,
              notification_text
          )
          VALUES (?,?,?)
          RETURNING id_notification;
          """;

      try (PreparedStatement ps = dalServices.getPreparedStatement(query)) {
        ps.setInt(1, notification.getItemId());
        ps.setDate(2, Date.valueOf(notification.getCreationDate()));
        ps.setString(3, notification.getNotificationText());

        try (ResultSet rs = ps.executeQuery()) {
          if (rs.next()) {
            generatedId = rs.getInt("id_notification");
          }
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return generatedId;
  }

  /**
   * Inserts a new NotificationReadDTO object into the database,
   * indicating that the given user has read the notification.
   *
   * @param notification the NotificationDTO object that was read.
   * @param u            the UserDTO object indicating the user who read the notification.
   */
  public void insertRead(NotificationDTO notification, UserDTO u) {
    try {
      String query = """
              INSERT INTO pae.notification_reads (
              is_read,
              notification,
              notified_user
          )
          VALUES (?,?,?);

            """;

      try (PreparedStatement ps = dalServices.getPreparedStatement(query)) {
        ps.setBoolean(1, false);
        ps.setInt(2, notification.getNotifId());
        ps.setInt(3, u.getUserId());
        ps.execute();

      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  /**
   * Retrieves the NotificationReadDTO object indicating that the given
   * user has read the notification with the given ID.
   *
   * @param idNotificationDTO the ID of the NotificationDTO object that was read.
   * @param userDTO           the UserDTO object indicating the user who read the notification.
   * @return the NotificationReadDTO object indicating that the given user
   *         has read the notification with the given ID.
   */
  public NotificationReadDTO readNotifBy(int idNotificationDTO, UserDTO userDTO) {
    NotificationReadDTO notificationReadDTO = new NotificationReadImpl();
    try {
      String query = """
                    
          UPDATE pae.notification_reads
                             SET is_read = true WHERE notification = ? AND notified_user = ?
                             RETURNING id_lecture, is_read, notification, notified_user;
          """;

      try (PreparedStatement ps = dalServices.getPreparedStatement(query)) {
        ps.setInt(1, idNotificationDTO);
        ps.setInt(2, userDTO.getUserId());

        try (ResultSet rs = ps.executeQuery()) {
          if (rs.next()) {
            notificationReadDTO = createNotifRead(rs);
          }
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return notificationReadDTO;
  }


  /**
   * Creates an ItemDTO object from a ResultSet object.
   *
   * @param rs the ResultSet object containing data to be used to create the ItemDTO object.
   * @return the ItemDTO object created from the ResultSet object.
   * @throws SQLException if an error occurs while retrieving data from the ResultSet object.
   */
  private NotificationDTO createNotif(ResultSet rs) throws SQLException {
    NotificationDTO notificationDTO = myDomainFactory.getNotification();
    int id = Integer.parseInt(rs.getString(1));
    notificationDTO.setNotifId(id);
    int idItem = Integer.parseInt(rs.getString(2));
    notificationDTO.setItemId(idItem);
    String creationDate = rs.getString(3);
    notificationDTO.setCreationDate(LocalDate.parse(creationDate));
    String notificationText = rs.getString(4);
    notificationDTO.setNotificationText(notificationText);
    return notificationDTO;
  }


  /**
   * Creates an ItemDTO object from a ResultSet object.
   *
   * @param rs the ResultSet object containing data to be used to create the ItemDTO object.
   * @return the ItemDTO object created from the ResultSet object.
   * @throws SQLException if an error occurs while retrieving data from the ResultSet object.
   */
  private NotificationReadDTO createNotifRead(ResultSet rs) throws SQLException {
    NotificationReadDTO notificationDTO = myDomainFactory.getNotificationRead();
    int id = Integer.parseInt(rs.getString(1));
    notificationDTO.setIdLecture(id);
    boolean isRead = rs.getBoolean(2);
    notificationDTO.setRead(isRead);

    int idNotif = Integer.parseInt(rs.getString(3));
    notificationDTO.setIdNotification(idNotif);

    int idUser = Integer.parseInt(rs.getString(4));
    notificationDTO.setIdUser(idUser);

    return notificationDTO;
  }
}
