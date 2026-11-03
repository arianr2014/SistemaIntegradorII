<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fn" uri="jakarta.tags.functions"%>
<c:set var="pageTitle" value="Cobrar cita - NovaSalud V3.2.1" scope="request" />
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

<h4 class="fw-bold text-success mb-3"><span class="page-title-icon"><i class="bi bi-wallet2"></i></span> Registrar pago</h4>
<c:if test="${empty pago}"><div class="alert alert-warning">No se encontró el pago solicitado.</div></c:if>
<c:if test="${not empty pago}">
  <div class="card premium-card border-0 mb-3"><div class="card-body"><h5 class="fw-bold mb-2">Cita #<c:out value="${pago.idCita}"/> <small class="text-muted">(<c:out value="${pago.codigoPago}"/>)</small></h5><p class="mb-1"><strong>Paciente:</strong> <c:out value="${pago.paciente}"/> | <strong>DNI:</strong> <c:out value="${pago.pacienteDni}"/></p><p class="mb-1"><strong>Especialidad:</strong> <c:out value="${pago.especialidad}"/> | <strong>Médico:</strong> <c:out value="${pago.medico}"/></p><p class="mb-0"><strong>Total:</strong> S/ <c:out value="${pago.montoTotal}"/> | <strong>Pagado:</strong> S/ <c:out value="${pago.montoPagado}"/> | <strong>Saldo:</strong> S/ <c:out value="${pago.saldoPendiente}"/></p></div></div>
  <c:choose>
    <c:when test="${pago.estadoPago == 'PAGADO'}">
      <div class="alert alert-success"><i class="bi bi-check-circle"></i> Este pago ya está completo. Puede emitir el comprobante.</div>
      <a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/pagos?accion=comprobante&id=${pago.idPago}"><i class="bi bi-printer"></i> Ver comprobante</a>
    </c:when>
    <c:otherwise>
      <form method="post" action="${pageContext.request.contextPath}/pagos" class="card premium-card border-0"><%@ include file="/WEB-INF/views/layout/csrf-token.jspf" %><div class="card-body row g-3"><input type="hidden" name="idPago" value="${pago.idPago}"><div class="col-md-4"><label class="form-label">Monto a cobrar</label><input type="number" min="0.01" max="${pago.saldoPendiente}" step="0.01" name="monto" class="form-control" required><div class="form-text">Puede registrar pago parcial o completar el saldo pendiente.</div></div><div class="col-md-4"><label class="form-label">Método</label><select id="metodoPago" name="metodoPago" class="form-select" required onchange="validarOperacion()"><option value="EFECTIVO">Efectivo</option><option value="YAPE">Yape</option><option value="PLIN">Plin</option><option value="TARJETA">Tarjeta</option><option value="TRANSFERENCIA">Transferencia</option></select></div><div class="col-md-4"><label class="form-label">Nro. operación externo</label><input id="numeroOperacion" name="numeroOperacion" class="form-control"><div class="form-text">Obligatorio para Yape, Plin, tarjeta y transferencia. En efectivo es opcional.</div></div><div class="col-12"><label class="form-label">Observación</label><textarea name="observacion" class="form-control" rows="2"></textarea></div><div class="col-12 d-flex justify-content-between"><a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/pagos">Volver</a><button class="btn btn-success"><i class="bi bi-check2-circle"></i> Confirmar pago</button></div></div></form>
    </c:otherwise>
  </c:choose>
</c:if>

    </div>
  </div>
</main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
function validarOperacion(){
  const metodo = document.getElementById('metodoPago').value;
  const operacion = document.getElementById('numeroOperacion');
  const requiere = ['YAPE','PLIN','TARJETA','TRANSFERENCIA'].includes(metodo);
  operacion.required = requiere;
  operacion.placeholder = requiere ? 'Ingrese operación o voucher' : 'Opcional en efectivo';
}
validarOperacion();
</script>
</body>
</html>
