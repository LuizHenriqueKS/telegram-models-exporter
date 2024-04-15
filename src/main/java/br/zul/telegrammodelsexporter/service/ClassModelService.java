package br.zul.telegrammodelsexporter.service;

import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import br.zul.telegrammodelsexporter.model.ClassModel;
import br.zul.telegrammodelsexporter.model.PropertyModel;
import br.zul.telegrammodelsexporter.model.Settings;
import br.zul.telegrammodelsexporter.model.TypeModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class ClassModelService {

    private final Settings settings;
    private final PropertyTypeService propertyTypeService;
    private final ClassTemplateService classTemplateService;
    private final FileService fileService;

    public Stream<ClassModel> findAll(String pageContent) {
        var typesPageContent = getTypesPageContent(pageContent);
        return findTypesTables(typesPageContent).map(this::typeToClassModel);
    }

    public void createClass(ClassModel classModel) {
        log.info("Creating class: {}", classModel.getName());
        var file = settings.getOutput().replace("{{name}}", classModel.getName());
        var classContent = classTemplateService.build(classModel);
        fileService.writeFile(file, classContent);
    }

    private Stream<TypeModel> findTypesTables(String typesPageContent) {
        var pattern = Pattern.compile("<\\/a>(\\w*)<\\/h4>.*?<table class=\"table\">(.*?)<\\/table>", Pattern.DOTALL);
        var matcher = pattern.matcher(typesPageContent);
        return matcher.results().map(this::matchResultToType);
    }

    private String getTypesPageContent(String pageContent) {
        var typesBeginIndex = pageContent.indexOf("Available types");
        var typesEndIndex = pageContent.indexOf("Available methods");
        return pageContent.substring(typesBeginIndex + 10, typesEndIndex);
    }

    private TypeModel matchResultToType(MatchResult mr) {
        return TypeModel
            .builder()
            .name(mr.group(1))
            .tableContent(mr.group(2))
            .build();
    }

    private ClassModel typeToClassModel(TypeModel type) {
        return ClassModel
            .builder()
            .name(buildClassModelName(type.getName()))
            .properties(findProperties(type.getTableContent()))
            .build();
    }

    private List<PropertyModel> findProperties(String tableContent) {
        var pattern = Pattern.compile("<tr>.*?<td>(\\w*?)</td>.*?<td>(.*?)</td>.*?</tr>", Pattern.DOTALL);
        var matcher = pattern.matcher(tableContent);
        return matcher
            .results()
            .map(this::matchResultToProperty)
            .collect(Collectors.toList());
    }

    private PropertyModel matchResultToProperty(MatchResult mr) {
        return PropertyModel
            .builder()
            .name(toCamelCase(mr.group(1)))
            .type(remapPropertyType(mr.group(2)))
            .build();
    }

    private String remapPropertyType(String propertyType) {
        return propertyTypeService.remap(propertyType);
    }

    private String buildClassModelName(String name) {
        var builder = new StringBuilder();
        if (settings.getClassNamePrefix() != null) {
            builder.append(settings.getClassNamePrefix());
        }
        builder.append(name);
        if (settings.getClassNameSuffix() != null) {
            builder.append(settings.getClassNameSuffix());
        }
        return builder.toString();
    }

    private String toCamelCase(String propertyType) {
        var pattern = Pattern.compile("_(\\w)");
        var matcher = pattern.matcher(propertyType);
        return matcher.replaceAll(mr -> mr.group(1).toUpperCase());
    }

}
