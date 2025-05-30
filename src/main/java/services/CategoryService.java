package services;
import entities.Category;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import repositories.CategoryRepository;
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    public Category save(Category category){
        return categoryRepository.save(category);
    }
    public Category findByCategoryId(Integer categoryId) throws BadRequestException {
        return categoryRepository.findByCategoryId(categoryId).orElseThrow(()->new BadRequestException("Category not found"));
    }
}
