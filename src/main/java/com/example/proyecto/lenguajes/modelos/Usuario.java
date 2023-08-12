package com.example.proyecto.lenguajes.modelos;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Usuario {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String nombre;
	private String username;
	private String email;
	private String direccion;
	private String telefono;
	private String tipo;
	private String password;
    private String metodopago;
    @OneToOne(mappedBy = "usuario")
    private Carrito carrito;

    @OneToMany(mappedBy = "usuario")
	private List<Producto> productos;
	
	@OneToMany(mappedBy = "usuario")
	private List<Orden> ordenes;
	
	public Carrito getCarrito() {
	    return carrito;
	}

}