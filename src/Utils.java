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
}
