package be.vinci.pae.business.domain;

import java.util.List;

/**
 * This interface extends the {@link ItemDTO} interface and adds business methods related to items.
 */
public interface Item extends ItemDTO {

  /**
   * Determines if the item is visible.
   *
   * @return true if the item is visible, false otherwise
   */
  boolean isVisible();

  /**
   * Accepts a proposal for the item.
   */
  void acceptProposal();

  /**
   * Declines a proposal for the item.
   *
   * @param reasonForRefusal for the item.
   */
  void declineProposal(String reasonForRefusal);

  /**
   * Indicate that the item is deposited in the store.
   */
  void indicateItemStore();

  /**
   * Indicate that the item is deposited in the Workshop.
   */
  void indicateItemWorkshop();


  /**
   * Indicate the item is for sale.
   *
   * @param sellingPrice of the item.
   */
  void indicateForSale(double sellingPrice);


  /**
   * Indicate the item is for sale.
   *
   * @param sellingPrice of the item.
   */
  void indicateInShopIsSold(double sellingPrice);


  /**
   * Indicate that the item is sold.
   */
  void indicateItemIsSold();

  /**
   * Indicate that the item is deposited in the Workshop.
   */
  void indicateItemWithdrawnFromSale();


  /**
   * Checks whether a given string is equal to one of the valid types.
   *
   * @param type the string to check.
   * @return true if the string is equal to one of the valid types, false otherwise.
   */
  boolean isValidType(String type, List<ItemTypeDTO> validType);
}
