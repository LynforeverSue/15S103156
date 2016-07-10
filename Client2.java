
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Client2 implements Runnable {
    private ServerSocket ss;
    private HashMap<String, String> hm;

    public Client2(int port) throws IOException {
        ss = new ServerSocket(port);
        ss.setSoTimeout(10000);
        hm = new HashMap<String, String>();
        hm.put("a.txt", "172.16.0.247");
        hm.put("b.txt", "172.16.0.247");
        hm.put("c.txt", "172.16.0.247");
        hm.put("d.txt", "172.16.0.247");
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket server = ss.accept();
                DataInputStream din = new DataInputStream(server.getInputStream());
                byte buffer[] = new byte[1024];

                int tmp = din.read(buffer);
                String key = new String(buffer, 0, tmp);
                System.out.println("you have received a request!");
                if (hm.containsKey(key)) {
                    DataOutputStream dout = new DataOutputStream(server.getOutputStream());
                    System.out.println(key);
                    dout.write(hm.get(key).getBytes());
                    dout.close();
                } else {
                    DataOutputStream dout = new DataOutputStream(server.getOutputStream());
                    String str = "There is no this file here!";
                    System.out.println(key);
                    dout.write(str.getBytes());
                    dout.close();
                }

            } catch (IOException e) {
            }
        }
    }

    public static void main(String[] args) throws IOException {
        TcpServer tcp = new TcpServer(8889);
        Server ser = new Server(9000);
        MyClient client = new MyClient(9001);
        new Thread(tcp).start();
        new Thread(ser).start();
        new Thread(client).start();


    }
}
