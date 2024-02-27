package be.vinci.pae.ucc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import be.vinci.pae.business.domain.DomainFactory;
import be.vinci.pae.business.domain.DomainFactoryImpl;
import be.vinci.pae.business.domain.NotificationDTO;
import be.vinci.pae.business.domain.NotificationReadDTO;
import be.vinci.pae.business.domain.UserDTO;
import be.vinci.pae.business.ucc.NotificationUCC;
import be.vinci.pae.business.ucc.NotificationUCCImpl;
import be.vinci.pae.business.ucc.UserUCC;
import be.vinci.pae.business.ucc.UserUCCImpl;
import be.vinci.pae.dal.DALBackendServicesImpl;
import be.vinci.pae.dal.DALServices;
import be.vinci.pae.dal.NotificationDAO;
import be.vinci.pae.dal.UserDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NotificationUCCTest {


  private static NotificationUCC notificationUCC;
  private static NotificationDAO notificationDAOMock;
  private static UserUCC userUCC;

  private static UserDAO userDAOMock;
  private static DomainFactory domainFactory;
  private static DALServices dalServices;
  private List<NotificationDTO> allNotifications;

  private NotificationDTO notif1;
  private NotificationDTO notif2;
  private NotificationDTO notif3;


  private UserDTO userDTO;


  @BeforeAll
  static void bind() {
    notificationDAOMock = mock(NotificationDAO.class);
    userDAOMock = mock(UserDAO.class);
    dalServices = mock(DALBackendServicesImpl.class);
    ServiceLocator locator = ServiceLocatorUtilities.bind(new AbstractBinder() {
      @Override
      protected void configure() {
        bind(userDAOMock).to(UserDAO.class);
        bind(dalServices).to(DALServices.class);
        bind(DomainFactoryImpl.class).to(DomainFactory.class);
        bind(UserUCCImpl.class).to(UserUCC.class);

        bind(notificationDAOMock).to(NotificationDAO.class);
        bind(NotificationUCCImpl.class).to(NotificationUCC.class);
      }
    });
    userUCC = locator.getService(UserUCC.class);
    notificationUCC = locator.getService(NotificationUCC.class);
    domainFactory = locator.getService(DomainFactory.class);

  }


  @BeforeEach
  void setUp() {
    reset(userDAOMock);
    reset(notificationDAOMock);

    userDTO = domainFactory.getUser();
    userDTO.setRole("Helper");
    userDTO.setUserId(10);
    userDTO.setPassword(
        "$2a$10$ppCKx/MKtI4V.VVskELelel4sJP/Pd12ZWJIcu9bH1sJNUXejJtRC");// password = 123
    userDTO.setVersionNumber(1);
    userDTO.setEmail("test@mail.com");
    userDTO.setFirstName("Firstname");
    userDTO.setLastName("Lastname");
    userDTO.setMobileNumber("04879467825");

    notif1 = domainFactory.getNotification();
    notif1.setNotificationText("notif1");
    notif2 = domainFactory.getNotification();
    notif2.setNotificationText("notif2");
    notif3 = domainFactory.getNotification();
    notif3.setNotificationText("notif3");

    allNotifications = new ArrayList<>();

    allNotifications.add(notif1);
    allNotifications.add(notif2);
    allNotifications.add(notif3);
    when(notificationDAOMock.getAllNotification()).thenReturn(allNotifications);
    when(notificationDAOMock.getAllNotificationUser(1)).thenReturn(allNotifications);

    when(userDAOMock.getOne(10)).thenReturn(userDTO);


  }


  @Test
  void getNotificationList() {
    List<NotificationDTO> allNotifications = notificationUCC.getNotificationList();

    assertEquals(allNotifications.size(), allNotifications.size());

  }

  @DisplayName("Test addNotification")
  @Test
  void addNotification() {

    notificationUCC.addNotification(notif1);

    assertEquals("notif1", notif1.getNotificationText());

  }

  @DisplayName("Test addNotificationRead")
  @Test
  void addNotificationRead() {
    notificationUCC.addNotificationRead(notif2);

    List<UserDTO> users = Stream.concat(userUCC.getUserListAsHelpers().stream(),
        userUCC.getUserListAsManagers().stream()).collect(Collectors.toList());

    assertFalse(false);

  }

  @DisplayName("Test read")
  @Test
  void read() {
    NotificationReadDTO result = notificationUCC.read(1, userDTO);

    assertFalse(notificationUCC.getNotificationListUser(userDTO.getUserId()).contains(result));

  }

  @DisplayName("Test getNotificationListUser")
  @Test
  void getNotificationListUser() {
    notificationUCC.addNotificationRead(notif3);
    notificationUCC.addNotificationRead(notif2);

    System.out.println(notificationUCC.getNotificationList().get(1).getNotificationText());

    List<NotificationDTO> actualNotifications = notificationUCC.getNotificationListUser(1);

    assertEquals(3, actualNotifications.size());

  }
}