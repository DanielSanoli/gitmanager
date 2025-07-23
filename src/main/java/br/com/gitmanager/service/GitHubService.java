package br.com.gitmanager.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import br.com.gitmanager.model.User;
import br.com.gitmanager.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
public class GitHubService {

    private UserRepository userRepository;
    private WebClient webClient;
    
    @Value("${github.api.url}")
    private String githubApiUrl;
    
    public GitHubService(UserRepository userRepository, 
                           WebClient webClient,
                           @Value("${github.api.url}") String githubApiUrl) {
        this.userRepository = userRepository;
        this.webClient = webClient;
        this.githubApiUrl = githubApiUrl;
    }

    @PostConstruct
    @Transactional
    public void syncGitHubUsers() {
        try {
            List<User> githubUsers = webClient.get()
                    .uri(githubApiUrl)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
                    .block()
                    .stream()
                    .limit(30)
                    .map(userData -> {
                        User user = new User();
                        user.setLogin((String) userData.get("login"));
                        user.setUrl((String) userData.get("url"));
                        return user;
                    })
                    .collect(Collectors.toList());
            
            userRepository.saveAll(githubUsers);
        } catch (Exception e) {
            throw new RuntimeException("Falha ao sincronizar os usuários do GitHub", e);
        }
    }
}

