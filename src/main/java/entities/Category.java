package entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.List;
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer categoryId;

    @Column(name="name", unique = true,nullable = false, length=30)
    @NotBlank(message="name is required ")
    @NotEmpty(message="name is required ")
    String name;
    @Column(name="description", unique = false,nullable = false, length =100 )
    @NotBlank(message="description is required ")
    @NotEmpty(message="description is required ")
    String description;

    @OneToMany(mappedBy = "category",fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    @JsonIgnore
    List<Product> products;

    @Override
    public String toString() {
        return
                ""+categoryId;
    }
}
