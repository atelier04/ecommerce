package entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
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
    String name;
    @Column(name="description", unique = false,nullable = false, length =100 )
    String description;
    @Cascade(CascadeType.DELETE_ORPHAN)
    @OneToMany(mappedBy = "category")
    @JsonIgnore
    List<Product> products;
}
