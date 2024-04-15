package br.zul.telegrammodelsexporter.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.zul.telegrammodelsexporter.model.Settings;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class TelegramBotApiService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final Settings settings;
    private final FileService fileService;

    public String getPageContent() {
        var botApiPageFile = fileService.getTempFile("botApiPage.html");
        if (fileService.exists(botApiPageFile)) {
            log.info("Reading bot api page content: {}", botApiPageFile);
            return fileService.readFile(botApiPageFile);
        } else {
            log.info("Requesting bot api page: {}", settings.getBotApiPageURL());
            var botApiPageContent = requestBotApiPageContent();
            fileService.writeFile(botApiPageFile, botApiPageContent);
            return botApiPageContent;
        }
    }

    private String requestBotApiPageContent() {
        return restTemplate.getForObject(settings.getBotApiPageURL(), String.class);
    }

}
