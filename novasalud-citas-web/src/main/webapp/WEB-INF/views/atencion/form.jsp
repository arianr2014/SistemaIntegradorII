<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fn" uri="jakarta.tags.functions"%>
<c:set var="pageTitle" value="Atención médica - NovaSalud V3.2.1" scope="request" />
<c:set var="moduloActivo" value="agenda" scope="request" />
<c:set var="estadoAgenda" value="${fn:toUpperCase(cita.estado)}" />
<!DOCTYPE html>
<html lang="es">
<%@ include file="/WEB-INF/views/layout/head.jspf" %>
<body class="bg-light">
<nav class="navbar navbar-expand-lg navbar-dark bg-success shadow-sm">
    <div class="container-fluid px-4">
        <button class="btn btn-outline-light d-lg-none me-2 mobile-menu-toggle" type="button" data-bs-toggle="offcanvas" data-bs-target="#sidebarMenu" aria-label="Abrir menú"><i class="bi bi-list fs-4"></i></button>
        <span class="navbar-brand fw-semibold"><i class="bi bi-hospital me-2"></i>NovaSalud V3.2.1</span>
        <div class="d-flex flex-column flex-md-row align-items-md-center gap-2 ms-auto"><span class="text-white small">Módulo: Atención médica</span><%@ include file="/WEB-INF/views/layout/usuario-sesion.jspf" %></div>
    </div>
</nav>
<%@ include file="/WEB-INF/views/layout/menu-mobile.jspf" %>
<main class="container-fluid py-4 px-4">
  <div class="row g-4">
    <div class="col-12 col-lg-3"><%@ include file="/WEB-INF/views/layout/menu-right.jspf" %></div>
    <div class="col-12 col-lg-9">

      <div class="d-flex justify-content-between align-items-center mb-3">
        <h4 class="fw-bold text-success mb-0"><span class="page-title-icon"><i class="bi bi-clipboard2-pulse"></i></span> Atención médica del paciente</h4>
        <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/agenda-medico?buscar=1&fecha=${fn:escapeXml(cita.fecha)}"><i class="bi bi-arrow-left"></i> Volver a mi agenda</a>
      </div>

      <c:if test="${param.msg == 'saved'}"><div class="alert alert-success"><i class="bi bi-check-circle"></i> Registro clínico guardado correctamente. Puede editarlo nuevamente o marcar la cita como atendida.</div></c:if>
      <c:if test="${not empty error}"><div class="alert alert-danger"><i class="bi bi-exclamation-triangle"></i> <c:out value="${error}" /></div></c:if>

      <div class="card premium-card border-0 mb-3">
        <div class="card-body">
          <div class="row g-3">
            <div class="col-md-3"><strong>Historia clínica</strong><div class="badge badge-soft-info fs-6"><c:out value="${cita.historiaClinicaCodigo}" /></div></div>
            <div class="col-md-3"><strong>DNI</strong><div><c:out value="${cita.pacienteDni}" /></div></div>
            <div class="col-md-6"><strong>Paciente</strong><div><c:out value="${cita.pacienteNombreCompleto}" /></div></div>
            <div class="col-md-3"><strong>Fecha</strong><div><c:out value="${cita.fecha}" /> <c:out value="${cita.hora}" /></div></div>
            <div class="col-md-3"><strong>Especialidad</strong><div><c:out value="${cita.especialidad}" /></div></div>
            <div class="col-md-3"><strong>Pago</strong><div><span class="badge ${cita.estadoPago == 'PAGADO' ? 'badge-soft-success' : 'badge-soft-warning'}"><c:out value="${cita.estadoPago}" /></span></div></div>
            <div class="col-md-3"><strong>Estado cita</strong><div><span class="badge ${estadoAgenda == 'ATENDIDA' ? 'bg-primary' : 'bg-success'}"><c:out value="${cita.estado}" /></span></div></div>
          </div>
        </div>
      </div>

      <c:choose>
        <c:when test="${cita.estadoPago != 'PAGADO'}">
          <div class="alert alert-warning"><i class="bi bi-cash-coin"></i> La cita aún no está pagada. Caja debe completar el pago antes de registrar atención médica.</div>
        </c:when>
        <c:otherwise>
          <form method="post" action="${pageContext.request.contextPath}/atencion-medica" class="card premium-card border-0">
            <%@ include file="/WEB-INF/views/layout/csrf-token.jspf" %>
            <input type="hidden" name="idCita" value="${cita.idCita}">
            <input type="hidden" name="fecha" value="${fn:escapeXml(cita.fecha)}">
            <input type="hidden" name="codigoHistoria" value="${fn:escapeXml(cita.historiaClinicaCodigo)}">
            <div class="card-body">
              <div class="alert alert-light border">
                <i class="bi bi-info-circle"></i> Registre primero los síntomas, diagnóstico, tratamiento y receta. Después de guardar puede cerrar la atención con <strong>Guardar y marcar atendida</strong>.
              </div>
              <div class="row g-3">
                <div class="col-md-6">
                  <label class="form-label">Motivo de consulta *</label>
                  <textarea name="motivoConsulta" class="form-control" rows="3" maxlength="255" required>${fn:escapeXml(atencion.motivoConsulta)}</textarea>
                </div>
                <div class="col-md-6">
                  <label class="form-label">Síntomas del paciente *</label>
                  <textarea name="sintomas" class="form-control" rows="3" maxlength="700" required>${fn:escapeXml(atencion.sintomas)}</textarea>
                </div>
                <div class="col-md-6">
                  <label class="form-label">Diagnóstico *</label>
                  <textarea name="diagnostico" class="form-control" rows="4" maxlength="700" required>${fn:escapeXml(atencion.diagnostico)}</textarea>
                </div>
                <div class="col-md-6">
                  <label class="form-label">Tratamiento indicado *</label>
                  <textarea name="tratamiento" class="form-control" rows="4" maxlength="700" required>${fn:escapeXml(atencion.tratamiento)}</textarea>
                </div>
                <div class="col-md-6">
                  <label class="form-label">Receta médica</label>
                  <textarea name="recetaMedica" class="form-control" rows="4" maxlength="700" placeholder="Medicamentos, dosis, frecuencia y duración.">${fn:escapeXml(atencion.recetaMedica)}</textarea>
                </div>
                <div class="col-md-6">
                  <label class="form-label">Indicaciones y recomendaciones</label>
                  <textarea name="indicaciones" class="form-control" rows="4" maxlength="700" placeholder="Reposo, exámenes, controles, señales de alarma.">${fn:escapeXml(atencion.indicaciones)}</textarea>
                </div>
              </div>
              <div class="d-flex flex-column flex-md-row justify-content-end gap-2 mt-4">
                <button type="submit" name="accion" value="guardar" class="btn btn-outline-success"><i class="bi bi-save"></i> Guardar atención</button>
                <button type="submit" name="accion" value="guardar_atender" class="btn btn-success" ${estadoAgenda == 'ATENDIDA' ? 'disabled' : ''}><i class="bi bi-check2-square"></i> Guardar y marcar atendida</button>
              </div>
            </div>
          </form>
              
              
        </c:otherwise>
      </c:choose>

    </div>
  </div>
</main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
