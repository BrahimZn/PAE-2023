package be.vinci.pae.business.domain;

import be.vinci.pae.exception.ConflictException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDate;
import java.util.List;

/**
 * This class represent s an item with various attributes. The class implements the Item interface,
 * which defines th e getters and setters for the attributes. The class is annotated with
 * JsonInclude Non default to ignore default values when serializing to JSON.
 */
@JsonInclude(Include.NON_DEFAULT)
public class ItemImpl implements Item {

  private static final String[] visibleStates = {"en magasin", "vendu", "en vente"};
  private int id;
  private String timeSlot;
  private String itemType;
  private String description;
  private LocalDate dateParkArrival;
  private String state;
  private String offeringUser;
  private int idOferringUser;
  private String phoneNumber;
  private String picture;
  private String reasonForRefusal;
  private double sellingPrice;
  private LocalDate sellingDate;
  private LocalDate storeDepositDate;
  private LocalDate marketWithdrawalDate;
  private LocalDate dateOfReceipt;

  private int versionNumber;

  @Override
  public int getIdOferringUser() {
    return idOferringUser;
  }

  @Override
  public void setIdOferringUser(int idOferringUser) {
    this.idOferringUser = idOferringUser;
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public String getTimeSlot() {
    return timeSlot;
  }

  @Override
  public void setTimeSlot(String timeSlot) {
    this.timeSlot = timeSlot;
  }

  @Override
  public String getItemType() {
    return itemType;
  }

  @Override
  public void setItemType(String itemType) {
    this.itemType = itemType;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public LocalDate getDateParkArrival() {
    return dateParkArrival;
  }

  @Override
  public void setDateParkArrival(LocalDate dateParkArrival) {
    this.dateParkArrival = dateParkArrival;
  }

  @Override
  public String getState() {
    return state;
  }

  @Override
  public void setState(String state) {
    this.state = state;
  }

  @Override
  public String getOfferingUser() {
    return offeringUser;
  }

  @Override
  public void setOfferingUser(String offeringUser) {
    this.offeringUser = offeringUser;
  }

  @Override
  public String getPhoneNumber() {
    return phoneNumber;
  }

  @Override
  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  @Override
  public String getPicture() {
    return picture;
  }

  @Override
  public void setPicture(String picture) {
    this.picture = picture;
  }

  @Override
  public String getReasonForRefusal() {
    return reasonForRefusal;
  }

  @Override
  public void setReasonForRefusal(String reasonForRefusal) {
    this.reasonForRefusal = reasonForRefusal;
  }

  @Override
  public double getSellingPrice() {
    return sellingPrice;
  }

  @Override
  public void setSellingPrice(double sellingPrice) {
    this.sellingPrice = sellingPrice;
  }

  @Override
  public LocalDate getSellingDate() {
    return sellingDate;
  }

  @Override
  public void setSellingDate(LocalDate sellingDate) {
    this.sellingDate = sellingDate;
  }

  @Override
  public LocalDate getStoreDepositDate() {
    return storeDepositDate;
  }

  @Override
  public void setStoreDepositDate(LocalDate storeDepositDate) {
    this.storeDepositDate = storeDepositDate;
  }

  @Override
  public LocalDate getMarketWithdrawalDate() {
    return marketWithdrawalDate;
  }

  @Override
  public void setMarketWithdrawalDate(LocalDate marketWithdrawalDate) {
    this.marketWithdrawalDate = marketWithdrawalDate;
  }

  @Override
  public LocalDate getDateOfReceipt() {
    return dateOfReceipt;
  }

  @Override
  public void setDateOfReceipt(LocalDate dateOfReceipt) {
    this.dateOfReceipt = dateOfReceipt;
  }

  @Override
  public boolean isVisible() {
    if (state == null) {
      return false;
    }

    for (String visibleState : visibleStates) {
      if (state.equals(visibleState)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Accepts the proposal for this item. The state of the item must be "proposé".
   *
   * @throws ConflictException if the item state is not "proposé".
   */
  @Override
  public void acceptProposal() {
    if (!state.equals("proposé")) {
      throw new ConflictException("States is not proposed");
    }
    setDateOfReceipt(LocalDate.now());

    state = "accepté";
  }

  /**
   * Declines a proposal for the item.
   */
  @Override
  public void declineProposal(String reasonForRefusal) {
    if (!state.equals("proposé")) {
      throw new ConflictException("States is not proposed");
    }

    setState("refusé");
    setReasonForRefusal(reasonForRefusal);

  }

  /**
   * indicate the item in store. The state of the item must be "accepté" or "à l'atelier".
   *
   * @throws ConflictException if the item state is not "accepté" or "à l'atelier".
   */
  @Override
  public void indicateItemStore() {
    if (!state.equals("accepté") && !state.equals("à l'atelier")) {
      throw new ConflictException("States is not accepted or in the workshop");
    }
    state = "en magasin";
    setStoreDepositDate(LocalDate.now());
  }

  /**
   * indicate the item in workshop. The state of the item must be "accepté".
   *
   * @throws ConflictException if the item state is not "accepté".
   */
  @Override
  public void indicateItemWorkshop() {
    if (!state.equals("accepté")) {
      throw new ConflictException("States is not accepted");
    }
    state = "à l'atelier";
  }

  /**
   * Indicate the item is for sale.
   *
   * @param sellingPrice the price of the item.
   * @throws ConflictException if the item state is not "accepté".
   */
  @Override
  public void indicateForSale(double sellingPrice) {
    if (!state.equals("en magasin")) {
      throw new ConflictException("States is not in store");
    }

    setState("en vente");
    setSellingPrice(sellingPrice);

  }

  /**
   * Indicate that the item is sold.
   *
   * @throws ConflictException if the item state is not "en vente".
   */
  @Override
  public void indicateItemIsSold() {
    if (!state.equals("en vente")) {
      throw new ConflictException("States is not in sale");
    }

    setState("vendu");
    setSellingDate(LocalDate.now());
  }

  /**
   * Indicate that the item is deposited in the Workshop.
   */
  @Override
  public void indicateItemWithdrawnFromSale() {
    if (!state.equals("en vente")) {
      throw new ConflictException("States is not in sale");
    }

    setState("retiré de la vente");
    setMarketWithdrawalDate(LocalDate.now());
  }

  /**
   * Checks whether a given string is equal to one of the valid types.
   *
   * @param type the string to check.
   * @return true if the string is equal to one of the valid types, false otherwise.
   */
  @Override
  public boolean isValidType(String type, List<ItemTypeDTO> validTypes) {

    for (ItemTypeDTO itemType : validTypes) {

      if (itemType.getLabel().equals(type)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns the version number of the entity.
   *
   * @return the version number of the entity.
   */
  @Override
  public int getVersionNumber() {
    return versionNumber;
  }

  /**
   * Sets the version number of the entity.
   *
   * @param versionNumber the version number to be set.
   */
  @Override
  public void setVersionNumber(int versionNumber) {

    this.versionNumber = versionNumber;
  }

  /**
   * Indicate the item is for sale.
   *
   * @param sellingPrice of the item.
   */
  @Override
  public void indicateInShopIsSold(double sellingPrice) {
    if (!state.equals("en magasin")) {
      throw new ConflictException("States is not in store");
    }
    setState("vendu");
    setSellingPrice(sellingPrice);
  }
}
