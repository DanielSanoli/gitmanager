package br.com.gitmanager.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.gitmanager.dto.UserDTO;
import br.com.gitmanager.model.Role;
import br.com.gitmanager.model.User;
import br.com.gitmanager.repository.RoleRepository;
import br.com.gitmanager.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAssignRoleToUser_WhenUserAndRoleExist_ShouldAssignRole() {
        Long userId = 1L;
        String roleName = "ADMIN";
        
        User user = new User();
        user.setId(userId);
        user.setLogin("testuser");
        user.setUrl("http://github.com/testuser");
        
        Role role = new Role();
        role.setId(1L);
        role.setName(roleName);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findByName(roleName)).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserDTO result = userService.assignRoleToUser(userId, roleName);

        assertNotNull(result);
        assertTrue(result.getRoles().contains(roleName));
        verify(userRepository, times(1)).findById(userId);
        verify(roleRepository, times(1)).findByName(roleName);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testAssignRoleToUser_WhenUserNotFound_ShouldThrowException() {
        Long userId = 99L;
        String roleName = "ADMIN";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            userService.assignRoleToUser(userId, roleName);
        });
    }

    @Test
    public void testGetAllUsersWithRoles_ShouldReturnUsers() {
        User user = new User();
        user.setId(1L);
        user.setLogin("testuser");
        user.setUrl("http://github.com/testuser");
        
        Role role = new Role();
        role.setId(1L);
        role.setName("ADMIN");
        user.setRoles(Collections.singleton(role));

        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        List<UserDTO> result = userService.getAllUsersWithRoles();

        assertEquals(1, result.size());
        assertEquals("testuser", result.get(0).getLogin());
        assertTrue(result.get(0).getRoles().contains("ADMIN"));
        verify(userRepository, times(1)).findAll();
    }
}