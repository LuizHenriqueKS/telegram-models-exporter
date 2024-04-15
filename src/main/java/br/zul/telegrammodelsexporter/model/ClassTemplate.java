package br.zul.telegrammodelsexporter.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClassTemplate {
    
    private String content;
    private String propertyRow;

}
