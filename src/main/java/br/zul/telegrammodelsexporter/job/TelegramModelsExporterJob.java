package br.zul.telegrammodelsexporter.job;

import org.springframework.stereotype.Component;

import br.zul.telegrammodelsexporter.service.ClassModelService;
import br.zul.telegrammodelsexporter.service.TelegramBotApiService;
import br.zul.telegrammodelsexporter.util.IteratorUtils;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Component
@RequiredArgsConstructor
@Log4j2
public class TelegramModelsExporterJob {
    
    private final TelegramBotApiService telegramBotApiService;
    private final ClassModelService classModelService;

    @PostConstruct
    public void onRun() {
        var pageContent = telegramBotApiService.getPageContent();
        var classModelIterator = classModelService.findAll(pageContent).iterator();
        for (var classModel : IteratorUtils.toIterable(classModelIterator)) {
            classModelService.createClass(classModel);
        } 
        System.exit(0);
    }

}
