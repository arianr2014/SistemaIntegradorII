<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fn" uri="jakarta.tags.functions"%>

<c:set var="editandoHorario" value="${not empty horario and horario.idHorario > 0}" />
<c:set var="diaHorario" value="${fn:toLowerCase(horario.dia)}" />
<c:set var="pageTitle"
       value="${editandoHorario ? 'Editar Horario' : 'Registrar Horario'}"
       scope="request" />

<!DOCTYPE html>
<html lang="es">
<%@ include file="/WEB-INF/views/layout/head.jspf" %>
<body class="bg-light">
    <nav class="navbar navbar-expand-lg navbar-dark bg-success shadow-sm">
        <div class="container-fluid px-4">

            <button class="btn btn-outline-light d-lg-none me-2 mobile-menu-toggle"
                    type="button"
                    data-bs-toggle="offcanvas"
                    data-bs-target="#sidebarMenu"
                    aria-controls="sidebarMenu"
                    aria-label="Abrir menu">
                <i class="bi bi-list fs-4"></i>
            </button>

            <span class="navbar-brand fw-semibold">NovaSalud V3.2.1</span>

            <!--
                Fase 4:
                Se muestra el módulo actual, el usuario autenticado y el rol activo.
            -->
            <div class="d-flex flex-column flex-md-row align-items-md-center gap-2 ms-auto">
                <span class="text-white small">Módulo: Horarios</span>
                <%@ include file="/WEB-INF/views/layout/usuario-sesion.jspf" %>
            </div>

        </div>
    </nav>

    <%@ include file="/WEB-INF/views/layout/menu-mobile.jspf" %>

    <main class="container-fluid py-4 px-4">
        <div class="row g-4">

            <div class="col-12 col-lg-3 order-1 order-lg-1">
                <%@ include file="/WEB-INF/views/layout/menu-right.jspf" %>
            </div>

            <div class="col-12 col-lg-9 order-2 order-lg-2">

                <div class="card border-0 shadow-sm">
                    <div class="card-header bg-success text-white d-flex justify-content-between align-items-center">
                        <h5 class="mb-0">
                            <i class="bi bi-clock"></i>

                            <c:choose>
                                <c:when test="${editandoHorario}">
                                    Editar Horario
                                </c:when>
                                <c:otherwise>
                                    Registrar Horario
                                </c:otherwise>
                            </c:choose>
                        </h5>

                        <a href="${pageContext.request.contextPath}/horarios"
                           class="btn btn-light btn-sm">
                            <i class="bi bi-arrow-left"></i> Volver
                        </a>
                    </div>

                    <div class="card-body">

                        <!--
                            Fase 5:
                            El mensaje de error se muestra con c:out para evitar salida directa.
                        -->
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger">
                                <c:out value="${error}" />
                            </div>
                        </c:if>

                        <form method="post"
                              action="${pageContext.request.contextPath}/horarios"
                              class="row g-3"><%@ include file="/WEB-INF/views/layout/csrf-token.jspf" %>

                            <input type="hidden"
                                   name="idHorario"
                                   value="${empty horario.idHorario ? 0 : horario.idHorario}">

                            <div class="col-md-6">
                                <label class="form-label">Médico *</label>

                                <select class="form-select"
                                        name="idMedico"
                                        required>
                                    <option value="">Seleccione un médico</option>

                                    <c:forEach var="m" items="${medicos}">
                                        <option value="${m.idMedico}"
                                                ${horario.idMedico == m.idMedico ? 'selected' : ''}>
                                            <c:out value="${m.nombreCompleto}" />
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="col-md-6">
                                <label class="form-label">Dia *</label>

                                <select class="form-select"
                                        name="dia"
                                        required>
                                    <option value="">Seleccione un dia</option>

                                    <option value="lunes" ${diaHorario == 'lunes' ? 'selected' : ''}>
                                        Lunes
                                    </option>

                                    <option value="martes" ${diaHorario == 'martes' ? 'selected' : ''}>
                                        Martes
                                    </option>

                                    <option value="miercoles" ${diaHorario == 'miercoles' ? 'selected' : ''}>
                                        Miercoles
                                    </option>

                                    <option value="jueves" ${diaHorario == 'jueves' ? 'selected' : ''}>
                                        Jueves
                                    </option>

                                    <option value="viernes" ${diaHorario == 'viernes' ? 'selected' : ''}>
                                        Viernes
                                    </option>

                                    <option value="sabado" ${diaHorario == 'sabado' ? 'selected' : ''}>
                                        Sabado
                                    </option>

                                    <option value="domingo" ${diaHorario == 'domingo' ? 'selected' : ''}>
                                        Domingo
                                    </option>
                                </select>
                            </div>

                            <!--
                                Fase 5:
                                Las horas se escapan antes de mostrarse dentro del atributo value.
                            -->
                            <div class="col-md-6">
                                <label class="form-label">Hora inicio *</label>
                                <input type="time"
                                       class="form-control"
                                       name="horaInicio"
                                       required
                                       value="${fn:escapeXml(horario.horaInicio)}">
                            </div>

                            <div class="col-md-6">
                                <label class="form-label">Hora fin *</label>
                                <input type="time"
                                       class="form-control"
                                       name="horaFin"
                                       required
                                       value="${fn:escapeXml(horario.horaFin)}">
                            </div>

                            <div class="col-12 d-flex justify-content-end gap-2">
                                <a href="${pageContext.request.contextPath}/horarios"
                                   class="btn btn-outline-secondary">
                                    Cancelar
                                </a>

                                <button type="submit" class="btn btn-success">
                                    <i class="bi bi-save"></i>

                                    <c:choose>
                                        <c:when test="${editandoHorario}">
                                            Actualizar
                                        </c:when>
                                        <c:otherwise>
                                            Guardar
                                        </c:otherwise>
                                    </c:choose>
                                </button>
                            </div>
                        </form>

                    </div>
                </div>

            </div>
        </div>
    </main>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>