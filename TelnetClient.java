package de.nic0g.nic0g_proteknewcamdsettings;




import java.io.*;
import java.util.LinkedList;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


import org.apache.commons.io.input.TeeInputStream;



public class TelnetClient {
    private TelnetConnection client;
    private OutputStream outstream;
    private org.apache.commons.net.telnet.TelnetClient rawConnection;
    private InputStream instream;
    AtomicInteger guy;
    private LinkedList<Thread> threads = new LinkedList();
    private PipedInputStream spyReader;

    public TelnetClient(String ip, int port) throws IOException {
        client = new TelnetConnection(ip, port);
        client.connect();
        rawConnection = client.getConnection();
        outstream = client.getOutput();
        instream = client.getReader();
    }

    public void close() throws IOException {
        rawConnection.disconnect();
    }

    /**
     * @param cmd the string of message you want to send
     * @return true if message was sent successfully
     */
    public boolean sendCommand(String cmd, boolean enter) {
        if (client == null || !client.isConnected()) {
            return false;
        }
        StringBuilder stringBuilder = new StringBuilder();

        //stringBuilder.append(cmd.toUpperCase(Locale.ENGLISH));
        stringBuilder.append(cmd);
        if(enter) {
            stringBuilder.append("\n\r");
        }
        byte[] cmdbyte = stringBuilder.toString().getBytes();

        try {
            outstream.write(cmdbyte, 0, cmdbyte.length);
            outstream.flush();
            return true;
        } catch (Exception e1) {
            return false;
        }
    }

    public boolean sendCharCommand(int character) {
        if (client == null || !client.isConnected()) {
            return false;
        }

        char ctrlBreak = (char)character;

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ctrlBreak);

        byte[] cmdbyte = stringBuilder.toString().getBytes();

        try {
            outstream.write(cmdbyte, 0, cmdbyte.length);
            outstream.flush();
            return true;
        } catch (Exception e1) {
            return false;
        }
    }


    public boolean sendArrowKey(String arrowKey){
        if (client == null || !client.isConnected()) {
            return false;
        }

        char ctrlBreak = (char)27;
        StringBuilder stringBuilder = new StringBuilder();
        switch (arrowKey){
            case "Left":
                stringBuilder.append(ctrlBreak+"[D");
                break;
            case "Right":
                stringBuilder.append(ctrlBreak+"[C");
                break;
            case "Down":
                stringBuilder.append(ctrlBreak+"[B");
                break;
            case "Up":
                stringBuilder.append(ctrlBreak + "[A");
                break;
        }
        byte[] cmdbyte = stringBuilder.toString().getBytes();

        try {
            outstream.write(cmdbyte, 0, cmdbyte.length);
            outstream.flush();
            return true;
        } catch (Exception e1) {
            return false;
        }
    }

    public boolean sendColorKey(String colorKey){
        if (client == null || !client.isConnected()) {
            return false;
        }

        char ctrlBreak = (char)27;
        StringBuilder stringBuilder = new StringBuilder();
        switch (colorKey){
            case "r":
                stringBuilder.append(ctrlBreak+"[15");
                break;
            case "g":
                stringBuilder.append(ctrlBreak+"[17");
                break;
            case "y":
                stringBuilder.append(ctrlBreak+"[18");
                break;
            case "b":
                stringBuilder.append(ctrlBreak + "[19");
                break;
        }
        byte[] cmdbyte = stringBuilder.toString().getBytes();

        try {
            outstream.write(cmdbyte, 0, cmdbyte.length);
            outstream.flush();
            return true;
        } catch (Exception e1) {
            return false;
        }
    }


    public InputStreamReader spawnSpy() throws InterruptedException, IOException {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream();
        in.connect(out);
        if(spyReader!=null) {
            return spawnSpy(spyReader, out);
        } else {
            spyReader = in;
            return spawnSpy(instream, out);
        }
    }

    public InputStreamReader spawnSpy(InputStream in, PipedOutputStream pipeout) throws InterruptedException {
        return new InputStreamReader(new TeeInputStream(in,pipeout));
    }

    private String readUntil(String expected) throws InterruptedException, TimeoutException, ExecutionException {
        return readUntil(expected, -1);
    }

    private String readUntil(String expected, int timeout) throws InterruptedException, TimeoutException, ExecutionException {
        final ExecutorService service;
        final Future<String> result;
        if (timeout == -1) {
            service = Executors.newFixedThreadPool(1);
            result = service.submit(new ReadUntil(expected));
            return result.get(5, TimeUnit.SECONDS);
        } else {
            service = Executors.newFixedThreadPool(1);
            result = service.submit(new ReadUntil(expected));
            return result.get(timeout, TimeUnit.MILLISECONDS);
        }
    }

    public boolean isConnected() {
        return client.isConnected();
    }

    public boolean disconnect() {
        spyReader = null;
        return client.disconnect();
    }

    private class ReadUntil implements Callable<String> {
        String expected;

        @Override
        public String call() {
            try {
                InputStreamReader a = spawnSpy();
                BufferedReader stream = new BufferedReader(a);

                String line;
                while ((line = stream.readLine()) != null){
                    if(line.contains(expected))
                        break;
                }
                return line;
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        public ReadUntil(String expect) {
            expected = expect;
        }
    }
}