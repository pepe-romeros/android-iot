package com.example.app.web.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Helper class to read html files as {@link String}
 */
public final class HtmlReaderUtil {

    private HtmlReaderUtil() {
        // Private empty constructor to avoid accidental instantiation
    }

    /**
     * Will read a file from the given InputStream and will return a String representing its contents
     *
     * @param inputStream the {@link InputStream} of the File/Asset to be parsed
     * @return {@link String} instance with the file/asset's contents.
     * @throws IOException thrown if there's an error performing the File I/O operations
     */
    public static String readFile(InputStream inputStream) throws IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder htmlBuilder = new StringBuilder();
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                htmlBuilder.append(mLine).append("\n");
            }
            return htmlBuilder.toString();
        } catch (IOException ioe) {
            throw new IOException(ioe.getCause());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
