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

import com.laura.easyflights.model.Policy;
import com.laura.easyflights.service.PolicyService;

@RestController
@RequestMapping("/api/policies")
@CrossOrigin(origins = "http://localhost:3000")
public class PolicyController {

    @Autowired
    private PolicyService policyService;

    //Endpoint para obtener todas las polìticas
    @GetMapping
    public List<Policy> findAllPolicies(){
        return policyService.findAllPolicies();
    }

    //Endpoint para guardar una policy
    @PostMapping
    public ResponseEntity<?> savePolicy(@RequestBody Policy policy){
        try{
            Policy savedPolicy = policyService.savePolicy(policy);
            return ResponseEntity.ok(savedPolicy);
        }
        catch (IllegalArgumentException err){
            return ResponseEntity.badRequest().body(err.getMessage());
        }
    }

    //Endpoint para eliminar una policy
    @DeleteMapping
    public void deletePolicy(@PathVariable Long id){
        policyService.deletePolicy(id);
    }
    
}
