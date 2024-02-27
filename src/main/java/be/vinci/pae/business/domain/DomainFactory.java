package be.vinci.pae.business.domain;

/**
 * This interface provides factory methods to create domain objects.
 */
public interface DomainFactory {

  /**
   * Creates and returns a new instance of UserImpl as a  UserDT object.
   *
   * @return a new instance of  UserImpl as a  UserDTO object.
   */
  UserDTO getUser();

  /**
   * Creates and returns an instance of the ItemImpl class which implements the ItemDTO interface.
   *
   * @return a new instance of ItemImpl.
   */
  ItemDTO getItem();

  /**
   * Creates and returns an instance of the ItemTypeImpl class which implements the ItemTypeDTO
   * interface.
   *
   * @return a new instance of ItemTypeImpl.
   */
  ItemTypeDTO getItemTypeDTO();

  /**
   * Creates and returns an instance of the NotificationImpl class which implements
   * the NotificationDTO interface.
   *
   * @return a new instance of NotificationImpl.
   */
  NotificationDTO getNotification();

  /**
   * Creates and returns an instance of the NotificationReadImpl class which implements
   * the NotificationReadDTO interface.
   *
   * @return a new instance of NotificationReadImpl.
   */
  NotificationReadDTO getNotificationRead();
}
