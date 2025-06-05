package services;
import entities.Product;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import repositories.ProductRepository;
import java.util.List;
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> findAll()
    {
        return productRepository.findAll();
    }
    public Product findById(Integer id) throws BadRequestException {
        return productRepository.findById(id).orElseThrow(()->new BadRequestException("Product with "+id+" not found"));
    }
    public Product save(Product product)
    {
        return productRepository.save(product);
    }
    public void delete(Integer id) throws BadRequestException {
        Product product =productRepository.findById(id).orElseThrow(()->new BadRequestException("Product with "+id+" not found"));
        productRepository.delete(product);
    }
    public Product update(Integer id, Product productUpdate) throws BadRequestException {
        Product product =productRepository.findById(id).orElseThrow(()->new BadRequestException("Product with "+id+" not found"));
        product.setBrand(productUpdate.getBrand());
        product.setTitle(productUpdate.getTitle());
        product.setDescription(productUpdate.getDescription());
        product.setCategory(productUpdate.getCategory());
        product.setData(productUpdate.getData());
        productRepository.save(product);
        return product;
    }
}
