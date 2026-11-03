<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fn" uri="jakarta.tags.functions"%>
<c:set var="editando" value="${not empty usuario and usuario.idUsuario > 0}" />
<c:set var="pageTitle" value="${editando ? 'Editar usuario' : 'Nuevo usuario'} - NovaSalud V3.2.1" scope="request" />
<c:set var="moduloActivo" value="usuarios" scope="request" />
<!DOCTYPE html>
<html lang="es">
<%@ include file="/WEB-INF/views/layout/head.jspf" %>
<body class="bg-light">
<nav class="navbar navbar-expand-lg navbar-dark bg-success shadow-sm">
  <div class="container-fluid px-4">
    <button class="btn btn-outline-light d-lg-none me-2 mobile-menu-toggle" type="button" data-bs-toggle="offcanvas" data-bs-target="#sidebarMenu" aria-label="Abrir menú"><i class="bi bi-list fs-4"></i></button>
    <span class="navbar-brand fw-semibold"><i class="bi bi-hospital me-2"></i>NovaSalud V3.2.1</span>
    <div class="d-flex flex-column flex-md-row align-items-md-center gap-2 ms-auto"><span class="text-white small">Módulo: Usuarios</span><%@ include file="/WEB-INF/views/layout/usuario-sesion.jspf" %></div>
  </div>
</nav>
<%@ include file="/WEB-INF/views/layout/menu-mobile.jspf" %>
<main class="container-fluid py-4 px-4">
  <div class="row g-4">
    <div class="col-12 col-lg-3"><%@ include file="/WEB-INF/views/layout/menu-right.jspf" %></div>
    <div class="col-12 col-lg-9">
      <div class="d-flex justify-content-between align-items-center mb-3">
        <h4 class="fw-bold text-success mb-0"><span class="page-title-icon"><i class="bi bi-person-gear"></i></span> ${editando ? 'Editar usuario' : 'Nuevo usuario'}</h4>
        <a href="${pageContext.request.contextPath}/usuarios" class="btn btn-outline-secondary"><i class="bi bi-arrow-left"></i> Volver</a>
      </div>
      <c:if test="${param.msg == 'invalid'}"><div class="alert alert-danger"><i class="bi bi-x-circle"></i> Revise los datos. Si el rol es DOCTOR, debe asociar un médico real activo.</div></c:if>
      <form method="post" action="${pageContext.request.contextPath}/usuarios" class="card premium-card border-0">
        <%@ include file="/WEB-INF/views/layout/csrf-token.jspf" %>
        <div class="card-body">
          <input type="hidden" name="idUsuario" value="${empty usuario.idUsuario ? 0 : usuario.idUsuario}">
          <div class="alert alert-light border mb-4">
            <strong><i class="bi bi-info-circle"></i> Regla de seguridad:</strong>
            el login solo solicita usuario y contraseña. El rol, la identidad del trabajador y el médico asociado se administran desde esta ficha.
          </div>
          <h6 class="fw-bold text-success mb-3"><i class="bi bi-person-vcard"></i> Datos del personal</h6>
          <div class="row g-3 mb-4">
            <div class="col-md-3"><label class="form-label">DNI *</label><input name="dni" class="form-control" maxlength="15" value="${fn:escapeXml(usuario.dni)}" required></div>
            <div class="col-md-5"><label class="form-label">Nombres *</label><input name="nombres" class="form-control" maxlength="80" value="${fn:escapeXml(usuario.nombres)}" required></div>
            <div class="col-md-4"><label class="form-label">Apellidos *</label><input name="apellidos" class="form-control" maxlength="90" value="${fn:escapeXml(usuario.apellidos)}" required></div>
            <div class="col-md-4"><label class="form-label">Teléfono</label><input name="telefono" class="form-control" maxlength="20" value="${fn:escapeXml(usuario.telefono)}"></div>
            <div class="col-md-4"><label class="form-label">Correo</label><input type="email" name="correo" class="form-control" maxlength="100" value="${fn:escapeXml(usuario.correo)}"></div>
            <div class="col-md-4"><label class="form-label">Cargo interno</label><input name="cargo" class="form-control" maxlength="80" value="${fn:escapeXml(usuario.cargo)}" placeholder="Ej.: Recepcionista turno mañana"></div>
          </div>
          <h6 class="fw-bold text-success mb-3"><i class="bi bi-shield-lock"></i> Datos de acceso</h6>
          <div class="row g-3">
            <div class="col-md-4">
              <label class="form-label">Usuario *</label>
              <input name="username" class="form-control" maxlength="60" value="${fn:escapeXml(usuario.username)}" ${editando ? 'readonly' : ''} required>
              <div class="form-text">Ejemplo: camila.torres, recepcionista1 o cajero1.</div>
            </div>
            <div class="col-md-4">
              <label class="form-label">Rol *</label>
              <select name="rol" id="rol" class="form-select" required>
                <option value="ADMIN" ${usuario.rol == 'ADMIN' ? 'selected' : ''}>ADMIN</option>
                <option value="RECEPCIONISTA" ${usuario.rol == 'RECEPCIONISTA' ? 'selected' : ''}>RECEPCIONISTA</option>
                <option value="CAJERO" ${usuario.rol == 'CAJERO' ? 'selected' : ''}>CAJERO</option>
                <option value="DOCTOR" ${usuario.rol == 'DOCTOR' ? 'selected' : ''}>DOCTOR</option>
                <option value="DIRECCION" ${usuario.rol == 'DIRECCION' ? 'selected' : ''}>DIRECCIÓN</option>
              </select>
            </div>
            <div class="col-md-4">
              <label class="form-label">Estado</label>
              <select name="estadoRegistro" class="form-select">
                <option value="ACTIVO" ${usuario.estadoRegistro != 'INACTIVO' ? 'selected' : ''}>ACTIVO</option>
                <option value="INACTIVO" ${usuario.estadoRegistro == 'INACTIVO' ? 'selected' : ''}>INACTIVO</option>
              </select>
            </div>
            <c:if test="${not editando}">
              <div class="col-md-4">
                <label class="form-label">Contraseña inicial *</label>
                <input type="password" name="passwordInicial" class="form-control" minlength="6" required>
              </div>
            </c:if>
            <div class="col-md-8" id="grupoMedicoDoctor">
              <label class="form-label">Médico asociado <span class="text-danger">*</span></label>
              <select name="idMedico" id="idMedico" class="form-select">
                <option value="0">Seleccione un médico activo</option>
                <c:forEach var="m" items="${medicos}">
                  <option value="${m.idMedico}" ${usuario.idMedico == m.idMedico ? 'selected' : ''}>
                    <c:out value="${m.nombres}" /> <c:out value="${m.apellidos}" /> - <c:out value="${m.nombreEspecialidad}" />
                  </option>
                </c:forEach>
              </select>
              <div class="form-text">Obligatorio solo para DOCTOR. Cada médico debe tener su propio usuario.</div>
            </div>
          </div>
          <div class="d-flex justify-content-end gap-2 mt-4">
            <a href="${pageContext.request.contextPath}/usuarios" class="btn btn-outline-secondary">Cancelar</a>
            <button class="btn btn-success"><i class="bi bi-save"></i> Guardar usuario</button>
          </div>
        </div>
      </form>
      <c:if test="${editando}">
        <div class="card premium-card border-0 mt-3">
          <div class="card-body">
            <h6 class="fw-bold"><i class="bi bi-key"></i> Resetear contraseña</h6>
            <form method="post" action="${pageContext.request.contextPath}/usuarios" class="row g-2">
              <%@ include file="/WEB-INF/views/layout/csrf-token.jspf" %>
              <input type="hidden" name="accion" value="reset">
              <input type="hidden" name="id" value="${usuario.idUsuario}">
              <div class="col-md-8"><input type="password" name="passwordNuevo" class="form-control" placeholder="Nueva contraseña" minlength="6" required></div>
              <div class="col-md-4"><button class="btn btn-outline-danger w-100"><i class="bi bi-key"></i> Resetear</button></div>
            </form>
          </div>
        </div>
      </c:if>
    </div>
  </div>
</main>
<script>
(function () {
  const rol = document.getElementById('rol');
  const grupo = document.getElementById('grupoMedicoDoctor');
  const medico = document.getElementById('idMedico');
  function actualizarMedicoDoctor() {
    const esDoctor = rol && rol.value === 'DOCTOR';
    if (grupo) grupo.style.display = esDoctor ? '' : 'none';
    if (medico) medico.required = esDoctor;
    if (!esDoctor && medico) medico.value = '0';
  }
  if (rol) { rol.addEventListener('change', actualizarMedicoDoctor); actualizarMedicoDoctor(); }
})();
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
