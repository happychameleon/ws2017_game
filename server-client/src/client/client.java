package client;

import java.io.*;
import java.net.Socket;

/**
 * Created by m on 3/10/17.
 */
public class client {

    public static void main(String[] args){
        try{
            Socket gameclient = new Socket("127.0.0.1", 1030);//starts a new socket that connects to server hosted locally
            InputStream in = gameclient.getInputStream();
            OutputStream out = gameclient.getOutputStream();
            gameclient.setSoTimeout(200);
            OutputThread th = new OutputThread(in);
            th.start();
            BufferedReader comandlinInput = new BufferedReader(new InputStreamReader(System.in));
            String line = "";
            while (true){
                line = comandlinInput.readLine();
                out.write(line.getBytes());
                out.write('\r');
                out.write('\n');
                if(line.equalsIgnoreCase("cquit"))break;
            }
            //stop program
            System.out.println("terminating");
            th.requestStop();
            try{
                Thread.sleep(1000);
            } catch (InterruptedException e){}
            in.close();
            out.close();
            gameclient.close();
        }catch (IOException e){
            System.err.println();
            System.exit(1);
        }
    }
}

class OutputThread extends Thread{
    InputStream in;
    boolean stopreaquest;
    public OutputThread(InputStream in){
        super();
        this.in = in;
        stopreaquest = false;
    }
    public synchronized void requestStop(){
        stopreaquest = true;
    }
    public void run(){
        int len;
        byte[] b = new byte[100];
        try {
            while (!stopreaquest) {
                try {
                    if ((len = in.read(b)) == -1) {
                        break;
                    }
                    System.out.write(b, 0, len);
                } catch (InterruptedIOException e) {
                 //try again
                }
            }
        } catch (IOException e) {
            System.err.println("OutputThread: " + e.toString());
        }
    }
}