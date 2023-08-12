package com.example.proyecto.lenguajes.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.proyecto.lenguajes.modelos.Carrito;
import com.example.proyecto.lenguajes.modelos.DetalleOrden;
import com.example.proyecto.lenguajes.modelos.Orden;
import com.example.proyecto.lenguajes.modelos.Producto;
import com.example.proyecto.lenguajes.modelos.Usuario;
import com.example.proyecto.lenguajes.repositories.UsuarioRepository;
import com.example.proyecto.lenguajes.services.CarritoService;
import com.example.proyecto.lenguajes.services.InvalidPaymentException;
import com.example.proyecto.lenguajes.services.impl.DetalleOrdenServiceImpl;
import com.example.proyecto.lenguajes.services.impl.OrdenServiceImpl;
import com.example.proyecto.lenguajes.services.impl.ProductoServiceImpl;


@RestController
@RequestMapping("/cart")
public class CarritoController {
	
	// para almacenar los detalles de la orden
	List<DetalleOrden> detalles = new ArrayList<DetalleOrden>();
	
	// datos de la orden
		Orden orden = new Orden();
	
	@Autowired
	private OrdenServiceImpl ordenserviceimpl;
	
	
	@Autowired
	private ProductoServiceImpl productoServiceimpl;
	
	@Autowired
	private DetalleOrdenServiceImpl detalleordenserviseimpl;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private final CarritoService carritoService;
	
	@Autowired
    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @PostMapping("/agregar")
    public ResponseEntity<String> agregarAlCarrito(@RequestBody DetalleOrden detalle) {
    	detalleordenserviseimpl.save(detalle);
        return ResponseEntity.ok("Producto agregado al carrito exitosamente.");
    }
    
    
    

	@PostMapping("/{cantidad}/{id}")
	public String addCart(@RequestParam Integer id, @RequestParam Integer cantidad) {
		
		
		
		
		DetalleOrden detalleOrden = new DetalleOrden();

		Producto producto = new Producto();
		
		double sumaTotal = 0;
		
		Optional<Producto> optionalProducto = productoServiceimpl.get(id);
		
		producto = optionalProducto.get();

		detalleOrden.setCantidad(cantidad);
		detalleOrden.setPrecio(producto.getPrecio());
		detalleOrden.setNombre(producto.getNombre());
		detalleOrden.setTotal(producto.getPrecio() * cantidad);
		detalleOrden.setProducto(producto);
		detalleordenserviseimpl.save(detalleOrden);
		
		//validar que le producto no se añada 2 veces
		Integer idProducto=producto.getId();
		boolean ingresado=detalles.stream().anyMatch(p -> p.getProducto().getId()==idProducto);   
		
		if (!ingresado) {
			detalles.add(detalleOrden);
		}
		sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();
		
		orden.setTotal(sumaTotal);
		ordenserviceimpl.save(orden);
		return "agregado exitosamente";
		
	}
	
	
	
	
	@DeleteMapping("/eliminar/carrito/{id}")
    public ResponseEntity<String> eliminarProductoCarrito(@PathVariable Integer id) {
        try {
            detalleordenserviseimpl.eliminarProductoDelCarrito(id);
            return new ResponseEntity<>("Producto eliminado del carrito exitosamente", HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Producto no encontrado en el carrito", HttpStatus.NOT_FOUND);
        }
    }
    
    
    /**esto fue lo que hice **/

	

    @PostMapping("/agregar-producto")
    public ResponseEntity<String> agregarProductoAlCarrito(
            @RequestParam Integer productoId,
            @RequestParam double cantidad) {
    	Usuario usuarioAutenticado = obtenerUsuarioAutenticado();
        Carrito carrito = usuarioAutenticado.getCarrito();
        carritoService.agregarProductoAlCarrito(carrito.getId(), productoId, cantidad);

        return ResponseEntity.ok("Producto agregado al carrito.");
    }
    
    private Usuario obtenerUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        
        return usuarioRepository.findByUsername(username);
    }

    
    @PostMapping("/realizar-pago")
    public ResponseEntity<String> realizarPago(
            @RequestParam Integer carritoId) {
        try {
            carritoService.realizarPago(carritoId);
            return ResponseEntity.ok("Pago realizado correctamente.");
        } catch (InvalidPaymentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }
    
    @DeleteMapping("/eliminar-producto")
    public ResponseEntity<String> eliminarProductoDelCarrito(
            @RequestParam Integer carritoId,
            @RequestParam Integer productoId) {
        carritoService.eliminarProductoDelCarrito(carritoId, productoId);
        return ResponseEntity.ok("Producto eliminado del carrito.");
    }
}
