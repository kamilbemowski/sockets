package com.socket.test.b;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by kamil on 09.06.15.
 */
public class Client {

    public static void main(String[] args) throws IOException {
        SocketChannel client = SocketChannel.open();

        client.configureBlocking(false);

        String localhost = "localhost";
        client.connect(new java.net.InetSocketAddress(localhost, 8000));

        Selector selector = Selector.open();

        client.register(selector, SelectionKey.OP_CONNECT);

        while (selector.select(500) > 0) {

            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> i = keys.iterator();

            while (i.hasNext()) {
                SelectionKey key = i.next();

                i.remove();

                SocketChannel channel = (SocketChannel) key.channel();

                if (key.isConnectable()) {

                    System.out.println("Server Found");

                    if (channel.isConnectionPending())
                        channel.finishConnect();

                    ByteBuffer buffer;
                    for (int j = 0; j < 10; j++) {
                        buffer =
                                ByteBuffer.wrap(new String(
                                        "Client " + j + "\n").getBytes());
                        channel.write(buffer);
                        buffer.clear();
                    }

                }
            }
        }
    }
}
