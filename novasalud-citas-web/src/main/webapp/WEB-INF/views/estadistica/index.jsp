<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fn" uri="jakarta.tags.functions"%>
<c:set var="pageTitle" value="Estadísticas clínicas - NovaSalud V3.2.1" scope="request" />
<!DOCTYPE html>
<html lang="es">
<%@ include file="/WEB-INF/views/layout/head.jspf" %>
<body class="bg-light">
<nav class="navbar navbar-expand-lg navbar-dark bg-success shadow-sm">
    <div class="container-fluid px-4">
        <button class="btn btn-outline-light d-lg-none me-2 mobile-menu-toggle" type="button" data-bs-toggle="offcanvas" data-bs-target="#sidebarMenu" aria-controls="sidebarMenu" aria-label="Abrir menu"><i class="bi bi-list fs-4"></i></button>
        <span class="navbar-brand fw-semibold"><i class="bi bi-hospital me-2"></i>NovaSalud V3.2.1</span>
        <div class="d-flex flex-column flex-md-row align-items-md-center gap-2 ms-auto"><span class="text-white small">Módulo: Estadísticas</span><%@ include file="/WEB-INF/views/layout/usuario-sesion.jspf" %></div>
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
            <h3 class="fw-bold mb-1"><i class="bi bi-bar-chart-line me-2"></i> Estadísticas Clínicas</h3>
            <p class="mb-0 text-white-50">Métricas de demanda por especialidad, volumen semanal y rendimiento operativo.</p>
        </div>
        <div>
            <span class="badge bg-white text-primary fs-6 px-3 py-2 rounded-pill shadow-sm">
                <i class="bi bi-graph-up me-1"></i> Indicadores Clínicos
            </span>
        </div>
    </div>
</div>
<form method="get" action="${pageContext.request.contextPath}/estadisticas" class="card premium-card border-0 mb-3"><div class="card-body row g-2 align-items-end"><div class="col-md-4"><label class="form-label">Desde</label><input type="date" class="form-control" name="fechaInicio" value="${fechaInicio}"></div><div class="col-md-4"><label class="form-label">Hasta</label><input type="date" class="form-control" name="fechaFin" value="${fechaFin}"></div><div class="col-md-4"><button class="btn btn-outline-success w-100"><i class="bi bi-search"></i> Consultar</button></div></div></form>
<div class="row g-3"><div class="col-lg-6"><div class="card premium-card border-0"><div class="card-header bg-success text-white">Especialidades con más demanda</div><div class="table-responsive"><table class="table mb-0"><tbody><c:forEach var="e" items="${topEspecialidades}"><tr><td><c:out value="${e.key}"/></td><td class="text-end"><span class="badge bg-primary"><c:out value="${e.value}"/> citas</span></td></tr></c:forEach></tbody></table></div></div></div><div class="col-lg-6"><div class="card premium-card border-0"><div class="card-header bg-success text-white">Atenciones / citas por día</div><div class="table-responsive"><table class="table mb-0"><tbody><c:forEach var="d" items="${resumenSemanal}"><tr><td><c:out value="${d.key}"/></td><td class="text-end"><span class="badge badge-soft-success"><c:out value="${d.value}"/></span></td></tr></c:forEach></tbody></table></div></div></div></div>

    </div>
  </div>
</main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>