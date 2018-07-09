package com.test.springBootApp.Service;

import com.test.springBootApp.Entity.Category;
import com.test.springBootApp.Entity.Product;
import com.test.springBootApp.ErrorMessageClass;
import com.test.springBootApp.Exception.NotFoundException;
import com.test.springBootApp.Repository.CategoryRepository;
import com.test.springBootApp.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

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
    public String uploadPath;

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
    public void categoryRepository(CategoryRepository repository) {
        this.categoryRepository = repository;
    }

    @Autowired
    public void setRepository(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Product> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Boolean delete(Integer id) {
        if (repository.findById(id).isPresent()) {
            Product delProduct = repository.findById(id).get();
            if (!ServiceFile.deleteImage(delProduct, uploadPath)) {
                return false;
            }
            repository.deleteById(id);
            return true;
        } else {
            throw new NotFoundException();
        }
    }

    public Map<Boolean, String> saveProduct(Product product, MultipartFile image) {
        Map<Boolean, String>  resultMap = new HashMap<>();
        String result;
        Product actionProduct;
        if (!checkProductName(product)) {
            if (product.getProductId() == null) {
                if (!image.isEmpty()) {
                    result = saveImage(image, product);
                    if (ErrorMessageClass.getErrorMessage(result) == null) {
                        repository.save(product);
                        resultMap.put(true, null);
                    } else {
                        resultMap.put(false, result);
                    }
                }
                repository.save(product);
            } else {
                resultMap.put(false, "1");
            }
        } else {
            actionProduct = repository.findById(product.getProductId()).get();
            if (!image.isEmpty()) {
                result = saveImage(image, actionProduct);
                if (ErrorMessageClass.getErrorMessage(result) == null) {
                    repository.save(actionProduct);
                    resultMap.put(true, null);
                } else {
                    resultMap.put(false, result);
                }
            } else {
                product.setProductImageName(actionProduct.getProductImageName());
                repository.save(product);
            }
        }
        return resultMap;
    }

    public Page<Product> filterAndSort(Pageable pageable) {
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

    public void sort(String sortColumn) {
        this.setSortColumn(sortColumn);
        if (this.sortNameMethod.equals("ASC"))
            this.setSortNameMethod("DESC");
        else
            this.setSortNameMethod("ASC") ;
    }

    public void sortProductByCategory() {
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
    }

    public Product findOne(Integer id) {
        if (repository.findById(id).isPresent()) {
            return repository.findById(id).get();
        }
        return null;
    }

    public Iterable<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    public void searchByCategory(Integer categoryId, Pageable pageable) {
        this.setFilterCategory(categoryId);
        if (categoryId == -1) {
            page = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
            products = repository.findAll(page);
        } else {
            page = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
            products = repository.findAllByCategoryIdOrderBySomeASC(filterCategory, page);
        }
    }

    public void setPageSizeFromModel(Integer pageSize) {
        if (pageSize == null)
            this.setPageSize(this.pageSize);
        else
            this.setPageSize(pageSize);
    }

    public String saveImage(MultipartFile image, Product product) {
        String result;
            result = ServiceFile.validFile(image, uploadPath);

            if(ErrorMessageClass.getErrorMessage(result) == null) {
                if (!result.isEmpty()) {
                    if(ServiceFile.deleteImage(product, uploadPath)) {
                        product.setProductImageName(result);
                    } else {
                        return "4";
                    }
                }
            }
        return result;
    }

    private boolean checkProductName(Product product){
        Product findProduct = repository.findByproductName(product.getProductName().trim());
        if(findProduct != null)
            return findProduct.getProductId().equals(product.getProductId()) ;
        return  false;
    }

    public ActionMode checkActionMode(Product product){
        if (product.getProductId() == null) {
            return ActionMode.ADD;
        } else {
            return ActionMode.EDIT;
        }
    }
}








