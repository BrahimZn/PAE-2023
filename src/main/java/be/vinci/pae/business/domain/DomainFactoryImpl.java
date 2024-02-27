package be.vinci.pae.business.domain;


/**
 * Implementation of the  DomainFactory interface that provides factory methods to create domain
 * objects.
 */
public class DomainFactoryImpl implements DomainFactory {

  /**
   * Creates and returns an instance of the UserImpl class which implements the UserDTO interface.
   *
   * @return a new instance of UserImpl.
   */
  @Override
  public UserDTO getUser() {
    return new UserImpl();
  }

  /**
   * Creates and returns an instance of the ItemImpl class which implements the ItemDTO interface.
   *
   * @return a new instance of ItemImpl.
   */
  @Override
  public ItemDTO getItem() {
    return new ItemImpl();
  }

  @Override
  public ItemTypeDTO getItemTypeDTO() {
    return new ItemTypeImpl();
  }


  /**
   * Creates and returns an instance of the NotificationImpl class which implements
   * the NotificationDTO interface.
   *
   * @return a new instance of NotificationImpl.
   */
  @Override
  public NotificationDTO getNotification() {
    return new NotificationImpl();
  }


  /**
   * Creates and returns an instance of the NotificationReadImpl class which implements
   * the NotificationReadDTO interface.
   *
   * @return a new instance of NotificationReadImpl.
   */
  public NotificationReadDTO getNotificationRead() {
    return new NotificationReadImpl();
  }


}
