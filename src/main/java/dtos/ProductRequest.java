package dtos;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@Data
@Builder
public class ProductRequest {
    private String title;
    private String brand;
    private String description;
    private MultipartFile file;
    private Integer categoryId;
}
