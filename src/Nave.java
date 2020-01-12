import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Nave extends Corpo implements Atiravel{
    private int id;
    public List<Missil> misseis;

    public Nave(Point posicao){
        ImageIcon referencia = new ImageIcon("res\\nave.png");
        this.setImagem(referencia.getImage());
        this.setTamanho(new Dimension(this.getImagem().getWidth(null), this.getImagem().getHeight(null)));
        this.setPosicao(posicao);
        this.setVida(3);
        this.setAlive(true);
        this.misseis = new ArrayList<Missil>();
    }

    @Override
    public void atirar(Point point) {
        this.misseis.add(new Missil(point));
    }

    public void moverDireita(){
        if(this.getPosicao().x>=460){
        }else{
            this.getPosicao().x += 10;
        }

    }

    public void moverEsquerda(){
        if(this.getPosicao().x<=1){
        }else{
            this.getPosicao().x -= 10;
        }
    }

    public void moverCima(){
        if(this.getPosicao().y<=1){
        }else{
            this.getPosicao().y-=10;
        }
    }

    public void moverBaixo(){
        if(this.getPosicao().y>=470){
        }else{
            this.getPosicao().y += 10;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Missil> getMisseis() {
        return misseis;
    }

    public void setMisseis(List<Missil> misseis) {
        this.misseis = misseis;
    }
}
