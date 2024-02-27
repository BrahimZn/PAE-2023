package be.vinci.pae.dal;

import be.vinci.pae.business.domain.DomainFactory;
import be.vinci.pae.business.domain.ItemDTO;
import be.vinci.pae.business.domain.ItemTypeDTO;
import be.vinci.pae.exception.ConflictException;
import be.vinci.pae.exception.FatalException;
import jakarta.inject.Inject;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the ItemDAO interface that handles the communication with the database to
 * retrieve information about items.
 */
public class ItemDAOImpl implements ItemDAO {

  @Inject
  private DALBackendServices dalServices;
  @Inject
  private DomainFactory myDomainFactory;

  /**
   * Retrieves all ItemDTO objects from the database.
   *
   * @return a List of all ItemDTO objects in the database.
   */
  @Override
  public List<ItemDTO> getAll() {
    try {
      String query = """
             SELECt i.id_item,
                    i.time_slot,
                    it.label,
                    i.description,
                    a.availability,
                    s.label,
                    i.offering_user,
                    i.phone_number,
                    i.picture,
                    i.reason_for_refusal,
                    i.selling_price,
                    i.selling_date,
                    i.store_deposit_date,
                    i.market_withdrawal_date,
                    i.date_of_recipt,
                    u.last_name,
                    u.first_name,
                    i.version_number
             FROM pae.items i LEFT OUTER JOIN pae.users u on u.id_user = i.offering_user,
                  pae.item_states s,
                  pae.item_types it,
                  pae.availabilities a
             
             WHERE i.item_type = it.id_item_type
               AND i.date_park_arrival = a.id_availability
               AND i.state = s.id_item_state
             
          """;
      List<ItemDTO> itemDTOList = new ArrayList<>();
      try (PreparedStatement ps = dalServices.getPreparedStatement(query)) {
        try (ResultSet rs = ps.executeQuery()) {
          while (rs.next()) {
            ItemDTO item = createItem(rs);
            itemDTOList.add(item);
          }
        }
      }
      return itemDTOList;
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  /**
   * Retrieves all ItemDTO objects from the database for 1 user.
   *
   * @param idUser this is the user id.
   * @return a List of all ItemDTO objects in the database.
   */
  public List<ItemDTO> getAllForUser(int idUser) {
    try {
      String query = """
             SELECT i.id_item,
                    i.time_slot,
                    it.label,
                    i.description,
                    a.availability,
                    s.label,
                    i.offering_user,
                    i.phone_number,
                    i.picture,
                    i.reason_for_refusal,
                    i.selling_price,
                    i.selling_date,
                    i.store_deposit_date,
                    i.market_withdrawal_date,
                    i.date_of_recipt,
                    u.last_name,
                    u.first_name,
                    i.version_number
             FROM pae.items i LEFT OUTER JOIN pae.users u on u.id_user = i.offering_user,
                  pae.item_states s,
                  pae.item_types it,
                  pae.availabilities a
             
             WHERE i.item_type = it.id_item_type
               AND i.date_park_arrival = a.id_availability
               AND i.state = s.id_item_state
               AND u.id_user = ?
               
             ORDER BY i.id_item DESC
             
          """;
      List<ItemDTO> itemDTOList = new ArrayList<>();
      try (PreparedStatement ps = dalServices.getPreparedStatement(query)) {
        ps.setInt(1, idUser);
        try (ResultSet rs = ps.executeQuery()) {
          while (rs.next()) {
            ItemDTO item = createItem(rs);
            itemDTOList.add(item);
          }
        }
      }
      return itemDTOList;
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  /**
   * Retrieves an ItemDTO object with the specified ID from the database.
   *
   * @param idItem the ID of the ItemDTO object to retrieve.
   * @return an ItemDTO object representing the ItemDTO object with the specified ID.
   */
  @Override
  public ItemDTO getOne(int idItem) {
    try {
      String query = """
             SELECt i.id_item,
                    i.time_slot,
                    it.label,
                    i.description,
                    a.availability,
                    s.label,
                    i.offering_user,
                    i.phone_number,
                    i.picture,
                    i.reason_for_refusal,
                    i.selling_price,
                    i.selling_date,
                    i.store_deposit_date,
                    i.market_withdrawal_date,
                    i.date_of_recipt,
                    u.last_name,
                    u.first_name,
                    i.version_number
             FROM pae.items i LEFT OUTER JOIN pae.users u on u.id_user = i.offering_user,
                  pae.item_states s,
                  pae.item_types it,
                  pae.availabilities a
             
             WHERE i.item_type = it.id_item_type
               AND i.date_park_arrival = a.id_availability
               AND i.state = s.id_item_state
               AND i.id_item = ?
             
          """;
      try (PreparedStatement ps = dalServices.getPreparedStatement(query)) {
        ps.setInt(1, idItem);
        try (ResultSet rs = ps.executeQuery()) {
          if (rs.next()) {
            return createItem(rs);
          } else {
            return null;
          }
        }
      }

    } catch (SQLException e) {
      throw new FatalException(e);
    }

  }

  /**
   * Updates an item in the system.
   *
   * @param item ItemDTO object containing the information of the item to be updated.
   */
  @Override
  public void update(ItemDTO item) {

    try {
      String query = """
                    
          UPDATE pae.items
                             SET time_slot              = ?,
                                 item_type              =
                                 (SELECT t.id_item_type FROM pae.item_types t WHERE t.label = ?),
                                 description            = ?,
                                 date_park_arrival      =
                                     (SELECT a.id_availability FROM pae.availabilities a
                                     WHERE a.availability = ?),
                                 state                  =
                                 (SELECT s.id_item_state FROM pae.item_states s WHERE s.label = ?),
                                 offering_user          = ?,
                                 phone_number           = ?,
                                 picture                = ?,
                                 reason_for_refusal     = ?,
                                 selling_price          = ?,
                                 selling_date           = ?,
                                 store_deposit_date     = ?,
                                 market_withdrawal_date = ?,
                                 date_of_recipt         = ?,
                                 version_number = version_number + 1
                             WHERE id_item = ? AND version_number = ?;
          """;

      try (PreparedStatement ps = dalServices.getPreparedStatement(query)) {

        ps.setString(1, item.getTimeSlot());
        ps.setString(2, item.getItemType());
        ps.setString(3, item.getDescription());
        ps.setDate(4, Date.valueOf(item.getDateParkArrival()));
        ps.setString(5, item.getState());
        int idOfferingUser = item.getIdOferringUser();
        if (idOfferingUser == 0) { // when the id is null, the dauflt value is 0
          ps.setNull(6, Types.INTEGER);
        } else {
          ps.setInt(6, idOfferingUser);
        }
        ps.setString(7, item.getPhoneNumber());
        ps.setString(8, item.getPicture());
        ps.setString(9, item.getReasonForRefusal());
        ps.setDouble(10, item.getSellingPrice());
        Date sellingDate = null; //Can be null
        if (item.getSellingDate() != null) {
          sellingDate = Date.valueOf(item.getSellingDate());
        }
        ps.setDate(11, sellingDate);

        Date storeDepositDate = null; //Can be null
        if (item.getStoreDepositDate() != null) {
          storeDepositDate = Date.valueOf(item.getStoreDepositDate());
        }
        ps.setDate(12, storeDepositDate);
        Date marketWithdrawalDate = null; //Can be null
        if (item.getMarketWithdrawalDate() != null) {
          marketWithdrawalDate = Date.valueOf(item.getMarketWithdrawalDate());
        }

        ps.setDate(13, marketWithdrawalDate);

        Date dateOfRecipt = null; //Can be null
        if (item.getDateOfReceipt() != null) {
          dateOfRecipt = Date.valueOf(item.getDateOfReceipt());
        }
        ps.setDate(14, dateOfRecipt);
        ps.setInt(15, item.getId());
        ps.setInt(16, item.getVersionNumber());
        int rowsAffected = ps.executeUpdate();
        if (rowsAffected == 0) {
          throw new ConflictException("Error with version number");
        }

      }

    } catch (SQLException e) {
      throw new FatalException(e);
    }

  }

  @Override
  public int insert(ItemDTO item) {
    int generatedId = 0;
    try {
      String query = """
              INSERT INTO pae.items (
              time_slot,
              item_type,
              description,
              date_park_arrival,
              state,
              offering_user,
              phone_number,
              picture,
              version_number
          )
          VALUES (?,
          (SELECT t.id_item_type FROM pae.item_types t WHERE t.label = ?),
           ?,
          (SELECT a.id_availability FROM pae.availabilities a WHERE a.availability = ?),
            (SELECT s.id_item_state FROM pae.item_states s WHERE s.label = ?), ?, ?, ?,?)
            RETURNING id_item;

            """;

      try (PreparedStatement ps = dalServices.getPreparedStatement(query)) {

        ps.setString(1, item.getTimeSlot());
        ps.setString(2, item.getItemType());
        ps.setString(3, item.getDescription());
        ps.setDate(4, Date.valueOf(item.getDateParkArrival()));
        ps.setString(5, item.getState());
        int idOffered = item.getIdOferringUser();
        if (idOffered != 0) {
          ps.setInt(6, idOffered);
        } else {
          ps.setNull(6, Types.INTEGER);
        }

        String phoneNumber = item.getPhoneNumber();
        if (phoneNumber != null) {
          ps.setString(7, phoneNumber);
        } else {
          ps.setNull(7, Types.VARCHAR);
        }

        ps.setString(8, item.getPicture());

        ps.setInt(9, 1);
        try (ResultSet rs = ps.executeQuery()) { // Utilisation de executeQuery pour récupérer le résultat
          if (rs.next()) {
            generatedId = rs.getInt("id_item");
          }
        }

      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    System.out.println("generatedId = " + generatedId);
    return generatedId;
  }


  @Override
  public List<ItemTypeDTO> getAllItemType() {
    try {
      String query = """
             SELECT id_item_type, label FROM pae.item_types
             
          """;
      List<ItemTypeDTO> typeDTOArrayList = new ArrayList<>();
      try (PreparedStatement ps = dalServices.getPreparedStatement(query)) {
        try (ResultSet rs = ps.executeQuery()) {
          while (rs.next()) {
            ItemTypeDTO itemType = myDomainFactory.getItemTypeDTO();
            itemType.setId(rs.getInt(1));
            itemType.setLabel(rs.getString(2));
            typeDTOArrayList.add(itemType);
          }
        }
      }
      return typeDTOArrayList;
    } catch (SQLException e) {
      throw new FatalException(e);
    }

  }

  @Override
  public List<LocalDate> getAvailabilities() {
    try {
      String query = """
             SELECT availability FROM pae.availabilities
             
          """;
      List<LocalDate> localDateArrayList = new ArrayList<>();
      try (PreparedStatement ps = dalServices.getPreparedStatement(query)) {
        try (ResultSet rs = ps.executeQuery()) {
          while (rs.next()) {
            LocalDate localDate = rs.getDate(1).toLocalDate();
            localDateArrayList.add(localDate);
          }
        }
      }
      return localDateArrayList;
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public void insertAvailability(LocalDate availability) {
    String query = """
          INSERT INTO pae.availabilities (availability) VALUES (?);
        """;

    try {
      try (PreparedStatement ps = dalServices.getPreparedStatement(query)) {
        ps.setDate(1, Date.valueOf(availability));
        ps.execute();
      }

    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }


  /**
   * Creates an ItemDTO object from a ResultSet object.
   *
   * @param rs the ResultSet object containing data to be used to create the ItemDTO object.
   * @return the ItemDTO object created from the ResultSet object.
   * @throws SQLException if an error occurs while retrieving data from the ResultSet object.
   */
  private ItemDTO createItem(ResultSet rs) throws SQLException {
    ItemDTO itemDTO = myDomainFactory.getItem();

    int id = Integer.parseInt(rs.getString(1));
    itemDTO.setId(id);

    String timeSlot = rs.getString(2);
    itemDTO.setTimeSlot(timeSlot);

    String itemType = rs.getString(3);
    itemDTO.setItemType(itemType);

    String description = rs.getString(4);
    itemDTO.setDescription(description);

    LocalDate dateParkArrival = LocalDate.parse(rs.getString(5));
    itemDTO.setDateParkArrival(dateParkArrival);

    String state = rs.getString(6);
    itemDTO.setState(state);

    String phoneNumber = rs.getString(8);
    if (phoneNumber != null) {
      itemDTO.setPhoneNumber(phoneNumber);
    }

    String picture = rs.getString(9);
    itemDTO.setPicture(picture);
    String reasonForRefusal = rs.getString(10);
    itemDTO.setReasonForRefusal(reasonForRefusal);

    double sellingPrice = rs.getDouble(11);
    itemDTO.setSellingPrice(sellingPrice);
    Date sellingDate = rs.getDate(12);
    if (sellingDate != null) {
      itemDTO.setSellingDate(sellingDate.toLocalDate());
    }

    Date storeDepositDate = rs.getDate(13);
    if (storeDepositDate != null) {
      itemDTO.setStoreDepositDate(storeDepositDate.toLocalDate());
    }

    Date marketWithdrawalDate = rs.getDate(14);
    if (marketWithdrawalDate != null) {
      itemDTO.setMarketWithdrawalDate(marketWithdrawalDate.toLocalDate());
    }

    Date dateOfReceipt = rs.getDate(15);
    if (dateOfReceipt != null) {
      itemDTO.setDateOfReceipt(dateOfReceipt.toLocalDate());
    }

    int idOfferingUser = rs.getInt(7);
    if (idOfferingUser != 0) {
      itemDTO.setIdOferringUser(idOfferingUser);

      String offeringUser = rs.getString(16) + " " + rs.getString(17);
      itemDTO.setOfferingUser(offeringUser);
    }
    itemDTO.setVersionNumber(rs.getInt(18));
    return itemDTO;
  }
}
