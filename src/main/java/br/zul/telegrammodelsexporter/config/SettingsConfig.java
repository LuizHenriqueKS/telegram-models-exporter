package br.zul.telegrammodelsexporter.config;

import java.io.File;
import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import br.zul.telegrammodelsexporter.exception.UnexpectedException;
import br.zul.telegrammodelsexporter.model.Settings;

@Configuration
public class SettingsConfig {
    
    @Bean
    public Settings settings() {
        try {
            var file = new File("settings.yml");
            var objectMapper = new ObjectMapper(new YAMLFactory());
            return objectMapper.readValue(file, Settings.class);
        } catch (IOException e) {
            throw new UnexpectedException(e);
        }
    }

}
