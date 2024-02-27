package be.vinci.pae.business.ucc;

import be.vinci.pae.business.domain.User;
import be.vinci.pae.business.domain.UserDTO;
import be.vinci.pae.dal.DALServices;
import be.vinci.pae.dal.UserDAO;
import be.vinci.pae.exception.ConflictException;
import be.vinci.pae.exception.UnauthorizedException;
import be.vinci.pae.main.Main;
import jakarta.inject.Inject;
import java.time.LocalDate;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The UserUCCImpl class implements the User use case controller methods.
 */
public class UserUCCImpl implements UserUCC {


  final Logger logger = LogManager.getLogger(Main.class.getName());
  @Inject
  private UserDAO myUserDAO;
  @Inject
  private DALServices dalServices;

  @Override
  public int login(String login, String password) {
    try {
      dalServices.start();

      User userFound = (User) myUserDAO.getUserByEmail(login);

      logger.info("login test = " + login);

      if (userFound == null || !userFound.checkPassword(password)) {
        throw new UnauthorizedException("Login or passsword incorrect");
      }

      logger.info("login successful = " + login);

      dalServices.closeReadTransaction();
      return userFound.getUserId();
    } catch (Exception e) {
      dalServices.closeReadTransaction();
      throw e;
    }

  }

  /**
   * Returns the UserDTO object with the given ID.
   *
   * @param idUser the ID of the user to retrieve.
   * @return the UserDTO object with the given ID, or null if no such user exists
   */
  @Override
  public UserDTO getUserById(int idUser) {
    try {
      dalServices.start();
      UserDTO userDTO = myUserDAO.getOne(idUser);
      dalServices.closeReadTransaction();
      return userDTO;
    } catch (Exception e) {
      dalServices.closeReadTransaction();
      throw e;
    }

  }


  @Override
  public List<UserDTO> getUserList() {
    try {
      dalServices.start();
      List<UserDTO> userDTOList = myUserDAO.getAll().stream()
          .filter(u -> u.getRole().equalsIgnoreCase("User")).toList();
      dalServices.closeReadTransaction();
      return userDTOList;
    } catch (Exception e) {
      dalServices.closeReadTransaction();
      throw e;
    }

  }

  @Override
  public List<UserDTO> getUserListAsHelpers() {
    try {
      dalServices.start();
      List<UserDTO> userDTOList = myUserDAO.getAll().stream()
          .filter(u -> u.getRole().equalsIgnoreCase("Helper")).toList();
      dalServices.closeReadTransaction();
      return userDTOList;
    } catch (Exception e) {
      dalServices.closeReadTransaction();
      throw e;
    }

  }

  @Override
  public List<UserDTO> getUserListAsManagers() {
    try {
      dalServices.start();
      List<UserDTO> userDTOList = myUserDAO.getAll().stream()
          .filter(u -> u.getRole().equalsIgnoreCase("Manager")).toList();
      dalServices.closeReadTransaction();
      return userDTOList;
    } catch (Exception e) {
      dalServices.closeReadTransaction();
      throw e;
    }

  }


  @Override
  public UserDTO indicateUserAsHelper(int idUserToIndicateAsHelper) {
    try {
      dalServices.start();
      User user = (User) myUserDAO.getOne(idUserToIndicateAsHelper);
      if (user == null) {
        dalServices.rollback();
        return null;
      }
      user.updateRoleToHelper();
      myUserDAO.update(user);
      dalServices.commit();
      return user;
    } catch (Exception e) {
      dalServices.rollback();
      throw e;
    }

  }

  @Override
  public UserDTO indicateManagerAsHelper(int idUserToIndicateAsHelper) {
    try {
      dalServices.start();
      User user = (User) myUserDAO.getOne(idUserToIndicateAsHelper);
      if (user == null) {
        dalServices.rollback();
        return null;
      }
      user.revokeManagerToHelper();
      myUserDAO.update(user);
      dalServices.commit();
      return user;
    } catch (Exception e) {
      dalServices.rollback();
      throw e;
    }
  }

  @Override
  public UserDTO indicateUserAsManager(int idUserToIndicateAsManager) {
    try {
      dalServices.start();
      User user = (User) myUserDAO.getOne(idUserToIndicateAsManager);
      if (user == null) {
        dalServices.rollback();
        return null;
      }
      user.updateRoleToManager();
      myUserDAO.update(user);
      dalServices.commit();
      return user;
    } catch (Exception e) {
      dalServices.rollback();
      throw e;
    }

  }

  @Override
  public UserDTO indicateUserAsMember(int idUserToIndicateAsMember) {
    try {
      dalServices.start();
      User user = (User) myUserDAO.getOne(idUserToIndicateAsMember);
      if (user == null) {
        dalServices.rollback();
        return null;
      }
      user.updateRoleToMember();
      myUserDAO.update(user);
      dalServices.commit();
      return user;
    } catch (Exception e) {
      dalServices.rollback();
      throw e;
    }

  }


  @Override
  public boolean userIsManager(UserDTO user) {
    User user1 = (User) user;
    return user1.isManager();
  }


  @Override
  public void registerUser(UserDTO user) {
    try {
      dalServices.start();
      user.setRegistrationDate(LocalDate.now());
      if (!(myUserDAO.getUserByEmail(user.getEmail()) == null)) {
        throw new ConflictException("This email already exists");
      }
      User userToHashPassword = (User) user;
      String passwordHashed = userToHashPassword.hashPassword(user.getPassword());
      user.setPassword(passwordHashed);
      user.setVersionNumber(1);
      myUserDAO.insertUser(user);

      dalServices.commit();
    } catch (Exception e) {
      dalServices.rollback();
      throw e;
    }
  }

  @Override
  public boolean userIsHelper(UserDTO user) {
    User user1 = (User) user;
    return user1.isHelper();
  }

  @Override
  public UserDTO updateProfile(UserDTO oldUser, UserDTO newUser, String oldPassword) {
    try {
      dalServices.start();
      newUser.setUserId(oldUser.getUserId());

      if (!newUser.getEmail()
          .equals(oldUser.getEmail()) && !(myUserDAO.getUserByEmail(newUser.getEmail()) == null)) {
        throw new ConflictException("This email already exists");
      }
      User userVerify = (User) oldUser;
      if (!userVerify.checkPassword(oldPassword)) {
        throw new UnauthorizedException("Password invalid");

      }

      if (newUser.getPassword().isBlank()) {
        newUser.setPassword(oldUser.getPassword());
      } else {

        String passwordNotHashed = newUser.getPassword();

        String hashedPassword = userVerify.hashPassword(passwordNotHashed);

        newUser.setPassword(hashedPassword);

      }

      newUser.setRole(oldUser.getRole());
      newUser.setRegistrationDate(oldUser.getRegistrationDate());
      newUser.setVersionNumber(oldUser.getVersionNumber());

      newUser.setProfilePicture(oldUser.getProfilePicture());
      myUserDAO.update(newUser);
      dalServices.commit();
      newUser.setVersionNumber(oldUser.getVersionNumber() + 1); // with version number updated
      return newUser;
    } catch (Exception e) {
      dalServices.rollback();
      throw e;
    }
  }
}
