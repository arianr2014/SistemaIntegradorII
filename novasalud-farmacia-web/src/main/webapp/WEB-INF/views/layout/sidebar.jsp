<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<aside class="sidebar">
    <div class="sidebar-header">
        <div class="sidebar-logo">
            <i class="fa-solid fa-pills"></i>
        </div>
        <div class="sidebar-brand">
            NovaSalud <span style="font-weight: 400; font-size: 12px; display: block; color: #10b981;">FARMACIA</span>
        </div>
    </div>

    <ul class="sidebar-menu">
        <li class="sidebar-label">Principal</li>
        <li class="sidebar-item">
            <a href="${pageContext.request.contextPath}/home" class="sidebar-link ${param.active == 'dashboard' ? 'active' : ''}">
                <i class="fa-solid fa-chart-line"></i> Dashboard
            </a>
        </li>
        <li class="sidebar-item">
            <a href="${pageContext.request.contextPath}/ventas?mode=pos" class="sidebar-link ${param.active == 'pos' ? 'active' : ''}">
                <i class="fa-solid fa-cash-register"></i> Punto de Venta (POS)
            </a>
        </li>

        <li class="sidebar-label">Gestión de Productos</li>
        <li class="sidebar-item">
            <a href="${pageContext.request.contextPath}/medicamentos" class="sidebar-link ${param.active == 'medicamentos' ? 'active' : ''}">
                <i class="fa-solid fa-capsules"></i> Medicamentos
            </a>
        </li>
        <li class="sidebar-item">
            <a href="${pageContext.request.contextPath}/categorias" class="sidebar-link ${param.active == 'categorias' ? 'active' : ''}">
                <i class="fa-solid fa-tags"></i> Categorías
            </a>
        </li>
        <li class="sidebar-item">
            <a href="${pageContext.request.contextPath}/lotes" class="sidebar-link ${param.active == 'lotes' ? 'active' : ''}">
                <i class="fa-solid fa-boxes-stacked"></i> Lotes
            </a>
        </li>
        <li class="sidebar-item">
            <a href="${pageContext.request.contextPath}/inventario" class="sidebar-link ${param.active == 'inventario' ? 'active' : ''}">
                <i class="fa-solid fa-warehouse"></i> Inventario & Stock
            </a>
        </li>

        <li class="sidebar-label">Operaciones & Salud</li>
        <li class="sidebar-item">
            <a href="${pageContext.request.contextPath}/ventas" class="sidebar-link ${param.active == 'ventas' ? 'active' : ''}">
                <i class="fa-solid fa-receipt"></i> Historial de Ventas
            </a>
        </li>
        <li class="sidebar-item">
            <a href="${pageContext.request.contextPath}/prescripciones" class="sidebar-link ${param.active == 'prescripciones' ? 'active' : ''}">
                <i class="fa-solid fa-file-prescription"></i> Prescripciones
            </a>
        </li>
        <li class="sidebar-item">
            <a href="${pageContext.request.contextPath}/proveedores" class="sidebar-link ${param.active == 'proveedores' ? 'active' : ''}">
                <i class="fa-solid fa-truck-field"></i> Proveedores
            </a>
        </li>

        <c:if test="${sessionScope.usuarioLogueado.rol == 'ADMINISTRADOR'}">
            <li class="sidebar-label">Administración</li>
            <li class="sidebar-item">
                <a href="${pageContext.request.contextPath}/usuarios" class="sidebar-link ${param.active == 'usuarios' ? 'active' : ''}">
                    <i class="fa-solid fa-users-gear"></i> Usuarios
                </a>
            </li>
        </c:if>

        <li class="sidebar-label" style="margin-top: 20px;">Cuenta</li>
        <li class="sidebar-item">
            <a href="${pageContext.request.contextPath}/logout" class="sidebar-link" style="color: #f87171;">
                <i class="fa-solid fa-right-from-bracket"></i> Cerrar Sesión
            </a>
        </li>
    </ul>
</aside>
