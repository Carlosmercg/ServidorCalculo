import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        String servidor = "10.43.103.197"; // Reemplaza con la IP de la VM que ejecuta el servidor
        int puerto = 6000;

        try (Socket socket = new Socket(servidor, puerto);
             PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            // Pedir los números al usuario
            System.out.print("Ingrese el primer número: ");
            String num1 = scanner.nextLine();

            System.out.print("Ingrese el segundo número: ");
            String num2 = scanner.nextLine();

            System.out.print("Ingrese el tercer número: ");
            String num3 = scanner.nextLine();

            System.out.print("Ingrese el operador de la 1era operación: ");
            String op1 = scanner.nextLine();

            System.out.print("Ingrese el operador de la 2da operación: ");
            String op2 = scanner.nextLine();

            // Enviar números primero
            salida.println(num1 + "," + num2 + "," + num3);

            // Luego enviar operadores
            salida.println(op1 + "," + op2); 

            println("Cliente envia: ",num1," + ",num2," + ",num3,"  ",op1," ",op2);

            // Recibir resultado final
            String resultado = entrada.readLine();
            System.out.println("Resultado final recibido del servidor: " + resultado);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
