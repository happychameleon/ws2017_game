package client;

import java.io.*;
import java.net.Socket;

import login.*;

/**
 * Created by m on 3/10/17.
 */

public class Client {

	public void OpenChat(){
		new Chat();
	}
	
    public static void main(String[] args){
        try{
        	new Login();
            Socket gameclient = new Socket("127.0.0.1", 1030);//starts a new socket that connects to server hosted locally
            InputStream in = gameclient.getInputStream();
            OutputStream out = gameclient.getOutputStream();
            gameclient.setSoTimeout(200);
            ClientThread th = new ClientThread(in, out);
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


class ClientThread extends Thread{
    InputStream in;
    OutputStream out;
    boolean stopreaquest;
    public ClientThread(InputStream in, OutputStream out){
        super();
        this.in = in;
        this.out = out;
        stopreaquest = false;
    }

    public synchronized void requestStop(){
        stopreaquest = true;
    }


    public void run(){
        ClientCommandParser commandParser = new ClientCommandParser(in, out, stopreaquest);
        commandParser.stopValidateingCommand(stopreaquest);
    }
}