package be.vinci.pae.business.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.LocalDate;

/**
 * Interface representing an item in the system.
 */
@JsonDeserialize(as = ItemImpl.class)
public interface ItemDTO {

  /**
   * Retrieves the ID of the item.
   *
   * @return the ID of the item.
   */
  int getId();

  /**
   * Sets the ID of the item.
   *
   * @param id the new ID of the item.
   */
  void setId(int id);

  /**
   * Retrieves the time slot of the item.
   *
   * @return the time slot of the item.
   */
  String getTimeSlot();

  /**
   * Sets the time slot of the item.
   *
   * @param timeSlot the new time slot of the item.
   */
  void setTimeSlot(String timeSlot);

  /**
   * Retrieves the item type of the item.
   *
   * @return the item type of the item.
   */
  String getItemType();

  /**
   * Sets the item type of the item.
   *
   * @param itemType the new item type of the item.
   */
  void setItemType(String itemType);

  /**
   * Retrieves the description of the item.
   *
   * @return the description of the item.
   */
  String getDescription();

  /**
   * Sets the description of the item.
   *
   * @param description the new description of the item.
   */
  void setDescription(String description);

  /**
   * Retrieves the date the item was parked in the system.
   *
   * @return the date the item was parked in the system.
   */
  LocalDate getDateParkArrival();

  /**
   * Sets the date the item was parked in the system.
   *
   * @param dateParkArrival the new date the item was parked in the system.
   */
  void setDateParkArrival(LocalDate dateParkArrival);

  /**
   * Retrieves the state of the item.
   *
   * @return the state of the item.
   */
  String getState();

  /**
   * Sets the state of the item.
   *
   * @param state the new state of the item.
   */
  void setState(String state);

  /**
   * Retrieves the username of the user who offered the item.
   *
   * @return the username of the user who offered the item.
   */
  String getOfferingUser();

  /**
   * Sets the username of the user who offered the item.
   *
   * @param offeringUser the new username of the user who offered the item.
   */
  void setOfferingUser(String offeringUser);

  /**
   * Retrieves the phone number associated with the item.
   *
   * @return the phone number associated with the item.
   */
  String getPhoneNumber();

  /**
   * Sets the phone number associated with the item.
   *
   * @param phoneNumber the new phone number associated with the item.
   */
  void setPhoneNumber(String phoneNumber);

  /**
   * Retrieves the picture of the item.
   *
   * @return the picture of the item.
   */
  String getPicture();

  /**
   * Sets the picture of the item.
   *
   * @param picture the new picture of the item.
   */
  void setPicture(String picture);

  /**
   * Retrieves the reason for refusal of the item.
   *
   * @return the reason for refusal of the item.
   */
  String getReasonForRefusal();

  /**
   * Sets the reason for refusal of the item.
   *
   * @param reasonForRefusal the new reason for refusal of the item.
   */
  void setReasonForRefusal(String reasonForRefusal);

  /**
   * Retrieves the selling price of the item.
   *
   * @return the selling price of the item.
   */
  double getSellingPrice();

  /**
   * Sets the selling price of the item.
   *
   * @param sellingPrice the new selling price of the item.
   */
  void setSellingPrice(double sellingPrice);

  /**
   * Retrieves the date the item was sold.
   *
   * @return the date the item was sold.
   */
  LocalDate getSellingDate();

  /**
   * Sets the date the item was sold.
   *
   * @param sellingDate the new date the item was sold.
   */
  void setSellingDate(LocalDate sellingDate);

  /**
   * Returns the store deposit date of the item.
   *
   * @return the store deposit date of the item as a LocalDate object.
   */
  LocalDate getStoreDepositDate();

  /**
   * Sets the store deposit date of the item.
   *
   * @param storeDepositDate the store deposit date of the item to set as a LocalDate object.
   */
  void setStoreDepositDate(LocalDate storeDepositDate);

  /**
   * Returns the market withdrawal date of the item.
   *
   * @return the market withdrawal date of the item as a LocalDate object.
   */
  LocalDate getMarketWithdrawalDate();

  /**
   * Sets the market withdrawal date of the item.
   *
   * @param marketWithdrawalDate the market withdrawal date of the item to set as a LocalDate
   *                             object.
   */
  void setMarketWithdrawalDate(LocalDate marketWithdrawalDate);

  /**
   * Returns the date of receipt of the item.
   *
   * @return the date of receipt of the item as a LocalDate object.
   */
  LocalDate getDateOfReceipt();

  /**
   * Sets the date of receipt of the item.
   *
   * @param dateOfReceipt the date of receipt of the item to set as a LocalDate object.
   */
  void setDateOfReceipt(LocalDate dateOfReceipt);

  /**
   * Returns the ID of the user who offered the item.
   *
   * @return the ID of the user who offered the item as an integer.
   */
  int getIdOferringUser();

  /**
   * Sets the ID of the user who offered the item.
   *
   * @param idOferringUser the ID of the user who offered the item to set as an integer.
   */
  void setIdOferringUser(int idOferringUser);

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
