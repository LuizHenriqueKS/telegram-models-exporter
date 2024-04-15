package br.zul.telegrammodelsexporter.service;

import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import br.zul.telegrammodelsexporter.model.Settings;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PropertyTypeService {

    private final Settings settings;

    public String remap(String propertyType) {
        propertyType = propertyType.replaceAll("<a href=\".*?\">(\\w*?)</a>", "$1");
        propertyType = remapEachType(propertyType);
        return propertyType;
    }

    private String remapEachType(String propertyType) {
        if (propertyType.contains(" ")) {
            propertyType = remapByPattern(propertyType, "(.*)Array of (\\w+)", "$1Array of $2");
            propertyType = remapByPattern(propertyType, "(\\w+?) or ", "$1 or ");
            propertyType = remapByPattern(propertyType, "(or \\w+) ", "or $1");
            propertyType = remapUsingSettings(propertyType);
        } else {
            propertyType = remapUsingSettings(propertyType);
            if (!isPrimitiveType(propertyType)) {
                propertyType = buildNonPrimitiveType(propertyType);
            }
        }
        return propertyType;
    }

    private String remapByPattern(String propertyType, String regex, String replacement) {
        var pattern = Pattern.compile(regex, Pattern.DOTALL);
        var matcher = pattern.matcher(propertyType);
        var result = new String[]{replacement};
        return matcher.replaceAll(mr -> {
            for (var i = 1; i <= mr.groupCount(); i++) {
                var subPropertyType = mr.group(i);
                subPropertyType = remapEachType(subPropertyType);
                result[0] = result[0].replace("$" + i, subPropertyType);
            }
            return result[0];
        });
    }

    private String buildNonPrimitiveType(String propertyType) {
        if (propertyType.isEmpty()) return propertyType;
        var builder = new StringBuilder();
        if (settings.getClassNamePrefix() != null) {
            builder.append(settings.getClassNamePrefix());
        }
        builder.append(propertyType);
        if (settings.getClassNameSuffix() != null) {
            builder.append(settings.getClassNameSuffix());
        }
        return builder.toString();
    }

    private boolean isPrimitiveType(String propertyType) {
        return settings
            .getPrimitiveTypes()
            .stream()
            .anyMatch(str -> isPrimitiveType(propertyType, str));
    }

    private boolean isPrimitiveType(String propertyType, String primitiveType) {
        return Pattern.compile(primitiveType, Pattern.DOTALL).matcher(propertyType).matches();
    }

    private String remapUsingSettings(String propertyType) {
        String old = "";
        while (!old.equals(propertyType)) {
            old = propertyType;
            for (Entry<String, String> e : settings.getPropertyRemap().entrySet()) {
                var pattern = Pattern.compile(e.getKey(), Pattern.DOTALL);
                var matcher = pattern.matcher(propertyType);
                propertyType = matcher.replaceAll(e.getValue());
            }
        }
        return propertyType;
    }

}
