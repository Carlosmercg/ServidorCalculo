import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        String servidor = "10.43.103.30"; // Reemplaza con la IP del servidor
        int puerto = 6000;

        try (Socket socket = new Socket(servidor, puerto);
             PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            // Pedir los números al usuario y validar entrada
            float num1 = leerNumero(scanner, "Ingrese el primer número: ");
            float num2 = leerNumero(scanner, "Ingrese el segundo número: ");
            float num3 = leerNumero(scanner, "Ingrese el tercer número: ");

            String op1 = leerOperador(scanner, "Ingrese el operador de la 1era operación (+, -, *, /): ");
            String op2 = leerOperador(scanner, "Ingrese el operador de la 2da operación (+, -, *, /): ");

            // Enviar datos al servidor
            String datos = num1 + "," + num2 + "," + num3 + "," + op1 + "," + op2;
            System.out.println("Cliente envía: " + datos);
            salida.println(datos);

            // Recibir resultado final
            String resultado = entrada.readLine();
            System.out.println("Resultado final recibido del servidor: " + resultado);

        } catch (IOException e) {
            System.err.println("Error de conexión con el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método para leer y validar números
    private static float leerNumero(Scanner scanner, String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Float.parseFloat(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Error: Ingrese un número válido.");
            }
        }
    }

    // Método para leer y validar operadores
    private static String leerOperador(Scanner scanner, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String operador = scanner.nextLine().trim();
            if (operador.matches("[+\\-*/]")) { // Solo permite +, -, *, /
                return operador;
            }
            System.out.println("⚠️ Error: Operador inválido. Use solo +, -, * o /.");
        }
    }
}