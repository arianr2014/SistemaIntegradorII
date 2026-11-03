<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fn" uri="jakarta.tags.functions"%>
<c:set var="pageTitle" value="Tarifas - NovaSalud V3.2.1" scope="request" />
<!DOCTYPE html>
<html lang="es">
<%@ include file="/WEB-INF/views/layout/head.jspf" %>
<body class="bg-light">
<nav class="navbar navbar-expand-lg navbar-dark bg-success shadow-sm">
    <div class="container-fluid px-4">
        <button class="btn btn-outline-light d-lg-none me-2 mobile-menu-toggle" type="button" data-bs-toggle="offcanvas" data-bs-target="#sidebarMenu" aria-controls="sidebarMenu" aria-label="Abrir menu"><i class="bi bi-list fs-4"></i></button>
        <span class="navbar-brand fw-semibold"><i class="bi bi-hospital me-2"></i>NovaSalud V3.2.1</span>
        <div class="d-flex flex-column flex-md-row align-items-md-center gap-2 ms-auto"><span class="text-white small">Módulo: Tarifas</span><%@ include file="/WEB-INF/views/layout/usuario-sesion.jspf" %></div>
    </div>
</nav>
<%@ include file="/WEB-INF/views/layout/menu-mobile.jspf" %>
<main class="container-fluid py-4 px-4">
  <div class="row g-4">
    <div class="col-12 col-lg-3 order-1 order-lg-1"><%@ include file="/WEB-INF/views/layout/menu-right.jspf" %></div>
    <div class="col-12 col-lg-9 order-2 order-lg-2">

<div class="card mb-4 border-0 shadow-sm" style="border-radius: 12px; background: linear-gradient(135deg, #1e3a8a 0%, #3b82f6 100%); color: #ffffff;">
    <div class="card-body p-4 d-flex justify-content-between align-items-center flex-wrap gap-3">
        <div>
            <h3 class="fw-bold mb-1"><i class="bi bi-cash-stack me-2"></i> Tarifas de Consulta</h3>
            <p class="mb-0 text-white-50">Define los costos vigentes por especialidad, vigencias y aranceles institucionales.</p>
        </div>
        <div class="d-flex align-items-center gap-2">
            <span class="badge bg-white text-primary fs-6 px-3 py-2 rounded-pill shadow-sm d-none d-md-inline-block">
                <i class="bi bi-coin me-1"></i> Aranceles Vigentes
            </span>
            <a href="${pageContext.request.contextPath}/tarifas?accion=form" class="btn btn-light text-primary fw-bold px-3 py-2 rounded-pill shadow-sm">
                <i class="bi bi-plus-lg me-1"></i> Nueva Tarifa
            </a>
        </div>
    </div>
</div>
<div class="card premium-card border-0"><div class="table-responsive"><table class="table table-hover align-middle mb-0"><thead class="table-green"><tr><th>Especialidad</th><th>Tarifa</th><th>Monto</th><th>Vigencia</th><th>Estado</th><th></th></tr></thead><tbody><c:forEach var="t" items="${tarifas}"><tr><td><c:out value="${t.especialidad}"/></td><td><c:out value="${t.nombreTarifa}"/></td><td class="fw-bold">S/ <c:out value="${t.monto}"/></td><td><c:out value="${t.vigenciaDesde}"/> - <c:out value="${empty t.vigenciaHasta ? 'Actual' : t.vigenciaHasta}"/></td><td><span class="badge ${t.estadoRegistro == 'ACTIVO' ? 'badge-soft-success' : 'badge-soft-danger'}"><c:out value="${t.estadoRegistro}"/></span></td><td class="text-end"><a class="btn btn-sm btn-outline-primary" href="${pageContext.request.contextPath}/tarifas?accion=editar&id=${t.idTarifa}"><i class="bi bi-pencil"></i></a></td></tr></c:forEach><c:if test="${empty tarifas}"><tr><td colspan="6" class="text-center text-muted py-4">No hay tarifas registradas.</td></tr></c:if></tbody></table></div></div>

    </div>
  </div>
</main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>