package com.example.gourmetpass;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pases_canjeados")
public class PaseCanjeado {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String descripcion;
    public String usuario;

    public PaseCanjeado(String descripcion, String usuario) {
        this.descripcion = descripcion;
        this.usuario = usuario;
    }
}
