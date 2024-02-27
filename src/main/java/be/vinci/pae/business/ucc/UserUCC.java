package be.vinci.pae.business.ucc;

import be.vinci.pae.business.domain.UserDTO;
import be.vinci.pae.exception.ConflictException;
import java.util.List;

/**
 * The UserUCC interface defines the methods that must be implemented by any User use case
 * controller.
 */
public interface UserUCC {

  /**
   * Tries to log in the user with the given login and password.
   *
   * @param login    the user's login.
   * @param password the user's password.
   * @return id of user if the login was successful, or null otherwise.
   */
  int login(String login, String password);

  /**
   * Returns the UserDTO object with the given ID.
   *
   * @param idUser the ID of the user to retrieve.
   * @return the UserDTO object with the given ID, or null if no such user exists.
   */
  UserDTO getUserById(int idUser);

  /**
   * Returns a list of all users from the database.
   *
   * @return The List of UserDTO objects representing all users.Otherwise,returns null.
   */
  List<UserDTO> getUserList();

  /**
   * Returns a list of all users who have the role of "helper" from the database.
   *
   * @return The List of UserDTO objects representing all helpers.Otherwise,returns null.
   */
  List<UserDTO> getUserListAsHelpers();

  /**
   * Returns a list of all users who have the role of "Manager" from the database.
   *
   * @return The List of UserDTO objects representing all managers.Otherwise,returns null.
   */
  List<UserDTO> getUserListAsManagers();

  /**
   * Indicates a user as a helper.
   *
   * @param idUserToIndicateAsHelper The ID of the user to indicate as a helper.
   * @return The UserDTO of the user who has been indicated as a helper.
   */
  UserDTO indicateUserAsHelper(int idUserToIndicateAsHelper);

  /**
   * Indicates a user as a manager.
   *
   * @param idUserToIndicateAsManager The ID of the user to indicate as a manager.
   * @return The UserDTO of the user who has been indicated as a manager.
   */
  UserDTO indicateUserAsManager(int idUserToIndicateAsManager);

  /**
   * Indicates a user as a member.
   *
   * @param idUserToIndicateAsMember The ID of the user to indicate as a member.
   * @return The UserDTO of the user who has been indicated as a member.
   */
  UserDTO indicateUserAsMember(int idUserToIndicateAsMember);

  /**
   * Indicates a manager as a helper.
   *
   * @param idUserToIndicateAsHelper The ID of the user to indicate as a helper.
   * @return The UserDTO of the user who has been indicated as a helper.
   */
  UserDTO indicateManagerAsHelper(int idUserToIndicateAsHelper);


  /**
   * Determines if a user is a manager.
   *
   * @param user The UserDTO to check.
   * @return true if the user is a manager, false otherwise.
   */
  boolean userIsManager(UserDTO user);

  /**
   * Registers a new user in the system by inserting their information into the database.
   *
   * @param user the UserDTO object representing the user to be registered.
   */
  void registerUser(UserDTO user) throws ConflictException;

  /**
   * Determines if a user is a helper.
   *
   * @param user The UserDTO to check.
   * @return true if the user is a helper, false otherwise.
   */
  boolean userIsHelper(UserDTO user);

  /**
   * Updates the profile of a user with the given ID using the information contained in the provided
   * {@link UserDTO}.
   *
   * @param oldUser     the ID of the user whose profile is being updated.
   * @param newUser     the {@link UserDTO} containing the updated user information.
   * @param oldPassword the password of user.
   * @return a {@link UserDTO} object containing the updated user information.
   */
  UserDTO updateProfile(UserDTO oldUser, UserDTO newUser, String oldPassword);


}
