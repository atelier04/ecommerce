package entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    String brand;
    @Column(name="title",unique = true, nullable = false,length = 20)
    String title;
    @Column(name="description", unique = false, nullable = false, length=100)
    String description;
    @ManyToOne
    @JoinColumn(name="categoryId")
    Category category;
}
