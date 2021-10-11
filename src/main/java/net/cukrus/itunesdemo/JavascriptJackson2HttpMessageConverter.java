package net.cukrus.itunesdemo;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.nio.charset.StandardCharsets;

public class JavascriptJackson2HttpMessageConverter extends AbstractJackson2HttpMessageConverter {
    public JavascriptJackson2HttpMessageConverter() {
        super(Jackson2ObjectMapperBuilder.json().build(),
                new MediaType("text", "javascript"),
                new MediaType("text", "javascript", StandardCharsets.UTF_8));
    }
}
