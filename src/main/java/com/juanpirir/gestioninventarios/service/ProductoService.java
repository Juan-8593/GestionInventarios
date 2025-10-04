package com.juanpirir.gestioninventarios.service;

import com.juanpirir.gestioninventarios.model.Producto;
import com.juanpirir.gestioninventarios.repository.ProductoRepository;

import java.util.List;

public class ProductoService {

    private final ProductoRepository productoRepo = new ProductoRepository();
    private static final int STOCK_BAJO_DEFAULT = 5;

    public List<Producto> listarTodos() {
        return productoRepo.findAll();
    }

    public Producto buscarPorId(Long id) {
        return productoRepo.findById(id);
    }

    public void agregarProducto(Producto producto) {
        validarProducto(producto);
        producto.setActivo(true);
        productoRepo.save(producto);
    }

    public void actualizarProducto(Producto producto) {
        validarProducto(producto);
        productoRepo.save(producto);
    }

    public void eliminarProducto(Long id) {
        productoRepo.delete(id);
    }

    public long contarStockBajo() {
        return productoRepo.findAll().stream().filter(p -> p.getStockActual() < STOCK_BAJO_DEFAULT).count();
    }

    private void validarProducto(Producto p) {
        if (p.getNombre() == null || p.getNombre().isEmpty()) {
            throw new RuntimeException("El nombre del producto es obligatorio");
        }
        if (p.getPrecio() < 0) {
            throw new RuntimeException("El precio no puede ser negativo");
        }
        if (p.getStockActual() < 0) {
            throw new RuntimeException("El stock no puede ser negativo");
        }
    }
}
