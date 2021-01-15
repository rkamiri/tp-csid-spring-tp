package fr.paris8univ.iut.csid.csidwebrepositorybase.core.dao;

import fr.paris8univ.iut.csid.csidwebrepositorybase.core.domain.GitHubIssue;
import fr.paris8univ.iut.csid.csidwebrepositorybase.core.domain.Issue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

@Component
public class GithubRepositoryDao {

    private final RestTemplate template;

    @Autowired
    public GithubRepositoryDao(RestTemplateBuilder restBuilder){
        this.template= restBuilder.build();
    }

    //Create the request to get the content of the git repo
    public GitRepositoryDTO getRepositoryDto(String repoName, String owner) throws URISyntaxException {
        return this.template.getForEntity(new URI("https://api.github.com/repos/"+owner+"/"+repoName), GitRepositoryDTO.class).getBody();
    }

    public void postOneIssue(String title, String body) {
        String token = "";
        String url = "https://api.github.com/repos/rkamiri/repoTestA/issues";
        Issue issue = new Issue(title, body);
        try{
            BufferedReader bf = new BufferedReader(new FileReader(new File("./src/main/resources/token.txt")));
            token = bf.readLine();
        }catch (Exception e){e.printStackTrace();}
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "token "+token);
        HttpEntity<Issue> entity = new HttpEntity<>(issue, headers);
        template.exchange(url, HttpMethod.POST, entity, GitHubIssue.class);
    }
}
