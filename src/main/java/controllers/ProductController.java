package controllers;

import dtos.CategoryRequest;
import dtos.ProductRequest;
import entities.Category;
import entities.Product;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import services.CategoryService;
import services.ProductService;

import java.util.ArrayList;
import java.util.List;
@Controller
@RequestMapping("/products")
public class ProductController {
    private final CategoryService categoryService;
    private final ProductService productService;
    public ProductController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }
    @GetMapping("/")
    //public ResponseEntity<List<Product>> findProducts(Model model)
    public String findProducts(Model model)
    {
        model.addAttribute("products",productService.findAll());
        model.addAttribute("productHeader","products");
        //return ResponseEntity.ok().body(productService.findAll());

        return "products";
    }
    @GetMapping("/{productId}")
    public ResponseEntity<Product> findById(@PathVariable Integer productId){
        try
        {
           return ResponseEntity.ok().body(productService.findById(productId));
        }
        catch(BadRequestException ex)
        {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/category/")
    public ResponseEntity<Category> save(@RequestBody CategoryRequest categoryRequest){
        Category category=Category.builder().description(categoryRequest.getDescription())
                .name(categoryRequest.getName()).build();
        return ResponseEntity.ok().body(categoryService.save(category));
    }
    @PostMapping("/")
    public ResponseEntity<Product> save(@RequestBody  ProductRequest productRequest) throws BadRequestException {

        Category category =categoryService.findByCategoryId(productRequest.getCategoryId());

        Product product=Product.builder().brand(productRequest.getBrand()).title(productRequest.getTitle()).
                description(productRequest.getDescription()).category(category).build();
        ;
        return ResponseEntity.ok().body(productService.save(product));
    }
    @GetMapping("/categoryform")
    public String getCategoryForm(Model model)
    {
        model.addAttribute("category",new Category());

        return "categoryform";
    }
    @PostMapping("/categoryform")
    public String postCategoryForm(@Valid @ModelAttribute Category category, BindingResult result,
                                   RedirectAttributes redirectAttributes
    )
    {
        if(result.hasErrors() || result.hasFieldErrors())
        {
           // System.out.println(result.getFieldErrors().get(1).getField());
           // System.out.println(result.getFieldErrors().get(0).getField());

            result.getFieldErrors().stream().
                    forEach(fe->{
                        if(fe.getField().contains("name")){
                            System.out.println("nameError");
                            redirectAttributes.addFlashAttribute("nameError",fe.getDefaultMessage());
                        }
                        if(fe.getField().contains("description")){
                            System.out.println("descriptionError");
                            redirectAttributes.addFlashAttribute("descriptionError",fe.getDefaultMessage());
                        }


                    });
            return "redirect:/products/categoryform";
        }
        categoryService.save(category);
        return  "redirect:/products/";
    }
    @GetMapping("/productform")
    public String getProductForm(Model model)
    {
        model.addAttribute("product",new Product());
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories",categories);
        model.addAttribute("category",new Category());

        return "productform";
    }
    @PostMapping("/productform")
    public String postProductForm(@ModelAttribute Product productRequest) throws BadRequestException {
        System.out.println("category "+productRequest.getCategory());
       if(!productRequest.getTitle().isEmpty() && !productRequest.getBrand().isEmpty() && !productRequest.getDescription().isEmpty()

        )
        {
            Category category=categoryService.findByCategoryId(productRequest.getCategory().getCategoryId());
            Product product=Product.builder().title(productRequest.getTitle()).brand(productRequest.getBrand())
                            .description(productRequest.getDescription()).category(category).build();
            productService.save(product);
        }
            return "redirect:/products/";
    }
    @GetMapping("/delete/{productId}")
    public String delete(@PathVariable Integer productId, RedirectAttributes redirectAttributes)
    {
        try
        {
           productService.delete(productId);
           //return ResponseEntity.ok().body("Delete of product with "+productId+" was successfull");
            redirectAttributes.addFlashAttribute("successMessage", "Product deleted successfully!");
            return "redirect:/products/";
        }
        catch(BadRequestException badRequestException)
        {
            //return ResponseEntity.ok().body("Delete of product with "+productId+" failure");
            redirectAttributes.addFlashAttribute("failureMessage", "Product was not deleted!");
            return "redirect:/products/";
        }
    }
    @GetMapping("/update/{productId}")
    public String updateForm(@PathVariable Integer productId, Model model) throws BadRequestException {
        Product product =productService.findById(productId);
        List<Category> categories=categoryService.findAll();
        model.addAttribute("product",product);
        model.addAttribute("categories",categories);
        return "updateform";

    }
    @GetMapping("/uploadform")
    public String uploadForm()
    {
          return "uploadform";
    }
    @PostMapping("/uploadform")
    public String uploadPostForm()
    {
        return "redirect:/products/";
    }



    @PostMapping("/update/{productId}")
    public String update(@PathVariable Integer productId, @ModelAttribute Product productRequest,RedirectAttributes redirectAttributes)
    //public ResponseEntity<Product> update(@PathVariable Integer productId, @RequestBody ProductRequest productRequest)
    {
        if(!productRequest.getTitle().isEmpty() && !productRequest.getBrand().isEmpty() && !productRequest.getDescription().isEmpty()

        )
        {
            try
            {
                Product product = Product.builder().brand(productRequest.getBrand()).title(productRequest.getTitle())
                        .description(productRequest.getDescription()).productId(productId).
                        category(productRequest.getCategory()).build();
                //  return ResponseEntity.ok(productService.update(productId,product));

                productService.update(productId,product);
                redirectAttributes.addFlashAttribute("updateSuccess","Product was updated");
            }
            catch(BadRequestException badRequestException)
            {
                System.out.println(badRequestException.getMessage());
                redirectAttributes.addFlashAttribute("updateFailure","Product was not updated!!");
                // return  ResponseEntity.badRequest().body(null);
            }
        }
        redirectAttributes.addFlashAttribute("fieldsEmpty","You didn't fill out all fields!! " +
                "Product was not updated!!");
        return "redirect:/products/";

    }
}
