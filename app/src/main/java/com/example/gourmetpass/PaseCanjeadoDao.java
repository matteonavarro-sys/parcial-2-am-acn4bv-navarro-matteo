package com.example.gourmetpass;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface PaseCanjeadoDao {

    @Insert
    void insertar(PaseCanjeado pase);

    @Query("SELECT * FROM pases_canjeados ORDER BY id DESC")
    List<PaseCanjeado> obtenerTodos();

    @Query("DELETE FROM pases_canjeados")
    void borrarTodos();
}
