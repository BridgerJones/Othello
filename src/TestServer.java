import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TestServer {
    public static void main(String[] args) {

        int row;
        int col;

        try {
            while(true){
                ServerSocket server = new ServerSocket(8000);
                Socket socket = server.accept();

                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());

                System.out.println(input.readInt());
            }


        }
        catch (Exception e){
            e.printStackTrace();
        }






    }
}