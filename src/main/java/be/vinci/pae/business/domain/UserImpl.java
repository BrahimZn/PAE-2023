package be.vinci.pae.business.domain;

import be.vinci.pae.exception.ConflictException;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.Objects;
import org.mindrot.jbcrypt.BCrypt;


/**
 * Represents a user implementation that extends the User interface and provides additional.
 * functionality.
 */
class UserImpl implements User {

  private int userId;
  private String lastName;
  private String firstName;
  private String email;
  private String mobileNumber;
  private String profilePicture;
  private String role = " ";
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;
  private LocalDate registrationDate;

  private int versionNumber;

  /**
   * Constructs a new UserImpl object with default attribute values.
   */

  public UserImpl() {
  }

  public int getVersionNumber() {
    return versionNumber;
  }

  public void setVersionNumber(int versionNumber) {
    this.versionNumber = versionNumber;
  }

  /**
   * Returns the user ID of this user.
   *
   * @return the user ID of this user.
   */
  @Override
  public int getUserId() {
    return userId;
  }

  /**
   * Sets the user ID of this user.
   *
   * @param userId the user ID to set for this user.
   */
  @Override
  public void setUserId(int userId) {
    this.userId = userId;
  }

  /**
   * Returns the last name of this user.
   *
   * @return the last name of this user.
   */
  @Override
  public String getLastName() {
    return lastName;
  }

  /**
   * Sets the last name of this user.
   *
   * @param lastName the last name to set for this user.
   */
  @Override
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * Returns the first name of this user.
   *
   * @return the first name of this user.
   */
  @Override
  public String getFirstName() {
    return firstName;
  }

  /**
   * Sets the first name of this user.
   *
   * @param firstName the first name to set for this user.
   */
  @Override
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * Returns the email address of this user.
   *
   * @return the email address of this user.
   */
  @Override
  public String getEmail() {
    return email;
  }

  /**
   * Sets the email address of this user.
   *
   * @param email the email address to set for this user.
   */
  @Override
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Returns the mobile number of this user.
   *
   * @return the mobile number of this user.
   */
  @Override
  public String getMobileNumber() {
    return mobileNumber;
  }

  /**
   * Sets the mobile number of this user.
   *
   * @param mobileNumber the mobile number to set for this user.
   */
  @Override
  public void setMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
  }

  /**
   * Returns the profile picture URL of this user.
   *
   * @return the profile picture URL of this user.
   */
  @Override
  public String getProfilePicture() {
    return profilePicture;
  }

  /**
   * Sets the profile picture URL of this user.
   *
   * @param profilePicture the profile picture URL to set for this user.
   */
  @Override
  public void setProfilePicture(String profilePicture) {
    this.profilePicture = profilePicture;
  }

  /**
   * Returns the role of this user.
   *
   * @return the role of this user.
   */
  @Override
  public String getRole() {
    return role;
  }

  /**
   * Sets the role of this user.
   *
   * @param role the role to set for this user.
   */
  @Override
  public void setRole(String role) {
    this.role = role;
  }

  /**
   * Returns the password of the user.
   *
   * @return the password of the user.
   */
  @Override
  public String getPassword() {
    return password;
  }

  /**
   * Sets the user's password to the specified value.
   *
   * @param password The password to set for the user.
   */
  @Override
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Returns the date on which the user registered.
   *
   * @return A Date object representing the user's registration date.
   */
  @Override
  public LocalDate getRegistrationDate() {
    return registrationDate;
  }

  /**
   * Sets the date on which the user registered.
   *
   * @param registrationDate A Date object representing the user's registration date.
   */
  @Override
  public void setRegistrationDate(LocalDate registrationDate) {
    this.registrationDate = registrationDate;
  }

  /**
   * Compares the specified object with this user for equality. Returns true if the object is also a
   * User object and their userIds are equal.
   *
   * @param o The object to be compared for equality with this user.
   * @return True if the specified object is equal to this user; false otherwise.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserImpl user = (UserImpl) o;
    return userId == user.userId;
  }

  /**
   * Returns a hash code value for the user.
   *
   * @return A hash code value for the user.
   */
  @Override
  public int hashCode() {
    return Objects.hash(userId);
  }

  /**
   * Checks if the specified password matches the user's password, using the BCrypt algorithm.
   *
   * @param password The password to check.
   * @return True if the password matches the user's password; false otherwise.
   */
  @Override
  public boolean checkPassword(String password) {
    return BCrypt.checkpw(password, this.password);
  }

  /**
   * Checks if the user is manager.
   *
   * @return true if the user is manager, false otherwise.
   */
  @Override
  public boolean isManager() {
    return role.equals("Manager");
  }

  /**
   * Hashes the given password string using the BCrypt algorithm, which generates a secure one-way
   * hash of the password. The salt used in the hashing process is automatically generated.
   *
   * @param password the plain text password to be hashed.
   * @return a string representing the hashed version of the given password.
   */
  @Override
  public String hashPassword(String password) {
    return BCrypt.hashpw(password, BCrypt.gensalt());
  }

  /**
   * Checks if the user is helper.
   *
   * @return true if the user is helper, false otherwise.
   */
  @Override
  public boolean isHelper() {
    return role.equals("Helper");
  }

  @Override
  public void updateRoleToHelper() {
    if (this.isHelper() || this.isManager()) {
      throw new ConflictException("User is already helper or manager");
    }

    this.role = "Helper";

  }

  @Override
  public void revokeManagerToHelper() {
    if (this.isHelper() || this.role.equals("User")) {
      throw new ConflictException("User is already members or manager");
    }
    this.role = "Helper";
  }

  @Override
  public void updateRoleToManager() {
    if (this.isManager()) {
      throw new ConflictException("User is already manager");
    }

    this.role = "Manager";

  }

  @Override
  public void updateRoleToMember() {
    if (!this.isManager() && !this.isHelper()) {
      throw new ConflictException("User is already member");
    }

    this.role = "User";

  }
}
