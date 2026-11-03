<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fn" uri="jakarta.tags.functions"%>
<c:set var="editandoPaciente" value="${not empty paciente and paciente.idPaciente > 0}" />
<c:set var="pageTitle" value="${editandoPaciente ? 'Editar Paciente' : 'Registrar Paciente'} - NovaSalud V3.2.1" scope="request" />
<c:set var="moduloActivo" value="pacientes" scope="request" />
<!DOCTYPE html>
<html lang="es">
<%@ include file="/WEB-INF/views/layout/head.jspf" %>
<body class="bg-light">
<nav class="navbar navbar-expand-lg navbar-dark bg-success shadow-sm">
  <div class="container-fluid px-4">
    <button class="btn btn-outline-light d-lg-none me-2 mobile-menu-toggle" type="button" data-bs-toggle="offcanvas" data-bs-target="#sidebarMenu" aria-label="Abrir menú"><i class="bi bi-list fs-4"></i></button>
    <span class="navbar-brand fw-semibold"><i class="bi bi-hospital me-2"></i>NovaSalud V3.2.1</span>
    <div class="d-flex flex-column flex-md-row align-items-md-center gap-2 ms-auto"><span class="text-white small">Módulo: Pacientes</span><%@ include file="/WEB-INF/views/layout/usuario-sesion.jspf" %></div>
  </div>
</nav>
<%@ include file="/WEB-INF/views/layout/menu-mobile.jspf" %>
<main class="container-fluid py-4 px-4">
  <div class="row g-4">
    <div class="col-12 col-lg-3"><%@ include file="/WEB-INF/views/layout/menu-right.jspf" %></div>
    <div class="col-12 col-lg-9">
      <div class="card border-0 shadow-sm">
        <div class="card-header bg-success text-white d-flex justify-content-between align-items-center">
          <h5 class="mb-0"><i class="bi bi-person-plus"></i> ${editandoPaciente ? 'Editar Paciente' : 'Registrar Paciente'}</h5>
          <a href="${pageContext.request.contextPath}/pacientes" class="btn btn-light btn-sm"><i class="bi bi-arrow-left"></i> Volver</a>
        </div>
        <div class="card-body">
          <c:if test="${not empty error}"><div class="alert alert-danger"><c:out value="${error}" /></div></c:if>
          <div class="alert alert-light border mb-3">
            <i class="bi bi-journal-medical"></i>
            La historia clínica se genera con el DNI del paciente para mantener un identificador único y fácil de ubicar.
          </div>
          <form method="post" action="${pageContext.request.contextPath}/pacientes" class="row g-3">
            <%@ include file="/WEB-INF/views/layout/csrf-token.jspf" %>
            <input type="hidden" name="idPaciente" value="${empty paciente.idPaciente ? 0 : paciente.idPaciente}">
            <div class="col-md-3">
              <label class="form-label">DNI *</label>
              <input type="text" class="form-control" id="dniPaciente" name="dni" maxlength="15" required value="${fn:escapeXml(paciente.dni)}">
              <div class="form-text">También será el número de historia clínica.</div>
            </div>
            <div class="col-md-3">
              <label class="form-label">Historia clínica</label>
              <input type="text" class="form-control" id="historiaClinicaPreview" value="${fn:escapeXml(editandoPaciente ? paciente.historiaClinicaCodigo : paciente.dni)}" readonly placeholder="Se generará con el DNI">
            </div>
            <div class="col-md-3"><label class="form-label">Nombres *</label><input type="text" class="form-control" name="nombres" maxlength="100" required value="${fn:escapeXml(paciente.nombres)}"></div>
            <div class="col-md-3"><label class="form-label">Apellidos *</label><input type="text" class="form-control" name="apellidos" maxlength="100" required value="${fn:escapeXml(paciente.apellidos)}"></div>
            <div class="col-md-3"><label class="form-label">Sexo</label><select class="form-select" name="sexo"><option value="">Seleccione</option><option value="F" ${paciente.sexo=='F'?'selected':''}>Femenino</option><option value="M" ${paciente.sexo=='M'?'selected':''}>Masculino</option></select></div>
            <div class="col-md-3"><label class="form-label">Fecha de nacimiento</label><input type="date" class="form-control" name="fechaNacimiento" value="${fn:escapeXml(paciente.fechaNacimiento)}"></div>
            <div class="col-md-3"><label class="form-label">Teléfono</label><input type="text" class="form-control" name="telefono" maxlength="20" value="${fn:escapeXml(paciente.telefono)}"></div>
            <div class="col-md-3"><label class="form-label">Correo</label><input type="email" class="form-control" name="correo" maxlength="100" value="${fn:escapeXml(paciente.correo)}"></div>
            <div class="col-md-6"><label class="form-label">Dirección</label><input type="text" class="form-control" name="direccion" maxlength="255" value="${fn:escapeXml(paciente.direccion)}"></div>
            <div class="col-md-3"><label class="form-label">Nombre contacto de emergencia</label><input type="text" class="form-control" name="contactoEmergencia" maxlength="150" value="${fn:escapeXml(paciente.contactoEmergencia)}"></div>
            <div class="col-md-3"><label class="form-label">Teléfono contacto de emergencia</label><input type="text" class="form-control" name="telefonoEmergencia" maxlength="20" value="${fn:escapeXml(paciente.telefonoEmergencia)}"></div>
            <div class="col-12 d-flex justify-content-end gap-2">
              <a href="${pageContext.request.contextPath}/pacientes" class="btn btn-outline-secondary">Cancelar</a>
              <button type="submit" class="btn btn-success"><i class="bi bi-save"></i> Guardar</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</main>
<script>
(function () {
  const dni = document.getElementById('dniPaciente');
  const hc = document.getElementById('historiaClinicaPreview');
  if (dni && hc) {
    dni.addEventListener('input', function () { hc.value = dni.value.trim(); });
  }
})();
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
