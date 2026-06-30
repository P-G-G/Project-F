package Utils;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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

    public static String calcularHash(String rutaArchivo) {
        return calcularHash(Path.of(rutaArchivo));
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

    public static void dormir(long milis) {
        try {
            Thread.sleep(milis); 
        } catch (InterruptedException e) {
            System.err.println("El hilo fue interrumpido: " + e.getMessage());
        }
    }

    public static String cambiarFormatoFecha(String fecha, String formatoActual, String formatoNuevo) {
        // 1. Definimos el formato en el que VIENE la fecha 
        // Por ejemplo, el que sueles usar en tu interfaz: Día/Mes/Año
        DateTimeFormatter formatoEntrada = DateTimeFormatter.ofPattern(formatoActual);
        
        // 2. Definimos el formato en el que QUEREMOS la fecha
        // Por ejemplo, el estándar ideal para ordenar fechas en SQLite: Año-Mes-Día
        DateTimeFormatter formatoSalida = DateTimeFormatter.ofPattern(formatoNuevo);
        
        try {
            // Paso A: Traducimos el texto a un objeto LocalDate real
            LocalDate fechaReal = LocalDate.parse(fecha, formatoEntrada);
            
            // Paso B: Convertimos el objeto real de vuelta a texto con el nuevo formato
            return fechaReal.format(formatoSalida);
            
        } catch (DateTimeParseException e) {
            System.err.println("El texto introducido no coincide con el formato de entrada: " + e.getMessage());
            return fecha; // Si falla, devolvemos lo original o null, según prefieras
        }
    }
}
