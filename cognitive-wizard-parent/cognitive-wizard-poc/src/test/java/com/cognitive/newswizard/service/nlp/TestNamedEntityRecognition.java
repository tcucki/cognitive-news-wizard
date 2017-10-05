package com.cognitive.newswizard.service.nlp;

import java.io.InputStream;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
//@SpringBootTest
public class TestNamedEntityRecognition {

	private static final String NER_MODEL_FILE_NAME = "/models/name_entity_recognition/en-ner-person.bin";
	private static final String TOKENIZER_MODEL_FILE_NAME = "/models/tokenizer/en-token.bin";
	private static final String TEXT_1 = "I know that Donal Trump is best friend of Bill Gates, because John told me so";

	@Test
	public void testFindNames() throws Exception {
		final TokenizerModel tokenizerModel = loadTokenizerModel();
		final Tokenizer tokenizer = new TokenizerME(tokenizerModel);
		final String[] tokens = tokenizer.tokenize(TEXT_1);
		
		final TokenNameFinderModel nameFinderModel = loadNERModel();
		final NameFinderME nameFinder = new NameFinderME(nameFinderModel);
		final Span[] spans = nameFinder.find(tokens);
		for (Span span : spans) {
			evaluate(span, tokens);
		}
		final String[] strs = Span.spansToStrings(spans, tokens);
		for (String string : strs) {
			System.out.println(string);
		}
	}
	
	private void evaluate(final Span span, final String[] tokens) {
		final StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (int i = span.getStart(); i < span.getEnd(); i++) {
			if (!first) {
				sb.append(" ");
			} else {
				first = false;
			}
			sb.append(tokens[i]);
		}
		System.out.println(sb.toString() + " - " + span.getType() + " " + span.getProb() + " " + span.length());
	}

	private TokenNameFinderModel loadNERModel() throws Exception {
		try (final InputStream is = this.getClass().getResourceAsStream(NER_MODEL_FILE_NAME)) {
			Assertions.assertThat(is).isNotNull();
			return new TokenNameFinderModel(is);
		} catch (Exception e) {
			Assertions.fail(e.getMessage());
			throw e;
		}
	}
	
	private TokenizerModel loadTokenizerModel() throws Exception {
		try (final InputStream is = this.getClass().getResourceAsStream(TOKENIZER_MODEL_FILE_NAME)) {
			Assertions.assertThat(is).isNotNull();
			return new TokenizerModel(is);
		} catch (Exception e) {
			Assertions.fail(e.getMessage());
			throw e;
		}
	}
}
