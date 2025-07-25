package br.com.gitmanager.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import br.com.gitmanager.dto.UserDTO;
import br.com.gitmanager.service.UserService;
import jakarta.persistence.EntityNotFoundException;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser
    void testGetAllUsers_ShouldReturnUsers() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setLogin("testuser");
        userDTO.setUrl("http://github.com/testuser");
        userDTO.setRoles(Collections.singleton("ADMIN"));

        List<UserDTO> users = Collections.singletonList(userDTO);
        given(userService.getAllUsersWithRoles()).willReturn(users);

        mockMvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].login").value("testuser"))
                .andExpect(jsonPath("$[0].url").value("http://github.com/testuser"))
                .andExpect(jsonPath("$[0].roles[0]").value("ADMIN"));
    }

    @Test
    @WithMockUser
    void testGetAllUsers_WhenNoUsers_ShouldReturnEmptyList() throws Exception {
        given(userService.getAllUsersWithRoles()).willReturn(Collections.emptyList());

        mockMvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @WithMockUser
    void testAssignRoleToUser_ShouldReturnUpdatedUser() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setLogin("testuser");
        userDTO.setUrl("http://github.com/testuser");
        userDTO.setRoles(Collections.singleton("ADMIN"));

        given(userService.assignRoleToUser(1L, "ADMIN")).willReturn(userDTO);

        mockMvc.perform(post("/api/users/1/roles")
                .param("role", "ADMIN")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.login").value("testuser"))
                .andExpect(jsonPath("$.url").value("http://github.com/testuser"))
                .andExpect(jsonPath("$.roles[0]").value("ADMIN"));
    }

    @Test
    @WithMockUser
    void testAssignRoleToUser_WhenUserNotFound_ShouldReturnNotFound() throws Exception {
        given(userService.assignRoleToUser(99L, "ADMIN"))
            .willThrow(new EntityNotFoundException("Usuário não encontrado"));

        mockMvc.perform(post("/api/users/99/roles")
                .param("role", "ADMIN")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void testAssignRoleToUser_WithDifferentRole_ShouldReturnUpdatedUser() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(2L);
        userDTO.setLogin("anotheruser");
        userDTO.setUrl("http://github.com/anotheruser");
        userDTO.setRoles(Collections.singleton("MODERATOR"));

        given(userService.assignRoleToUser(2L, "MODERATOR")).willReturn(userDTO);

        mockMvc.perform(post("/api/users/2/roles")
                .param("role", "MODERATOR")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.login").value("anotheruser"))
                .andExpect(jsonPath("$.roles[0]").value("MODERATOR"));
    }

    @Test
    @WithMockUser
    void testAssignRoleToUser_WithInvalidUserId_ShouldReturnNotFound() throws Exception {
        given(userService.assignRoleToUser(-1L, "ADMIN"))
            .willThrow(new EntityNotFoundException("Usuário não encontrado"));

        mockMvc.perform(post("/api/users/-1/roles")
                .param("role", "ADMIN")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().isNotFound());
    }
}
