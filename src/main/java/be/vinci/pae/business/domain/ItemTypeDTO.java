package be.vinci.pae.business.domain;

/**
 * Interface representing an item type. Defines the methods to get and set the id and label of an
 * item type.
 */
public interface ItemTypeDTO {

  /**
   * Get the id of the ItemType.
   *
   * @return the id of the ItemType.
   */
  int getId();

  /**
   * Set the id of the ItemType.
   *
   * @param id the id of the ItemType.
   */
  void setId(int id);

  /**
   * Get the label of the ItemType.
   *
   * @return the label of the ItemType.
   */
  String getLabel();

  /**
   * Set the label of the ItemType.
   *
   * @param itemLabel the label of the ItemType.
   */
  void setLabel(String itemLabel);
}
