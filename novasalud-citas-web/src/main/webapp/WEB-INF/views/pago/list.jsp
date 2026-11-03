<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fn" uri="jakarta.tags.functions"%>
<c:set var="pageTitle" value="Pagos y Caja - NovaSalud V3.2.1" scope="request" />
<c:set var="moduloActivo" value="pagos" scope="request" />
<!DOCTYPE html>
<html lang="es">
<%@ include file="/WEB-INF/views/layout/head.jspf" %>
<body class="bg-light">
<nav class="navbar navbar-expand-lg navbar-dark bg-success shadow-sm">
    <div class="container-fluid px-4">
        <button class="btn btn-outline-light d-lg-none me-2 mobile-menu-toggle" type="button" data-bs-toggle="offcanvas" data-bs-target="#sidebarMenu" aria-controls="sidebarMenu" aria-label="Abrir menú"><i class="bi bi-list fs-4"></i></button>
        <span class="navbar-brand fw-semibold"><i class="bi bi-hospital me-2"></i>NovaSalud V3.2.1</span>
        <div class="d-flex flex-column flex-md-row align-items-md-center gap-2 ms-auto"><span class="text-white small">Módulo: Pagos / Caja</span><%@ include file="/WEB-INF/views/layout/usuario-sesion.jspf" %></div>
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
            <h3 class="fw-bold mb-1"><i class="bi bi-cash-coin me-2"></i> Pagos y Caja</h3>
            <p class="mb-0 text-white-50">Control de cobros, recaudaciones, pagos de citas y comprobantes de atención.</p>
        </div>
        <div>
            <span class="badge bg-white text-primary fs-6 px-3 py-2 rounded-pill shadow-sm">
                <i class="bi bi-wallet2 me-1"></i> Caja Operativa
            </span>
        </div>
    </div>
</div>

<c:choose>
    <c:when test="${param.msg == 'paid'}"><div class="alert alert-success"><i class="bi bi-check-circle"></i> <strong>Pago completado correctamente.</strong> La cita ya puede pasar a consulta.</div></c:when>
    <c:when test="${param.msg == 'partial'}"><div class="alert alert-info"><i class="bi bi-wallet2"></i> <strong>Pago parcial registrado.</strong> El saldo pendiente queda visible en caja.</div></c:when>
    <c:when test="${param.msg == 'annulled'}"><div class="alert alert-warning"><i class="bi bi-exclamation-triangle"></i> <strong>Pago anulado correctamente.</strong></div></c:when>
    <c:when test="${param.msg == 'invalid'}"><div class="alert alert-danger"><i class="bi bi-x-circle"></i> <strong>No se pudo registrar el pago.</strong> Verifique monto, saldo pendiente y número de operación.</div></c:when>
    <c:when test="${param.msg == 'error'}"><div class="alert alert-danger"><i class="bi bi-x-circle"></i> <strong>Error inesperado.</strong> Intente nuevamente o revise los datos enviados.</div></c:when>
</c:choose>

<form method="get" action="${pageContext.request.contextPath}/pagos" class="card premium-card border-0 mb-3">
    <div class="card-body row g-2 align-items-end">
        <div class="col-md-3">
            <label class="form-label">DNI del paciente</label>
            <input class="form-control" name="dniPaciente" value="${fn:escapeXml(dniPaciente)}" placeholder="Buscar por DNI">
            <div class="form-text">Puede buscar por DNI aunque no recuerde la fecha de cita.</div>
        </div>
        <div class="col-md-2"><label class="form-label">Desde</label><input type="date" class="form-control" name="fechaInicio" value="${fechaInicio}"></div>
        <div class="col-md-2"><label class="form-label">Hasta</label><input type="date" class="form-control" name="fechaFin" value="${fechaFin}"></div>
        <div class="col-md-3">
            <label class="form-label">Estado</label>
            <select class="form-select" name="estadoPago">
                <option value="">Todos</option>
                <option value="PENDIENTE" ${estadoPago=='PENDIENTE'?'selected':''}>Pendiente</option>
                <option value="PARCIAL" ${estadoPago=='PARCIAL'?'selected':''}>Parcial</option>
                <option value="PAGADO" ${estadoPago=='PAGADO'?'selected':''}>Pagado</option>
                <option value="ANULADO" ${estadoPago=='ANULADO'?'selected':''}>Anulado</option>
            </select>
        </div>
        <div class="col-md-2"><button class="btn btn-outline-success w-100"><i class="bi bi-filter"></i> Filtrar</button></div>
    </div>
</form>

<div class="card premium-card border-0 mb-3">
  <div class="card-body">
    <div class="d-flex justify-content-between align-items-center mb-2">
      <h6 class="fw-bold text-success mb-0"><i class="bi bi-tags"></i> Tarifario rápido</h6>
      <small class="text-muted">Referencia de caja vigente</small>
    </div>
    <div class="table-responsive">
      <table class="table table-sm align-middle mb-0">
        <thead><tr><th>Especialidad</th><th>Precio consulta</th><th>Moneda</th><th>Estado</th></tr></thead>
        <tbody>
          <c:forEach var="t" items="${tarifasActivas}">
            <tr><td class="fw-semibold"><c:out value="${t.especialidad}" /></td><td>S/ <c:out value="${t.monto}" /></td><td><c:out value="${t.moneda}" /></td><td><span class="badge badge-soft-success">Vigente</span></td></tr>
          </c:forEach>
        </tbody>
      </table>
    </div>
  </div>
</div>

<div class="card premium-card border-0">
  <div class="table-responsive">
    <table class="table table-hover align-middle mb-0">
      <thead class="table-green"><tr><th>Cita</th><th>Paciente</th><th>DNI</th><th>Especialidad</th><th>Fecha</th><th>Total</th><th>Pagado</th><th>Saldo</th><th>Estado</th><th></th></tr></thead>
      <tbody>
        <c:forEach var="p" items="${pagos}">
          <tr>
            <td>#<c:out value="${p.idCita}"/><br><small class="text-muted"><c:out value="${p.codigoPago}"/></small></td>
            <td><c:out value="${p.paciente}"/><br><small class="text-muted"><c:out value="${p.medico}"/></small></td>
            <td><c:out value="${p.pacienteDni}"/></td>
            <td><c:out value="${p.especialidad}"/></td>
            <td><c:out value="${p.fechaCita}"/> <small class="text-muted"><c:out value="${p.horaCita}"/></small></td>
            <td>S/ <c:out value="${p.montoTotal}"/></td>
            <td>S/ <c:out value="${p.montoPagado}"/></td>
            <td>S/ <c:out value="${p.saldoPendiente}"/></td>
            <td><span class="badge ${p.estadoPago=='PAGADO'?'badge-soft-success':(p.estadoPago=='ANULADO'?'badge-soft-danger':(p.estadoPago=='PARCIAL'?'badge-soft-info':'badge-soft-warning'))}"><c:out value="${p.estadoPago}"/></span></td>
            <td class="text-end">
              <c:choose>
                <c:when test="${p.estadoPago=='PENDIENTE' || p.estadoPago=='PARCIAL'}"><a class="btn btn-sm btn-success" href="${pageContext.request.contextPath}/pagos?accion=cobrar&id=${p.idPago}"><i class="bi bi-wallet2"></i> Cobrar</a></c:when>
                <c:when test="${p.estadoPago=='PAGADO'}"><a class="btn btn-sm btn-outline-primary" href="${pageContext.request.contextPath}/pagos?accion=comprobante&id=${p.idPago}"><i class="bi bi-printer"></i> Comprobante</a></c:when>
                <c:otherwise><span class="text-muted small">Sin acción</span></c:otherwise>
              </c:choose>
            </td>
          </tr>
        </c:forEach>
        <c:if test="${empty pagos}"><tr><td colspan="10" class="text-center text-muted py-4">No hay pagos para el filtro seleccionado.</td></tr></c:if>
      </tbody>
    </table>
  </div>
</div>

    </div>
  </div>
</main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
