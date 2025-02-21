import java.io.*;
import java.net.*;

public class ServidorOperacion1 {
    public static void main(String[] args) {
        int puerto = 6001;

        try (ServerSocket servidor = new ServerSocket(puerto)) {
            System.out.println("Servidor de Operacion 1 esperando conexiones en el puerto " + puerto);

            while (true) {
                Socket socket = servidor.accept();
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);

                // Recibir los dos primeros números
                String[] datos = entrada.readLine().split(",");

                if(datos[0].equals("Error")){
                    salida.println("Error");
                    System.out.println("Error en los datos ingresados");
                    continue;
                }

                float num1 = Float.parseFloat(datos[0]);
                float num2 = Float.parseFloat(datos[1]);


                String operador = entrada.readLine();
                float resultado=0;
                // Realizar la primera suma
                if(operador.equals("+")) {
                    resultado = num1 + num2;
                    System.out.println("Servidor 1: " + num1 + " + " + num2 + " = " + resultado);
                }
                if(operador.equals("-")) {
                    resultado = num1 - num2;
                    System.out.println("Servidor 1: " + num1 + " - " + num2 + " = " + resultado);
                }
                if(operador.equals("*")) {
                    resultado = num1 * num2;
                    System.out.println("Servidor 1: " + num1 + " * " + num2 + " = " + resultado);
                }
                if(operador.equals("/")) {
                    if (num2 == 0) {
                        System.out.println("Error por dividion entre 0");
                        salida.println("Error");
                        continue;
                    }
                    resultado = num1 / num2;
                    System.out.println("Servidor 1: " + num1 + " / " + num2 + " = " + resultado);
                }

                // Enviar el resultado de la suma al Servidor de Cálculo
                salida.println(resultado);

                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
