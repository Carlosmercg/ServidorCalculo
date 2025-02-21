import java.io.*;
import java.net.*;

public class ServidorCalculo {
    public static void main(String[] args) {
        int puerto = 6000;

        try (ServerSocket servidor = new ServerSocket(puerto)) {
            System.out.println("Servidor de Calculo esperando conexiones en el puerto " + puerto);

            while (true) {
                Socket socketCliente = servidor.accept();
                BufferedReader entradaCliente = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
                PrintWriter salidaCliente = new PrintWriter(socketCliente.getOutputStream(), true);

                // Conectar con el Servidor de Operación 1
                Socket socketOp1 = new Socket("localhost", 6001);
                PrintWriter salidaOp1 = new PrintWriter(socketOp1.getOutputStream(), true);
                BufferedReader entradaOp1 = new BufferedReader(new InputStreamReader(socketOp1.getInputStream()));
                // Recibir los tres números del cliente

                String datos = entradaCliente.readLine();
                String operadores = entradaCliente.readLine();
                String[] numeros = datos.split(",");

                System.out.println("Servidor de Calculo recibio: " + datos);
                System.out.println("Operadores recibidos: " + operadores);

                float num1 = Float.parseFloat(numeros[0]);
                float num2 = Float.parseFloat(numeros[1]);
                float num3 = Float.parseFloat(numeros[2]);

                String[] opera = operadores.split(",");
                String operador1 = opera[0];
                String operador2 = opera[1];

                // Enviar los primeros dos números al Servidor de Operación 1
                salidaOp1.println(num1 + "," + num2);
                salidaOp1.println(operador1);
                String resultadoIntermedio = entradaOp1.readLine();
                socketOp1.close();

                // Conectar con el Servidor de Operación 2
                Socket socketOp2 = new Socket("localhost", 6002);
                PrintWriter salidaOp2 = new PrintWriter(socketOp2.getOutputStream(), true);
                BufferedReader entradaOp2 = new BufferedReader(new InputStreamReader(socketOp2.getInputStream()));

                // Enviar el resultado intermedio y el tercer número al Servidor de Operación 2
                salidaOp2.println(resultadoIntermedio + "," + num3);
                salidaOp2.println(operador2);

                String resultadoFinal = entradaOp2.readLine();
                socketOp2.close();

                // Enviar el resultado final al cliente
                salidaCliente.println(resultadoFinal);
                System.out.println("Resultado final enviado al cliente: " + resultadoFinal);

                socketCliente.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
