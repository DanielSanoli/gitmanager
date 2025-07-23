package br.com.gitmanager.dto;

import java.util.Set;
import java.util.stream.Collectors;

import br.com.gitmanager.model.Role;
import br.com.gitmanager.model.User;

public class UserDTO {
    private Long id;
    private String login;
    private String url;
    private Set<String> roles;

    public UserDTO() {
    }
    
    public UserDTO(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.url = user.getUrl();
        this.roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    
}
