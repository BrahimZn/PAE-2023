package be.vinci.pae.business.domain;

/**
 * This interface represents a User domain object that extends the UserDTO interface.
 */
public interface User extends UserDTO {

  /**
   * Checks if the provided password matches the user's password.
   *
   * @param password the password to check.
   * @return true if the password is correct, false otherwise.
   */
  boolean checkPassword(String password);

  /**
   * Checks if the user is manager.
   *
   * @return true if the user is manager, false otherwise.
   */
  boolean isManager();

  /**
   * Hashes the given password string using a secure one-way hash function.
   *
   * @param password the plain text password to be hashed.
   * @return a string representing the hashed version of the given password.
   */
  String hashPassword(String password);

  /**
   * Checks if the user is helper.
   *
   * @return true if the user is helper, false otherwise.
   */
  boolean isHelper();

  /**
   * Updates the role of the specified user to "Helper".
   */
  void updateRoleToHelper();

  /**
   * Revoke a manager as a helper.
   */
  void revokeManagerToHelper();

  /**
   * Updates the role of the specified user to "Manager".
   */
  void updateRoleToManager();

  /**
   * Updates the role of the specified user to "Member".
   */
  void updateRoleToMember();
}
