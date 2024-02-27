package be.vinci.pae.ucc;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import be.vinci.pae.business.domain.DomainFactory;
import be.vinci.pae.business.domain.DomainFactoryImpl;
import be.vinci.pae.business.domain.ItemDTO;
import be.vinci.pae.business.domain.ItemTypeDTO;
import be.vinci.pae.business.ucc.ItemUCC;
import be.vinci.pae.business.ucc.ItemUCCImpl;
import be.vinci.pae.dal.DALBackendServicesImpl;
import be.vinci.pae.dal.DALServices;
import be.vinci.pae.dal.ItemDAO;
import be.vinci.pae.exception.ConflictException;
import be.vinci.pae.exception.FatalException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ItemUCCImplTest {

  private static ItemUCC itemUCC;
  private static ItemDAO itemDAOMock;
  private static DomainFactory domainFactory;
  private static DALServices dalServices;
  private final int idUserToGetHisItems = 45;
  ItemDTO itemDTO1 = domainFactory.getItem();
  ItemDTO itemDTO2 = domainFactory.getItem();
  ItemDTO item = domainFactory.getItem();
  private int itemId = 1;
  private ItemDTO itemInStore;
  private ItemDTO itemSold;
  private ItemDTO itemForSale;
  private ItemDTO itemProposed;
  private ItemDTO itemAccepted;
  private ItemDTO itemRemovable;
  private int idItemNoExistent;
  private List<ItemDTO> allItems;

  private List<ItemDTO> allItemsOfUser;

  private List<LocalDate> availabilities;
  private List<ItemTypeDTO> itemTypeList;

  @BeforeAll
  static void bind() {
    itemDAOMock = mock(ItemDAO.class);
    dalServices = mock(DALBackendServicesImpl.class);
    ServiceLocator locator = ServiceLocatorUtilities.bind(new AbstractBinder() {
      @Override
      protected void configure() {
        bind(itemDAOMock).to(ItemDAO.class);
        bind(dalServices).to(DALServices.class);
        bind(DomainFactoryImpl.class).to(DomainFactory.class);
        bind(ItemUCCImpl.class).to(ItemUCC.class);
      }
    });
    itemUCC = locator.getService(ItemUCC.class);
    domainFactory = locator.getService(DomainFactory.class);
  }

  @BeforeEach
  void setUp() {
    reset(itemDAOMock);
    when(itemDAOMock.getOne(idItemNoExistent)).thenReturn(null);

    ItemTypeDTO type1 = domainFactory.getItemTypeDTO();
    type1.setLabel("type1");
    ItemTypeDTO type2 = domainFactory.getItemTypeDTO();
    type2.setLabel("type2");
    ItemTypeDTO type3 = domainFactory.getItemTypeDTO();
    type3.setLabel("type3");

    itemTypeList = new ArrayList<>();
    itemTypeList.add(type1);
    itemTypeList.add(type2);
    itemTypeList.add(type3);

    when(itemDAOMock.getAllItemType()).thenReturn(itemTypeList);

    availabilities = new ArrayList<>();
    LocalDate saturday = LocalDate.of(2023, 5, 6);

    availabilities.add(saturday);
    availabilities.add(saturday.plusWeeks(1));
    availabilities.add(saturday.plusWeeks(2));
    availabilities.add(saturday.plusWeeks(3));

    when(itemDAOMock.getAvailabilities()).thenReturn(availabilities);

    itemInStore = domainFactory.getItem();
    itemInStore.setState("en magasin");
    itemInStore.setId(1);
    itemInStore.setItemType(itemTypeList.get(2).getLabel());
    itemInStore.setDateParkArrival(availabilities.get(0));
    when(itemDAOMock.getOne(1)).thenReturn(itemInStore);

    itemSold = domainFactory.getItem();
    itemSold.setState("vendu");
    itemSold.setSellingPrice(7);
    itemSold.setId(2);
    itemSold.setItemType(itemTypeList.get(2).getLabel());
    itemSold.setDateParkArrival(availabilities.get(1));
    when(itemDAOMock.getOne(2)).thenReturn(itemSold);

    itemForSale = domainFactory.getItem();
    itemForSale.setState("en vente");
    itemForSale.setSellingPrice(9);
    itemForSale.setId(3);
    itemForSale.setStoreDepositDate(LocalDate.now().minusDays(5));
    itemForSale.setItemType(itemTypeList.get(1).getLabel());
    itemForSale.setDateParkArrival(availabilities.get(1));
    when(itemDAOMock.getOne(3)).thenReturn(itemForSale);

    itemProposed = domainFactory.getItem();
    itemProposed.setState("proposé");
    itemProposed.setId(4);
    itemProposed.setItemType(itemTypeList.get(0).getLabel());
    itemProposed.setDateParkArrival(availabilities.get(1));
    when(itemDAOMock.getOne(4)).thenReturn(itemProposed);

    itemAccepted = domainFactory.getItem();
    itemAccepted.setState("accepté");
    itemAccepted.setId(5);
    itemAccepted.setItemType(itemTypeList.get(0).getLabel());
    itemAccepted.setDateParkArrival(availabilities.get(2));
    when(itemDAOMock.getOne(5)).thenReturn(itemAccepted);

    itemRemovable = domainFactory.getItem();
    itemRemovable.setState("en vente");
    itemRemovable.setId(6);
    itemRemovable.setStoreDepositDate(LocalDate.now().minusDays(35));
    itemRemovable.setItemType(itemTypeList.get(0).getLabel());
    itemRemovable.setSellingPrice(5);
    itemRemovable.setDateParkArrival(availabilities.get(2));
    when(itemDAOMock.getOne(6)).thenReturn(itemRemovable);

    allItems = new ArrayList<>();

    allItems.add(itemInStore);
    allItems.add(itemSold);
    allItems.add(itemForSale);
    allItems.add(itemProposed);
    allItems.add(itemRemovable);
    when(itemDAOMock.getAll()).thenReturn(allItems);

    allItemsOfUser = new ArrayList<>();
    allItemsOfUser.add(itemInStore);
    allItemsOfUser.add(itemRemovable);
    when(itemDAOMock.getAllForUser(idUserToGetHisItems)).thenReturn(allItemsOfUser);

    doNothing().when(itemDAOMock).insertAvailability(any());
    when(itemDAOMock.insert(any())).thenReturn(50);

  }


  @DisplayName("Test update item with valid type")
  @Test
  void testUpdateItem1() {

    ItemDTO updateItem = itemAccepted;
    updateItem.setItemType("type2");
    updateItem.setPicture("picture.jpg");

    ItemDTO itemUpdated = itemUCC.updateItem(updateItem);

    assertEquals(itemAccepted.getId(), itemUpdated.getId());


  }

  @DisplayName("Test update item with invalid type")
  @Test
  void testUpdateItem2() {

    ItemDTO updateItem = itemAccepted;
    updateItem.setItemType("typeNOTVALID");
    assertNull(itemUCC.updateItem(updateItem));

  }


  @DisplayName("Test update item with no existent item")
  @Test
  void testUpdateItem3() {

    ItemDTO updateItem = domainFactory.getItem();
    updateItem.setId(idItemNoExistent);
    assertNull(itemUCC.updateItem(updateItem));

  }

  @DisplayName("Test update item with error dao")
  @Test
  void testUpdateItem4() {
    when(itemDAOMock.getOne(idItemNoExistent)).thenThrow(FatalException.class);

    ItemDTO updateItem = domainFactory.getItem();
    updateItem.setId(idItemNoExistent);

    assertThrows(FatalException.class, () -> itemUCC.updateItem(updateItem));
  }

  @DisplayName("Test get all itemType")
  @Test
  void getAllItemType() {

    assertEquals(itemTypeList.size(), itemUCC.getAllItemType().size());


  }

  @DisplayName("Test get all itemType error dao")
  @Test
  void getAllItemTypeWithException() {

    when(itemDAOMock.getAllItemType()).thenThrow(FatalException.class);
    assertThrows(FatalException.class, () -> itemUCC.getAllItemType());


  }


  @DisplayName("Test addItem")
  @Test
  void testAddItem() {

    ItemDTO newItem = domainFactory.getItem();

    ItemDTO itemDTOAdded = itemUCC.addItem(newItem);

    assertEquals("proposé", itemDTOAdded.getState());


  }

  @DisplayName("Test addItem with exception DAO")
  @Test
  void testAddItemWithExceptionDAO() {

    when(itemDAOMock.insert(any())).thenThrow(FatalException.class);
    ItemDTO newItem = domainFactory.getItem();

    assertThrows(FatalException.class, () -> itemUCC.addItem(newItem));


  }

  @DisplayName("Test get availabilities")
  @Test
  void getAvailabilities() {

    assertEquals(availabilities.size(), itemUCC.getAvailabilities().size());


  }

  @DisplayName("Test get availabilities with error dao")
  @Test
  void getAvailabilitiesWithExceptionDAO() {
    when(itemDAOMock.getAvailabilities()).thenThrow(FatalException.class);
    assertThrows(FatalException.class, () -> itemUCC.getAvailabilities());
  }

  @DisplayName("Test add availability already exist")
  @Test
  void testAddAvailabilityAlreadyExist() {

    assertThrows(ConflictException.class, () -> itemUCC.addAvailability(availabilities.get(0)));
  }

  @DisplayName("Test add availability should return void")
  @Test
  void testAddAvailability() {

    assertDoesNotThrow(() -> itemUCC.addAvailability(availabilities.get(0).minusWeeks(7)));
  }

  @DisplayName("Test getAllItem")
  @Test
  void getAll() {
    List<ItemDTO> allItem = itemUCC.getAll("all", "all", null, null, null);

    assertEquals(allItems.size(), allItem.size());


  }

  @DisplayName("Test getAllItem bydate")
  @Test
  void getAllByDate() {
    List<ItemDTO> allItem = itemUCC.getAll("all", "all", null, null, availabilities.get(1));

    assertEquals(3, allItem.size());


  }

  @DisplayName("Test getAllItem filtered by type")
  @Test
  void getAllByType() {
    List<ItemDTO> allItem = itemUCC.getAll("all", itemTypeList.get(0).getLabel(), null, null, null);

    assertEquals(2, allItem.size());


  }

  @DisplayName("Test getAllItem filtered by amount")
  @Test
  void getAllByAmount() {
    List<ItemDTO> allItem = itemUCC.getAll("all", "all", 7.0, 10.0, null);

    assertEquals(2, allItem.size());


  }

  @DisplayName("Test getAllItem with exceptionDAO")
  @Test
  void getAllWithExceptionDAO() {
    when(itemDAOMock.getAll()).thenThrow(FatalException.class);
    assertThrows(FatalException.class, () -> itemUCC.getAll("all", "all", null, null, null));

  }

  @DisplayName("Test getAllItem of user")
  @Test
  void getAllOfUser() {

    List<ItemDTO> itemOfUser = itemUCC.getAllForUser(idUserToGetHisItems);

    assertEquals(allItemsOfUser.size(), itemOfUser.size());


  }


  @DisplayName("Test getAllItem of user with exceptionDAO")
  @Test
  void getAllOfUserWithExceptionDAO() {
    when(itemDAOMock.getAllForUser(idUserToGetHisItems)).thenThrow(FatalException.class);
    assertThrows(FatalException.class, () -> itemUCC.getAllForUser(idUserToGetHisItems));

  }

  @DisplayName("Test getAllItem fitltered by vendu")
  @Test
  void getAllItemSold() {

    List<ItemDTO> allItemSold = itemUCC.getAll("vendu", "all", null, null, null);

    assertEquals(1, allItemSold.size());


  }

  @DisplayName("Test getAllItem showOnlyVisible")
  @Test
  void getAllOnlyVisible() {

    List<ItemDTO> allItemResult = itemUCC.getAll("visible", "all", null, null, null);

    assertEquals(4, allItemResult.size());


  }

  @DisplayName("Test get item by id")
  @Test
  void getItemById() {

    int id = itemInStore.getId();
    ItemDTO itemFound = itemUCC.getItemById(id);
    assertEquals(id, itemFound.getId());

  }


  @DisplayName("Test get item by id with exceptionDAO")
  @Test
  void getItemByIdWithExceptionDAO() {

    int id = itemInStore.getId();
    when(itemDAOMock.getOne(id)).thenThrow(FatalException.class);
    assertThrows(FatalException.class, () -> itemUCC.getItemById(id));


  }

  @Test
  void acceptProposal_validId_shouldReturnUpdatedItem() {

    itemUCC.acceptProposal(itemProposed.getId());
    assertEquals("accepté", itemProposed.getState());
  }


  @Test
  void acceptProposal_stateInvalid_shouldThrowsExecption() {

    assertThrows(ConflictException.class, () -> itemUCC.acceptProposal(itemSold.getId()));
  }

  @Test
  void acceptProposal_forItemDosNotExist_shouldReturnNull() {

    ItemDTO itemResult = itemUCC.acceptProposal(idItemNoExistent);
    assertNull(itemResult);
  }

  @Test
  void declineProposal_validState_shouldReturnUpdatedItem() {

    String reasonForRefusal = "Not interested";

    ItemDTO result = itemUCC.declineProposal(itemProposed.getId(), reasonForRefusal);
    assertAll(() -> assertEquals("refusé", result.getState()),
        () -> assertEquals(reasonForRefusal, result.getReasonForRefusal())

    );
  }

  @Test
  void declineProposal_invalidState_shouldThrowsExeption() {

    String reasonForRefusal = "Not interested";
    assertThrows(ConflictException.class,
        () -> itemUCC.declineProposal(itemSold.getId(), reasonForRefusal));
  }


  @Test
  void declineProposal_forItemDosNotExist_shouldReturnNull() {
    ItemDTO itemResult = itemUCC.declineProposal(idItemNoExistent, "Not interested");
    assertNull(itemResult);
  }


  @Test
  void indicateItemInStore_validState_shouldReturnUpdatedItem() {

    ItemDTO itemResult = itemUCC.indicateItemStore(itemAccepted.getId());
    assertEquals("en magasin", itemResult.getState());
  }

  @Test
  void indicateItemInStore_invalidState_shouldThrowExeption() {
    assertThrows(ConflictException.class, () -> itemUCC.indicateItemStore(itemSold.getId()));
  }

  @Test
  void indicateItemInStore_doesNotExist_shouldReturnNull() {

    ItemDTO itemDTOUpdated = itemUCC.indicateItemStore(idItemNoExistent);
    assertNull(itemDTOUpdated);
  }

  @Test
  void indicateItemToWorkshop_validState_shouldReturnUpdatedItem() {

    ItemDTO itemResult = itemUCC.indicateItemWorkshop(itemAccepted.getId());
    assertEquals("à l'atelier", itemResult.getState());
  }

  @Test
  void indicateItemToWorkshop_invalidState_shouldReturnUpdatedItem() {

    assertThrows(ConflictException.class, () -> itemUCC.indicateItemWorkshop(itemSold.getId()));
  }

  @Test
  void indicateItemToWorkshop_doesNotExist_shouldReturnNull() {

    ItemDTO itemDTOUpdated = itemUCC.indicateItemWorkshop(idItemNoExistent);
    assertNull(itemDTOUpdated);
  }

  @Test
  void indicateForSale_invalidState_shouldThrowsExeption() {

    assertThrows(ConflictException.class,
        () -> itemUCC.indicateItemIsForSale(itemForSale.getId(), 2.0));
  }


  @Test
  void indicateForSale_validState_shouldReturnUpdatedItem() {

    double price = 3;
    ItemDTO itemResult = itemUCC.indicateItemIsForSale(itemInStore.getId(), price);
    assertAll(() -> assertEquals("en vente", itemResult.getState()),
        () -> assertEquals(price, itemResult.getSellingPrice())

    );
  }

  @Test
  void indicateForSale_notExists_shouldReturnNull() {

    ItemDTO result = itemUCC.indicateItemIsForSale(idItemNoExistent, 5.0);
    assertNull(result);
  }

  @Test
  void indicateInShopIs_notExists_shouldReturnNull() {

    ItemDTO result = itemUCC.indicateItemInShopIsSold(idItemNoExistent, 5.0);
    assertNull(result);
  }

  @Test
  void indicateInShopIsSale_shouldReturnItemUpdated() {

    double price = 3;
    ItemDTO itemUpdated = itemUCC.indicateItemInShopIsSold(itemInStore.getId(), price);

    assertAll(() -> assertEquals(itemUpdated.getState(), "vendu"),
        () -> assertEquals(price, itemUpdated.getSellingPrice()));
  }

  @Test
  void indicateInShopIsSale_shouldThrowException() {

    double price = 3;
    assertThrows(ConflictException.class,
        () -> itemUCC.indicateItemInShopIsSold(itemProposed.getId(), price));
  }

  @Test
  void indicateIsSold_shouldReturnItemUpdated() {

    ItemDTO itemUpdated = itemUCC.indicateItemIsSold(itemForSale.getId());

    assertEquals(itemUpdated.getState(), "vendu");
  }

  @Test
  void indicateIsSoldWithIdNoExistent_shouldReturnNull() {
    ItemDTO itemUpdated = itemUCC.indicateItemIsSold(idItemNoExistent);
    assertNull(itemUpdated);

  }

  @Test
  void indicateIsSoldWithInvalidState_sshouldThrowException() {

    assertThrows(ConflictException.class, () -> itemUCC.indicateItemIsSold(itemProposed.getId()));

  }

  @Test
  void getAllRemovable() {

    assertEquals(1, itemUCC.getAllRemovableItems().size());

  }

  @Test
  void removeItemFromSale() {

    ItemDTO itemResult = itemUCC.removeItemFromSale(itemForSale.getId());

    assertAll(() -> assertEquals("retiré de la vente", itemResult.getState()),
        () -> assertEquals(LocalDate.now(), itemResult.getMarketWithdrawalDate()));

  }

  @Test
  void removeItemFromSaleWithInvalidStates() {
    assertThrows(ConflictException.class, () -> itemUCC.removeItemFromSale(itemProposed.getId()));

  }

  @DisplayName("Get statistics ")
  @Test
  void getStats() {
    Map<String, Long> stats = itemUCC.getNumberOfItemsByStateAndPeriod(
        availabilities.get(0).minusDays(1),
        availabilities.get(3).plusWeeks(2));
    
    assertAll(
        () -> assertEquals(stats.get("accepté"), 0),
        () -> assertEquals(stats.get("vendu"), 1),
        () -> assertEquals(stats.get("refusé"), 0),
        () -> assertEquals(stats.get("proposé"), 1)
    );

  }

  // test ok jusqua cette ligne

  @DisplayName("Test getAllRemovableItems")
  @Test
  void testGetAllRemovableItems() {
    ItemDTO itemDTO1 = domainFactory.getItem();
    itemDTO1.setState("en vente");
    itemDTO1.setStoreDepositDate(LocalDate.now().minusDays(35));

    ItemDTO itemDTO2 = domainFactory.getItem();
    itemDTO2.setState("vendu");
    itemDTO2.setStoreDepositDate(LocalDate.now().minusDays(25));

    ItemDTO itemDTO3 = domainFactory.getItem();
    itemDTO3.setState("en vente");
    itemDTO3.setStoreDepositDate(LocalDate.now().minusDays(15));

    when(itemDAOMock.getAll()).thenReturn(List.of(itemDTO1));

    List<ItemDTO> removableItems = itemUCC.getAllRemovableItems();

    assertEquals(1, removableItems.size(), "nombre d'items retirable de la vente inéxacte");
    assertEquals(itemDTO1, removableItems.get(0),
        "un mauvais item a été introduis dans la liste des items retirables");

    assertFalse(removableItems.contains(itemDTO2),
        "l'objet à été vendu et ne peux donc pas être retirable de la vente");
    assertFalse(removableItems.contains(itemDTO3),
        "les 30 jours ne sont pas passés pour que item soit retirable de la vente");
  }

  @DisplayName("Test removeItemFromSale")
  @Test
  void testRemoveItemFromSale() {
    // Création d'un objet ItemDTO factice pour les tests
    ItemDTO item = domainFactory.getItem();
    item.setId(17);
    item.setState("en vente");
    item.setStoreDepositDate(LocalDate.now().minusDays(35));
    when(itemDAOMock.getOne(item.getId())).thenReturn(item);

    // Appel de la méthode à tester
    ItemDTO removedItemDTO = itemUCC.removeItemFromSale(item.getId());

    // Vérification du résultat de la méthode
    assertNotNull(removedItemDTO, "L'objet ItemDTO retourné ne doit pas être null");
    assertEquals("retiré de la vente", removedItemDTO.getState(),
        "L'état de l'objet ItemDTO doit être mis à jour");
    assertNotNull(removedItemDTO.getMarketWithdrawalDate(),
        "La date de retrait du marché doit être définie");

    // Vérification que l'objet ItemDTO a été correctement mis à jour dans la base de données
    ItemDTO itemFromDB = itemUCC.getItemById(removedItemDTO.getId());
    assertNotNull(itemFromDB, "L'objet ItemDTO doit être trouvé dans la base de données");
    assertEquals("retiré de la vente", itemFromDB.getState(),
        "L'état de l'objet ItemDTO doit être mis à jour dans la base de données");
    assertNotNull(itemFromDB.getMarketWithdrawalDate(),
        "La date de retrait du marché doit être définie dans la base de données");

    // Vérification que l'objet ItemDTO ne peut pas être retiré de la vente s'il a déjà été vendu
    ItemDTO soldItemDTO = domainFactory.getItem();
    soldItemDTO.setState("vendu");
    soldItemDTO.setStoreDepositDate(LocalDate.now().minusDays(25));
    assertNull(itemUCC.removeItemFromSale(soldItemDTO.getId()),
        "Un objet vendu ne doit pas être retiré de la vente");

    // Vérification que l'objet ItemDTO ne peut pas être retiré de la vente si moins de 30 jours se
    // sont écoulés depuis son dépôt en magasin
    ItemDTO newItemDTO = domainFactory.getItem();
    newItemDTO.setState("en vente");
    newItemDTO.setStoreDepositDate(LocalDate.now().minusDays(15));
    assertNull(itemUCC.removeItemFromSale(newItemDTO.getId()),
        "Un objet qui n'a pas été en vente pendant 30 jours ne doit pas être retiré de la vente");
  }

}