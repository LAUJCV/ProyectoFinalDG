package com.laura.easyflights.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.laura.easyflights.model.Category;
import com.laura.easyflights.model.CategoryImage;
import com.laura.easyflights.service.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "http://localhost:3000")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // Obtener todas las categorías
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    // Crear una nueva categoría con imagen
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createCategory(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("image") MultipartFile imageFile) {

        String normalizedTitle = title.trim().toLowerCase();

        System.out.println(">> Título recibido del frontend: '" + title + "'");
        System.out.println(">> Título normalizado: '" + normalizedTitle + "'");

        if (categoryService.categoryTitleExists(normalizedTitle)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("La categoría ya existe");
        }

        Category category = new Category();
        category.setTitle(title.trim());
        category.setDescription(description.trim());

        // Guardar imagen en el sistema de archivos
        String assetsDir = "/Users/lauracamargo/Documents/ProyectoDG/backend/uploads";
        File dir = new File(assetsDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String fileName = imageFile.getOriginalFilename();
        String path = assetsDir + "/" + fileName;
        try {
            imageFile.transferTo(new File(path));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar la imagen");
        }

        // Asociar la imagen a la categoría
        CategoryImage imageCategory = new CategoryImage();
        imageCategory.setUrl("/uploads/" + fileName);
        imageCategory.setCategory(category);
        category.setImage(imageCategory);

        // Guardar la categoría con la imagen
        Category savedWithImage = categoryService.createCategory(category);
        System.out.println(">> Guardando categoría: " + savedWithImage.getTitle());
        return ResponseEntity.ok(savedWithImage);
    }

    // Buscar categoría por ID
    @GetMapping("/by-category/{categoryId}")
    public ResponseEntity<Category> getCategoryById(@PathVariable("categoryId") Long id) {
        Optional<Category> optionalCategory = categoryService.findCategoryById(id);
        return optionalCategory.map(ResponseEntity::ok)
                               .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Eliminar una categoría
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllCategories(){
        categoryService.deleteAllCategories();
        return ResponseEntity.noContent().build();
    }
}
