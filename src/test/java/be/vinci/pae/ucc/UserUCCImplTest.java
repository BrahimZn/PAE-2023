package be.vinci.pae.ucc;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import be.vinci.pae.business.domain.DomainFactory;
import be.vinci.pae.business.domain.DomainFactoryImpl;
import be.vinci.pae.business.domain.UserDTO;
import be.vinci.pae.business.ucc.UserUCC;
import be.vinci.pae.business.ucc.UserUCCImpl;
import be.vinci.pae.dal.DALBackendServicesImpl;
import be.vinci.pae.dal.DALServices;
import be.vinci.pae.dal.UserDAO;
import be.vinci.pae.exception.ConflictException;
import be.vinci.pae.exception.FatalException;
import be.vinci.pae.exception.UnauthorizedException;
import java.util.ArrayList;
import java.util.List;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserUCCImplTest {


  private static UserUCC userUCC;

  private static UserDAO userDAOMock;

  private static DomainFactory domainFactory;

  private static DALServices dalServices;
  private final int idUserNoExistent = 45;
  private UserDTO userMember;
  private UserDTO helper;
  private UserDTO manager;
  private UserDTO userForUpdate;

  private UserDTO newInfoUser;

  private String mailAlreadyExist;
  private List<UserDTO> userList;

  @BeforeAll
  static void bind() {
    userDAOMock = mock(UserDAO.class);
    dalServices = mock(DALBackendServicesImpl.class);

    ServiceLocator locator = ServiceLocatorUtilities.bind(new AbstractBinder() {
      @Override
      protected void configure() {
        bind(userDAOMock).to(UserDAO.class);
        bind(dalServices).to(DALServices.class);
        bind(DomainFactoryImpl.class).to(DomainFactory.class);
        bind(UserUCCImpl.class).to(UserUCC.class);
      }
    });
    userUCC = locator.getService(UserUCC.class);
    domainFactory = locator.getService(DomainFactory.class);
  }

  @BeforeEach
  void setUp() {
    reset(userDAOMock);

    mailAlreadyExist = "already@mail.com";
    userMember = domainFactory.getUser();
    userMember.setRole("User");
    userMember.setUserId(1);
    when(userDAOMock.getOne(1)).thenReturn(userMember);
    when(userDAOMock.getUserByEmail(mailAlreadyExist)).thenReturn(userMember);
    helper = domainFactory.getUser();
    helper.setRole("Helper");
    helper.setUserId(2);
    when(userDAOMock.getOne(2)).thenReturn(helper);

    manager = domainFactory.getUser();
    manager.setRole("Manager");
    manager.setUserId(3);
    when(userDAOMock.getOne(3)).thenReturn(manager);

    when(userDAOMock.getOne(idUserNoExistent)).thenReturn(null);

    userList = new ArrayList<>();
    // 3 members
    userList.add(userMember);
    userList.add(userMember);
    userList.add(userMember);
    userList.add(helper);
    //2 manager
    userList.add(manager);
    userList.add(manager);

    when(userDAOMock.getAll()).thenReturn(userList);

    userForUpdate = domainFactory.getUser();
    userForUpdate.setRole("Helper");
    userForUpdate.setUserId(10);
    userForUpdate.setPassword("$2a$10$ppCKx/MKtI4V.VVskELelel4sJP/Pd12ZWJIcu9bH1sJNUXejJtRC");
    // password = 123
    userForUpdate.setVersionNumber(1);
    userForUpdate.setEmail("test@mail.com");
    userForUpdate.setFirstName("Firstname");
    userForUpdate.setLastName("Lastname");
    userForUpdate.setMobileNumber("04879467825");
    when(userDAOMock.getOne(10)).thenReturn(userForUpdate);

    //new information to update

    newInfoUser = domainFactory.getUser();
    newInfoUser.setEmail("newEmail@gmail.com");
    newInfoUser.setMobileNumber("048888888");
    newInfoUser.setFirstName("FirstnameUpdated");
    newInfoUser.setLastName("LastnameUpdated");


  }

  @DisplayName("Correct login and password")
  @Test
  void loginTC1() {
    String loginCorrect = "test@test.com";

    String hashedPassword = "$2a$10$rum9I6r7AX3XOzvdzCFoVufkVlPZvNmOV.Khf.zcziduKkln7mjAS";
    UserDTO userFound = domainFactory.getUser();
    userFound.setPassword(hashedPassword);
    userFound.setUserId(3);
    when(userDAOMock.getUserByEmail(loginCorrect)).thenReturn(userFound);
    String password = "password";
    int idUserFound = userUCC.login(loginCorrect, password);

    assertEquals(3, idUserFound);


  }

  @DisplayName("Correct login and incorrect password")
  @Test
  void loginTC2() {
    String login = "test2@test.com";
    UserDTO userFound = domainFactory.getUser();
    userFound.setPassword("$2a$10$mr00.oDgGf2GiIewCViWr.vt7dysu2TaR2DFpdLlhaxUnz/Rg6Xk2");
    userFound.setUserId(5);

    when(userDAOMock.getUserByEmail(login)).thenReturn(userFound);
    String password = "password";
    assertThrows(UnauthorizedException.class, () -> userUCC.login(login, password));

  }

  @DisplayName("Nonexistent login ")
  @Test
  void loginTC3() {
    String login = "nonexistent@test.com";
    String password = "password";
    when(userDAOMock.getUserByEmail(login)).thenReturn(null);
    assertThrows(UnauthorizedException.class, () -> userUCC.login(login, password));
  }


  @DisplayName("get existing user with their id")
  @Test
  void getUserByIdTC1() {

    UserDTO resultUser = userUCC.getUserById(userMember.getUserId());
    assertEquals(userMember, resultUser);
  }

  @DisplayName("get nonexistent user with incorrect id")
  @Test
  void getUserByIdTC2() {

    UserDTO resultUser = userUCC.getUserById(idUserNoExistent);
    assertNull(resultUser);
  }

  @DisplayName("Get user by id with ExceptionDAO")
  @Test
  void getUserByIdTC3() {
    int userId = 5;
    when(userDAOMock.getOne(userId)).thenThrow(FatalException.class);

    assertThrows(FatalException.class, () -> userUCC.getUserById(userId));
  }

  @DisplayName("Get Users Member List ")
  @Test
  public void getUserListTC1() {
    List<UserDTO> result = userUCC.getUserList();

    assertEquals(3, result.size());
  }

  @DisplayName("Get Users Member List with exception ")
  @Test
  public void getUserListTC2() {

    when(userDAOMock.getAll()).thenThrow(FatalException.class);
    assertThrows(FatalException.class, () -> userUCC.getUserList());
  }

  @DisplayName("Get helpers List As Manager")
  @Test
  public void getHelperListTC1() {

    List<UserDTO> result = userUCC.getUserListAsHelpers();

    assertEquals(1, result.size());
  }

  @DisplayName("Get helper List with exception ")
  @Test
  public void getHelperListTC2() {

    when(userDAOMock.getAll()).thenThrow(FatalException.class);
    assertThrows(FatalException.class, () -> userUCC.getUserListAsHelpers());
  }

  @DisplayName("Get managers List")
  @Test
  public void getManagerListTC1() {
    List<UserDTO> result = userUCC.getUserListAsManagers();
    assertEquals(2, result.size());
  }


  @DisplayName("Get Manager List with exception ")
  @Test
  public void getManagerListTC2() {

    when(userDAOMock.getAll()).thenThrow(FatalException.class);
    assertThrows(FatalException.class, () -> userUCC.getUserListAsManagers());
  }

  @DisplayName("indicate user as helper")
  @Test
  void testIndicateUserAsHelper() {
    UserDTO userUpdated = userUCC.indicateUserAsHelper(userMember.getUserId());
    assertEquals("Helper", userUpdated.getRole());
  }


  @DisplayName("indicate user as helper when the user is already helper")
  @Test
  void testIndicateUserAsHelperWhenIsAlreadyHelper() {

    assertThrows(ConflictException.class, () -> userUCC.indicateUserAsHelper(helper.getUserId()));

  }

  @DisplayName("indicate user as helper when the user does not exist")
  @Test
  void testIndicateUserAsHelperWhenTheUserDoesNotExist() {

    assertNull(userUCC.indicateUserAsHelper(idUserNoExistent));
  }

  @DisplayName("indicate manager as helper")
  @Test
  void testIndicateManagerAsHelper() {

    UserDTO userDTOUpdated = userUCC.indicateManagerAsHelper(manager.getUserId());
    assertEquals("Helper", userDTOUpdated.getRole());
  }


  @DisplayName("indicate manager as helper when the user is already helper")
  @Test
  void testIndicateManagerAsHelperWhenIsAlreadyHelper() {

    assertThrows(ConflictException.class,
        () -> userUCC.indicateManagerAsHelper(helper.getUserId()));
  }

  @DisplayName("indicate Manager as helper when the user does not exist")
  @Test
  void testIndicateManagerAsHelperWhenTheUserDoesNotExist() {

    assertNull(userUCC.indicateManagerAsHelper(idUserNoExistent));
  }

  @DisplayName("indicate user as manager")
  @Test
  void testIndicateUserAsManager() {

    UserDTO userUpdate = userUCC.indicateUserAsManager(userMember.getUserId());
    assertEquals("Manager", userUpdate.getRole());
  }


  @DisplayName("indicate manager as helper when the user is already manager")
  @Test
  void testIndicateUserAsManagerWhenIsAlreadyManager() {

    assertThrows(ConflictException.class, () -> userUCC.indicateUserAsManager(manager.getUserId()));
  }

  @DisplayName("indicate Manager as helper when the user does not exist")
  @Test
  void testIndicateUserAsManagerWhenTheUserDoesNotExist() {
    assertNull(userUCC.indicateUserAsManager(idUserNoExistent));
  }

  @DisplayName("indicate helper as User")
  @Test
  void testIndicateUserAsMember() {

    UserDTO userDTOUpdate = userUCC.indicateUserAsMember(helper.getUserId());
    assertEquals("User", userDTOUpdate.getRole());
  }


  @DisplayName("indicate helper as User when the user is already User")
  @Test
  void testIndicateUserAsMemberWhenIsAlreadyMember() {

    assertThrows(ConflictException.class,
        () -> userUCC.indicateUserAsMember(userMember.getUserId()));

  }

  @DisplayName("indicate Manager as helper when the user does not exist")
  @Test
  void testIndicateUserAsMemberWhenTheUserDoesNotExist() {

    assertNull(userUCC.indicateUserAsMember(idUserNoExistent));
  }


  @DisplayName("Test userIsManager when the user is manager should return true")
  @Test
  void testUserIsManagerTC1() {
    boolean isManager = userUCC.userIsManager(manager);
    assertTrue(isManager);
  }

  @DisplayName("Test userIsManager when the user is not manager should return false")
  @Test
  void testUserIsManagerTC2() {
    boolean isManager = userUCC.userIsManager(helper);
    assertFalse(isManager);
  }

  @Test
  @DisplayName("Email already exists")
  void testRegisterUserEmailExists() {
    UserDTO userDTO = domainFactory.getUser();
    userDTO.setEmail("test@gmail.com");
    when(userDAOMock.getUserByEmail(userDTO.getEmail())).thenReturn(userDTO);

    assertThrows(ConflictException.class, () -> userUCC.registerUser(userDTO));

  }

  @Test
  @DisplayName("Email does not exist ")
  void testRegisterUserEmailDoesNotExist() {
    UserDTO userDTO = domainFactory.getUser();
    userDTO.setEmail("test@gmail.com");
    when(userDAOMock.getUserByEmail(userDTO.getEmail())).thenReturn(null);

    assertDoesNotThrow(() -> userUCC.registerUser(userDTO));
  }


  @Test
  @DisplayName("User is helper is helper")
  void testUserIsHelperTC1() {

    assertTrue(userUCC.userIsHelper(helper));
  }

  @Test
  @DisplayName(" test User is helper when is not helper  ")
  void testUserIsHelperTC2() {

    assertFalse(userUCC.userIsHelper(userMember));
  }

  @Test
  @DisplayName(" Test update profile")
  void testUpdateProfileTC1() {

    newInfoUser.setPassword("");
    UserDTO resultOfUpdate = userUCC.updateProfile(userForUpdate, newInfoUser, "123");

    assertAll(() -> assertEquals(2, resultOfUpdate.getVersionNumber()),
        () -> assertEquals(userForUpdate.getRole(), resultOfUpdate.getRole()));

  }

  @Test
  @DisplayName(" Test update profile with password incorrect")
  void testUpdateProfileTC2() {

    assertThrows(UnauthorizedException.class,
        () -> userUCC.updateProfile(userForUpdate, newInfoUser, "incorrectPassword"));
  }

  @Test
  @DisplayName(" Test update profile with mail already exist")
  void testUpdateProfileTC3() {

    newInfoUser.setEmail(mailAlreadyExist);
    assertThrows(ConflictException.class,
        () -> userUCC.updateProfile(userForUpdate, newInfoUser, "123"));
  }

  @Test
  @DisplayName(" Test update profile and password")
  void testUpdateProfileTC4() {

    String oldPassword = userForUpdate.getPassword();
    newInfoUser.setPassword("password");

    UserDTO userResultOfUpdate = userUCC.updateProfile(userForUpdate, newInfoUser, "123");
    assertNotEquals(userResultOfUpdate.getPassword(), oldPassword);
  }


}