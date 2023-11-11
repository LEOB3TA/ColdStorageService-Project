package unibo.serviceaccessbe.utils;

import unibo.basicomm23.coap.CoapConnection;
import unibo.basicomm23.interfaces.Interaction2021;
import unibo.basicomm23.tcp.TcpClientSupport;
import unibo.basicomm23.utils.ColorsOut;
import unibo.basicomm23.utils.CommSystemConfig;

public class UtilsStatus {
    private static String ctxName = "ctx_wasteservice";
    private static String actorName = "status_controller";

    private static Interaction2021 conn;
    public static Interaction2021 connTCP;


    public static CoapConnection connectWithUtilsUsingCoap(String ip, int port){
        try{
            CommSystemConfig.tracing = true;
            String path = ctxName + "/" + actorName;
            conn = new CoapConnection(ip + ":" + port, path);
            ColorsOut.out("[UtilsStatusGUI] connect Tcp conn:" + conn );
            ColorsOut.outappl("[UtilsStatusGUI] connect Coap conn:" + conn , ColorsOut.CYAN);
        }catch(Exception e){
            ColorsOut.outerr("[UtilsStatusGUI] connect with GUIupdater ERROR:"+e.getMessage());
        }
        return (CoapConnection) conn;
    }

    public static void connectWithUtilsUsingTcp(String ip, int port){
        try {
            CommSystemConfig.tracing = true;
            connTCP = TcpClientSupport.connect(ip, port, 10);
            ColorsOut.out("[UtilsStatusGUI] connect Tcp conn:" + conn );
            ColorsOut.outappl("[UtilsStatusGUI] connect Tcp conn:" + conn , ColorsOut.CYAN);
        }catch(Exception e){
            ColorsOut.outerr("[UtilsStatusGUI] connect with GUIupdater ERROR:"+e.getMessage());
        }
    }


    public static void sendMsg() {
        try {
            String msg = "msg(get_data, dispatch, ws_gui, " + actorName + ", get_data(_), 1)"; // TODO: cambia ws_gui con nome nuovo attore QAK
            ColorsOut.outappl("[UtilsStatusGUI] sendMsg msg:" + msg + " conn=" + conn, ColorsOut.BLUE);
            connTCP.forward(msg);
        } catch (Exception e) {
            ColorsOut.outerr("[UtilsStatusGUI] sendMsg on:" + conn + " ERROR:" + e.getMessage());
        }
    }
}



