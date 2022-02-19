package com.origami.sigef.tesoreria.comprobantelectronico.sri.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SimpleFileServer {

    public final static int SOCKET_PORT = 13267;  // you may change this
    public final static String CONNECTION_ADDRESS = "Z:\\comprobantes";  // you may change this

    public static void sendFile(String FILE_TO_SEND) throws IOException {
        Path temp = Files.move(Paths.get(FILE_TO_SEND),
                Paths.get(CONNECTION_ADDRESS + "\\445.txt"));

        if (temp != null) {
            System.out.println("File renamed and moved successfully");
        } else {
            System.out.println("Failed to move the file");
        }
    }
}
