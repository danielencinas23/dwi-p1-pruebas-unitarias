package com.dwi.p1.service;

import com.dwi.p1.model.Producto;
import com.dwi.p1.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    public Producto create(Producto producto) {
        // El id lo genera la base de datos; uno recibido sobrescribiria un registro existente
        producto.setId(null);
        return productoRepository.save(producto);
    }
}
