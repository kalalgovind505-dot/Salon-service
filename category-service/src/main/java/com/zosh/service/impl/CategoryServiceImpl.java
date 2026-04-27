package com.zosh.service.impl;

import com.zosh.dto.SalonDTO;
import com.zosh.model.Category;
import com.zosh.repository.CategoryRepository;
import com.zosh.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category saveCategory(Category category, SalonDTO salonDTO) {
        Category newCategory =new Category();
        newCategory.setName(category.getName());
        newCategory.setSalonId(salonDTO.getId());
        newCategory.setImage(category.getImage());

        return categoryRepository.save(newCategory);
    }

    @Override
    public Set<Category> getAllCategoriesBySalon(Long id) {

        return categoryRepository.findBySalonId(id);
    }

    @Override
    public Category getCategoryById(Long id) throws Exception {

        Category category = categoryRepository.findById(id).orElse(null);
        if(category == null){
            throw new Exception("categoty not exist with id"+id);
        }
        return category;
    }

    @Override
    public void deleteCategoryById(Long id,Long salonId) throws Exception {
        Category category=getCategoryById(id);
        if(!category.getSalonId().equals(salonId)){
            throw new Exception("you dont have permission to delete category");
        }
        categoryRepository.deleteById(id);

    }

    @Override
    public Category findByIdAndSalonId(Long id, Long salonId) throws Exception {
        Category category=categoryRepository.findByIdAndSalonId(id,salonId);
        if (category==null)
        {
            throw  new Exception("cateogry not found");
        }
        return category;
    }
}
