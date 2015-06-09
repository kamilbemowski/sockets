package com.socket.test.one;

import java.io.*;
import java.net.Socket;

/**
 * Created by kamil on 09.06.15.
 */
public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 4223);

        OutputStream outToServer = socket.getOutputStream();
        DataOutputStream out =
                new DataOutputStream(outToServer);

        out.writeUTF("Hello from "
                + socket.getLocalSocketAddress());
        InputStream inFromServer = socket.getInputStream();
        DataInputStream in =
                new DataInputStream(inFromServer);
        System.out.println("Server says " + in.readUTF());
        socket.close();
    }
}
