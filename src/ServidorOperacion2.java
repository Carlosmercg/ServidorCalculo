import java.io.*;
import java.net.*;

public class ServidorOperacion2 {
    public static void main(String[] args) {
        int puerto = 6002;

        try (ServerSocket servidor = new ServerSocket(puerto)) {
            System.out.println("Servidor de Operacion 2 esperando conexiones en el puerto " + puerto);

            while (true) {
                Socket socket = servidor.accept();
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
                float resultadoFinal = 0;

                // Recibir el resultado intermedio y el tercer número
                String[] datos = entrada.readLine().split(",");
                if(datos[0].equals("Error")){
                    salida.println("Error en los datos ingresados");
                    System.out.println("Error en los datos ingresados");
                    continue;
                }
                float resultadoIntermedio = Float.parseFloat(datos[0]);
                float num3 = Float.parseFloat(datos[1]);

                String operador = entrada.readLine();


                // Realizar la segunda operacion
                if(operador.equals("+")) {

                    resultadoFinal = resultadoIntermedio + num3;
                    System.out.println("Servidor 2: " + resultadoIntermedio + " + " + num3 + " = " + resultadoFinal);
                }

                if(operador.equals("-")) {

                    resultadoFinal = resultadoIntermedio - num3;
                    System.out.println("Servidor 2: " + resultadoIntermedio + " - " + num3 + " = " + resultadoFinal);
                }

                if(operador.equals("*")) {

                    resultadoFinal = resultadoIntermedio * num3;
                    System.out.println("Servidor 2: " + resultadoIntermedio + " * " + num3 + " = " + resultadoFinal);
                }

                if(operador.equals("/")) {
                    if (num3 == 0) {
                        salida.println("Error por division entre 0");
                        System.out.println("Error por dividion entre 0");
                        continue;
                    }
                    resultadoFinal = resultadoIntermedio / num3;
                    System.out.println("Servidor 2: " + resultadoIntermedio + " / " + num3 + " = " + resultadoFinal);
                }

                // Enviar el resultado final al Servidor de Cálculo
                salida.println(resultadoFinal);

                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
