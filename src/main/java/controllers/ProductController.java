package controllers;

import dtos.CategoryRequest;
import dtos.ProductRequest;
import entities.Category;
import entities.Product;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import services.CategoryService;
import services.ProductService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
    //@CachePut(value={"products"},key="#product.productId")
    public String findProducts(Model model)
    {
        List<Product> products= productService.findAll();
        String cwd = System.getProperty("user.dir");
        System.out.println(cwd);
        File f=new File(cwd+File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator+"static"+File.separator+"downloads");

        if(!f.exists()){
            System.out.println(f.mkdir());
        }
        products.stream().forEach((p)->{
            try(FileOutputStream outputStream =new FileOutputStream(f.getAbsolutePath()+File.separator+p.getProductId()+".png"))
            {
                 outputStream.write(p.getData());
                 outputStream.flush();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        });

        model.addAttribute("products",products);
        model.addAttribute("productHeader","products");
        model.addAttribute("absolutePath",f.getAbsolutePath()+File.separator);
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
        //ProductRequest.builder() not here from Lombok but I can do Product.builder()

        Product product =productService.findById(productId);
        List<Category> categories=categoryService.findAll();
        ProductRequest productRequest=ProductRequest.builder().

                brand(product.getBrand()).title(product.getTitle())
                        .description(product.getDescription()).categoryId(product.getCategory().getCategoryId()).
                build();
        model.addAttribute("product",product);
        model.addAttribute("productRequest",productRequest);
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
    public String update(@PathVariable Integer productId, @RequestPart("file") MultipartFile multipartFile, @Valid @ModelAttribute Product productRequest,
                         BindingResult result, RedirectAttributes redirectAttributes)
    //public ResponseEntity<Product> update(@PathVariable Integer productId, @RequestBody ProductRequest productRequest)
    {
        System.out.println("multipartFile.getOriginalFilename() "+multipartFile.getOriginalFilename());
        if(result.hasErrors() || result.hasFieldErrors())
        {
            System.out.println("result.hasErrors() "+result.hasErrors());
            System.out.println("result.getFieldErrors() "+result.getFieldErrors());
            result.getFieldErrors().stream().
                    forEach(fe->{
                        if(fe.getField().contains("title")){
                            System.out.println("titleError");
                            redirectAttributes.addFlashAttribute("titleError",fe.getDefaultMessage());
                        }
                        if(fe.getField().contains("brand")){
                            System.out.println("brandError");
                            redirectAttributes.addFlashAttribute("brandError",fe.getDefaultMessage());
                        }
                        if(fe.getField().contains("description")){
                            System.out.println("descriptionError");
                            redirectAttributes.addFlashAttribute("descriptionError",fe.getDefaultMessage());
                        }
                    });
            return "redirect:/products/";
        }
        try{
            System.out.println("multipartFile.getOriginalFilename()() " + multipartFile.getOriginalFilename());

            Product product = Product.builder().brand(productRequest.getBrand()).title(productRequest.getTitle())
                        .description(productRequest.getDescription()).productId(productId).
                         data(multipartFile.getBytes()).
                        category(productRequest.getCategory()).build();
                //  return ResponseEntity.ok(productService.update(productId,product));
                productService.update(productId,product);
                redirectAttributes.addFlashAttribute("updateSuccess","Product was updated");
             return "redirect:/products/";
            }
        catch(BadRequestException badRequestException)
        {
            System.out.println(badRequestException.getMessage());
            redirectAttributes.addFlashAttribute("updateFailure","Product was not updated!!");
            return "redirect:/products/";
        } catch (IOException e) {
            System.out.println(e.getMessage());
            redirectAttributes.addFlashAttribute("updateFailure","Product was not updated!! IOEXception "+e.getMessage());
            return "redirect:/products/";
        }
    }
}
