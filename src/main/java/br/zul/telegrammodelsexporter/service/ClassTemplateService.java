package br.zul.telegrammodelsexporter.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.zul.telegrammodelsexporter.model.ClassModel;
import br.zul.telegrammodelsexporter.model.Settings;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClassTemplateService {

    private final Settings settings;
    private final FileService fileService;

    private List<String> templateRows;

    @PostConstruct
    public void onStartup() {
        templateRows = fileService.readLines(settings.getTemplate());
    }

    public String build(ClassModel classModel) {
        var builder = new StringBuilder();
        for (var row : templateRows) {
            var newRow = buildRow(classModel, row);
            builder.append(newRow);
            builder.append(getBreakRow());
        }
        return builder.toString();
    }

    private String getBreakRow() {
        if ("\\n".equals(settings.getBreakRow())) {
            return "\n";
        } else {
            return "\r\n";
        }
    }

    private Object buildRow(ClassModel classModel, String row) {
        var newRow = row;
        if (row.contains("{{repeatRowByProperty}}")) {
            newRow = repeatRowByProperty(classModel, row);
        }
        newRow = newRow.replace("{{className}}", classModel.getName());
        return newRow;
    }

    private String repeatRowByProperty(ClassModel classModel, String row) {
        var rowTemplate = row.replace("{{repeatRowByProperty}}", "");
        var builder = new StringBuilder();
        for (var property : classModel.getProperties()) {
            var newRow = rowTemplate;
            newRow = newRow.replace("{{propertyName}}", property.getName());
            newRow = newRow.replace("{{propertyType}}", property.getType());
            if (builder.length() > 0) {
                builder.append(getBreakRow());
            }
            builder.append(newRow);
        }
        return builder.toString();
    }

}
