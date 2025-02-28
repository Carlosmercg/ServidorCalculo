import java.io.*;
import java.net.*;

public class ServidorCalculo {
    public static void main(String[] args) {
        int puerto = 6000;
        String resultadoFinal;
        String resultadoIntermedio;
        String ipServidorOp2 = "localhost";  // Cambia esto por la IP real del servidor de operaciones 2
        int puertoOp2 = 6002;

        try (ServerSocket servidor = new ServerSocket(puerto, 50, InetAddress.getByName("0.0.0.0"))) {
            System.out.println("Servidor de Cálculo esperando conexiones en el puerto " + puerto);

            while (true) {
                try (Socket socketCliente = servidor.accept();
                     BufferedReader entradaCliente = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
                     PrintWriter salidaCliente = new PrintWriter(socketCliente.getOutputStream(), true)) {

                    // Recibir los datos del cliente
                    String datos = entradaCliente.readLine();
                    String operadores = entradaCliente.readLine();

                    if (datos == null || operadores == null) {
                        System.out.println("Error: datos nulos recibidos del cliente.");
                        continue;
                    }

                    System.out.println("Servidor de Cálculo recibió: " + datos);
                    System.out.println("Operadores recibidos: " + operadores);

                    String[] numeros = datos.split(",");
                    String[] opera = operadores.split(",");

                    if (numeros.length != 3 || opera.length != 2) {
                        System.out.println("Error: formato de datos incorrecto.");
                        continue;
                    }

                    float num1 = Float.parseFloat(numeros[0]);
                    float num2 = Float.parseFloat(numeros[1]);
                    float num3 = Float.parseFloat(numeros[2]);
                    String operador1 = opera[0];
                    String operador2 = opera[1];

                    // Conectar con el Servidor de Operación 1
                    String ipServidorOp1 = "localhost";  // Cambia esto por la IP real del servidor de operaciones 1
                    int puertoOp1 = 6001;

                    try (Socket socketOp1 = new Socket(ipServidorOp1, puertoOp1);
                         PrintWriter salidaOp1 = new PrintWriter(socketOp1.getOutputStream(), true);
                         BufferedReader entradaOp1 = new BufferedReader(new InputStreamReader(socketOp1.getInputStream()))) {

                        // Enviar datos al Servidor de Operación 1
                        salidaOp1.println(num1 + "," + num2);
                        salidaOp1.println(operador1);

                        // Recibir resultado intermedio
                        resultadoIntermedio = entradaOp1.readLine();
                        System.out.println("Resultado intermedio recibido: " + resultadoIntermedio);

                        // Conectar con el Servidor de Operación 2
                        try (Socket socketOp2 = new Socket(ipServidorOp2, puertoOp2);
                             PrintWriter salidaOp2 = new PrintWriter(socketOp2.getOutputStream(), true);
                             BufferedReader entradaOp2 = new BufferedReader(new InputStreamReader(socketOp2.getInputStream()))) {

                            // Enviar datos al Servidor de Operación 2
                            salidaOp2.println(resultadoIntermedio + "," + num3);
                            salidaOp2.println(operador2);

                            // Recibir resultado final
                            resultadoFinal = entradaOp2.readLine();
                            System.out.println("Resultado final recibido del servidor de operaciones 2: " + resultadoFinal);

                            // Enviar el resultado final al cliente
                            salidaCliente.println(resultadoFinal);
                            System.out.println("Resultado final enviado al cliente: " + resultadoFinal);
                        } catch (Exception e) {

                            Float resultado2 = calcular(Float.parseFloat(resultadoIntermedio), num3, operador2);
                            if (resultado2 == null) {
                                System.out.println("Error: Operador inválido.");
                            } else {
                                System.out.println("Servidor 1: " + resultadoIntermedio + " " + operador2 + " " + num3 + " = " + resultado2);
                                resultadoFinal = String.valueOf(resultado2);
                                salidaCliente.println(resultadoFinal);
                            }
                        }   // Realizar la operación
                    } catch (Exception e) {
                        System.out.println("Error en la conexión con el cliente: " + e.getMessage());
                        // Realizar la operación
                        Float resultado = calcular(num1, num2, operador1);
                        if (resultado == null) {
                            System.out.println("Error: Operador inválido.");
                        } else {
                            System.out.println("Servidor 1: " + num1 + " " + operador1 + " " + num2 + " = " + resultado);
                            resultadoIntermedio = String.valueOf(resultado);
                            Socket socketOp2 = new Socket(ipServidorOp2, puertoOp2);
                            BufferedReader entradaOp2 = new BufferedReader(new InputStreamReader(socketOp2.getInputStream()));
                            PrintWriter salidaOp2 = new PrintWriter(socketOp2.getOutputStream(), true);
                            salidaOp2.println(resultadoIntermedio + "," + num3);
                            salidaOp2.println(operador2);
                            // Recibir resultado final
                            resultadoFinal = entradaOp2.readLine();
                            System.out.println("Resultado final recibido del servidor de operaciones 2: " + resultadoFinal);
                            // Enviar el resultado final al cliente
                            salidaCliente.println(resultadoFinal);
                            System.out.println("Resultado final enviado al cliente: " + resultadoFinal);

                        }
                    }
                }
            }
        }catch(IOException e){
            System.out.println("Error al iniciar el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private static Float calcular(float num1, float num2, String operador){
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
