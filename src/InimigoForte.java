import javax.swing.*;
import java.awt.*;

public class InimigoForte extends Inimigo {
    public InimigoForte(Point posicao) {
        super(posicao);
        ImageIcon referencia = new ImageIcon("res\\inimigo_1.png");
        this.setImagem(referencia.getImage());
        this.setVida(3);
    }
}
