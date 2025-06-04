package services;
import entities.Category;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import repositories.CategoryRepository;

import java.util.List;

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
    public List<Category> findAll()
    {
        return this.categoryRepository.findAll();
    }
}
