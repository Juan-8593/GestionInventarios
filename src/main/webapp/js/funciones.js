document.addEventListener("DOMContentLoaded", () => {
    const contextPath = window.location.pathname.split('/')[1]; // Ajusta el contexto
    const tabla = document.querySelector("#tablaProductos tbody");
    const form = document.querySelector("#formProducto");

    // Función para cargar productos en la tabla
    function cargarProductos() {
        fetch(`/${contextPath}/productos`)
            .then(resp => resp.json())
            .then(data => {
                if(!tabla) return;
                tabla.innerHTML = "";
                data.forEach(p => {
                    const fila = document.createElement("tr");
                    fila.innerHTML = `
                        <td>${p.id}</td>
                        <td>${p.nombre}</td>
                        <td>${p.descripcion}</td>
                        <td>${p.precio.toFixed(2)}</td>
                        <td>${p.stockActual}</td>
                    `;
                    tabla.appendChild(fila);
                });
            });
    }

    // Enviar nuevo producto
    if(form){
        form.addEventListener("submit", e => {
            e.preventDefault();
            const formData = new FormData(form);
            fetch(`/${contextPath}/productos`, {
                method: 'POST',
                body: new URLSearchParams(formData)
            })
                .then(resp => resp.text())
                .then(msg => {
                    alert(msg);
                    form.reset();
                    cargarProductos();
                });
        });
    }

    // Inicializar tabla
    cargarProductos();

    // Actualizar dashboard si existen los elementos
    fetch(`/${contextPath}/productos`)
        .then(resp => resp.json())
        .then(data => {
            const totalProductos = document.getElementById('totalProductos');
            const stockBajo = document.getElementById('stockBajo');
            const productosInactivos = document.getElementById('productosInactivos');
            const ultimaActualizacion = document.getElementById('ultimaActualizacion');

            if(totalProductos) totalProductos.textContent = data.length;
            if(stockBajo) stockBajo.textContent = data.filter(p => p.stockActual < 5).length;
            if(productosInactivos) productosInactivos.textContent = data.filter(p => !p.activo).length;
            if(ultimaActualizacion){
                const ultima = data.sort((a,b)=>new Date(b.fechaActualizacion)-new Date(a.fechaActualizacion))[0];
                ultimaActualizacion.textContent = ultima ? new Date(ultima.fechaActualizacion).toLocaleDateString() : '--/--/----';
            }
        });
});
