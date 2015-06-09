package com.socket.test.b;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.Set;

public class Server {
    private static String clientChannel = "clientChannel";
    private static String serverChannel = "serverChannel";
    private static String channelType = "channelType";

    /**
     * ServerSocketChannel represents a channel for sockets that listen to
     * incoming connections.
     *
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        String localhost = "localhost";

        ServerSocketChannel server = ServerSocketChannel.open();
        server.configureBlocking(false);
        server.socket().bind(new java.net.InetSocketAddress(localhost, 8000));
        Selector selector = Selector.open();
        server.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> i = keys.iterator();

            while (i.hasNext()) {
                SelectionKey key = i.next();

                i.remove();

                if (key.isAcceptable()) {
                    SocketChannel client = server.accept();
                    client.configureBlocking(false);
                    client.register(selector, SelectionKey.OP_READ);
                    continue;
                }

                if (key.isReadable()) {
                    SocketChannel client = (SocketChannel) key.channel();
                    int BUFFER_SIZE = 32;
                    ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
                    try {
                        client.read(buffer);
                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;
                    }

                    buffer.flip();
                    Charset charset = Charset.forName("ISO-8859-1");
                    CharsetDecoder decoder = charset.newDecoder();
                    CharBuffer charBuffer = decoder.decode(buffer);
                    System.out.print(charBuffer.toString());
                    continue;
                }
            }
        }
    }
}