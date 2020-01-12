import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Fase extends JPanel  implements Runnable{

    private static Image fundo;
    private int cont1;
    private Nave naveLocal,naveRemota;
    private Thread thread;
    private ArrayList<Inimigo> inimigos;
    private int score;
    private boolean isPaused = true;
    private boolean ended = false;
    private String serverIpAdress;
    private float FPS;
    private int PORT = 5007;
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public Fase(){
        inimigos = new ArrayList<Inimigo>();
        setFocusable(true);
        setVisible(true);
        setPreferredSize(new Dimension(500,500));
        acoes();
        ImageIcon referenciaFundo = new ImageIcon("res\\fundo.png");
        fundo = referenciaFundo.getImage();
        setUpPlayers();
        FPS = 30;
        conectarNoServidor();
    }

    private void setUpPlayers() {
        naveLocal = new Nave(new Point());
        naveRemota = new Nave(new Point());
    }


    private void acoes() {
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!isPaused && naveLocal.isAlive(true)) {
                    if (e.getKeyCode() == KeyEvent.VK_LEFT){
                        naveLocal.moverEsquerda();
                        sendMessage("xy:" + naveLocal.getPosicao().x + "," + naveLocal.getPosicao().y);
                    }
                    if (e.getKeyCode() == KeyEvent.VK_RIGHT){
                        naveLocal.moverDireita();
                        sendMessage("xy:" + naveLocal.getPosicao().x + ","+ naveLocal.getPosicao().y);
                    }
                    if(e.getKeyCode()==KeyEvent.VK_UP){
                        naveLocal.moverCima();
                        sendMessage("xy:" + naveLocal.getPosicao().x + ","+ naveLocal.getPosicao().y);
                    }
                    if(e.getKeyCode()==KeyEvent.VK_DOWN){
                        naveLocal.moverBaixo();
                        sendMessage("xy:" + naveLocal.getPosicao().x + ","+ naveLocal.getPosicao().y);
                    }
                    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                        Point missilPoint = new Point(naveLocal.getPosicao().x+naveLocal.getTamanho().width/ 2, naveLocal.getPosicao().y+naveLocal.getTamanho().height/ 2);
                        naveLocal.atirar(missilPoint);
                        sendMessage("fire:" + missilPoint.x + "," + missilPoint.y);
                    }
                }
            }
        });
    }

    private void iniciarJogo() {

        if (naveLocal.getId() == 1) {
            naveLocal.setPosicao(new Point(55,
                    55));
            naveRemota.setPosicao(new Point(55,
                    (int) this.getPreferredSize().getHeight() - naveRemota.getTamanho().height - 55));
        } else {
            naveLocal.setPosicao(new Point(55,
                    (int) this.getPreferredSize().getHeight() - naveLocal.getTamanho().height - 55));
            naveRemota.setPosicao(new Point(55,
                    55));
        }
        isPaused = false;
        if (thread == null) {
            thread = new Thread(this);
        }
        if (!thread.isAlive()) {
            thread.start();
        }
    }

    private void criarInimigo(Point posicao) {
        if(cont1==3){
            inimigos.add(new InimigoForte(posicao));
            cont1=0;
        }else{
            inimigos.add(new Inimigo(posicao));
            cont1++;
        }
    }

    private void conectarNoServidor() {
        try {
            socket = new Socket(serverIpAdress, PORT);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            startListeningServer();
            sendMessage("ok:" + naveLocal.getId());
        }catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Falha geral no servidor.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void startListeningServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Object data;
                try {
                    while ((data = ois.readObject()) != null) {
                        messageReceived(data.toString());
                    }
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void messageReceived(String message) {
        System.out.println(message);
        String[] parts = message.split(":");
        String[] args = null;

        switch (parts[0]) {
            case "id":
                naveLocal.setId(Integer.parseInt(parts[1]));
                break;
            case "ok":
                naveRemota.setId(Integer.parseInt(parts[1]));
                if (isPaused) {
                    iniciarJogo();
                    sendMessage("ok:" + naveLocal.getId());
                }
                break;
            case "xy":
                args = parts[1].split(",");
                naveRemota.setPosicao(new Point(Integer.parseInt(args[0]),
                        Integer.parseInt(args[1])));
                break;
            case "fire":
                args = parts[1].split(",");
                naveLocal.atirar(new Point(Integer.parseInt(args[0]),
                        Integer.parseInt(args[1])));
                break;
            case "inimigo":
                args = parts[1].split(",");

                criarInimigo(
                        new Point(Integer.parseInt(args[0]), Integer.parseInt(args[1])));
                break;
        }
    }

    private void sendMessage(String message) {
        try {
            oos.writeObject(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(fundo, 0, 0, null);

        if(naveLocal.isAlive(true)&&isPaused){
            ImageIcon fimJogo = new ImageIcon("res\\fundo_carregando.png");
            g2d.drawImage(fimJogo.getImage(), 0, 0, null);
        }else{

            for (Inimigo inimigo : inimigos) {
                g2d.drawImage(inimigo.getImagem(), inimigo.getPosicao().x, inimigo.getPosicao().y, null);
            }

            for (Missil missil : naveLocal.misseis) {
                g2d.drawImage(missil.getImagem(), missil.getPosicao().x, missil.getPosicao().y, null);
            }

            if (naveLocal.isAlive(true)) {
                g2d.drawImage(naveLocal.getImagem(), naveLocal.getPosicao().x,
                        naveLocal.getPosicao().y, null);
            }

            if (naveRemota.isAlive(true)) {
                g2d.drawImage(naveRemota.getImagem(), naveRemota.getPosicao().x,
                        naveRemota.getPosicao().y, null);
            }

            g2d.setColor(Color.WHITE);
            g2d.drawString("INIMIGOS MORTOS: " + score, 5, 15);
            g2d.drawString("VIDAS: "+naveLocal.getVida(),5,35);
            g2d.drawString("VIDAS DO COMPANHEIRO: "+naveRemota.getVida(),5,55);

        }if(ended&&isPaused) {
            ImageIcon fimJogo = new ImageIcon("res\\fundo_game_over.png");
            g2d.drawImage(fimJogo.getImage(), 0, 0, null);

        }
        g.dispose();
    }

    @Override
    public void run() {
        while (!isPaused) {
            try {
                Thread.sleep((int) FPS);
                ArrayList<Inimigo> auxInimigo = (ArrayList<Inimigo>) inimigos.clone();
                ArrayList<Missil> a = (ArrayList<Missil>) naveLocal.misseis;
                ArrayList<Missil> auxMissil = (ArrayList<Missil>) a.clone();
                for (Inimigo inimigoTeste : inimigos) {
                    inimigoTeste.getPosicao().x -= inimigoTeste.getVelocidade().y;
                    if (naveLocal.isAlive(true)) {
                        Rectangle formaInimigo = inimigoTeste.getBounds();
                        Rectangle formaNave = naveLocal.getBounds();
                        if(formaInimigo.intersects(formaNave)){
                            auxInimigo.remove(inimigoTeste);
                            if (naveLocal.getVida()>1){
                                int quant = naveLocal.getVida();
                                naveLocal.setVida(quant-1);
                            }else{
                                int quant = naveLocal.getVida();
                                naveLocal.setVida(quant-1);
                                naveLocal.setAlive(false);
                            }
                        }
                    }
                    if (naveRemota.isAlive(true)) {
                        Rectangle formaInimigo = inimigoTeste.getBounds();
                        Rectangle formaNave = naveRemota.getBounds();
                        if(formaInimigo.intersects(formaNave)){
                            auxInimigo.remove(inimigoTeste);
                            if (naveRemota.getVida()>1){
                                int quant = naveRemota.getVida();
                                naveRemota.setVida(quant-1);
                            }else{
                                int quant = naveRemota.getVida();
                                naveRemota.setVida(quant-1);
                                naveRemota.setAlive(false);
                            }
                        }
                    }
                    for (Missil missilTeste : naveLocal.misseis) {
                        Rectangle formaMissil = missilTeste.getBounds();
                        Rectangle formaInimigo = inimigoTeste.getBounds();
                        if(formaMissil.intersects(formaInimigo)){
                            auxMissil.remove(missilTeste);
                            if(inimigoTeste.getVida()>1){
                                int quant = inimigoTeste.getVida();
                                inimigoTeste.setVida(quant-1);
                            }else{
                                int quant = inimigoTeste.getVida();
                                auxInimigo.remove(inimigoTeste);
                            }
                            score += 1;
                        }
                    }
                }
                inimigos = auxInimigo;
                for (Missil missilTeste : naveLocal.misseis) {
                    missilTeste.getPosicao().x += missilTeste.getVelocidade().y;
                    if (missilTeste.getPosicao().y < 0) {
                        auxMissil.remove(missilTeste);
                    }
                }
                naveLocal.misseis = auxMissil;
                repaint();
            } catch (Exception e) {
            }
            if(naveRemota.isAlive==false && naveLocal.isAlive==false){
                isPaused=true;
                ended = true;
            }
        }
    }
}
