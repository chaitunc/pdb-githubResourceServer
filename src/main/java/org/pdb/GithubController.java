package org.pdb;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
@PropertySource("classpath:graphQueries.properties")
public class GithubController {

	@Autowired
	OAuth2RestOperations template;

	@Autowired
	Environment env;

	@RequestMapping("/user")
	public Map<String, Object> driveInfo() throws URISyntaxException {
		String body = env.getProperty("repositoriesList");
		@SuppressWarnings("unchecked")
		Map<String, Object> user = template.getForObject("https://api.github.com/user", Map.class);
		RequestEntity request = RequestEntity.post(new URI("https://api.github.com/graphql"))
				.accept(MediaType.APPLICATION_JSON).body(body);
		ResponseEntity<Object> response = template.exchange(request, Object.class);
		System.out.println(response.getBody());
		return user;
	}

}
