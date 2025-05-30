package dtos;
import lombok.Data;
import org.springframework.stereotype.Component;
@Component
@Data
public class ProductRequest {
    private String title;
    private String brand;
    private String description;
    private Integer categoryId;
}
