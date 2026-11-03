<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/views/layout/header.jsp">
    <jsp:param name="title" value="Dashboard - NovaSalud Farmacia" />
    <jsp:param name="pageTitle" value="Dashboard de Control" />
    <jsp:param name="active" value="dashboard" />
</jsp:include>

<div class="stats-grid">
    <div class="stat-card">
        <div class="stat-info">
            <h3>Medicamentos</h3>
            <div class="stat-value">${totalMedicamentos != null ? totalMedicamentos : 0}</div>
        </div>
        <div class="stat-icon icon-emerald">
            <i class="fa-solid fa-capsules"></i>
        </div>
    </div>

    <div class="stat-card">
        <div class="stat-info">
            <h3>Categorías</h3>
            <div class="stat-value">${totalCategorias != null ? totalCategorias : 0}</div>
        </div>
        <div class="stat-icon icon-blue">
            <i class="fa-solid fa-tags"></i>
        </div>
    </div>

    <div class="stat-card">
        <div class="stat-info">
            <h3>Ventas Realizadas</h3>
            <div class="stat-value">${totalVentas != null ? totalVentas : 0}</div>
        </div>
        <div class="stat-icon icon-purple">
            <i class="fa-solid fa-receipt"></i>
        </div>
    </div>

    <div class="stat-card">
        <div class="stat-info">
            <h3>Ingresos Totales</h3>
            <div class="stat-value">S/ <fmt:formatNumber value="${totalIngresos != null ? totalIngresos : 0.00}" pattern="#,##0.00"/></div>
        </div>
        <div class="stat-icon icon-emerald">
            <i class="fa-solid fa-sack-dollar"></i>
        </div>
    </div>
</div>

<div style="display: grid; grid-template-columns: 2fr 1fr; gap: 24px;">
    <div class="table-card">
        <div class="table-header">
            <div class="table-title"><i class="fa-solid fa-capsules"></i> Últimos Medicamentos Registrados</div>
            <a href="${pageContext.request.contextPath}/medicamentos" class="btn btn-secondary btn-sm">Ver Todos</a>
        </div>
        <table class="data-table">
            <thead>
                <tr>
                    <th>Código</th>
                    <th>Nombre Comercial</th>
                    <th>Presentación</th>
                    <th>Precio</th>
                    <th>Receta</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${recientesMedicamentos}" var="m">
                    <tr>
                        <td><strong>${m.codigoBarras}</strong></td>
                        <td>${m.nombreComercial}</td>
                        <td>${m.presentacion} ${m.concentracion}</td>
                        <td>S/ ${m.precioVenta}</td>
                        <td>
                            <c:choose>
                                <c:when test="${m.requiereReceta}">
                                    <span class="badge badge-warning">Receta Médica</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge badge-success">Venta Libre</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <div class="glass-panel" style="padding: 24px;">
        <h3 style="font-size: 16px; font-weight: 700; margin-bottom: 16px;"><i class="fa-solid fa-bolt" style="color: #f59e0b;"></i> Accesos Rápido</h3>
        <div style="display: flex; flex-direction: column; gap: 12px;">
            <a href="${pageContext.request.contextPath}/ventas?mode=pos" class="btn btn-primary" style="justify-content: center; padding: 14px;">
                <i class="fa-solid fa-cash-register"></i> Abrir Punto de Venta (POS)
            </a>
            <a href="${pageContext.request.contextPath}/medicamentos" class="btn btn-secondary" style="justify-content: center; padding: 12px;">
                <i class="fa-solid fa-plus"></i> Registrar Medicamento
            </a>
            <a href="${pageContext.request.contextPath}/lotes" class="btn btn-secondary" style="justify-content: center; padding: 12px;">
                <i class="fa-solid fa-boxes-stacked"></i> Ingreso de Lote
            </a>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
