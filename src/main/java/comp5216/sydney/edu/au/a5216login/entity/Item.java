package comp5216.sydney.edu.au.a5216login.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long userId;

    private String name;

    private String catName;

    private String listCode;

    private BigDecimal price;

    private String location;

    private String purchaseUrl;

    private String description;

    private Integer quantity;

    private Integer deleted;

    private Integer sharable;

    private Integer complete;

    private Integer version;

    private Integer type;

    private Date date;

    private String pictureUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPurchaseUrl() {
        return purchaseUrl;
    }

    public void setPurchaseUrl(String purchaseUrl) {
        this.purchaseUrl = purchaseUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Integer getSharable() {
        return sharable;
    }

    public void setSharable(Integer sharable) {
        this.sharable = sharable;
    }

    public Integer getComplete() {
        return complete;
    }

    public void setComplete(Integer complete) {
        this.complete = complete;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getListCode() {
        return listCode;
    }

    public void setListCode(String listCode) {
        this.listCode = listCode;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", catName='" + catName + '\'' +
                ", listCode='" + listCode + '\'' +
                ", price=" + price +
                ", location='" + location + '\'' +
                ", purchaseUrl='" + purchaseUrl + '\'' +
                ", description='" + description + '\'' +
                ", quantity=" + quantity +
                ", deleted=" + deleted +
                ", sharable=" + sharable +
                ", complete=" + complete +
                ", version=" + version +
                ", type=" + type +
                ", date=" + date +
                ", pictureUrl='" + pictureUrl + '\'' +
                '}';
    }
}