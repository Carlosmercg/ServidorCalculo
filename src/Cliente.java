import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        String servidor = "localhost";
        int puerto = 6000;

        try (Socket socket = new Socket(servidor, puerto);
             PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
             PrintWriter salida2 = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            // Pedir los tres números al usuario
            System.out.print("Ingrese el primer numero: ");
            String num1 = scanner.nextLine();

            System.out.print("Ingrese el segundo numero: ");
            String num2 = scanner.nextLine();

            System.out.print("Ingrese el tercer numero: ");
            String num3 = scanner.nextLine();

            System.out.print("Ingrese el operador de la 1era operacion: ");
            String op1 = scanner.nextLine();

            System.out.print("Ingrese el operador de la 2nda operacion: ");
            String op2 = scanner.nextLine();

            // Enviar los números al Servidor de Cálculo
            String numeros = num1 + "," + num2 + "," + num3;
            System.out.println("Cliente envia: " + numeros);
            String operadores = op1 + "," + op2;
            System.out.println("El cliente va a operar con: "+op1+" y "+op2);
            salida.println(numeros);
            salida2.println(operadores);


            // Recibir resultado final
            String resultado = entrada.readLine();
            System.out.println("Resultado final recibido del servidor: " + resultado);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}