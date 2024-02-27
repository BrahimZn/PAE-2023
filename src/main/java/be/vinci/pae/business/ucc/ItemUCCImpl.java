package be.vinci.pae.business.ucc;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

import be.vinci.pae.business.domain.Item;
import be.vinci.pae.business.domain.ItemDTO;
import be.vinci.pae.business.domain.ItemTypeDTO;
import be.vinci.pae.dal.DALServices;
import be.vinci.pae.dal.ItemDAO;
import be.vinci.pae.exception.ConflictException;
import jakarta.inject.Inject;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the {@link ItemUCC} interface.
 */
public class ItemUCCImpl implements ItemUCC {

  @Inject
  private ItemDAO itemDAO;
  @Inject
  private DALServices dalServices;

  /**
   * Retrieves a list of items based on their state and/or type.
   *
   * @param state a String indicating the state of the items to retrieve. Possible values are "all",
   *              perl Copy code "visible", or any other state defined for the items.
   * @param type  a String indicating the type of the items to retrieve. Can be null.
   * @return a list of {@link ItemDTO} objects representing the items matching the given criteria.
   *     If state is "visible", only items that are marked as visible will be returned. If state is
   *     "all" and type is not null, only items of the given type will be returned. If state is any
   *     other value, only items with that state will be returned.
   */
  @Override
  public List<ItemDTO> getAll(String state, String type, Double amountMin, Double amountMax,
      LocalDate dateArrival) {
    try {
      dalServices.start();
      List<ItemDTO> allItems = itemDAO.getAll();
      dalServices.closeReadTransaction();
      if (state.equals("visible")) {
        allItems = allItems.stream().filter(itemDTO -> {
          Item item = (Item) itemDTO;
          return item.isVisible();
        }).toList();
      } else {
        if (!state.equals("all")) {
          allItems = allItems.stream().filter(itemDTO -> itemDTO.getState().equals(state)).toList();
        }
      }

      if (!type.equals("all")) {
        allItems = allItems.stream().filter(itemDTO -> itemDTO.getItemType().equals(type)).toList();
      }

      if (amountMin != null && amountMax != null) {
        allItems = allItems.stream()
            .filter(itemDTO -> Double.compare(itemDTO.getSellingPrice(), amountMin) >= 0
                && Double.compare(itemDTO.getSellingPrice(), amountMax) <= 0).toList();
      }

      if (dateArrival != null) {
        allItems = allItems.stream()
            .filter(itemDTO -> dateArrival.equals(itemDTO.getDateParkArrival()))
            .toList();
      }
      return allItems;
    } catch (Exception e) {
      dalServices.closeReadTransaction();
      throw e;
    }
  }


  /**
   * Retrieves a list of items based on their state and/or type.
   *
   * @param idUser this is the user id. This is used to retrieve all the objects belonging to them.
   * @return a list of {@link ItemDTO} objects representing the items matching the given criteria.
   *     If state is "visible", only items that are marked as visible will be returned. If state is
   *     "all" and type is not null, only items of the given type will be returned. If state is any
   *     other value, only items with that state will be returned.
   */
  public List<ItemDTO> getAllForUser(int idUser) {
    try {
      dalServices.start();
      List<ItemDTO> allItems = itemDAO.getAllForUser(idUser);
      dalServices.closeReadTransaction();

      return allItems;
    } catch (Exception e) {
      dalServices.closeReadTransaction();
      throw e;
    }
  }


  @Override
  public ItemDTO getItemById(int idItem) {
    try {
      dalServices.start();
      ItemDTO itemDTO = itemDAO.getOne(idItem);

      dalServices.closeReadTransaction();
      return itemDTO;
    } catch (Exception e) {
      dalServices.closeReadTransaction();
      throw e;
    }
  }

  /**
   * Accepts a proposal for the given item and updates the item in the database.
   *
   * @param idItem the id of the item to update.
   * @return the updated item as an ItemDTO, or null if the item with the given id does not exist.
   */
  @Override
  public ItemDTO acceptProposal(int idItem) {
    try {

      dalServices.start();

      Item itemToUpdate = (Item) itemDAO.getOne(idItem);

      if (itemToUpdate == null) {
        dalServices.rollback();
        return null;
      }

      itemToUpdate.acceptProposal();
      itemDAO.update(itemToUpdate);
      dalServices.commit();
      return itemToUpdate;
    } catch (Exception e) {
      dalServices.rollback();
      throw e;
    }


  }

  @Override
  public ItemDTO declineProposal(int idItem, String reasonForRefusal) {
    try {
      dalServices.start();

      Item itemToUpdate = (Item) itemDAO.getOne(idItem);

      if (itemToUpdate == null) {
        dalServices.rollback();
        return null;
      }

      itemToUpdate.declineProposal(reasonForRefusal);
      itemDAO.update(itemToUpdate);
      dalServices.commit();
      return itemToUpdate;
    } catch (Exception e) {
      dalServices.rollback();
      throw e;
    }

  }

  @Override
  public ItemDTO indicateItemStore(int idItem) {
    try {
      dalServices.start();

      Item itemToUpdate = (Item) itemDAO.getOne(idItem);

      if (itemToUpdate == null) {
        dalServices.rollback();
        return null;
      }

      itemToUpdate.indicateItemStore();
      itemDAO.update(itemToUpdate);
      dalServices.commit();
      return itemToUpdate;
    } catch (Exception e) {
      dalServices.rollback();
      throw e;
    }


  }

  @Override
  public ItemDTO indicateItemWorkshop(int idItem) {
    try {
      dalServices.start();

      Item itemToUpdate = (Item) itemDAO.getOne(idItem);

      if (itemToUpdate == null) {
        dalServices.rollback();
        return null;
      }

      itemToUpdate.indicateItemWorkshop();
      itemDAO.update(itemToUpdate);
      dalServices.commit();
      return itemToUpdate;
    } catch (Exception e) {
      dalServices.rollback();
      throw e;
    }


  }

  /**
   * Indicate for sale an item with the specified ID and sets price.
   *
   * @param idItem the ID of the item to idciate for sale.
   * @param price  the price of the item.
   * @return an ItemDTO representing the updated item.
   */
  @Override
  public ItemDTO indicateItemIsForSale(int idItem, Double price) {
    try {
      dalServices.start();

      Item itemToUpdate = (Item) itemDAO.getOne(idItem);

      if (itemToUpdate == null) {
        dalServices.rollback();
        return null;
      }

      itemToUpdate.indicateForSale(price);
      itemDAO.update(itemToUpdate);
      dalServices.commit();
      return itemToUpdate;
    } catch (Exception e) {
      dalServices.rollback();
      throw e;
    }


  }

  /**
   * Indicates an item is sold with the specified ID and changes its status.
   *
   * @param idItem the ID of the item to sell.
   * @return an ItemDTO representing the updated item.
   */
  @Override
  public ItemDTO indicateItemIsSold(int idItem) {
    try {
      dalServices.start();

      Item itemToUpdate = (Item) itemDAO.getOne(idItem);

      if (itemToUpdate == null) {
        dalServices.rollback();
        return null;
      }

      itemToUpdate.indicateItemIsSold();
      itemDAO.update(itemToUpdate);
      dalServices.commit();
      return itemToUpdate;
    } catch (Exception e) {
      dalServices.rollback();
      throw e;
    }


  }

  @Override
  public ItemDTO addItem(ItemDTO itemDTO) {

    try {
      dalServices.start();

      itemDTO.setState("proposé");
      int id = itemDAO.insert(itemDTO);
      itemDTO.setId(id);

      dalServices.commit();
      return itemDTO;

    } catch (Exception e) {
      dalServices.rollback();
      System.out.println("e.getMessage() = " + e.getMessage());
      throw e;
    }
  }

  @Override
  public List<ItemTypeDTO> getAllItemType() {

    try {
      dalServices.start();

      List<ItemTypeDTO> itemTypeDTOList = itemDAO.getAllItemType();

      dalServices.closeReadTransaction();

      return itemTypeDTOList;
    } catch (Exception e) {
      dalServices.closeReadTransaction();
      throw e;
    }
  }

  @Override
  public List<LocalDate> getAvailabilities() {
    try {
      dalServices.start();

      List<LocalDate> availabilities = itemDAO.getAvailabilities();

      dalServices.closeReadTransaction();

      return availabilities;
    } catch (Exception e) {
      dalServices.closeReadTransaction();
      throw e;
    }
  }

  @Override
  public void addAvailability(LocalDate availability) {
    try {
      dalServices.start();
      List<LocalDate> localDateList = itemDAO.getAvailabilities();
      if (localDateList.contains(availability)) {
        throw new ConflictException("Date already exist");
      }
      itemDAO.insertAvailability(availability);
      dalServices.commit();
    } catch (Exception e) {
      dalServices.rollback();
      throw e;
    }
  }

  @Override
  public ItemDTO removeItemFromSale(int idItem) {
    try {
      System.out.println("idItem = " + idItem);
      dalServices.start();
      Item item = (Item) itemDAO.getOne(idItem);
      if (item == null) {
        dalServices.rollback();
        return null;
      }

      item.indicateItemWithdrawnFromSale();
      itemDAO.update(item);
      dalServices.commit();
      return item;
    } catch (Exception e) {
      dalServices.rollback();
      throw e;
    }
  }

  @Override
  public List<ItemDTO> getAllRemovableItems() {
    List<ItemDTO> allItems = getAll("en vente", "all", null, null, null);
    List<ItemDTO> removableItems = new ArrayList<>();

    for (ItemDTO item : allItems) {
      if ((int) ChronoUnit.DAYS.between(item.getStoreDepositDate(), LocalDate.now()) >= 30) {
        removableItems.add(item);
      }
    }
    return removableItems;
  }

  @Override
  public ItemDTO updateItem(ItemDTO itemToUpdate) {

    try {
      dalServices.start();
      ItemDTO itemBeforeUpdate = itemDAO.getOne(itemToUpdate.getId());

      if (itemBeforeUpdate == null) {
        dalServices.rollback();
        return null;
      }
      if (itemToUpdate.getPicture() != null) {
        itemBeforeUpdate.setPicture(itemToUpdate.getPicture());
      }
      itemBeforeUpdate.setSellingPrice(itemToUpdate.getSellingPrice());
      itemBeforeUpdate.setItemType(itemToUpdate.getItemType());
      itemBeforeUpdate.setDescription(itemToUpdate.getDescription());

      Item itemBiz = (Item) itemBeforeUpdate;
      List<ItemTypeDTO> allType = itemDAO.getAllItemType();
      if (!itemBiz.isValidType(itemToUpdate.getItemType(), allType)) {
        dalServices.rollback();
        return null;
      }
      itemDAO.update(itemBeforeUpdate);
      dalServices.commit();
      return itemBeforeUpdate;
    } catch (Exception e) {
      dalServices.rollback();
      throw e;
    }
  }

  /**
   * Indicate for sale an item with the specified ID and sets price.
   *
   * @param idItem the ID of the item to idciate for sale.
   * @param price  the price of the item.
   * @return an ItemDTO representing the updated item.
   */
  @Override
  public ItemDTO indicateItemInShopIsSold(int idItem, Double price) {
    try {
      dalServices.start();

      ItemDTO itemToUpdate = itemDAO.getOne(idItem);

      if (itemToUpdate == null) {
        dalServices.rollback();
        return null;
      }

      Item item = (Item) itemToUpdate;
      item.indicateInShopIsSold(price);
      itemDAO.update(itemToUpdate);
      dalServices.commit();
      return itemToUpdate;

    } catch (Exception e) {
      dalServices.rollback();
      throw e;
    }
  }

  @Override
  public Map<String, Long> getNumberOfItemsByStateAndPeriod(LocalDate startDate,
      LocalDate endDate) {
    List<ItemDTO> allItems = getAll("all", "all", null, null, null);
    
    String[] tabStates = {"accepté", "vendu", "refusé", "proposé"};
    List<String> listValidStates = Arrays.asList(tabStates);
    allItems = allItems.stream().filter(
            itemDTO -> listValidStates.contains(itemDTO.getState()))
        .toList();

    allItems = allItems.stream()
        .filter(objet -> !objet.getDateParkArrival().isBefore(startDate)
            && !objet.getDateParkArrival().isAfter(endDate))
        .toList();

    Map<String, Long> statistics = Arrays.stream(tabStates)
        .collect(toMap(state -> state, state -> 0L));

    Map<String, Long> statisticsTemp = allItems.stream()
        .collect(groupingBy(ItemDTO::getState, counting()));

    for (String state : statisticsTemp.keySet()) {
      Long value = statisticsTemp.get(state);
      statistics.put(state, value);
    }

    return statistics;
  }
}