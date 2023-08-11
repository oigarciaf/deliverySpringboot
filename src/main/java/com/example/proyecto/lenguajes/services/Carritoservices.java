package com.example.proyecto.lenguajes.services;

public interface Carritoservices {
	//public void realizarPago(Integer carritoId);

	public void eliminarProductoDelCarrito(Integer carritoId, Integer productoId);
	 public void realizarPago(Integer carritoId) throws InvalidPaymentException;
}
