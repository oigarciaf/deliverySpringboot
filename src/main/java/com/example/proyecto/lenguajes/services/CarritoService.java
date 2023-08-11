package com.example.proyecto.lenguajes.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.proyecto.lenguajes.modelos.Carrito;
import com.example.proyecto.lenguajes.modelos.DetalleOrden;
import com.example.proyecto.lenguajes.modelos.Producto;
import com.example.proyecto.lenguajes.repositories.CarritoRepository;
import com.example.proyecto.lenguajes.repositories.ProductoRepository;

@Service
public class CarritoService implements Carritoservices{
	
	//esto agregue
	@Autowired
	public CarritoRepository  carritoRepository;
	public ProductoRepository  productoRepository;
	//atta aqui
	private final DetalleOrdenService detalleOrdenService;
	private List<DetalleOrden> detalles = new ArrayList<>();

    @Autowired
    public CarritoService(DetalleOrdenService detalleOrdenService) {
        this.detalleOrdenService = detalleOrdenService;
    }

    public void agregarAlCarrito(DetalleOrden detalle) {
        int idProducto = detalle.getProducto().getId();
        boolean ingresado = detalles.stream().anyMatch(p -> p.getProducto().getId() == idProducto);

        if (!ingresado) {
            detalles.add(detalle);
            detalleOrdenService.save(detalle); // Guardar en la base de datos
        }
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////
    //aqui comienza lo que agregue
    public void agregarProductoAlCarrito(Integer carritoId, Integer productoId, double cantidad) {
        Carrito carrito = carritoRepository.findById(carritoId).orElseThrow();
        Producto producto = productoRepository.findById(productoId).orElseThrow();
        
        // Crea un nuevo detalle en el carrito con el producto y la cantidad
        DetalleOrden detalle = new DetalleOrden(producto, cantidad);
        carrito.getDetalles().add(detalle);

        carritoRepository.save(carrito);
    }
    
    //le agreger throws InvalidPaymentException  para manejar la exxion de la targeta 
    public void realizarPago(Integer carritoId) throws InvalidPaymentException  {
        Carrito carrito = carritoRepository.findById(carritoId).orElseThrow();

        if ("tarjeta".equals(carrito.getMetodoPago())) {
            LocalDate fechaActual = LocalDate.now();
            if (carrito.getFechaVencimiento().isBefore(fechaActual)) {
                throw new InvalidPaymentException("La tarjeta está vencida.");
            }
            
            // Procesa el pago con tarjeta
            procesarPagoConTarjeta(carrito);
        } else if ("paypal".equals(carrito.getMetodoPago())) {
            // Procesa el pago a través de PayPal
            procesarPagoConPaypal(carrito);
        } else {
            throw new InvalidPaymentException("Método de pago no válido.");
        }

        // Actualiza el estado del carrito o realiza otras acciones necesarias
        carrito.setEstado("Pagado");
        carritoRepository.save(carrito);
    }
    
    private void procesarPagoConTarjeta(Carrito carrito) {
        // Aquí simularemos una lógica de pago con tarjeta simple
        double total = calcularTotalCarrito(carrito);
        System.out.println("Pago con tarjeta realizado correctamente. Total: " + total);
    }
    
    private void procesarPagoConPaypal(Carrito carrito) {
        // Aquí simularemos una lógica de pago con PayPal simple
        double total = calcularTotalCarrito(carrito);
        System.out.println("Pago a través de PayPal realizado correctamente. Total: " + total);
    }
    
    private double calcularTotalCarrito(Carrito carrito) {
        double total = 0.0;
        for (DetalleOrden detalle : carrito.getDetalles()) {
            total += detalle.getProducto().getPrecio() * detalle.getCantidad();
        }
        return total;
    }
    
    public void eliminarProductoDelCarrito(Integer carritoId, Integer productoId) {
        Carrito carrito = carritoRepository.findById(carritoId).orElseThrow();
        DetalleOrden detalleEliminar = null;
        for (DetalleOrden detalle : carrito.getDetalles()) {
            if (detalle.getProducto().getId().equals(productoId)) {
                detalleEliminar = detalle;
                break;
            }
        }

        if (detalleEliminar != null) {
            carrito.getDetalles().remove(detalleEliminar);
            carritoRepository.save(carrito);
        }
    }
}
