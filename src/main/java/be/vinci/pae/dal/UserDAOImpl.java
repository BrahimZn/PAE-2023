package be.vinci.pae.dal;

import be.vinci.pae.business.domain.DomainFactory;
import be.vinci.pae.business.domain.UserDTO;
import be.vinci.pae.exception.ConflictException;
import be.vinci.pae.exception.FatalException;
import jakarta.inject.Inject;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * The UserDAOImpl class provides an implementation of the UserDAO interface for accessing user data
 * in the system.
 */
public class UserDAOImpl implements UserDAO {

  @Inject
  private DALBackendServices dalServices;
  @Inject
  private DomainFactory myDomainFactory;

  /**
   * Returns the User object associated with the given email address.
   *
   * @param email the email address to search for
   * @return the User object associated with the given email address, or null if no such user exists
   */
  @Override
  public UserDTO getUserByEmail(String email) {
    try {
      String query = """
          SELECT u.id_user,
                 last_name,
                 first_name,
                 email,
                 password,
                 phone_number,
                 profile_picture,
                 registration_date,
                 r.role,
                 u.version_number
          FROM pae.users u,
               pae.roles r
          WHERE u.role = r.id_role
            AND u.email = ?""";
      try (PreparedStatement ps = dalServices.getPreparedStatement(query)) {
        ps.setString(1, email);
        try (ResultSet rs = ps.executeQuery()) {
          if (rs.next()) {
            return createUser(rs);
          }
        }
      }
      return null;

    } catch (SQLException e) {
      throw new FatalException(e);
    }

  }

  /**
   * Returns the UserDTO object with the given ID.
   *
   * @param idUser the ID of the user to retrieve
   * @return the UserDTO object with the given ID, or null if no such user exists
   */
  @Override
  public UserDTO getOne(int idUser) {
    try {
      String query = """
          SELECT u.id_user,
                 last_name,
                 first_name,
                 email,
                 password,
                 phone_number,
                 profile_picture,
                 registration_date,
                 r.role,
                 u.version_number
          FROM pae.users u,
               pae.roles r
          WHERE u.role = r.id_role
            AND u.id_user = ?""";

      try (PreparedStatement ps = dalServices.getPreparedStatement(query)) {
        ps.setInt(1, idUser);
        try (ResultSet rs = ps.executeQuery()) {
          if (rs.next()) {
            return createUser(rs);
          }
        }
      }
      return null;

    } catch (SQLException e) {
      throw new FatalException(e);
    }

  }

  /**
   * Retrieves a List of all users from the database.
   *
   * @return The List of UserDTO objects representing all users.
   */
  @Override
  public List<UserDTO> getAll() {
    try {
      String query = """
          SELECT u.id_user,
                 last_name,
                 first_name,
                 email,
                 password,
                 phone_number,
                 profile_picture,
                 registration_date,
                 r.role,
                 u.version_number
          FROM pae.users u,
               pae.roles r
          WHERE u.role = r.id_role
          ORDER BY u.last_name, u.first_name
            """;
      List<UserDTO> userDTOList = new ArrayList<>();
      try (PreparedStatement ps = dalServices.getPreparedStatement(query)) {
        try (ResultSet rs = ps.executeQuery()) {
          while (rs.next()) {
            UserDTO user = createUser(rs);
            userDTOList.add(user);
          }
        }
      }
      return userDTOList;
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  /**
   * Inserts the given UserDTO object into the database as a new user.
   *
   * @param user the UserDTO object representing the user to be inserted.
   * @return an integer.
   * @throws RuntimeException if a database error occurs.
   */
  @Override
  public int insertUser(UserDTO user) {

    try {
      String query = """
          INSERT INTO pae.users(last_name, first_name, email, password,
                                                   phone_number, profile_picture, registration_date,
                                                    role,version_number)
                             VALUES (?, ?, ?,?,?, ?,?,?,?);
            
            """;

      try (PreparedStatement ps = dalServices.getPreparedStatement(query)) {
        ps.setString(1, user.getLastName());
        ps.setString(2, user.getFirstName());
        ps.setString(3, user.getEmail());
        ps.setString(4, user.getPassword());
        ps.setString(5, user.getMobileNumber());
        ps.setString(6, user.getProfilePicture());
        ps.setDate(7, Date.valueOf(user.getRegistrationDate()));
        ps.setInt(8, 1);
        ps.setInt(9, user.getVersionNumber());
        ps.execute();
        return 1;
      }

    } catch (SQLException e) {
      throw new FatalException(e);
    }

  }

  @Override
  public void update(UserDTO userToUpdate) {

    try {
      String query = """
                    
          UPDATE pae.users u SET last_name = ?, first_name = ? , email = ?, password = ?,
                                                             phone_number = ?, profile_picture = ?,
                                                             registration_date = ?,
                                                              role = (SELECT r.id_role FROM
                                                              pae.roles r WHERE r.role = ?),
                                                              version_number = version_number + 1
          WHERE u.id_user = ? AND u.version_number = ?
          """;

      try (PreparedStatement ps = dalServices.getPreparedStatement(query)) {
        ps.setString(1, userToUpdate.getLastName());
        ps.setString(2, userToUpdate.getFirstName());
        ps.setString(3, userToUpdate.getEmail());
        ps.setString(4, userToUpdate.getPassword());
        ps.setString(5, userToUpdate.getMobileNumber());
        ps.setString(6, userToUpdate.getProfilePicture());
        ps.setDate(7, Date.valueOf(userToUpdate.getRegistrationDate()));
        // 8 role
        ps.setString(8, userToUpdate.getRole());

        ps.setInt(9, userToUpdate.getUserId());

        ps.setInt(10, userToUpdate.getVersionNumber());
        //ps.setInt(10, 1);
        int rowsAffected = ps.executeUpdate();
        if (rowsAffected == 0) {
          throw new ConflictException("Error with version number");
        }

      }

    } catch (SQLException e) {
      throw new FatalException(e);
    }

  }


  /**
   * Creates a UserDTO object from a ResultSet obtained from a SQL query.
   *
   * @param rs the ResultSet obtained from a SQL query
   * @return a UserDTO object containing user information
   * @throws SQLException if there is an error accessing the ResultSet
   */
  private UserDTO createUser(ResultSet rs) throws SQLException {
    UserDTO userDTO = myDomainFactory.getUser();
    int id = Integer.parseInt(rs.getString(1));
    userDTO.setUserId(id);
    String lastName = rs.getString(2);
    userDTO.setLastName(lastName);
    String firstName = rs.getString(3);
    userDTO.setFirstName(firstName);
    String emailUserFound = rs.getString(4);
    userDTO.setEmail(emailUserFound);
    String password = rs.getString(5).trim();
    userDTO.setPassword(password);
    String mobileNumber = rs.getString(6);
    userDTO.setMobileNumber(mobileNumber);
    String profilePicture = rs.getString(7);
    userDTO.setProfilePicture(profilePicture);
    String registrationDate = rs.getString(8);
    userDTO.setRegistrationDate(LocalDate.parse(registrationDate));
    String role = rs.getString(9);
    userDTO.setRole(role);
    int versionNumber = rs.getInt(10);
    userDTO.setVersionNumber(versionNumber);
    return userDTO;
  }
}
