package br.com.gitmanager.service;

import org.springframework.stereotype.Service;

import br.com.gitmanager.dto.RoleDTO;
import br.com.gitmanager.model.Role;
import br.com.gitmanager.repository.RoleRepository;

@Service
public class RoleService {
    private RoleRepository roleRepository;
    
    public RoleDTO createRole(RoleDTO roleDTO) {
        Role role = new Role();
        role.setName(roleDTO.getName());
        return new RoleDTO(roleRepository.save(role));
    }
}
