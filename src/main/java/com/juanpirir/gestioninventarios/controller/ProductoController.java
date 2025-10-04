package com.juanpirir.gestioninventarios.controller;

import com.juanpirir.gestioninventarios.model.Producto;
import com.juanpirir.gestioninventarios.service.ProductoService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/productos")
public class ProductoController extends HttpServlet {

    private ProductoService service = new ProductoService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Producto> productos = service.obtenerProductos();
        resp.setContentType("application/json");
        resp.getWriter().write(productos.toString()); // luego usar Gson/Jackson para JSON
    }
}
