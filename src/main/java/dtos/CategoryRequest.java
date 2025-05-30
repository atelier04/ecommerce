package dtos;
import lombok.Data;
import org.springframework.stereotype.Component;
@Component
@Data
public class CategoryRequest {
    private String name;
    private String description;
}
