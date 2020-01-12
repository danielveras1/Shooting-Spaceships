import javax.swing.*;
import java.awt.*;

public class Missil extends Corpo {
    public Missil(Point posicao) {
        ImageIcon referencia = new ImageIcon("res\\missil.png");
        this.setImagem(referencia.getImage());
        this.setPosicao(posicao);
        this.setTamanho(new Dimension(this.getImagem().getWidth(null), this.getImagem().getHeight(null)));
        this.setVelocidade(new Point(0, 10));
    }
}
