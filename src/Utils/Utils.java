package Utils;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;

public class Utils {
    public static void log(String mensaje) {
        // 1. Obtenemos el historial de llamadas actual
        StackTraceElement[] pila = Thread.currentThread().getStackTrace();

        // 2. Buscamos al "culpable". 
        // pila[0] es la llamada interna de Java (getStackTrace)
        // pila[1] es esta misma función (Debug.log)
        // pila[2] es LA FUNCIÓN QUE NOS HA LLAMADO
        if (pila.length >= 3) {
            StackTraceElement llamador = pila[2];
            
            // Extraemos los datos útiles
            String claseCompleta = llamador.getClassName();
            String metodo = llamador.getMethodName();
            int linea = llamador.getLineNumber();
            String archivo = llamador.getFileName(); // Ej: "Ventana.java"

            // Limpiamos el nombre de la clase (quita los paquetes como "UI.Ventana" -> "Ventana")
            String claseSimple = claseCompleta.substring(claseCompleta.lastIndexOf('.') + 1);

            // 3. Formateamos el mensaje
            // Truco Pro: Si lo formateas como "(Archivo.java:Linea)", tu editor 
            // lo convertirá en un enlace en el que puedes hacer clic en la terminal.
            System.out.println("[DEBUG] " + claseSimple + "." + metodo + "() " + 
                               "(" + archivo + ":" + linea + ") -> " + mensaje);
        } else {
            // Por si acaso falla la lectura de la pila
            System.out.println("[DEBUG] -> " + mensaje);
        }
    }

    public static String calcularHash(Path rutaArchivo) {
        try {
            // 1. Elegimos el algoritmo (SHA-256 es el más seguro y estándar actual)
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            
            // 2. Leemos todo el contenido del archivo en bytes
            byte[] bytesDelArchivo = Files.readAllBytes(rutaArchivo);
            
            // 3. Generamos la huella dactilar (en formato binario)
            byte[] hashBinario = digest.digest(bytesDelArchivo);
            
            // 4. Lo convertimos a un texto hexadecimal (letras y números legibles)
            StringBuilder textoHex = new StringBuilder();
            for (byte b : hashBinario) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    textoHex.append('0');
                }
                textoHex.append(hex);
            }
            
            return textoHex.toString();
            
        } catch (Exception e) {
            System.err.println("Error al calcular el hash: " + e.getMessage());
            return null;
        }
    }
}
