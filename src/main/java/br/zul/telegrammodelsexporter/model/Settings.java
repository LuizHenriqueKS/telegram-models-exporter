package br.zul.telegrammodelsexporter.model;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Settings {
    
    private String botApiPageURL;
    private String output;
    private String template;
    private String breakRow;
    private String classNamePrefix;
    private String classNameSuffix;
    private List<String> primitiveTypes;
    private Map<String, String> propertyRemap;

}
