package controllers;

import dtos.CategoryRequest;
import dtos.ProductRequest;
import entities.Category;
import entities.Product;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.CategoryService;
import services.ProductService;
import java.util.List;
@RestController
@RequestMapping("/products")
public class ProductController {
    private final CategoryService categoryService;
    private final ProductService productService;
    public ProductController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }
    @GetMapping("/")
    public ResponseEntity<List<Product>> findProducts()
    {
        return ResponseEntity.ok().body(productService.findAll());
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
    @DeleteMapping("/{productId}")
    public ResponseEntity<String> delete(@PathVariable Integer productId)
    {
        try
        {
           productService.delete(productId);
           return ResponseEntity.ok().body("Delete of product with "+productId+" was successfull");
        }
        catch(BadRequestException badRequestException)
        {
            return ResponseEntity.ok().body("Delete of product with "+productId+" failure");
        }
    }
    @PutMapping("/{productId}")
    public ResponseEntity<Product> update(@PathVariable Integer productId, @RequestBody ProductRequest productRequest)
    {
        try
        {
              Product product = Product.builder().brand(productRequest.getBrand()).title(productRequest.getTitle())
                              .description(productRequest.getDescription()).productId(productId).build();
               return ResponseEntity.ok(productService.update(productId,product));
        }
        catch(BadRequestException badRequestException)
        {
             System.out.println(badRequestException.getMessage());
             return  ResponseEntity.badRequest().body(null);
        }
    }
}
