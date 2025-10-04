package com.juanpirir.gestioninventarios.service;

import com.juanpirir.gestioninventarios.model.Producto;
import com.juanpirir.gestioninventarios.repository.ProductoRepository;

import java.util.List;

public class ProductoService {

    private ProductoRepository repo = new ProductoRepository();

    public List<Producto> obtenerProductos() {
        return repo.listarTodos();
    }

    public void crearProducto(Producto p) throws Exception {
        if (p.getNombre() == null || p.getNombre().isBlank()) {
            throw new Exception("El nombre del producto es obligatorio");
        }
        if (p.getPrecio() < 0 || p.getStockActual() < 0) {
            throw new Exception("Precio y stock no pueden ser negativos");
        }
        repo.guardar(p);
    }
}
