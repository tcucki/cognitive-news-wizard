package com.cognitive.newswizard.agent.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;

public class NewsParserTest {

	@Test
	public void parseNewsTest() {
//		final String url = "http://www.bbc.co.uk/news/election-2017-39930865";
		final String url = "http://exame.abril.com.br/brasil/lava-jato-diz-que-caseiro-informava-lula-sobre-dia-a-dia-de-sitio";
		final String content = readNews(url);
//		System.out.println(content);
		System.out.println(content.length());
		
//		System.out.println(getAllElementValues("title", content));
		System.out.println();
		printList("meta", getAllMetaAttrContent(content));
		
		printList("h1", getAllElementValuesJsoup("h1", content));
		printList("h2", getAllElementValuesJsoup("h2", content));
		printList("h3", getAllElementValuesJsoup("h3", content));
		printList("Paragraph", getAllElementValuesJsoup("p", content));
		printList("Div", getAllElementValues("div", content));
		
//		printList("Anchor", getAllElementValuesJsoup("a", content));
		
		System.out.println("******************** THE ULTIMATE TEXT *********************");
		final Document document = Jsoup.parse(content);
		System.out.println(document.text());
	}
	
	private List<String> getAllMetaAttrContent(String content) {
		final Document document = Jsoup.parse(content);
		final Elements elements = document.select("meta");
		return elements.stream().map(element -> element.attr("content")).collect(Collectors.toList());
	}

	private List<String> getAllElementValuesJsoup(final String elementName, final String html) {
		final Document document = Jsoup.parse(html);
		final Elements elements = document.select(elementName);
		return elements.stream().map(element -> element.text()).collect(Collectors.toList());
	}
	
	private void printList(final String title, final List<String> values) {
		System.out.println("************ " + title + " ****************");
		values.stream().forEach(System.out::println);
	}
	
	private List<String> getAllElementValues(final String element, final String html) {
		final List<String> values = new ArrayList<>();
		final Pattern pattern = Pattern.compile("<" + element + "[^>]*>([^<]+)</" + element + ">");
//		final Pattern pattern = Pattern.compile("<p[^>]*>([^<]+)</p>");
//		final Pattern pattern = Pattern.compile("<p[^>]*>(.*)</p>");
		final Matcher matcher = pattern.matcher(html);
		while (matcher.find()) {
			final String value = matcher.group(1);
			values.add(value);
//			System.out.println(value);
		}
		return values;
	}

	private String readNews(String urlString) {
		URL url;
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e1) {
			throw new IllegalArgumentException(e1);
		}
		final StringBuilder sb = new StringBuilder();
        try (final BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
            	sb.append(inputLine);
            }
        } catch (IOException e) {
        	throw new RuntimeException(e);
		} 
		return sb.toString();
	}
}
