package com.pedroth.builder;

import com.pedroth.utils.FilesCrawler;
import com.pedroth.utils.TextIO;
import com.pedroth.utils.Tokenizer;
import com.pedroth.utils.Zipper;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.function.Function;

@Slf4j
public class WebSiteBuilder {
    private final static String base = "C:/pedro/";
    private final static String jarBuildingAddress = base + "visualExperiments/tools/JarsBuilding/";
    private final static String canonAddress = base + "visualExperiments/tools/canon.html";
    private final static String canonWithCommentsAddress = base + "visualExperiments/tools/canonWithComments.html";
    private final static String commentsAddress = base + "visualExperiments/tools/comments.html";
    private final static String mainAddress = base + "visualExperiments/main.html";
    private final static String indexAddress = base + "visualExperiments/index.html";
    private final static String javaExperimentsAddress = base + "visualExperiments/JavaExperiments/JavaExperiments";
    private final static String jsExperimentsAddress = base + "visualExperiments/JsExperiments/JsExperiments";
    private final static String blogAddress = base + "visualExperiments/Blog/Blog";
    private final static Map<String, List<String>> jarConfig = new HashMap<>();

    static {
        try {
            //assumes config is correct
            TextIO textIO = new TextIO(jarBuildingAddress + "config");
            final String text = textIO.getText();
            for (String line : text.split("\n")) {
                final String[] split = line.split(" ");
                List<String> list = new ArrayList<>(split.length - 1);
                list.addAll(Arrays.asList(split).subList(1, split.length));
                jarConfig.put(split[0], list);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


    public static void buildJavaPage(String name, String path) throws IOException {
        fillPage(name, path, x -> "\n\n<h1>" + x + "</h1>\n", x -> "</br></br></br><p>Download app here :<a href='" + name + ".zip'>" + name + ".zip</a></p>");
        // add Java zip
        // create folder
        File file = new File(name);
        file.mkdir();
        // generate README and run.bat
        TextIO textIO = new TextIO();
        String readMe = textIO.read(jarBuildingAddress + "README.txt");
        String runBat = textIO.read(jarBuildingAddress + "run.bat");
        String newJarName = name + ".jar";
        final List<String> config = jarConfig.get(name);
        readMe = readMe.replace("<jar>", newJarName);
        readMe = readMe.replace("<app name>", config.get(1));
        runBat = runBat.replace("<jar>", newJarName);
        runBat = runBat.replace("<app name>", config.get(1));
        // copy jar
        Files.copy(Paths.get(jarBuildingAddress + config.get(0)), Paths.get(name + "/" + newJarName), StandardCopyOption.REPLACE_EXISTING);
        textIO.write(name + "/README.txt", readMe);
        textIO.write(name + "/README.md", readMe);
        textIO.write(name + "/run.bat", runBat);
        textIO.write(name + "/run.sh", runBat);
        Zipper.zipIt(name, path + "/" + name + ".zip");
        FilesCrawler.applyFiles(name, File::delete);
        file.delete();
    }

    private static void fillPage(String name, String path, Function<String, String> beginString, Function<String, String> endString) {
        String regex = "<!--Special-->";
        StringBuilder text = new StringBuilder(beginString.apply(name));
        try {
            final Tokenizer parser = Tokenizer.of(regex);
            String content = new Scanner(new File(path + "/" + name + "App.html")).useDelimiter("\\Z").next();
            BufferedReader reader = new BufferedReader(new FileReader(canonWithCommentsAddress));
            BufferedWriter writer = new BufferedWriter(new FileWriter(path + "/" + name + ".html"));

            text.append(content);

            String line;
            while ((line = reader.readLine()) != null) {
                String[] aux = parser.tokenize(line);
                System.out.println(line);
                if (aux.length == 0) {
                    writer.write(line + "\n");
                } else {
                    writer.write(text + "\n");
                    writer.write(endString.apply(name));
                }
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void buildJsPage(String name, String path) {
        fillPage(name, path, x -> "\n\n<h1>" + x + "</h1>\n", x -> "");
    }

    private static void buildPages(String path, WebSiteBuilder.PageBuilder pageBuilder) throws IOException {
        String[] directories = FilesCrawler.getDirs(path);
        for (String directory : directories) {
            pageBuilder.build(directory, path + "/" + directory);
            log.info(directory);
        }
        log.info(Arrays.toString(directories));
    }

    private static void buildPage(String contentAddress, String outputAddress) throws IOException {
        String regex = "<!--Special-->";
        StringBuilder text = new StringBuilder();
        Tokenizer parser = Tokenizer.of(regex);
        String content = new Scanner(new File(contentAddress)).useDelimiter("\\Z").next();
        BufferedReader reader = new BufferedReader(new FileReader(WebSiteBuilder.canonAddress));
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputAddress));

        text.append(content);

        String line;
        while ((line = reader.readLine()) != null) {
            String[] aux = parser.tokenize(line);
            log.info(line);
            if (aux.length == 0) {
                writer.write(line + "\n");
            } else {
                writer.write(text + "\n");
            }
        }
        writer.close();
        reader.close();
    }

    private static void BuildWeb() throws IOException {
        String pathJava = base + "visualExperiments/JavaExperiments";
        String pathJs = base + "visualExperiments/JsExperiments";
        String pathBlog = base + "visualExperiments/Blog";

        buildPage(commentsAddress, canonWithCommentsAddress);
        buildPage(mainAddress, indexAddress);
        buildPage(javaExperimentsAddress + "App.html", javaExperimentsAddress + ".html");
        buildPage(jsExperimentsAddress + "App.html", jsExperimentsAddress + ".html");
        buildPage(blogAddress + "App.html", blogAddress + ".html");
        buildPages(pathJava, WebSiteBuilder::buildJavaPage);
        buildPages(pathJs, WebSiteBuilder::buildJsPage);
        buildPages(pathBlog, WebSiteBuilder::buildJsPage);
    }


    public static void main(String[] args) {
        try {
            BuildWeb();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    interface PageBuilder {
        void build(String name, String address) throws IOException;
    }
}
