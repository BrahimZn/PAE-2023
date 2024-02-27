package be.vinci.pae.business.ucc;

import be.vinci.pae.business.domain.ItemDTO;
import be.vinci.pae.business.domain.ItemTypeDTO;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * The ItemUCC interface represents the business logic for managing items.
 */
public interface ItemUCC {

  /**
   * Retrieves a list of all items filtered by their state and type.
   *
   * @param state a string indicating whether to return all items or only visible items.
   * @param type  a string indicating the type of items to return.
   * @return a list of ItemDTO objects representing the items that match the given state and type.
   */
  List<ItemDTO> getAll(String state, String type, Double amountMin, Double amountMax,
      LocalDate date);

  /**
   * Retrieves a list of all items filtered by their state and type for 1 user.
   *
   * @param idUser this is the user id. This is used to retrieve all the objects belonging to them.
   * @return a list of ItemDTO objects representing the items that match the given state and type.
   */
  List<ItemDTO> getAllForUser(int idUser);

  /**
   * Retrieves an item by its ID.
   *
   * @param idItem the ID of the item to retrieve.
   * @return an ItemDTO object representing the item with the specified ID.
   */
  ItemDTO getItemById(int idItem);

  /**
   * Accepts a proposal for the item with the specified ID and updates the database accordingly.
   *
   * @param idItem the ID of the item to accept the proposal for.
   * @return the updated ItemDTO representing the object, or null if it was not found/not be found.
   */

  ItemDTO acceptProposal(int idItem);

  /**
   * Declines a proposal for an item with the specified ID and sets the reason for refusal.
   *
   * @param idItem           the ID of the item to decline the proposal for.
   * @param reasonForRefusal the reason for declining the proposal.
   * @return an ItemDTO representing the updated item.
   */
  ItemDTO declineProposal(int idItem, String reasonForRefusal);

  /**
   * Indicates an item in the store with the specified ID and changes its status.
   *
   * @param idItem the ID of the item to decline the proposal for.
   * @return an ItemDTO representing the updated item.
   */
  ItemDTO indicateItemStore(int idItem);

  /**
   * Indicates an item in the workshop with the specified ID and changes its status.
   *
   * @param idItem the ID of the item to decline the proposal for.
   * @return an ItemDTO representing the updated item.
   */
  ItemDTO indicateItemWorkshop(int idItem);


  /**
   * Indicate for sale an item with the specified ID and sets price.
   *
   * @param idItem the ID of the item to indicate for sale.
   * @param price  the price of the item.
   * @return an ItemDTO representing the updated item.
   */
  ItemDTO indicateItemIsForSale(int idItem, Double price);


  /**
   * Indicates an item is sold with the specified ID and changes its status.
   *
   * @param idItem the ID of the item to sell.
   * @return an ItemDTO representing the updated item.
   */
  ItemDTO indicateItemIsSold(int idItem);

  /**
   * Adds an item to the database.
   *
   * @param itemDTO an ItemDTO object representing the item to add.
   * @return the added ItemDTO object representing the added item.
   */
  ItemDTO addItem(ItemDTO itemDTO);

  /**
   * Retrieves a list of all item types.
   *
   * @return a list of {@link ItemTypeDTO} objects representing all item types.
   */
  List<ItemTypeDTO> getAllItemType();

  /**
   * Retrieves a list of available dates for an item.
   *
   * @return a list of LocalDate objects representing available dates for an item.
   */
  List<LocalDate> getAvailabilities();

  /**
   * Retrieves a list of all removable items that have been in store for 30 days or more and are
   * still in the "en vente" state.
   *
   * @return A list of ItemDTO objects that meet the removable criteria.
   */
  List<ItemDTO> getAllRemovableItems();


  /**
   * This method is used to indicate that an item has been removed from sale.
   *
   * @param idItem the ID of the item to mark as sold.
   * @return the ItemDTO object that has been marked as sold.
   */
  ItemDTO removeItemFromSale(int idItem);

  /**
   * Adds a new availability for helpers or supervisors to pick up an object. The availability
   * parameter represents the date of availability.
   *
   * @param availability the date of availability for the object.
   */
  void addAvailability(LocalDate availability);

  /**
   * Updates an item in the system.
   *
   * @param itemToUpdate the updated item to be saved in the system.
   * @return the updated item object if the update was successful, null otherwise.
   */
  ItemDTO updateItem(ItemDTO itemToUpdate);


  /**
   * Indicate for sale an item with the specified ID and sets price.
   *
   * @param idItem the ID of the item to idciate for sale.
   * @param price  the price of the item.
   * @return an ItemDTO representing the updated item.
   */
  ItemDTO indicateItemInShopIsSold(int idItem, Double price);


  /**
   * Returns an array of integers representing the number of items in each state between the
   * specified dates.
   *
   * @param startDate the start date (inclusive) to filter items by
   * @param endDate   the end date (inclusive) to filter items by
   * @return an int array representing the number of items in each state (proposé, refusé, accepté,
   *     vendu) between the specified dates
   */
  Map<String, Long> getNumberOfItemsByStateAndPeriod(LocalDate startDate,
      LocalDate endDate);
}
