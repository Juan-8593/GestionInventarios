package com.juanpirir.gestioninventarios.service;

import com.juanpirir.gestioninventarios.model.Categoria;
import com.juanpirir.gestioninventarios.repository.CategoriaRepository;

import java.util.List;

public class CategoriaService {

    private final CategoriaRepository categoriaRepo = new CategoriaRepository();

    public List<Categoria> listarTodos() {
        return categoriaRepo.findAll();
    }

    public Categoria buscarPorId(Long id) {
        return categoriaRepo.findById(id);
    }

    public void agregarCategoria(Categoria categoria) {
        if (categoria.getNombre() == null || categoria.getNombre().isEmpty()) {
            throw new RuntimeException("El nombre de la categoría es obligatorio");
        }
        categoriaRepo.save(categoria);
    }

    public void actualizarCategoria(Categoria categoria) {
        if (categoria.getNombre() == null || categoria.getNombre().isEmpty()) {
            throw new RuntimeException("El nombre de la categoría es obligatorio");
        }
        categoriaRepo.save(categoria);
    }

    public void eliminarCategoria(Long id) {
        categoriaRepo.delete(id);
    }
}
