package co.edu.escuelaing.arep.parcialt1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

/**
 *
 * @author laura.rsanchez
 */
public class FacadeServer {

    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String GET_URL = "http://localhost:36000";

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
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
            System.out.println(request);

            String path = request.split(" ")[1];
            if (path.startsWith("/cliente")) {
                outputLine = getIndex();
            } else if (path.startsWith("/request")) {
                System.out.println(path);
                outputLine = "HTTP/1.1 200 OK\r\n"
                        + "Content-Type: text/html\r\n"
                        + "\r\n"
                        + getResponse(path);
            } else {
                outputLine = "HTTP/1.1 200 OK\r\n"
                        + "Content-Type: text/html\r\n"
                        + "\r\n"
                        + "<!DOCTYPE html>\n"
                        + "<html>\n"
                        + "<head>\n"
                        + "<meta charset=\"UTF-8\">\n"
                        + "<title>Respuesta servidor fachada</title>\n"
                        + "</head>\n"
                        + "<body>\n"
                        + "<h1>Path incorrecto, intente con /cliente</h1>\n"
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

    public static String getResponse(String path) throws IOException {
        URL obj = new URL(GET_URL + path.replace("/request", "/app"));
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);

        //The following invocation perform the connection implicitly before getting the code
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println(response.toString());
            return response.toString();
        } else {
            System.out.println("GET request not worked");
            return "HTTP/1.1 502 Bad Gateway\r\n"
                    + "Content-Type: application/json\r\n"
                    + "\r\n"
                    + "{\r\n"
                    + "\"status\": \"ERR\",\r\n"
                    + "\"error\": \"backend_unreachable\"\r\n"
                    + "}\r\n";
        }
    }

    private static String getIndex() {
        return "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "<!DOCTYPE html>\r\n"
                + "<html>\r\n"
                + "\r\n"
                + "<head>\r\n"
                + "<title>Parcial1 AREP</title>\r\n"
                + "<meta charset==\"UTF-8=\">\r\n"
                + "<meta name==\"viewport=\" content==\"width=device-width, initial-scale=1.0=\">\r\n"
                + "</head>\r\n"
                + "\r\n"
                + "<body>\r\n"
                + "<h1>Registrar un valor</h1>\r\n"
                + "<form action=\"/request/add\">\r\n"
                + "<label for=\"name\">Valor a registrar:</label><br>\r\n"
                + "<input type=\"text\" id=\"numero\" numero=\"numero\"><br><br>\r\n"
                + "<input type=\"button\" value=\"Agregar\" onclick=\"loadAddNumber()\">\r\n"
                + "</form>\r\n"
                + "<div id=\"getAddNumber\"></div>\r\n"
                + "\r\n"
                + "<script>\r\n"
                + "function loadAddNumber() {\r\n"
                + "let nameVar = document.getElementById(\"numero\").value;\r\n"
                + "const xhttp = new XMLHttpRequest();\r\n"
                + "xhttp.onload = function () {\r\n"
                + "document.getElementById(\"getAddNumber\").innerHTML =\r\n"
                + "this.responseText;\r\n"
                + "}\r\n"
                + "xhttp.open(\"GET\", \"/request/add ? numero = \" + nameVar);\r\n"
                + "xhttp.send();\r\n"
                + "}\r\n"
                + " </script>\r\n"
                + "\r\n"
                + "<h1>Consultar lista</h1>\r\n"
                + "<form action=\"/request/list\">\r\n"
                + "<input type=\"button\" value=\"Consultar\" onclick=\"loadList()\">\r\n"
                + "</form>\r\n"
                + "<div id=\"getList\"></div>\r\n"
                + "\r\n"
                + "<script>\r\n"
                + "function loadList() {\r\n"
                + "const xhttp = new XMLHttpRequest();\r\n"
                + "xhttp.onload = function () {\r\n"
                + "document.getElementById(\"getList\").innerHTML =\r\n"
                + "this.responseText;\r\n"
                + "}\r\n"
                + "xhttp.open(\"GET\", \"/request/list\");\r\n"
                + "xhttp.send();\r\n"
                + "}\r\n"
                + " </script>\r\n"
                + "\r\n"
                + "<h1>Borrar lista</h1>\r\n"
                + "<form action=\"/request/clear\">\r\n"
                + "<input type=\"button\" value=\"Borrar\" onclick=\"loadClear()\">\r\n"
                + "</form>\r\n"
                + "<div id=\"getClear\"></div>\r\n"
                + "\r\n"
                + "<script>\r\n"
                + "function loadClear() {\r\n"
                + "const xhttp = new XMLHttpRequest();\r\n"
                + "xhttp.onload = function () {\r\n"
                + "document.getElementById(\"getClear\").innerHTML =\r\n"
                + "this.responseText;\r\n"
                + "}\r\n"
                + "xhttp.open(\"GET\", \"/request/clear\");\r\n"
                + "xhttp.send();\r\n"
                + "}\r\n"
                + " </script>\r\n"
                + "\r\n"
                + "<h1>Estadisticas</h1>\r\n"
                + "<form action=\"/request/stats\">\r\n"
                + "<input type=\"button\" value=\"Calcular estadisticas\" onclick=\"loadStats()\">\r\n"
                + "</form>\r\n"
                + "<div id=\"getStats\"></div>\r\n"
                + "\r\n"
                + "<script>\r\n"
                + "function loadStats() {\r\n"
                + "const xhttp = new XMLHttpRequest();\r\n"
                + "xhttp.onload = function () {\r\n"
                + "document.getElementById(\"getStats\").innerHTML =\r\n"
                + "this.responseText;\r\n"
                + "}\r\n"
                + "xhttp.open(\"GET\", \"/request/stats\");\r\n"
                + "xhttp.send();\r\n"
                + "}\r\n"
                + " </script>\r\n"
                + "\r\n"
                + "</body>\r\n"
                + "\r\n"
                + "</html>\r\n";
    }
}
