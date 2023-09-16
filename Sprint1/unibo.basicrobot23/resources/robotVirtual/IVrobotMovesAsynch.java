package robotVirtual;

public interface IVrobotMovesAsynch extends IVrobotMoves{
    void stepAsynch(int time) throws Exception;
    void setTrace(boolean v);
}
