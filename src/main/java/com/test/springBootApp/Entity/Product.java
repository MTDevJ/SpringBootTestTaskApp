package com.test.springBootApp.Entity;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "products")
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer productId;

    @Column(nullable = false,unique = true, length = 50)
    @NotNull
    @Size(max = 50)
    private String productName;

    @Column(nullable = false, length = 250,columnDefinition = "TEXT")
    @Size(max = 250)
    private String productDescription;

    @Column
    @NotNull
    @DecimalMin(value = "0.1", message = "the price must be >=1 simbol")
    private Double productPrice;

    @Column(nullable = false)
    @NotNull
    private Integer productAmount;

    @Column
    private String productImageName;

    @ManyToOne
    @JoinColumn(name = "categoryId", nullable = false)
    private Category productCategory;


    public Product() {
    }

    public Product(Integer productId, @NotNull @Size(max = 50) String productName,
                   @Size(max = 250) String productDescription,
                   @NotNull @DecimalMin(value = "0.1", message = "the price must be >=1 simbol") Double productPrice,
                   @NotNull Integer productAmount,
                   String productImageName,
                   Category productCategory) {
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productAmount = productAmount;
        this.productImageName = productImageName;
        this.productCategory = productCategory;
    }


    public Product(@NotNull @Size(max = 50) String productName, @Size(max = 250) String productDescription,
                   @NotNull @DecimalMin(value = "0.1", message = "the price must be >=1 simbol") Double productPrice,
                   @NotNull Integer productAmount,
                   String productImageName,
                   Category productCategory) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productAmount = productAmount;
        this.productImageName = productImageName;
        this.productCategory = productCategory;
    }

    public String getProductImageName() {
        return productImageName;
    }

    public void setProductImageName(String productImageName) {
        this.productImageName = productImageName;
    }

    public Integer getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(Integer productAmount) {
        this.productAmount = productAmount;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public Category getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(Category productCategory) {
        this.productCategory = productCategory;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productDescription='" + productDescription + '\'' +
                ", productPrice=" + productPrice +
                ", productAmount=" + productAmount +
                ", productImageName='" + productImageName + '\'' +
                ", productCategory=" + productCategory +
                '}';
    }
}
