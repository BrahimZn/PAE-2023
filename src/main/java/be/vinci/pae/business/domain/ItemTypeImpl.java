package be.vinci.pae.business.domain;

/**
 * This class represents an implementation of the ItemTypeDTO interface. It contains the id and
 * label properties of an ItemType.
 */
public class ItemTypeImpl implements ItemTypeDTO {

  private int id;
  private String label;


  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public String getLabel() {
    return label;
  }

  @Override
  public void setLabel(String label) {
    this.label = label;
  }
}