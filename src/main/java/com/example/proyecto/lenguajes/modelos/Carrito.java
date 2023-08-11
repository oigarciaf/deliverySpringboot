package com.example.proyecto.lenguajes.modelos;

import java.time.LocalDate;
import java.util.List;
import javax.persistence.JoinColumn;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="carrito")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Carrito {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @OneToOne
    private Usuario usuario;
    
    @OneToMany(mappedBy = "orden")
    private List<DetalleOrden> detalles;

    
    private String metodoPago; // "tarjeta" o "paypal"
    
    // Detalles para tarjeta de crédito
    private String numeroTarjeta;
    private String ccv;
    private LocalDate fechaVencimiento;
    
    // Detalles para PayPal
    private String correoPaypal;
    
    private String estado; 
    
    @ManyToMany
    @JoinTable(name = "carrito_producto",
               joinColumns = @JoinColumn(name = "carrito_id"),
               inverseJoinColumns = @JoinColumn(name = "producto_id"))
    private List<Producto> productos; 


}
