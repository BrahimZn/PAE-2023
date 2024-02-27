package be.vinci.pae.business.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.LocalDate;

/**
 * This interface defines a Data Transfer Object (DTO) representing a user. It provides getters and
 * setters for all the user properties.
 */
@JsonDeserialize(as = UserImpl.class)
public interface UserDTO {

  /**
   * Returns the user ID.
   *
   * @return the user ID
   */
  int getUserId();

  /**
   * Sets the user ID.
   *
   * @param userId the user ID to set
   */
  void setUserId(int userId);

  /**
   * Returns the user's last name.
   *
   * @return the user's last name
   */
  String getLastName();

  /**
   * Sets the user's last name.
   *
   * @param lastName the user's last name to set
   */
  void setLastName(String lastName);

  /**
   * Returns the user's first name.
   *
   * @return the user's first name
   */
  String getFirstName();

  /**
   * Sets the user's first name.
   *
   * @param firstName the user's first name to set
   */
  void setFirstName(String firstName);

  /**
   * Returns the user's email address.
   *
   * @return the user's email address
   */
  String getEmail();

  /**
   * Sets the user's email address.
   *
   * @param email the user's email address to set
   */
  void setEmail(String email);

  /**
   * Returns the user's mobile number.
   *
   * @return the user's mobile number
   */
  String getMobileNumber();

  /**
   * Sets the user's mobile number.
   *
   * @param mobileNumber the user's mobile number to set
   */
  void setMobileNumber(String mobileNumber);

  /**
   * Returns the user's profile picture.
   *
   * @return the user's profile picture
   */
  String getProfilePicture();

  /**
   * Sets the user's profile picture.
   *
   * @param profilePicture the user's profile picture to set
   */
  void setProfilePicture(String profilePicture);

  /**
   * Returns the user's role.
   *
   * @return the user's role
   */
  String getRole();

  /**
   * Sets the user's role.
   *
   * @param role the user's role to set
   */
  void setRole(String role);

  /**
   * Returns the user's password.
   *
   * @return the user's password
   */
  String getPassword();

  /**
   * Sets the user's password.
   *
   * @param password the user's password to set
   */
  void setPassword(String password);

  /**
   * Returns the user's registration date.
   *
   * @return the user's registration date
   */
  LocalDate getRegistrationDate();

  /**
   * Sets the user's registration date.
   *
   * @param registrationDate the user's registration date to set
   */
  void setRegistrationDate(LocalDate registrationDate);

  /**
   * Returns the version number of the entity.
   *
   * @return the version number of the entity.
   */
  int getVersionNumber();

  /**
   * Sets the version number of the entity.
   *
   * @param versionNumber the version number to be set.
   */
  void setVersionNumber(int versionNumber);
}
