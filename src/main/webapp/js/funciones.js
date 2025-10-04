// Función para productos (similar para categorias y movimientos)
document.addEventListener("DOMContentLoaded", () => {
    const tabla = document.querySelector("#tablaProductos tbody");
    const form = document.querySelector("#formProducto");

    function cargarProductos() {
        fetch('productos')
            .then(resp=>resp.json())
            .then(data=>{
                if(!tabla) return;
                tabla.innerHTML="";
                data.forEach(p=>{
                    const fila=document.createElement("tr");
                    fila.innerHTML=`
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

    if(form){
        form.addEventListener("submit", e=>{
            e.preventDefault();
            const formData = new FormData(form);
            fetch('productos',{
                method:'POST',
                body:new URLSearchParams(formData)
            })
                .then(resp=>resp.text())
                .then(msg=>{
                    alert(msg);
                    form.reset();
                    cargarProductos();
                });
        });
    }

    cargarProductos();

    // Dashboard dinámico
    fetch('productos')
        .then(resp=>resp.json())
        .then(data=>{
            if(document.getElementById('totalProductos'))
                document.getElementById('totalProductos').textContent=data.length;
            if(document.getElementById('stockBajo'))
                document.getElementById('stockBajo').textContent=data.filter(p=>p.stockActual<5).length;
            if(document.getElementById('productosInactivos'))
                document.getElementById('productosInactivos').textContent=data.filter(p=>!p.activo).length;
            if(document.getElementById('ultimaActualizacion')){
                const ultima=data.sort((a,b)=>new Date(b.fechaActualizacion)-new Date(a.fechaActualizacion))[0];
                document.getElementById('ultimaActualizacion').textContent=ultima?new Date(ultima.fechaActualizacion).toLocaleDateString():'--/--/----';
            }
        });
});
