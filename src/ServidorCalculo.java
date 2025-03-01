import java.io.*;
import java.net.*;

public class ServidorCalculo {
    public static void main(String[] args) {
        int puerto = 6000;
        String ipOperacion1 = "localhost"; // IP del ServidorOperacion1
        String ipOperacion2 = "localhost"; // IP del ServidorOperacion2
        int puertoOp1 = 6001;
        int puertoOp2 = 6002;

        try (ServerSocket servidor = new ServerSocket(puerto, 50, InetAddress.getByName("0.0.0.0"))) {
            System.out.println("Servidor de Cálculo esperando conexiones en el puerto " + puerto);

            while (true) {
                try (Socket socketCliente = servidor.accept();
                     BufferedReader entradaCliente = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
                     PrintWriter salidaCliente = new PrintWriter(socketCliente.getOutputStream(), true)) {

                    // Leer primera línea: números
                    String numeros = entradaCliente.readLine();
                    if (numeros == null || numeros.isEmpty()) {
                        salidaCliente.println("Error: Datos vacíos recibidos.");
                        continue;
                    }
                    String[] partesNum = numeros.split(",");
                    if (partesNum.length != 3) {
                        salidaCliente.println("Error: Formato incorrecto en números.");
                        continue;
                    }

                    // Leer segunda línea: operadores
                    String operadores = entradaCliente.readLine();
                    if (operadores == null || operadores.isEmpty()) {
                        salidaCliente.println("Error: Datos vacíos en operadores.");
                        continue;
                    }
                    String[] partesOp = operadores.split(",");
                    if (partesOp.length != 2) {
                        salidaCliente.println("Error: Formato incorrecto en operadores.");
                        continue;
                    }

                    // Convertir los valores numéricos
                    float num1 = Float.parseFloat(partesNum[0]);
                    float num2 = Float.parseFloat(partesNum[1]);
                    float num3 = Float.parseFloat(partesNum[2]);
                    String operador1 = partesOp[0];
                    String operador2 = partesOp[1];

                    Float resultadoIntermedio = null;
                    Float resultadoFinal = null;

                    // Intentar conexión con ServidorOperacion1
                    resultadoIntermedio = realizarOperacionRemota(ipOperacion1, puertoOp1, num1, num2, operador1);
                    if (resultadoIntermedio == null) {
                        System.out.println("Servidor 1 no disponible. Realizando operación local.");
                        resultadoIntermedio = calcular(num1, num2, operador1);
                        System.out.println("El resultado intermedio de la operacion es: " + resultadoIntermedio+"Enviando dato al servidor 2");
                    } else System.out.println("El resultado intermedio de la operacion es: " + resultadoIntermedio+"Enviando dato al servidor 2");

                    // Intentar conexión con ServidorOperacion2
                    resultadoFinal = realizarOperacionRemota(ipOperacion2, puertoOp2, resultadoIntermedio, num3, operador2);
                    if (resultadoFinal == null) {
                        System.out.println("Servidor 2 no disponible. Realizando operación local.");
                        resultadoFinal = calcular(resultadoIntermedio, num3, operador2);
                    }

                    // Enviar el resultado al cliente
                    salidaCliente.println(resultadoFinal);
                    System.out.println("Resultado final enviado al cliente: " + resultadoFinal);
                }
            }
        } catch (IOException e) {
            System.out.println("Error en el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Intenta realizar una operación con un servidor remoto.
     * Si no se puede conectar, devuelve null.
     */
    private static Float realizarOperacionRemota(String ip, int puerto, float num1, float num2, String operador) {
        try (Socket socket = new Socket(ip, puerto);
             PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Enviar números
            salida.println(num1 + "," + num2);
            // Enviar operador
            salida.println(operador);

            // Recibir resultado
            String respuesta = entrada.readLine();
            if (respuesta == null || respuesta.equals("Error")) {
                return null;
            }

            return Float.parseFloat(respuesta);
        } catch (IOException e) {
            return null; // Si hay error de conexión, retornar null
        }
    }

    /**
     * Realiza una operación local si un servidor de operación está caído.
     */
    private static Float calcular(float num1, float num2, String operador) {
        switch (operador) {
            case "+": return num1 + num2;
            case "-": return num1 - num2;
            case "*": return num1 * num2;
            case "/": return num2 != 0 ? num1 / num2 : null;
            default: return null;
        }
    }
}

