package com.cognitive.newswizard.agent;

import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

public class TestHttpClient {

	@Test
	public void testHttpClientWithRestTemplate() {
		
		final String test = "http://redir.folha.com.br/redir/online/poder/rss091/*http://blogdofred.blogfolha.uol.com.br/?p=37677";
		final String knownRedirectUrl = "http://g1.globo.com/mundo/noticia/com-merkel-como-favorita-alemanha-vai-as-urnas-neste-domingo.ghtml";
		final String content1 = readContent(test);
		System.out.println(content1);
		
//		final String workingUrl = "https://g1.globo.com/mundo/noticia/com-merkel-como-favorita-alemanha-vai-as-urnas-neste-domingo.ghtml";
//		final String content2 = readContent(workingUrl);
//		System.out.println(content2);
//		
//		Assertions.assertThat(content1).as("content is not the same").isEqualTo(content2);
	}

	private String readContent(final String url) {
		final RestTemplate restTemplate = new RestTemplate();

		HttpEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);

		HttpHeaders headers = response.getHeaders();
		if (headers.getLocation() != null) {
			return readContent(headers.getLocation().toASCIIString());
		}

		String resultString = response.getBody();
		
		return resultString;
		
//		return restTemplate.getForObject(url, String.class);
	}
}
