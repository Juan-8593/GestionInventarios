package com.juanpirir.gestioninventarios.service;

import com.juanpirir.gestioninventarios.model.Movimiento;
import com.juanpirir.gestioninventarios.model.Producto;
import com.juanpirir.gestioninventarios.repository.MovimientoRepository;
import com.juanpirir.gestioninventarios.repository.ProductoRepository;

import java.time.LocalDateTime;
import java.util.List;

public class MovimientoService {

    private final MovimientoRepository movimientoRepo = new MovimientoRepository();
    private final ProductoRepository productoRepo = new ProductoRepository();

    public List<Movimiento> listarTodos() {
        return movimientoRepo.findAll();
    }

    public Movimiento buscarPorId(Long id) {
        return movimientoRepo.findById(id);
    }

    public void registrarMovimiento(Movimiento movimiento) {
        if (movimiento.getProducto() == null) {
            throw new RuntimeException("Debe seleccionar un producto");
        }
        if (movimiento.getCantidad() <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a 0");
        }
        Producto p = productoRepo.findById(movimiento.getProducto().getId());
        if (p == null) {
            throw new RuntimeException("Producto no encontrado");
        }

        // Reglas de negocio
        if (!p.isActivo() && "Salida".equalsIgnoreCase(movimiento.getTipo())) {
            throw new RuntimeException("No se puede registrar salida de un producto inactivo");
        }
        if ("Salida".equalsIgnoreCase(movimiento.getTipo()) && p.getStockActual() < movimiento.getCantidad()) {
            throw new RuntimeException("No hay stock suficiente para la salida");
        }

        // Actualizar stock
        if ("Entrada".equalsIgnoreCase(movimiento.getTipo())) {
            p.setStockActual(p.getStockActual() + movimiento.getCantidad());
        } else {
            p.setStockActual(p.getStockActual() - movimiento.getCantidad());
        }
        p.setFechaActualizacion(LocalDateTime.now());

        // Guardar
        productoRepo.save(p);
        movimiento.setFecha(LocalDateTime.now());
        movimientoRepo.save(movimiento);
    }

    public void eliminarMovimiento(Long id) {
        movimientoRepo.delete(id);
    }
}
