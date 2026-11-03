<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fn" uri="jakarta.tags.functions"%>
<c:set var="pageTitle" value="Formulario Tarifa - NovaSalud V3.2.1" scope="request" />
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

<c:set var="editando" value="${not empty tarifa and tarifa.idTarifa > 0}" />
<h4 class="fw-bold text-success mb-3"><span class="page-title-icon"><i class="bi bi-cash-stack"></i></span> ${editando ? 'Editar tarifa' : 'Nueva tarifa'}</h4>
<form method="post" action="${pageContext.request.contextPath}/tarifas" class="card premium-card border-0"><%@ include file="/WEB-INF/views/layout/csrf-token.jspf" %><div class="card-body row g-3"><input type="hidden" name="idTarifa" value="${empty tarifa.idTarifa ? 0 : tarifa.idTarifa}"><div class="col-md-6"><label class="form-label">Especialidad</label><select name="idEspecialidad" class="form-select" required><option value="0">Seleccione</option><c:forEach var="e" items="${especialidades}"><option value="${e.idEspecialidad}" ${tarifa.idEspecialidad == e.idEspecialidad ? 'selected' : ''}><c:out value="${e.nombre}"/></option></c:forEach></select></div><div class="col-md-6"><label class="form-label">Nombre tarifa</label><input name="nombreTarifa" class="form-control" value="${fn:escapeXml(tarifa.nombreTarifa)}" placeholder="Consulta médica"></div><div class="col-md-4"><label class="form-label">Monto</label><input type="number" step="0.01" min="1" name="monto" class="form-control" value="${tarifa.monto}" required></div><div class="col-md-4"><label class="form-label">Moneda</label><input name="moneda" class="form-control" value="${empty tarifa.moneda ? 'PEN' : tarifa.moneda}" required></div><div class="col-md-4"><label class="form-label">Estado</label><select name="estadoRegistro" class="form-select"><option value="ACTIVO" ${tarifa.estadoRegistro != 'INACTIVO' ? 'selected' : ''}>ACTIVO</option><option value="INACTIVO" ${tarifa.estadoRegistro == 'INACTIVO' ? 'selected' : ''}>INACTIVO</option></select></div><div class="col-md-6"><label class="form-label">Vigencia desde</label><input type="date" name="vigenciaDesde" class="form-control" value="${fn:escapeXml(tarifa.vigenciaDesde)}" required></div><div class="col-md-6"><label class="form-label">Vigencia hasta</label><input type="date" name="vigenciaHasta" class="form-control" value="${fn:escapeXml(tarifa.vigenciaHasta)}"></div><div class="col-12 d-flex justify-content-between"><a href="${pageContext.request.contextPath}/tarifas" class="btn btn-outline-secondary">Volver</a><button class="btn btn-success"><i class="bi bi-save"></i> Guardar tarifa</button></div></div></form>

    </div>
  </div>
</main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>