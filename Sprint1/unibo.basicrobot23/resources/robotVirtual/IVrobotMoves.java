package robotVirtual;

public interface IVrobotMoves {
    boolean step(long time) throws Exception;
    void turnLeft() throws Exception;
    void turnRight() throws Exception;
    void forward(int time) throws Exception;
    void backward(int time) throws Exception;
    void move(String cmd) throws Exception;
    void halt() throws Exception;
}
