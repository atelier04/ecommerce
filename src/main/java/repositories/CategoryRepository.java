package repositories;

import entities.Category;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Component
public interface CategoryRepository extends JpaRepository<Category, Integer> {
       Optional<Category> findByCategoryId(Integer categoryId);
}
