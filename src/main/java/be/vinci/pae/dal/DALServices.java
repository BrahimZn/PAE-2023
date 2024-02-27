package be.vinci.pae.dal;

/**
 * The DALServices interface defines the basic functionalities required to work with a database.
 */
public interface DALServices {

  /**
   * Starts a transaction in the database.
   */
  void start();

  /**
   * Commits the changes made to the database within the current transaction.
   */
  void commit();

  /**
   * Rolls back the changes made to the database within the current transaction.
   */
  void rollback();


  /**
   * Closes the read transaction. This method should be called after all read operations have been
   * completed on the database.
   */
  void closeReadTransaction();
}