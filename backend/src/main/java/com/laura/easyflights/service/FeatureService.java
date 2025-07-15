package com.laura.easyflights.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.laura.easyflights.model.Feature;
import com.laura.easyflights.repository.FeatureRepository;

@Service
public class FeatureService {

    @Autowired
    private FeatureRepository featureRepository;

    //Método para guardar las características
    public Feature saveFeature(Feature feature){
        return featureRepository.save(feature);
    }

    //Método para buscar si existe el nombre
    public boolean existsByName(String name){
        return featureRepository.existsByNameIgnoreCase(name);
    }

    //Método para eliminar características
    public void deleteFeature(Long id){
        featureRepository.deleteById(id);
    }

    //Método para obtener las características.
    public List<Feature> getAllFeatures(){
        return featureRepository.findAll();
    }
    
}
