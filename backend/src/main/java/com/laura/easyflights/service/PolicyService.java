package com.laura.easyflights.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.laura.easyflights.model.Policy;
import com.laura.easyflights.repository.PolicyRepository;

@Service
public class PolicyService {

    @Autowired
    private PolicyRepository policyRepository;

    public List<Policy> findAllPolicies(){
        return policyRepository.findAll();
    }

    public Policy savePolicy(Policy policy){
        if(policyRepository.existsByTitle(policy.getTitle().trim())){
            throw new IllegalArgumentException("Ya existe una política con este título");
        }
        return policyRepository.save(policy);
    }

    public void deletePolicy(Long id){
        policyRepository.deleteById(id);
    }
    
}
