package main;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.*;
import java.net.Socket;

@RunWith(JUnit4.class)
public class ServerTest {
    private Server server;

    @Before
    public void setup() {
        // postawienie serwera
        new Thread(() -> server = Server.getInstance()).start();
    }

    @After
    public void reset() {
        // zresetowanie instancji serwera
        Server.resetInstance();
    }

    @Test
    public void checkResponseOnClientConnection() throws IOException {
        Socket clientSocket = new Socket("127.0.0.1", 9898); //
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String responseLine = in.readLine();
        Assert.assertEquals("SUBMITNAME", responseLine);
    }

    @Test
    public void checkHowManyPlayersSetting() throws IOException, InterruptedException {
        Socket clientSocket = new Socket("127.0.0.1", 9898); //
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String responseLine = in.readLine();
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        Assert.assertEquals("SUBMITNAME", responseLine);
        out.println("NazwaGracza");

        out.println("HOWMANYPLAYERS");
        out.println("4");

        Thread.sleep(100);
        Assert.assertEquals(4, server.getMatch().getHmp());
    }

    @Test
    public void checkConnectionOverloadResponse() throws IOException {
        for (int i = 0; i < 100; i++) {
            Socket clientSocket = new Socket("127.0.0.1", 9898);
        }

        Socket clientSocket = new Socket("127.0.0.1", 9898);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String responseLine = in.readLine();
        Assert.assertEquals("TRY LATER", responseLine);
    }

}
