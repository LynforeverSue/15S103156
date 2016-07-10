
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client1 implements Runnable {
    private ServerSocket ss;

    public MyClient(int port) throws IOException {
        ss = new ServerSocket(port);
        ss.setSoTimeout(10000);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket server = ss.accept();
                DataInputStream din = new DataInputStream(server.getInputStream());
                byte buffer[] = new byte[1024];
                int len = din.read(buffer);
                String getString = new String(buffer, 0, len);
                System.out.println("you have received a file request!");
                DataOutputStream dout = new DataOutputStream(server.getOutputStream());
                FileInputStream fis = new FileInputStream(new File("E:\\" + getString));
                byte b[] = new byte[1024];
                fis.read(b);
                dout.write(b);
                dout.close();
                server.close();
            } catch (IOException e) {
            }
        }
    }

    public static void main(String[] args) throws UnknownHostException, IOException {
        Socket socket = new Socket("127.0.0.1", 8889);
        DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
        String key = "a.txt";
        dout.write(key.getBytes());

        InputStream input = socket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String getString;
        getString = reader.readLine();
        input.close();
        System.out.println(getString);
        socket.close();


        socket = new Socket(getString, 9000);
        OutputStream outstream = socket.getOutputStream();
        outstream.write(key.getBytes());
        input = socket.getInputStream();
        FileOutputStream fos = new FileOutputStream(new File("F:\\" + key));
        int tmp = 0;
        while ((tmp = input.read()) != -1) {
            fos.write(tmp);
        }
        fos.close();
        outstream.close();
        socket.close();

    }
}
