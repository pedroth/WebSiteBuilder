package com.pedroth.utils;

import java.io.*;
import java.net.URL;

public class TextIO {
    private StringBuilder text;

    public TextIO() {
        this.text = new StringBuilder();
    }

    public TextIO(String address) throws IOException {
        super();
        this.read(address);
    }

    public String read(String address) throws IOException {
        this.text = new StringBuilder();
        BufferedReader in;
        if (isUrl(address)) {
            URL url;
            url = new URL(address);
            in = new BufferedReader(new InputStreamReader(url.openStream()));
        } else {
            in = new BufferedReader(new FileReader(address));
        }
        String line;
        while ((line = in.readLine()) != null) {
            this.text.append(line).append(String.format("\n"));
        }
        return this.text.toString();
    }

    public String read(InputStream address) {
        return read(address, -1);
    }

    public String read(InputStream address, int numberOfLines) {
        boolean isInf = numberOfLines < 0;
        int acc = 0;
        this.text = new StringBuilder();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(address));
            String line;
            while ((line = in.readLine()) != null && (acc < numberOfLines || isInf)) {
                this.text.append(line).append(String.format("\n"));
                acc++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.text.toString();
    }

    public void write(String address, String text) throws IOException {
        if (isUrl(address))
            return;

        File file = new File(address);

        // if file doesnt exists, then create it
        if (!file.exists()) {
            file.createNewFile();
        }
        PrintStream bw = new PrintStream(file);

        bw.print(text);

        bw.close();

    }

    private boolean isUrl(String address) {
        String[] aux = address.split("http");
        if (aux.length > 1)
            return true;
        else
            return false;
    }

    public String getText() {
        return this.text.toString();
    }
}
