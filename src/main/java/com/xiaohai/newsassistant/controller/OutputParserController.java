package com.xiaohai.newsassistant.controller;

import java.util.List;
import java.util.Objects;

import com.xiaohai.newsassistant.pojo.entity.ActorsFilms;
import reactor.core.publisher.Flux;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
public class OutputParserController {

	private final ChatClient chatClient;


	@Autowired
	public OutputParserController(ChatClient.Builder builder) {
		this.chatClient = builder.build();
	}

	@GetMapping("/output/simple")
	public ActorsFilms generate(@RequestParam(value = "actor", defaultValue = "Jeff Bridges") String actor) {

		// ChatClient API
		return chatClient.prompt()
				.user(u -> u.text("Generate the filmography of 5 movies for {actor}.")
						.param("actor", "Tom Hanks"))
				.call()
				.entity(ActorsFilms.class);
	}

	@GetMapping("/output/stream")
	public List<ActorsFilms> generateStream(@RequestParam(value = "actor", defaultValue = "Jeff Bridges") String actor) {

		var converter = new BeanOutputConverter<>(new ParameterizedTypeReference<List<ActorsFilms>>() { });

		Flux<String> flux = this.chatClient.prompt()
				.user(u -> u.text("""
								  Generate the filmography for a random actor.
								  {format}
								""")
						.param("format", converter.getFormat()))
				.stream()
				.content();

		return converter.convert(String.join("", Objects.requireNonNull(flux.collectList().block())));
	}

}
