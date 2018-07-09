package com.test.springBootApp.Controller;

import com.test.springBootApp.Entity.Product;
import com.test.springBootApp.ErrorMessageClass;
import com.test.springBootApp.Service.ActionMode;
import com.test.springBootApp.Service.ProductActionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ProductController  {

    private ProductActionServiceImpl service;
    private Page<Product> products;
    private String sortColumn;

    @Autowired
    public void setService(ProductActionServiceImpl service) {
        this.service = service;
    }

    @GetMapping("/goods")
    public Model getAll(Model model,Pageable pageable, @AuthenticationPrincipal User user) {
        products = service.filterAndSort(pageable);
        initModel(model, user);
        return model;
    }

    @GetMapping("/addProduct")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String addProduct(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categoryList", service.findAllCategories());
        model.addAttribute("model", "ADD");
        return "/actions/actionProduct";
    }

    @GetMapping("/editProduct/{id}")
    public String editProduct(@PathVariable Integer id, Model model) {
        model.addAttribute("product",service.findOne(id));
        model.addAttribute("categoryList",service.findAllCategories());
        model.addAttribute("model", "EDIT");
        return "/actions/actionProduct";
    }

    @GetMapping("/deleteProduct/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteProduct(@PathVariable Integer id, RedirectAttributes redirectAttributes){
        if (!service.delete(id))
            redirectAttributes.addFlashAttribute( "message", ErrorMessageClass.getErrorMessage("4"));
        return  "redirect:/goods";
    }

    @PostMapping("/saveProduct")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String saveProduct(@ModelAttribute @Valid Product product,
                              @RequestParam("productImage") MultipartFile productImage,
                              RedirectAttributes redirectAttributes) {
        Map<Boolean,String> safeResult;
        safeResult = service.saveProduct(product,productImage);
        if (safeResult.containsKey(true)) {
            return "redirect:/goods";
        } else {
            redirectAttributes.addFlashAttribute("message", ( ErrorMessageClass.getErrorMessage(safeResult.get(false))));
            switch (service.checkActionMode(product)) {
                case ADD:
                    return "redirect:/addProduct";
                case EDIT:
                    return "redirect:/editProduct/" + product.getProductId();
            }
        }
        return "redirect:/goods";
    }

    @GetMapping("/searchByCategory")
    public String searchByCategory(@RequestParam Integer categoryId,
                                   Pageable pageable,
                                   Model model,
                                   @AuthenticationPrincipal User user){
        service.searchByCategory(categoryId, pageable);
        initModel(model,user);
        return "redirect:/goods";
    }

    @GetMapping("/sortProduct/{sortColumn}")
    public String sortProduct(@PathVariable String sortColumn) {
        service.sort(sortColumn);
        return "redirect:/goods";
    }

    @GetMapping("/sortProductByCategory")
    public String sortProductByCategory() {
        service.sortProductByCategory();
        return "redirect:/goods";
    }

    @GetMapping("/setPageSizeGoods")
    public String setPageSizeFromModel(@RequestParam Integer pageSize) {
        service.setPageSizeFromModel(pageSize);
        return "redirect:/goods";
    }

    private void initModel(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("pageNumber", products.getNumber());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("totalElements", products.getTotalElements());
        model.addAttribute("size", products.getSize());
        model.addAttribute("productList", products.getContent());
        model.addAttribute("categoryList",service.findAllCategories());
        model.addAttribute("sortColumn",sortColumn);
        model.addAttribute("user", user);
    }

}
