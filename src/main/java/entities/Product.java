package entities;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer productId;
    @Column(name="brand",unique = false, nullable = true,length = 20)
    @NotEmpty(message="brand is required ")
    String brand;
    @Column(name="title",unique = false, nullable = false,length = 20)
    @NotEmpty(message="title is required ")
    String title;
    @Column(name="description", unique = false, nullable = false, length=100)
    @NotEmpty(message="description is required ")
    String description;
    @Column(name="data", nullable = true)
    byte[] data;
    @ManyToOne
    @JoinColumn(name="categoryId")
    Category category;
}
