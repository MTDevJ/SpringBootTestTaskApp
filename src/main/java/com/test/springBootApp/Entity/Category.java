package com.test.springBootApp.Entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
public class Category implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer categoryId;

    @Column(nullable = false,unique = true, length = 50)
    @NotNull
    @Size(min = 1,max = 50)
    private String categoryName;

    @Column(nullable = false, length = 250,columnDefinition = "TEXT")
    @Size(max = 250)
    private String categoryDescription;

    @OneToMany(mappedBy = "productCategory",fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Product> categoryProducts = new HashSet<>(0);

    public Category() {}

    public Category(Integer categoryId, @NotNull @Size(min = 1, max = 50) String categoryName,
                    @Size(max = 250) String categoryDescription, Set<Product> categoryProducts) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
        this.categoryProducts = categoryProducts;
    }

    public Category(@NotNull @Size(min = 1, max = 50) String categoryName, @Size(max = 250) String categoryDescription) {
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public Set<Product> getCategoryProducts() {
        return categoryProducts;
    }

    public void setCategoryProducts(Set<Product> categoryProducts) {
        this.categoryProducts = categoryProducts;
    }
}
