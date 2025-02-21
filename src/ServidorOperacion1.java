import java.io.*;
import java.net.*;

public class ServidorOperacion1 {
    public static void main(String[] args) {
        int puerto = 6001;

        try (ServerSocket servidor = new ServerSocket(puerto, 50, InetAddress.getByName("0.0.0.0"))) {
            System.out.println("Servidor de Operación 1 esperando conexiones en el puerto " + puerto);

            while (true) {
                try (Socket socket = servidor.accept();
                     BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     PrintWriter salida = new PrintWriter(socket.getOutputStream(), true)) {

                    // Recibir los dos números
                    String datosEntrada = entrada.readLine();
                    if (datosEntrada == null || datosEntrada.isEmpty()) {
                        System.out.println("Error: Datos vacíos recibidos.");
                        salida.println("Error");
                        continue;
                    }

                    String[] datos = datosEntrada.split(",");
                    if (datos.length != 2) {
                        System.out.println("Error: Formato incorrecto de entrada.");
                        salida.println("Error");
                        continue;
                    }

                    try {
                        float num1 = Float.parseFloat(datos[0]);
                        float num2 = Float.parseFloat(datos[1]);

                        // Recibir el operador
                        String operador = entrada.readLine();
                        if (operador == null || operador.isEmpty()) {
                            System.out.println("Error: Operador no recibido.");
                            salida.println("Error");
                            continue;
                        }

                        // Realizar la operación
                        Float resultado = calcular(num1, num2, operador);
                        if (resultado == null) {
                            System.out.println("Error: Operador inválido.");
                            salida.println("Error");
                        } else {
                            System.out.println("Servidor 1: " + num1 + " " + operador + " " + num2 + " = " + resultado);
                            salida.println(resultado);
                        }

                    } catch (NumberFormatException e) {
                        System.out.println("Error: Datos no numéricos recibidos.");
                        salida.println("Error");
                    }
                } catch (IOException e) {
                    System.out.println("Error en la conexión con el cliente: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Error al iniciar el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static Float calcular(float num1, float num2, String operador) {
        switch (operador) {
            case "+":
                return num1 + num2;
            case "-":
                return num1 - num2;
            case "*":
                return num1 * num2;
            case "/":
                if (num2 == 0) {
                    System.out.println("Error: División por cero.");
                    return null;
                }
                return num1 / num2;
            default:
                return null;  // Operador inválido
        }
    }
}
