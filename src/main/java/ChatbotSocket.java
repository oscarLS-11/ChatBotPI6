import org.alicebot.ab.Chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatbotSocket extends Thread{

    //hacer atrib a salida
    private PrintWriter salida; //modificacion actual
    private Socket socket; // se crea un atributo que almacena el socket de la conexión
    private ChatbotV2 chatbot; //se crea un atributo que almacena una instancia del chatbot

    //este constructor recibe un socket de conexión
    public ChatbotSocket(Socket socket){
        //asignación del atributo socket
        super("EchoMultiServerThread");
        this.socket = socket; //se asigna el soctek recibido al atributo socket
        chatbot = new ChatbotV2(this);// se crea una instancia de chatbot
        //salida = new PrintWriter;   //modificacion actual

        try {
            salida = new PrintWriter(socket.getOutputStream(), true);
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public ChatbotSocket() {}


    public PrintWriter getSalida() {
        return salida;
    }

    public void setSalida(PrintWriter salida) {
        this.salida = salida;
    }

    //este método se ejecuta cuando se inicia el hilo
    public void run() {
        try (

                //se crean los flujos de entrada y salida para comunicarse con el cliente
                //PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);

                //salida = new PrintWriter(socket.getOutputStream(), true);

                BufferedReader entrada = new BufferedReader(
                        new InputStreamReader(socket.getInputStream())
                );
        ){
            String lectura = entrada.readLine();//se lee la primera línea recibida desde el cliente
            while (lectura != null) {// mientras haya líneas por leer
                System.out.println(lectura);// se muestra en consola la línea recibida
                String escritura = chatbot.getResponse(lectura);// se procesa la línea con el chatbot
                System.out.println(escritura);// se muestra en consola la respuesta del chatbot
                salida.println(escritura);// se envía la respuesta del chatbot al cliente /85956565****************
                lectura = entrada.readLine();// se lee la siguiente línea del cliente
            }

        }catch (IOException e){
            System.out.println(e);
        }
    }
}