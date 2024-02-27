package be.vinci.pae.dal;

import be.vinci.pae.exception.FatalException;
import be.vinci.pae.exception.UnauthorizedException;
import be.vinci.pae.utils.Config;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.commons.dbcp2.BasicDataSource;


/**
 * This class implements the  DALServices interface to provide access to prepared statements for
 * interacting with the database.
 */
public class DALBackendServicesImpl implements DALBackendServices, DALServices {

  // private final Logger logger = LogManager.getLogger(Main.class.getName());
  private final ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<>();
  private final BasicDataSource basicDataSource;

  /**
   * Constructs a new DALServicesImpl instance and establishes a connection to the database using
   * the configuration properties specified in the Config class.
   */
  public DALBackendServicesImpl() {
    basicDataSource = new BasicDataSource();
    String url = Config.getProperty("DatabaseFilePath");
    String user = Config.getProperty("DatabaseUser");
    String password = Config.getProperty("DatabasePassword");
    basicDataSource.setDriverClassName("org.postgresql.Driver");
    basicDataSource.setUrl(url);
    basicDataSource.setUsername(user);
    basicDataSource.setPassword(password);
    basicDataSource.setMaxTotal(5);

  }

  /**
   * Returns a prepared statement object for the given SQL query.
   *
   * @param sql the SQL query string
   * @return a prepared statement object for the given query
   */
  @Override
  public PreparedStatement getPreparedStatement(String sql) {
    try {
      Connection connection = connectionThreadLocal.get();
      if (connection == null) {
        throw new UnauthorizedException("Missing Connection to data base");

      }
      return connection.prepareStatement(sql);
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public void start() {

    if (connectionThreadLocal.get() == null) {
      try {
        connectionThreadLocal.set(basicDataSource.getConnection());
      } catch (SQLException e) {
        throw new FatalException(e);
      }
      try {
        connectionThreadLocal.get().setAutoCommit(false);
      } catch (SQLException e) {
        throw new FatalException(e);
      }

    } else {
      throw new FatalException("Connexion deja présente");
    }


  }

  /**
   * Commits the current transaction and closes the connection. Throws a RuntimeException if an SQL
   * error occurs.
   */
  @Override
  public void commit() {
    try {
      connectionThreadLocal.get().commit();

    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      try {
        connectionThreadLocal.get().close();
      } catch (SQLException e) {
        throw new FatalException(e);
      } finally {
        connectionThreadLocal.remove();
      }

    }
  }

  /**
   * Rolls back the current transaction and closes the connection. Throws a RuntimeException if an
   * SQL error occurs.
   */
  @Override
  public void rollback() {
    try {
      connectionThreadLocal.get().rollback();
    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      try {
        connectionThreadLocal.get().close();
      } catch (SQLException e) {
        throw new FatalException(e);
      } finally {
        connectionThreadLocal.remove();
      }

    }
  }

  /**
   * Closes the current read-only transaction and sets the connection back to read-write mode. If
   * the connection is not in use, nothing happens. Throws a RuntimeException if an SQL error
   * occurs.
   */
  @Override
  public void closeReadTransaction() {

    if (connectionThreadLocal.get() != null) {

      try {
        connectionThreadLocal.get().close();
      } catch (SQLException e) {
        throw new FatalException(e);
      } finally {
        connectionThreadLocal.remove();
      }

    } else {
      throw new FatalException("Connexion manquante");
    }
  }
}
