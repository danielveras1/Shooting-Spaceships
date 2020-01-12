import java.awt.*;

public abstract class Corpo {
    protected Point posicao;
    protected Dimension tamanho;
    protected Point velocidade;
    protected boolean isAlive;
    protected int vida;
    protected Image imagem;

    public Rectangle getBounds() {
        int x = getPosicao().x;
        int y = getPosicao().y;
        int altura = getImagem().getHeight(null);
        int largura = getImagem().getWidth(null);
        return new Rectangle(x,y,largura,altura);
    }

    public Point getPosicao() {
        return posicao;
    }

    public void setPosicao(Point posicao) {
        this.posicao = posicao;
    }

    public Dimension getTamanho() {
        return tamanho;
    }

    public void setTamanho(Dimension tamanho) {
        this.tamanho = tamanho;
    }

    public Point getVelocidade() {
        return velocidade;
    }

    public void setVelocidade(Point velocidade) {
        this.velocidade = velocidade;
    }

    public boolean isAlive(boolean b) {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    public Image getImagem() {
        return imagem;
    }

    public void setImagem(Image imagem) {
        this.imagem = imagem;
    }
}
