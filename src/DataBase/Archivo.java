package DataBase;

import java.io.File;
import java.util.Date;

public record Archivo(File info, String tipo, Date fecha, Familiar familiar) { }
