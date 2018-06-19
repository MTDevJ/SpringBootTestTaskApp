package com.test.springBootApp.Service;

import com.test.springBootApp.Entity.Product;
import com.test.springBootApp.Exception.NotFoundException;
import com.test.springBootApp.Repository.CategoryRepository;
import com.test.springBootApp.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class ProductActionServiceImpl implements ActionService, ProductService{

    private ProductRepository repository;
    private CategoryRepository categoryRepository;

    private String sortNameMethod = "ASC";
    private String sortColumn = "productId";
    private Integer filterCategory;
    private Page<Product> products;
    private Pageable page;
    private Integer pageSize = 10;

    @Value("${upload.path}")
    private String uploadPath;

    public void setSortNameMethod(String sortNameMethod) {
        this.sortNameMethod = sortNameMethod;
    }

    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }

    public void setFilterCategory(Integer filterCategory) {
        this.filterCategory = filterCategory;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Autowired
    public void setRepository(CategoryRepository repository) {
        this.categoryRepository = repository;
    }

    @Autowired
    public void setRepository(ProductRepository repository) {
        this.repository = repository;
    }

    public Model getAll(Model model, Pageable pageable, User user) {
        products = filterAndSort(pageable);
        initModel(model, user);
        return model;
    }

    public String add(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categoryList", categoryRepository.findAll());
        model.addAttribute("model", "ADD");
        return "/actions/actionProduct";
    }

    public String edit(Integer id, Model model) {
        Product product;
        if (repository.findById(id).isPresent())  {
            product = repository.findById(id).get();
            model.addAttribute("product",product);
            model.addAttribute("categoryList",categoryRepository.findAll());
            model.addAttribute("model", "EDIT");
        } else  {
            throw new NotFoundException();
        }
        return "/actions/actionProduct" ;
    }

    public String delete(Integer id) {
        Product delProduct;
        if (repository.findById(id).isPresent()) {
            delProduct = repository.findById(id).get();
            deleteImage(delProduct);
            repository.deleteById(id);
        } else {
            throw new NotFoundException();
        }
        return "redirect:/goods";
    }

    public String saveProduct(Product product,
                              MultipartFile productImage,
                              RedirectAttributes redirectAttributes) {
        String imageName;
        if (product.getProductId() == null) {
            if ( repository.findByproductName(product.getProductName()) != null ) {
                redirectAttributes.addFlashAttribute( "message","Товар с данным наименованием уже имеется в базе!");
                return "redirect:/add";
            } else {
                Product newProduct = new Product();

                newProduct.setProductName(product.getProductName());
                newProduct.setProductDescription(product.getProductDescription());
                newProduct.setProductPrice(product.getProductPrice());
                newProduct.setProductAmount(product.getProductAmount());
                if (!productImage.isEmpty()) {
                    imageName = safeImage(productImage);
                    newProduct.setProductImageName(imageName);
                }
                newProduct.setProductCategory(product.getProductCategory());

                repository.save(newProduct);
            }
        } else {
            Product editProduct = new Product();
            if (repository.findById(product.getProductId()).isPresent())
                editProduct = repository.findById(product.getProductId()).get();
            editProduct.setProductName(product.getProductName());
            editProduct.setProductDescription(product.getProductDescription());
            editProduct.setProductPrice(product.getProductPrice());
            editProduct.setProductAmount(product.getProductAmount());
            if (!productImage.isEmpty()){
                imageName = safeImage(productImage);
                deleteImage(editProduct);
                editProduct.setProductImageName(imageName);
            }
            editProduct.setProductCategory(product.getProductCategory());

            repository.save(editProduct);
        }
        return "redirect:/goods";
    }

    public String searchByCategory(Integer categoryId, Pageable pageable, Model model, User user) {
        this.setFilterCategory(categoryId);
        if (categoryId == -1) {
            page = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
            products = repository.findAll(page);
        } else {
            page = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
            products = repository.findAllByCategoryIdOrderBySomeASC(filterCategory, page);
        }
        initModel(model,user);
        return "/goods";
    }

    public String sort(String sortColumn) {
        this.setSortColumn(sortColumn);
        if (this.sortNameMethod.equals("ASC"))
            this.setSortNameMethod("DESC");
        else
            this.setSortNameMethod("ASC") ;
        return "redirect:/goods";
    }

    public String sortProductByCategory() {
        switch (this.sortNameMethod) {
            case "ASCCategory" :
                this.setSortNameMethod("DESCCategory");
                break;
            case "DESCCategory" :
                this.setSortNameMethod("ASCCategory");
                break;
            default :
                this.setSortNameMethod("ASCCategory");
                break;
        }
        return "redirect:/goods";
    }

    public String setPageSizeFromModel(Integer pageSize) {
        if (pageSize == null)
            this.setPageSize(this.pageSize);
        else
            this.setPageSize(pageSize);
        return "redirect:/goods";
    }

    private Page<Product> filterAndSort(Pageable pageable) {
        if (filterCategory == null || filterCategory == -1 ){
            switch (sortNameMethod) {
                case "ASC":
                    page = PageRequest.of(pageable.getPageNumber(), pageSize,
                            Sort.by(Sort.Direction.ASC, sortColumn));
                    products = repository.findAll(page);
                    break;
                case "DESC":
                    page = PageRequest.of(pageable.getPageNumber(),pageSize,
                            Sort.by(Sort.Direction.DESC, sortColumn));
                    products = repository.findAll(page);
                    break;
                case "ASCCategory" :
                    page = PageRequest.of(pageable.getPageNumber(), pageSize);
                    products = repository.sortByCategoryASC(page);
                    break;
                case "DESCCategory" :
                    page = PageRequest.of(pageable.getPageNumber(), pageSize);
                    products = repository.sortByCategoryDESC(page);
                    break;
                default:
                    page = PageRequest.of(pageable.getPageNumber(), pageSize,
                            Sort.by(Sort.Direction.ASC,"productId"));
                    products = repository.findAll(page);
                    break;
            }
        } else {
            switch (sortNameMethod) {
                case "ASC":
                    page = PageRequest.of(pageable.getPageNumber(), pageSize,
                            Sort.by(Sort.Direction.ASC, sortColumn));
                    products = repository.findAllByCategoryIdOrderBySomeASC(filterCategory, page);
                    break;
                case "DESC":
                    page = PageRequest.of(pageable.getPageNumber(), pageSize,
                            Sort.by(Sort.Direction.DESC,sortColumn));
                    products = repository.findAllByCategoryIdOrderBySomeDESC(filterCategory, page);
                    break;
                default:
                    page = PageRequest.of(pageable.getPageNumber(), pageSize,
                            Sort.by(Sort.Direction.ASC,"productId"));
                    products = repository.findAllByCategoryIdOrderBySomeASC(filterCategory, page);
                    break;
            }
        }
        return products;
    }

    private void initModel(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("pageNumber", products.getNumber());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("totalElements", products.getTotalElements());
        model.addAttribute("size", products.getSize());
        model.addAttribute("productList", products.getContent());
        model.addAttribute("categoryList",categoryRepository.findAll());
        model.addAttribute("sortColumn",sortColumn);
        model.addAttribute("user", user);
    }

    private String safeImage(MultipartFile productImage)  {
        File uploadDir = new File(uploadPath);
        String resultFileName = "";
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        if (productImage != null && !productImage.getOriginalFilename().isEmpty()) {
            String uuidFile = UUID.randomUUID().toString();
            resultFileName = uuidFile + "." + productImage.getOriginalFilename();
            try {
                productImage.transferTo( new File(uploadDir.getAbsolutePath() + File.separator + resultFileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return resultFileName;
        }
        return resultFileName;
    }

    private void deleteImage(Product product) {
        if (product.getProductImageName() != null) {
            if(!product.getProductImageName().equals("")) {
                try{
                    String imageName = product.getProductImageName();
                    File file = new File(uploadPath + File.separator + imageName);
                    file.delete();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
