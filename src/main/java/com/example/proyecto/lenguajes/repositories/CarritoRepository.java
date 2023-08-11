package com.example.proyecto.lenguajes.repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.proyecto.lenguajes.modelos.Carrito;

public interface CarritoRepository extends CrudRepository<Carrito, Integer>{
	
	public void realizarPago(Integer carritoId);

	public void eliminarProductoDelCarrito(Integer carritoId, Integer productoId);
}
