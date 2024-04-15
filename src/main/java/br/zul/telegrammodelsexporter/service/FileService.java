package br.zul.telegrammodelsexporter.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.springframework.stereotype.Service;

import br.zul.telegrammodelsexporter.exception.UnexpectedException;

@Service
public class FileService {

    public String getFile(String directory, String file) {
        return new File(directory, file).getAbsolutePath();
    }
    
    public String getTempFile(String name) {
        var dir = getTempDirectory();
        return new File(dir, name).getAbsolutePath();
    }

    public String getTempDirectory() {
        return new File("tmp").getAbsolutePath();
    }

    public String readFile(String file) {
        try {
            var path = Path.of(file);
            return Files.readString(path);
        } catch (IOException ex) {
            throw new UnexpectedException(ex);
        }
    }

    public void writeFile(String file, String content) {
        try {
            new File(file).getAbsoluteFile().getParentFile().mkdirs();
            var path = Path.of(file);
            Files.writeString(path, content);
        } catch (IOException ex) { 
            throw new UnexpectedException(ex);
        }
    }

    public boolean exists(String file) {
        return new File(file).exists();
    }

    public List<String> readLines(String template) {
        try {
            var path = Path.of(template);
            return Files.readAllLines(path);
        } catch (IOException ex) {
            throw new UnexpectedException(ex);
        }
    }

}
