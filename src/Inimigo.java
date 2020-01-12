import javax.swing.*;
import java.awt.*;

public class Inimigo extends Corpo{
    public Inimigo(Point posicao) {
        ImageIcon referencia = new ImageIcon("res\\inimigo_2.png");
        this.setImagem(referencia.getImage());
        this.setPosicao(posicao);
        this.setTamanho(new Dimension(this.getImagem().getWidth(null), this.getImagem().getHeight(null)));
        this.setVelocidade(new Point(0, 2));
        this.setVida(1);
        this.isAlive(true);
    }
}
