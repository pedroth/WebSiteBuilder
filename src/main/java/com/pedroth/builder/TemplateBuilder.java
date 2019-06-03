package com.pedroth.builder;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

final class TemplateBuilder {
    private JtwigModel model;
    private String templateSource;

    TemplateBuilder(String templateSource) {
        this.model = JtwigModel.newModel();
        this.templateSource = templateSource;
    }

    TemplateBuilder render(String tag, File file) throws IOException {
        this.model.with(tag, String.join("", Files.readAllLines(file.toPath())));
        return this;
    }

    TemplateBuilder render(String tag, String text) {
        this.model.with(tag, text);
        return this;
    }


    void apply(String outSrc) throws FileNotFoundException {
        JtwigTemplate template = JtwigTemplate.fileTemplate(this.templateSource);
        template.render(this.model, new FileOutputStream(new File(outSrc)));
    }

    String apply() {
        JtwigTemplate template = JtwigTemplate.fileTemplate(new File(this.templateSource));
        return template.render(this.model);
    }

    static TemplateBuilder of(String templateSource) {
        return new TemplateBuilder(templateSource);
    }

}
