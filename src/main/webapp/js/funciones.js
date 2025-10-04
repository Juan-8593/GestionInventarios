document.addEventListener("DOMContentLoaded", () => {
    const tabla = document.querySelector("#tablaProductos tbody");
    const form = document.querySelector("#formProducto");
    let productos = JSON.parse(localStorage.getItem("productos")) || [];

    function cargarProductos() {
        tabla.innerHTML = "";
        productos.forEach(p => {
            const fila = document.createElement("tr");
            fila.innerHTML = `
                <td>${p.id}</td>
                <td>${p.nombre}</td>
                <td>${p.descripcion}</td>
                <td>${p.precio.toFixed(2)}</td>
                <td>${p.stock}</td>
            `;
            tabla.appendChild(fila);
        });
    }

    if(form){
        form.addEventListener("submit", e => {
            e.preventDefault();
            const data = Object.fromEntries(new FormData(form));
            data.id = productos.length + 1;
            data.precio = parseFloat(data.precio);
            data.stock = parseInt(data.stock);
            productos.push(data);
            localStorage.setItem("productos", JSON.stringify(productos));
            form.reset();
            cargarProductos();
        });
    }

    cargarProductos();
});
