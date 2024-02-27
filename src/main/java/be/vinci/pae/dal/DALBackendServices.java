package be.vinci.pae.dal;

import java.sql.PreparedStatement;

/**
 * This interface defines the methods that the DAL services layer must implement in order to provide
 * access to prepared statements for interacting with the database.
 */
public interface DALBackendServices {

  /**
   * Returns a prepared statement object for the given SQL query.
   *
   * @param sql the SQL query string
   * @return a prepared statement object for the given query
   */
  PreparedStatement getPreparedStatement(String sql);

}
