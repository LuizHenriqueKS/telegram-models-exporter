package br.zul.telegrammodelsexporter.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypeModel {
    
    private String name;
    private String tableContent;

}
