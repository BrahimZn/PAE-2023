package be.vinci.pae.dal;

import be.vinci.pae.business.domain.UserDTO;
import java.util.List;

/**
 * The UserDAO interface defines a set of methods for accessing user data in the system.
 */
public interface UserDAO {

  /**
   * Returns the User object associated with the given email address.
   *
   * @param email the email address to search for
   * @return the User object associated with the given email address, or null if no such user exists
   */
  UserDTO getUserByEmail(String email);

  /**
   * Returns the UserDTO object with the given ID.
   *
   * @param idUser the ID of the user to retrieve
   * @return the UserDTO object with the given ID, or null if no such user exists
   */
  UserDTO getOne(int idUser);

  /**
   * Retrieves a List of all users from the database.
   *
   * @return The List of UserDTO objects representing all users.
   */
  List<UserDTO> getAll();


  /**
   * Inserts the given UserDTO object into the database as a new user.
   *
   * @param user the UserDTO object representing the user to be inserted.
   * @return id of user.
   */
  int insertUser(UserDTO user);


  /**
   * Updates a user in the system.
   *
   * @param userToUpdate UserDTO object containing the information of the user to be updated.
   */
  void update(UserDTO userToUpdate);

}
