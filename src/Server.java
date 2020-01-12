import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server extends JFrame {
    private ServerSocket socketServidor = null;
    private int Port = 5007;
    private final int MAX_CONNECTED_USERS = 2;
    private HashMap<Integer, ObjectOutputStream> oosList;
    private Socket client;

    public Server(){
        executar();
    }

    private void executar(){
        try {
            oosList = new HashMap<>();
            System.out.println("Iniciando servidor");
            socketServidor = new ServerSocket(Port);

            for (int i=1; i<= MAX_CONNECTED_USERS; i++){
                client = socketServidor.accept();
                String name = client.getInetAddress().getHostName();
                System.out.println(name+" conectou-se");
                oosList.put(i,new ObjectOutputStream(client.getOutputStream()));
                System.out.println("id:" + i);
                oosList.get(i).writeObject("id:" + i);
                new ServerThread(i).start();

            }
            new GameTasks();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ServerThread extends Thread {
        private int id;
        public ServerThread(int id){
            this.id = id;
        }

        @Override
        public void run() {
            Object data = null;

            try {
                ObjectInputStream inputStream = new ObjectInputStream(client.getInputStream());
                while ((data = inputStream.readObject()) != null){
                    for (int i : oosList.keySet()){
                        if(i != id && oosList.containsKey(i)){
                            oosList.get(i).writeObject(data);
                            oosList.get(i).flush();
                        }
                    }
                }
                inputStream.reset();
                inputStream.close();

            } catch (Exception e) {
                System.out.println("O cliente " + id + " se desconectou");
                disconect();
            }
        }

        private void disconect() {
            try{
                oosList.get(id).close();

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            oosList.remove(id);
        }
    }

    private class GameTasks {
        private Random r = new Random();
        private int delay = 1000;
        private int intervaloDeCriacao = 10;


        public GameTasks() {
            Timer inimigoTimer = new Timer("InimigoTask");
            inimigoTimer.scheduleAtFixedRate(criarInimigos(),intervaloDeCriacao,delay);
        }

        private TimerTask criarInimigos() {
            return new TimerTask() {
                @Override
                public void run() {
                    String command = "inimigo:%d,%d";
                    int y= 40;
                    int random = r.nextInt(5);
                    for(int i=0;i<5;i++){
                        if(random==i){
                            y +=90*i;
                            break;
                        }
                    }

                    for (int i : oosList.keySet()){
                        try{
                            oosList.get(i).writeObject(String.format(command,500,y));
                            oosList.get(i).flush();

                        } catch (IOException e) {
                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, e);
                        }
                    }

                }
            };
        }
    }

    public static void main(String args[]) {
        new Server();
    }
}
