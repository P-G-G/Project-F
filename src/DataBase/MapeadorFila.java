package DataBase;

import java.sql.ResultSet;
import java.sql.SQLException;

// Interfaz funcional para mapear filas
public interface MapeadorFila<T> {
    T mapear(ResultSet rs) throws SQLException;
}
