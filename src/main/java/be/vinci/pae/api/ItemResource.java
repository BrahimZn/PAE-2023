package be.vinci.pae.api;

import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.api.filters.IsHelper;
import be.vinci.pae.api.filters.IsManager;
import be.vinci.pae.business.domain.ItemDTO;
import be.vinci.pae.business.domain.ItemTypeDTO;
import be.vinci.pae.business.domain.UserDTO;
import be.vinci.pae.business.ucc.ItemUCC;
import be.vinci.pae.business.ucc.NotificationUCC;
import be.vinci.pae.business.ucc.UserUCC;
import be.vinci.pae.main.Main;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.server.ContainerRequest;


/**
 * This class represents the REST API endpoint for items. The class is annotated with @Singleton to
 * ensure that only one instance of the class exists in the application. The class is annotated with
 * Path items to specify the base URI path for all requests handled by this class.
 */
@Singleton
@Path("/items")
public class ItemResource {

  private final Logger logger = LogManager.getLogger(Main.class.getName());
  @Inject
  private ItemUCC itemUCC;

  @Inject
  private UserUCC userUCC;

  @Inject
  private NotificationUCC notificationUCC;


  /**
   * Retrieves all items from the API based on the given parameters.
   *
   * @param state the state of the items to retrieve.
   * @param type  the type of items to retrieve.
   * @return a list of ItemDTO objects.
   * @throws WebApplicationException if no items are found or an internal server error occurs.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<ItemDTO> getAllItems(@DefaultValue("all") @QueryParam("state") String state,
      @DefaultValue("all") @QueryParam("type") String type,
      @QueryParam("amountMin") Double amountMin,
      @QueryParam("amountMax") Double amountMax, @QueryParam("date") String date) {

    if (amountMax == null && amountMin != null || amountMin == null && amountMax != null
        || amountMax != null && amountMax < 0 || amountMax != null && amountMax > 10 ||
        amountMin != null && amountMin < 0
        || amountMin != null && amountMin > 10
        || amountMax != null && amountMin > amountMax) {
      throw new WebApplicationException(
          Response.status(Status.BAD_REQUEST).entity("Invalid data").type("text/plain")
              .build());
    }

    LocalDate dateFilter = null;
    if (date != null) {
      try {
        dateFilter = LocalDate.parse(date);
      } catch (Exception e) {
        throw new WebApplicationException(
            Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).type("text/plain")
                .build());
      }
    }

    logger.info(
        "state = " + state + ", type = " + type + ", amountMin = " + amountMin + ", amountMax = "
            + amountMax + ", date = " + date);
    return itemUCC.getAll(state, type, amountMin, amountMax, dateFilter);

  }

  /**
   * Retrieves all items from the API based on the given parameters.
   *
   * @param request contains information of the requester.
   * @return a list of ItemDTO objects.
   * @throws WebApplicationException if no items are found or an internal server error occurs.
   */

  @Path("/myItems")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public List<ItemDTO> getAllItemsForUser(@Context ContainerRequest request) {

    UserDTO authenticatedUser = (UserDTO) request.getProperty("user");
    int idUser = authenticatedUser.getUserId();

    List<ItemDTO> itemDTOList = itemUCC.getAllForUser(idUser);

    if (itemDTOList == null) {
      throw new WebApplicationException("No content", Status.NO_CONTENT);
    }

    return itemDTOList;
  }


  /**
   * Retrieves all items for the user with the given parameters.
   *
   * @param idUser of user.
   * @return a list of ItemDTO objects.
   * @throws WebApplicationException if no items are found or an internal server error occurs.
   */

  @Path("/user/{id}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  @IsHelper
  public List<ItemDTO> getAllItemsForUser(@PathParam("id") int idUser) {

    List<ItemDTO> itemDTOList = itemUCC.getAllForUser(idUser);

    if (itemDTOList == null) {
      throw new WebApplicationException("No content", Status.NO_CONTENT);
    }

    return itemDTOList;
  }

  /**
   * Retrieves an ItemDTO object with the specified ID through a GET request.
   *
   * @param idItem the ID of the ItemDTO object to retrieve.
   * @return an ItemDTO object representing the ItemDTO object with the specified ID.
   */
  @Path("/{id}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public ItemDTO getOneItem(@PathParam("id") int idItem) {

    ItemDTO itemDTO = itemUCC.getItemById(idItem);
    if (itemDTO == null) {
      throw new WebApplicationException("Item not found", Status.NOT_FOUND);

    }
    return itemDTO;
  }

  /**
   * REST endpoint for adding a new item.
   *
   * @param newItemDTO the ItemDTO object representing the new item to add.
   * @return the ItemDTO object representing the added item.
   * @throws WebApplicationException if the item is not valid or cannot be added.
   */
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public ItemDTO addItem(ItemDTO newItemDTO) {

    // Validate the new item
    if (newItemDTO == null || newItemDTO.getItemType() == null || newItemDTO.getItemType().isBlank()
        || newItemDTO.getDescription() == null || newItemDTO.getDescription().isBlank()
        || newItemDTO.getPhoneNumber() == null || newItemDTO.getPhoneNumber().isBlank()
        || newItemDTO.getPicture() == null || newItemDTO.getPicture().isBlank()
        || newItemDTO.getTimeSlot() == null || newItemDTO.getTimeSlot().isBlank()) {
      throw new WebApplicationException("Invalid item data", Status.BAD_REQUEST);
    }

    // Add the new item
    ItemDTO addedItemDTO = itemUCC.addItem(newItemDTO);
    if (addedItemDTO == null) {
      throw new WebApplicationException("Item could not be added", Status.INTERNAL_SERVER_ERROR);
    }

    return addedItemDTO;
  }

  /**
   * REST endpoint for adding a new item for a user connected.
   *
   * @param request    contains information of the requester.
   * @param newItemDTO the ItemDTO object representing the new item to add.
   * @return the ItemDTO object representing the added item.
   * @throws WebApplicationException if the item is not valid or cannot be added.
   */
  @Path("/userisconnected")
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Authorize
  public ItemDTO addItemUserConnected(@Context ContainerRequest request, ItemDTO newItemDTO) {
    UserDTO authenticatedUser = (UserDTO) request.getProperty("user");
    // Validate the new item
    if (newItemDTO == null || newItemDTO.getItemType() == null || newItemDTO.getItemType().isBlank()
        || newItemDTO.getDescription() == null || newItemDTO.getDescription().isBlank()
        || newItemDTO.getPicture() == null || newItemDTO.getPicture().isBlank()
        || newItemDTO.getTimeSlot() == null || newItemDTO.getTimeSlot().isBlank()) {
      throw new WebApplicationException("Invalid item data", Status.BAD_REQUEST);
    }
    newItemDTO.setIdOferringUser(authenticatedUser.getUserId());
    // Add the new item
    ItemDTO addedItemDTO = itemUCC.addItem(newItemDTO);
    if (addedItemDTO == null) {
      throw new WebApplicationException("Item could not be added", Status.INTERNAL_SERVER_ERROR);
    }

    return addedItemDTO;
  }

  /**
   * Updates the item with the provided data.
   *
   * @param item the data of the item to be updated.
   * @return the updated item.
   * @throws WebApplicationException if the provided data is invalid or the item does not exist.
   */
  @PATCH
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public ItemDTO updateItem(ItemDTO item) {

    if (item == null || item.getItemType() == null || item.getItemType().isBlank()
        || item.getDescription() == null || item.getDescription().isBlank() || item.getId() == 0) {

      throw new WebApplicationException(
          Response.status(Status.BAD_REQUEST).entity("Invalid item data").type("text/plain")
              .build());
    }
    if (item.getSellingPrice() > 10 || item.getSellingPrice() < 0) {
      throw new WebApplicationException(
          Response.status(Status.BAD_REQUEST).entity("Invalid Price").type("text/plain").build());
    }

    ItemDTO itemUpdated = itemUCC.updateItem(item);

    if (itemUpdated == null) {
      throw new WebApplicationException(
          Response.status(Status.BAD_REQUEST).entity("Item does not exist or invalid type")
              .type("text/plain").build());
    }
    return itemUpdated;

  }


  /**
   * Retrieves all availabilities  through a GET request.
   *
   * @return a List of LocalDate objects representing all availabilities in the system.
   */
  @Path("/availabilities")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<LocalDate> getAvailabilities() {

    List<LocalDate> availabilities = itemUCC.getAvailabilities();
    if (availabilities == null || availabilities.size() == 0) {

      throw new WebApplicationException("No content", Status.NO_CONTENT);


    }
    return availabilities;
  }

  /**
   * Adds an availability for the helpers or responsible to retrieve the object. The availability is
   * provided in JSON format and must contain the date in the format "YYYY-MM-DD". The availability
   * must be a Saturday after the current date. If the availability is valid, it is added to the
   * database through the itemUCC object. If the availability is not valid, a BAD_REQUEST error is
   * returned with the appropriate message.
   *
   * @param jsonNode a JsonNode object containing the availability in the format {"availability":
   *                 "YYYY-MM-DD"}
   * @return a Response with a status of 200 OK and the date in JSON format if successful.
   * @throws WebApplicationException if the JsonNode object is missing the "availability" field, if
   *                                 the date is invalid, or if there is an issue with adding the
   *                                 availability to the database.
   */
  @Path("/availabilities")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  @IsHelper
  public Response addAvailability(JsonNode jsonNode) {

    if (!jsonNode.has("availability") || jsonNode.get("availability").asText().isBlank()) {
      throw new WebApplicationException(
          Response.status(Response.Status.BAD_REQUEST).entity("Invalid data").type("text/plain")
              .build());
    }
    LocalDate availability;
    try {
      availability = LocalDate.parse(jsonNode.get("availability").asText());
    } catch (Exception e) {
      throw new WebApplicationException(
          Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).type("text/plain")
              .build());
    }

    if (availability == null || availability.isBefore(LocalDate.now())
        || !availability.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
      throw new WebApplicationException(
          Response.status(Response.Status.BAD_REQUEST).entity("Invalid date").type("text/plain")
              .build());
    }
    itemUCC.addAvailability(availability);

    return Response.ok().entity(availability).type(MediaType.APPLICATION_JSON).build();
  }


  /**
   * Retrieves all ItemTypeDTO objects through a GET request.
   *
   * @return a List of ItemTypeDTO objects representing all items types in the system.
   */
  @Path("/types")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<ItemTypeDTO> getAllItem() {

    List<ItemTypeDTO> allItemType = itemUCC.getAllItemType();
    if (allItemType == null || allItemType.size() == 0) {

      throw new WebApplicationException("No content", Status.NO_CONTENT);


    }
    return allItemType;
  }


  /**
   * REST endpoint for accepting a proposal for an item.
   *
   * @param idItem the ID of the item to accept the proposal for.
   * @return the ItemDTO of the accepted item.
   * @throws WebApplicationException if the item is not found.
   */
  @Path("/acceptProposal/{id}")
  @PATCH
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  @IsHelper
  public ItemDTO acceptProposal(@PathParam("id") int idItem) {

    ItemDTO itemDTO = itemUCC.acceptProposal(idItem);
    if (itemDTO == null) {
      throw new WebApplicationException("Item not found", Status.NOT_FOUND);

    }
    return itemDTO;
  }


  /**
   * REST endpoint for decline a proposal for an item.
   *
   * @param idItem the ID of the item to decline the proposal.
   * @param json   a JSON object containing the reason for refusal.
   * @return the ItemDTO of the declined item.
   * @throws WebApplicationException if the item is not found.
   */
  @Path("/declineProposal/{id}")
  @PATCH
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  @IsHelper
  public ItemDTO declineProposal(@PathParam("id") int idItem, JsonNode json) {

    if (!json.hasNonNull("reasonForRefusal") || json.get("reasonForRefusal").asText().isBlank()
        || json.get("reasonForRefusal").asText().length() > 120) {
      throw new WebApplicationException("Reason for refusal is not valid", Status.BAD_REQUEST);
    }

    String reasonForRefusal = json.get("reasonForRefusal").asText();

    ItemDTO itemDTO = itemUCC.declineProposal(idItem, reasonForRefusal);
    if (itemDTO == null) {
      throw new WebApplicationException("Item not found", Status.NOT_FOUND);

    }
    return itemDTO;
  }

  /**
   * REST endpoint for storing an element.
   *
   * @param idItem the ID of the item for which it is to be placed in the store.
   * @return the ItemDTO of the accepted item in the store.
   * @throws WebApplicationException if the item is not found.
   */
  @Path("/indicateItemStore/{id}")
  @PATCH
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  @IsHelper
  public ItemDTO indicateItemStore(@PathParam("id") int idItem) {

    ItemDTO itemDTO = itemUCC.indicateItemStore(idItem);
    if (itemDTO == null) {
      throw new WebApplicationException("Item not found", Status.NOT_FOUND);

    }
    return itemDTO;
  }

  /**
   * REST access point for placing an element in the workshop.
   *
   * @param idItem the ID of the item for which it is to be placed in the workshop.
   * @return the ItemDTO of the accepted item in workshop.
   * @throws WebApplicationException if the item is not found.
   */
  @Path("/indicateItemWorkshop/{id}")
  @PATCH
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  @IsHelper
  public ItemDTO indicateItemWorkshop(@PathParam("id") int idItem) {

    ItemDTO itemDTO = itemUCC.indicateItemWorkshop(idItem);
    if (itemDTO == null) {
      throw new WebApplicationException("Item not found", Status.NOT_FOUND);

    }
    return itemDTO;
  }

  /**
   * REST access point for indicate an items is for sale.
   *
   * @param idItem the ID of the item for which it is to be for sale.
   * @param json   a JSON object containing the price.
   * @return the ItemDTO of .
   * @throws WebApplicationException if the item is not found.
   */
  @Path("/indicateItemForSale/{id}")
  @PATCH
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Authorize
  @IsHelper
  public ItemDTO indicateItemForSale(@PathParam("id") int idItem, JsonNode json) {

    if (!json.hasNonNull("price") || json.get("price").asDouble() > 10) {
      throw new WebApplicationException("Price is not valid", Status.BAD_REQUEST);
    }

    Double price = json.get("price").asDouble();

    ItemDTO itemDTO = itemUCC.indicateItemIsForSale(idItem, price);
    if (itemDTO == null) {
      throw new WebApplicationException("Item not found", Status.NOT_FOUND);

    }
    return itemDTO;
  }

  /**
   * REST access point for indicate an items is for sale.
   *
   * @param idItem the ID of the item for which it is to be for sale.
   * @return the ItemDTO of .
   * @throws WebApplicationException if the item is not found.
   */
  @Path("/indicateItemIsSold/{id}")
  @PATCH
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  @IsHelper
  public ItemDTO indicateItemIsSold(@PathParam("id") int idItem) {

    ItemDTO itemDTO = itemUCC.indicateItemIsSold(idItem);
    if (itemDTO == null) {
      throw new WebApplicationException("Item not found", Status.NOT_FOUND);

    }
    return itemDTO;
  }

  /**
   * Retrieves all ItemDTO objects that are marked as "removable" in the system.
   *
   * @return A list of ItemDTO objects marked as "removable".
   * @throws WebApplicationException If no items are found.
   */
  @Path("/allRemovableItems")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<ItemDTO> getAllRemovable() {
    List<ItemDTO> allRemovableItems = itemUCC.getAllRemovableItems();
    if (allRemovableItems == null) {
      throw new WebApplicationException("Not found", Status.NOT_FOUND);
    }
    return allRemovableItems;
  }

  /**
   * Removes the object with the specified ID from sale.
   *
   * @param idItem The ID of the object to remove from sale.
   * @return The ItemDTO object that was removed from sale.
   * @throws WebApplicationException If the item is not found.
   */
  @Path("/removeObjectFromSale/{id}")
  @PATCH
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  @IsHelper
  public ItemDTO removeObjectFromSale(@PathParam("id") int idItem) {
    ItemDTO itemDTO = itemUCC.removeItemFromSale(idItem);
    if (itemDTO == null) {
      throw new WebApplicationException("Item not found", Status.NOT_FOUND);

    }
    return itemDTO;

  }


  /**
   * REST access point for indicate an items is for sale.
   *
   * @param idItem the ID of the item for which it is to be for sale.
   * @param json   a JSON object containing the price.
   * @return the ItemDTO of .
   * @throws WebApplicationException if the item is not found.
   */
  @Path("/indicateItemInShopIsSold/{id}")
  @PATCH
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Authorize
  @IsManager
  public ItemDTO indicateItemInShopForSale(@PathParam("id") int idItem, JsonNode json) {

    if (!json.hasNonNull("price") || json.get("price").asDouble() > 10) {
      throw new WebApplicationException("Price is not valid", Status.BAD_REQUEST);
    }

    Double price = json.get("price").asDouble();

    ItemDTO itemDTO = itemUCC.indicateItemInShopIsSold(idItem, price);
    if (itemDTO == null) {
      throw new WebApplicationException("Item not found", Status.NOT_FOUND);

    }
    return itemDTO;
  }


  /**
   * Retrieves statistics of the number of items by state and period.
   *
   * @param dateStartString the start date of the period in the format "yyyy-MM-dd"
   * @param dateEndString   the end date of the period in the format "yyyy-MM-dd"
   * @return a map with the number of items for each state during the given period
   * @throws WebApplicationException if either dateStartString or dateEndString is null or cannot be
   *                                 parsed
   */
  @Path("/statistics")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  @IsHelper
  public Map<String, Long> stats(@QueryParam("dateStart") String dateStartString,
      @QueryParam("dateEnd") String dateEndString) {

    if (dateStartString == null || dateEndString == null) {
      throw new WebApplicationException(
          Response.status(Response.Status.BAD_REQUEST).entity("missing date").type("text/plain")
              .build());
    }
    LocalDate dateStart = null;
    LocalDate dateEnd = null;

    try {
      dateStart = LocalDate.parse(dateStartString);
      dateEnd = LocalDate.parse(dateEndString);
    } catch (Exception e) {
      throw new WebApplicationException(
          Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).type("text/plain")
              .build());
    }

    return itemUCC.getNumberOfItemsByStateAndPeriod(dateStart, dateEnd);

  }
}
