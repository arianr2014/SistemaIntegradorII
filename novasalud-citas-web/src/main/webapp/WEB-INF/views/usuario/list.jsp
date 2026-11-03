<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fn" uri="jakarta.tags.functions"%>
<c:set var="pageTitle" value="Usuarios - NovaSalud V3.2.1" scope="request" />
<c:set var="moduloActivo" value="usuarios" scope="request" />
<!DOCTYPE html>
<html lang="es">
<%@ include file="/WEB-INF/views/layout/head.jspf" %>
<body class="bg-light">
<nav class="navbar navbar-expand-lg navbar-dark bg-success shadow-sm">
    <div class="container-fluid px-4">
        <button class="btn btn-outline-light d-lg-none me-2 mobile-menu-toggle" type="button" data-bs-toggle="offcanvas" data-bs-target="#sidebarMenu" aria-controls="sidebarMenu" aria-label="Abrir menú"><i class="bi bi-list fs-4"></i></button>
        <span class="navbar-brand fw-semibold"><i class="bi bi-hospital me-2"></i>NovaSalud V3.2.1</span>
        <div class="d-flex flex-column flex-md-row align-items-md-center gap-2 ms-auto"><span class="text-white small">Módulo: Usuarios</span><%@ include file="/WEB-INF/views/layout/usuario-sesion.jspf" %></div>
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
            <h3 class="fw-bold mb-1"><i class="bi bi-person-gear me-2"></i> Gestión de Usuarios</h3>
            <p class="mb-0 text-white-50">Administra accesos, personal interno, roles, estado de cuenta y vincular médico-usuario.</p>
        </div>
        <div class="d-flex align-items-center gap-2">
            <span class="badge bg-white text-primary fs-6 px-3 py-2 rounded-pill shadow-sm d-none d-md-inline-block">
                <i class="bi bi-shield-lock-fill me-1"></i> Control de Accesos
            </span>
            <a href="${pageContext.request.contextPath}/usuarios?accion=form" class="btn btn-light text-primary fw-bold px-3 py-2 rounded-pill shadow-sm">
                <i class="bi bi-plus-lg me-1"></i> Nuevo Usuario
            </a>
        </div>
    </div>
</div>

<c:choose>
  <c:when test="${param.msg == 'saved'}"><div class="alert alert-success"><i class="bi bi-check-circle"></i> Usuario guardado correctamente.</div></c:when>
  <c:when test="${param.msg == 'activated'}"><div class="alert alert-success"><i class="bi bi-person-check"></i> Usuario activado correctamente.</div></c:when>
  <c:when test="${param.msg == 'deactivated'}"><div class="alert alert-warning"><i class="bi bi-person-x"></i> Usuario desactivado correctamente. En la siguiente validación de sesión perderá acceso.</div></c:when>
  <c:when test="${param.msg == 'reset'}"><div class="alert alert-info"><i class="bi bi-key"></i> Contraseña actualizada correctamente.</div></c:when>
  <c:when test="${param.msg == 'invalid'}"><div class="alert alert-danger"><i class="bi bi-x-circle"></i> No se pudo procesar la operación. Revise rol, médico asociado o datos obligatorios.</div></c:when>
  <c:when test="${param.msg == 'error'}"><div class="alert alert-danger"><i class="bi bi-x-circle"></i> Error inesperado. Intente nuevamente.</div></c:when>
</c:choose>

<form method="get" action="${pageContext.request.contextPath}/usuarios" class="card premium-card border-0 mb-3">
  <div class="card-body row g-2 align-items-end">
    <div class="col-md-8"><label class="form-label">Buscar</label><input class="form-control" name="q" value="${fn:escapeXml(q)}" placeholder="Usuario, DNI, nombre, rol o médico"></div>
    <div class="col-md-4"><button class="btn btn-outline-success w-100"><i class="bi bi-search"></i> Buscar</button></div>
  </div>
</form>

<div class="card premium-card border-0">
  <div class="table-responsive">
    <table class="table table-hover align-middle mb-0">
      <thead class="table-green">
        <tr><th>ID</th><th>Personal / Usuario</th><th>DNI</th><th>Rol</th><th>Médico asociado</th><th>Estado</th><th class="text-end">Acciones</th></tr>
      </thead>
      <tbody>
        <c:forEach var="u" items="${usuarios}">
          <tr>
            <td><c:out value="${u.idUsuario}"/></td>
            <td>
              <div class="fw-semibold"><i class="bi bi-person-circle me-1"></i><c:out value="${u.nombreCompleto}"/></div>
              <small class="text-muted">@<c:out value="${u.username}"/> <c:if test="${not empty u.cargo}"> · <c:out value="${u.cargo}"/></c:if></small>
              <div class="collapse mt-2" id="detalleUsuario${u.idUsuario}">
                <div class="small text-muted border rounded p-2 bg-light">
                  <div><strong>Teléfono:</strong> <c:out value="${empty u.telefono ? '-' : u.telefono}"/></div>
                  <div><strong>Correo:</strong> <c:out value="${empty u.correo ? '-' : u.correo}"/></div>
                  <div><strong>Especialidad:</strong> <c:out value="${empty u.especialidadNombre ? '-' : u.especialidadNombre}"/></div>
                </div>
              </div>
            </td>
            <td><c:out value="${u.dni}"/></td>
            <td><span class="badge bg-primary"><c:out value="${u.rol}"/></span></td>
            <td><c:out value="${empty u.medicoNombreCompleto ? '-' : u.medicoNombreCompleto}"/></td>
            <td><span class="badge ${u.estadoRegistro == 'ACTIVO' ? 'badge-soft-success' : 'badge-soft-danger'}"><c:out value="${u.estadoRegistro}"/></span></td>
            <td class="text-end">
              <button class="btn btn-sm btn-outline-secondary" type="button" data-bs-toggle="collapse" data-bs-target="#detalleUsuario${u.idUsuario}" title="Ver detalle"><i class="bi bi-eye"></i></button>
              <a class="btn btn-sm btn-outline-primary" title="Editar" href="${pageContext.request.contextPath}/usuarios?accion=editar&id=${u.idUsuario}"><i class="bi bi-pencil"></i></a>
              <c:choose>
                <c:when test="${u.estadoRegistro == 'ACTIVO'}">
                  <form method="post" action="${pageContext.request.contextPath}/usuarios" class="d-inline">
                    <%@ include file="/WEB-INF/views/layout/csrf-token.jspf" %>
                    <input type="hidden" name="accion" value="desactivar"><input type="hidden" name="id" value="${u.idUsuario}">
                    <button class="btn btn-sm btn-outline-danger" title="Desactivar usuario"><i class="bi bi-person-x"></i></button>
                  </form>
                </c:when>
                <c:otherwise>
                  <form method="post" action="${pageContext.request.contextPath}/usuarios" class="d-inline">
                    <%@ include file="/WEB-INF/views/layout/csrf-token.jspf" %>
                    <input type="hidden" name="accion" value="activar"><input type="hidden" name="id" value="${u.idUsuario}">
                    <button class="btn btn-sm btn-outline-success" title="Activar usuario"><i class="bi bi-person-check"></i></button>
                  </form>
                </c:otherwise>
              </c:choose>
            </td>
          </tr>
        </c:forEach>
        <c:if test="${empty usuarios}"><tr><td colspan="7" class="text-center text-muted py-4">No se encontraron usuarios.</td></tr></c:if>
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
