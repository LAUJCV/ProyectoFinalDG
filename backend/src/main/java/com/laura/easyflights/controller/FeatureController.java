package com.laura.easyflights.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.laura.easyflights.model.Feature;
import com.laura.easyflights.service.FeatureService;


@RestController
@RequestMapping("/api/features")
@CrossOrigin(origins = "http://localhost:3000")
public class FeatureController {
    
    @Autowired
    private FeatureService featureService;


    //Método para obtener todas las características
    @GetMapping
    public List<Feature> getAllFeatures(){
        return featureService.getAllFeatures();
    }

    //Método para crear características
    @PostMapping
    public ResponseEntity<?> createFeature(@RequestBody Feature feature){
        if(featureService.existsByName(feature.getName().trim())){
            return ResponseEntity.status(409).body("Ya existe una característica con ese nombre");
        }

        Feature saved = featureService.saveFeature(feature);
        return ResponseEntity.ok(saved);
    }

    //Método para eliminar características
    @DeleteMapping("/{id}")
    public void deleteFeature(@PathVariable Long id){
        featureService.deleteFeature(id);
    }

}
