package co.edu.escuelaing.arep.parcialt1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

/**
 *
 * @author laura.rsanchez
 */
public class BackendServer {

    public static LinkedList array = new LinkedList();
    public static Double totalVal = 0.0;
    public static Double totalDesv = 0.0;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(36000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 36000.");
            System.exit(1);
        }
        boolean running = true;
        while (running) {
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            PrintWriter out = new PrintWriter(
                    clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            String inputLine, outputLine;

            boolean firstLine = true;
            String request = "";

            while ((inputLine = in.readLine()) != null) {
                if (firstLine) {
                    request = inputLine;
                    firstLine = false;
                }
                if (!in.ready()) {
                    break;
                }
            }

            String path = request.split(" ")[1];
            System.out.println("Backend: " + path);

            if (path.startsWith("/app")) {
                System.out.println("Entra: " + path);
                outputLine = responseRequest(path);
            } else {
                outputLine = "HTTP/1.1 200 OK\r\n"
                        + "Content-Type: text/html\r\n"
                        + "\r\n"
                        + "<!DOCTYPE html>\n"
                        + "<html>\n"
                        + "<head>\n"
                        + "<meta charset=\"UTF-8\">\n"
                        + "<title>Respuesta servidor backend</title>\n"
                        + "</head>\n"
                        + "<body>\n"
                        + "<h1>Path incorrecto, intente con /app</h1>\n"
                        + "</body>\n"
                        + "</html>\n";
            }
            out.println(outputLine);
            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }

    private static String responseRequest(String path) {
        System.out.println("Entra response: " + path);
        String request = path.split("/")[2];
        if (request.startsWith("add")) {
            String[] values = request.split("=");
            Double num = Double.parseDouble(values[1].split("%20")[1]);
            array.add(num);
            Integer sizeArray = array.size();
            totalVal = Double.sum(num, totalVal);
            String nStr = String.valueOf(sizeArray);
            Double n2 = Double.parseDouble​(nStr);
            Double media = totalVal / n2;
            totalDesv = Double.sum(pow(num - media, 2.0), totalDesv);

            System.out.println("Retorna");
            return "HTTP/1.1 200 OK\r\n"
                    + "Content-Type: application/json\r\n"
                    + "\r\n"
                    + "{\r\n"
                    + "\"status\": \"OK\",\r\n"
                    + "\"added\": " + num + " \r\n"
                    + "\"count\": " + sizeArray + " \r\n"
                    + "}\r\n";
        } else if (request.startsWith("list")) {
            System.out.println(array);
            return "HTTP/1.1 200 OK\r\n"
                    + "Content-Type: application/json\r\n"
                    + "\r\n"
                    + "{\r\n"
                    + "\"status\": \"OK\",\r\n"
                    + "\"values\": " + array + " \r\n"
                    + "}\r\n";
        } else if (request.startsWith("stats")) {
            Integer n = array.size();
            if (n == 0) {
                return "HTTP/1.1 409 Conflict OK\r\n"
                        + "Content-Type: application/json\r\n"
                        + "\r\n"
                        + "{\r\n"
                        + "\"status\": \"ERR\",\r\n"
                        + "\"error\": \"empty_list\",\r\n"
                        + "}\r\n";
            }

            // Calculo Media
            String nStr = String.valueOf(n);
            Double n2 = Double.parseDouble​(nStr);

            Double media = totalVal / n2;

            Double n_1 = n2 - 1.0;

            Double desvEst = sqrt(totalDesv / n_1);

            return "HTTP/1.1 200 OK\r\n"
                    + "Content-Type: application/json\r\n"
                    + "\r\n"
                    + "{\r\n"
                    + "\"status\": \"OK\",\r\n"
                    + "\"media\": " + media + " \r\n"
                    + "\"desviacion estandar muestral\": " + desvEst + " \r\n"
                    + "}\r\n";
        } else if (request.startsWith("clear")) {
            array.clear();
            Integer sizeArray = array.size();
            return "HTTP/1.1 200 OK\r\n"
                    + "Content-Type: application/json\r\n"
                    + "\r\n"
                    + "{\r\n"
                    + "\"status\": \"OK\",\r\n"
                    + "\"count\": " + sizeArray + " \r\n"
                    + "}\r\n";
        }
        return "";
    }
}
