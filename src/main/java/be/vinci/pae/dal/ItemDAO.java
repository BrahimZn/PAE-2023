package be.vinci.pae.dal;

import be.vinci.pae.business.domain.ItemDTO;
import be.vinci.pae.business.domain.ItemTypeDTO;
import java.time.LocalDate;
import java.util.List;

/**
 * This interface provides methods to interact with the persistence layer for ItemDTO objects.
 */
public interface ItemDAO {

  /**
   * Retrieves all ItemDTO objects from the database.
   *
   * @return a List of all ItemDTO objects in the database.
   */
  List<ItemDTO> getAll();

  /**
   * Retrieves all ItemDTO objects from the database for 1 user.
   *
   * @param idUser this is the user id.
   * @return a List of all ItemDTO objects in the database.
   */
  List<ItemDTO> getAllForUser(int idUser);

  /**
   * Retrieves an ItemDTO object with the specified ID from the database.
   *
   * @param idItem the ID of the ItemDTO object to retrieve.
   * @return an ItemDTO object representing the ItemDTO object with the specified ID.
   */
  ItemDTO getOne(int idItem);


  /**
   * Updates an item in the system.
   *
   * @param itemDTOToUpdate ItemDTO object containing the information of the item to be updated.
   */
  void update(ItemDTO itemDTOToUpdate);

  /**
   * Inserts a new item in the system.
   *
   * @param itemDTOToInsert ItemDTO object containing the information of the item to be inserted.
   * @return int of the object created
   */
  int insert(ItemDTO itemDTOToInsert);

  /**
   * Retrieves all item types from the database.
   *
   * @return a List of all ItemTypeDTO objects in the database.
   */
  List<ItemTypeDTO> getAllItemType();

  /**
   * Retrieves all available dates for items in the database.
   *
   * @return a List of all available dates in the database.
   */
  List<LocalDate> getAvailabilities();

  /**
   * Inserts a new availability date in the database.
   *
   * @param availability the availability date to insert
   */
  void insertAvailability(LocalDate availability);
}
