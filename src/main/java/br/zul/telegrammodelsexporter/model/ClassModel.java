package br.zul.telegrammodelsexporter.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClassModel {
    
    private String name;
    private List<PropertyModel> properties;

}
