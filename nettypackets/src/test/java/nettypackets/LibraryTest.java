/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package nettypackets;

import nettypackets.packet.PacketHolder;
import nettypackets.packetregistry.DefaultPacketRegistry;
import nettypackets.packetregistry.PacketRegistry;
import nettypackets.packetregistry.SidedPacketRegistryContainer;
import org.junit.Test;

public class LibraryTest {

    public static SidedPacketRegistryContainer serverPacketRegistries;
    public static PacketRegistry serverRegistry;


    public static SidedPacketRegistryContainer clientPacketRegistries;
    public static PacketRegistry clientRegistry;

    public static Server server;
    public static Client client;


    @Test
    public void test() throws InterruptedException {

        serverPacketRegistries = new SidedPacketRegistryContainer();
        serverRegistry = serverPacketRegistries.addRegistry(new DefaultPacketRegistry("iogames"));
        serverRegistry.register(new PacketHolder<>(TestPacket::new, TestPacket::serverHandle, TestPacket.class), 0);
        serverRegistry.register(new PacketHolder<>(TestPacket2::new, TestPacket2::serverHandle, TestPacket2.class), 1);

        clientPacketRegistries = new SidedPacketRegistryContainer();
        clientRegistry = clientPacketRegistries.addRegistry(new DefaultPacketRegistry("iogames"));
        clientRegistry.register(new PacketHolder<>(TestPacket::new, TestPacket::clientHandle, TestPacket.class), 0);
        clientRegistry.register(new PacketHolder<>(TestPacket2::new, TestPacket2::clientHandle, TestPacket2.class), 1);



        server = new Server(8080, serverPacketRegistries);
        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    server.startUp();
                }catch(Exception ignored){}
            }
        };

        thread.start();

        Thread.sleep(1000);

        client = new Client("localhost", 8080, clientPacketRegistries);
        thread = new Thread(){
            @Override
            public void run() {
                try {
                    client.run();
                }catch(Exception e){}
            }
        };
        thread.start();

        Thread.sleep(1000);

        for(int i=0;i<50;i++){

            //Thread.sleep(100);
            if(Math.random()>0.5) {
                client.sendPacket(clientRegistry, new TestPacket("1 hi " + i));
            }else{
                client.sendPacket(clientRegistry, new TestPacket2("2 hi " + i));
            }
        }
        client.channel.flush();
        Thread.sleep(1000);
        server.channels.flush();
        Thread.sleep(1000);


    }

}
