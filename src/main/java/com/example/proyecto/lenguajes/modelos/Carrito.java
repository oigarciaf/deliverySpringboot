package com.example.proyecto.lenguajes.modelos;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    
    @OneToMany(mappedBy = "carrito")
    private List<DetalleOrden> detalles;
    
    private String metodoPago; // "tarjeta" o "paypal"
    
    // Detalles para tarjeta de crédito
    private String numeroTarjeta;
    private String ccv;
    private LocalDate fechaVencimiento;
    
    // Detalles para PayPal
    private String correoPaypal;
    
    private String estado; 
}
