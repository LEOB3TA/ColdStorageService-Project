package unibo.serviceaccessbe.utils;

public class CommonUtils {
    public static String ip = "localhost";
    public static String robotIp = "localhost";
    public static String cssPort = "8099";
    public static String robotPort = "8020";

    public static String getTicketRequestUrl() {
        return "http://"+ip+":8080/ticket";
    }

    public static String getEvaluationRequestUrl() {
        return "http://\"+ip+\":8080/evaluation";
    }

    public String getRobotUrl() {
        return "http://"+robotIp+":"+robotPort;
    }

    public String getCssUrl() {
        return "http://"+ip+":"+cssPort;
    }


}
