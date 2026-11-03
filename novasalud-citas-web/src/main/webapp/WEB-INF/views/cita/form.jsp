<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fn" uri="jakarta.tags.functions"%>

<c:set var="editandoCita" value="${not empty cita and cita.idCita > 0}" />

<c:set var="idEspecialidadSeleccionada"
       value="${empty idEspecialidadSel ? 0 : idEspecialidadSel}" />

<c:set var="idMedicoSeleccionado"
       value="${empty idMedicoSel ? cita.idMedico : idMedicoSel}" />

<c:set var="fechaSeleccionada"
       value="${empty fechaSel ? cita.fecha : fechaSel}" />

<c:set var="horaSeleccionada"
       value="${empty horaSel ? cita.hora : horaSel}" />

<c:set var="estadoSeleccionado"
       value="${empty cita.estado ? 'PROGRAMADA' : fn:toUpperCase(cita.estado)}" />

<c:choose>
    <c:when test="${not empty dniBusqueda}">
        <c:set var="dniBusquedaValor" value="${dniBusqueda}" />
    </c:when>

    <c:when test="${not empty pacienteEncontrado}">
        <c:set var="dniBusquedaValor" value="${pacienteEncontrado.dni}" />
    </c:when>

    <c:otherwise>
        <c:set var="dniBusquedaValor" value="" />
    </c:otherwise>
</c:choose>

<c:set var="filtrosCompletos"
       value="${not empty idMedicoSeleccionado and idMedicoSeleccionado > 0 and not empty fechaSeleccionada}" />

<c:set var="sinHorarios"
       value="${filtrosCompletos and empty horariosDisponibles}" />

<c:set var="horaActualIncluida" value="false" />

<c:forEach var="hDisponible" items="${horariosDisponibles}">
    <c:if test="${hDisponible == horaSeleccionada}">
        <c:set var="horaActualIncluida" value="true" />
    </c:if>
</c:forEach>

<c:set var="pageTitle"
       value="${editandoCita ? 'Editar Cita' : 'Registrar Cita'}"
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
                <span class="text-white small">Módulo: Citas</span>
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

                <div class="card border-0 shadow-sm mb-3">
                    <div class="card-header bg-success text-white d-flex justify-content-between align-items-center">
                        <h5 class="mb-0">
                            <i class="bi bi-calendar-plus"></i>

                            <c:choose>
                                <c:when test="${editandoCita}">
                                    Editar Cita
                                </c:when>
                                <c:otherwise>
                                    Registrar Cita
                                </c:otherwise>
                            </c:choose>
                        </h5>

                        <a href="${pageContext.request.contextPath}/citas"
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

                        <!--
                            Búsqueda de paciente por DNI.
                            Se mantiene por GET porque solo consulta información.
                        -->
                        <form method="get"
                              action="${pageContext.request.contextPath}/citas"
                              class="row g-2 mb-3">

                            <input type="hidden" name="accion" value="form">

                            <c:if test="${editandoCita}">
                                <input type="hidden" name="id" value="${cita.idCita}">
                            </c:if>

                            <input type="hidden"
                                   name="idEspecialidadSel"
                                   value="${idEspecialidadSeleccionada}">

                            <div class="col-md-8">
                                <label class="form-label">Buscar paciente por DNI</label>
                                <input type="text"
                                       class="form-control"
                                       name="dniBusqueda"
                                       value="${fn:escapeXml(dniBusquedaValor)}"
                                       placeholder="Ingrese DNI">
                            </div>

                            <div class="col-md-4 d-grid align-content-end">
                                <button type="submit"
                                        class="btn btn-outline-success mt-md-4">
                                    <i class="bi bi-search"></i> Buscar paciente
                                </button>
                            </div>
                        </form>

                        <!--
                            Datos del paciente seleccionado.
                            Fase 5: todos los datos visibles se muestran con c:out.
                        -->
                        <div class="border rounded p-3 mb-3 bg-white">
                            <h6 class="text-green mb-2">Datos del paciente</h6>

                            <c:choose>
                                <c:when test="${not empty pacienteEncontrado}">
                                    <input type="hidden"
                                           form="formCita"
                                           name="idPaciente"
                                           value="${pacienteEncontrado.idPaciente}">

                                    <div>
                                        <strong>DNI:</strong>
                                        <c:out value="${pacienteEncontrado.dni}" />
                                    </div>

                                    <div>
                                        <strong>Nombre:</strong>
                                        <c:out value="${pacienteEncontrado.nombres}" />
                                        <c:out value="${pacienteEncontrado.apellidos}" />
                                    </div>

                                    <div>
                                        <strong>Teléfono:</strong>
                                        <c:out value="${pacienteEncontrado.telefono}" />
                                    </div>

                                    <div>
                                        <strong>Dirección:</strong>
                                        <c:out value="${pacienteEncontrado.direccion}" />
                                    </div>
                                </c:when>

                                <c:otherwise>
                                    <div class="text-muted">
                                        No hay paciente seleccionado. Busque por DNI.
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <!--
                            Filtro de especialidad.
                            Al cambiar la especialidad, se recarga el formulario para listar médicos.
                        -->
                        <form id="formEspecialidad"
                              method="get"
                              action="${pageContext.request.contextPath}/citas"
                              class="row g-2 mb-3">

                            <input type="hidden" name="accion" value="form">

                            <c:if test="${editandoCita}">
                                <input type="hidden" name="id" value="${cita.idCita}">
                            </c:if>

                            <input type="hidden"
                                   name="dniBusqueda"
                                   value="${fn:escapeXml(dniBusquedaValor)}">

                            <input type="hidden"
                                   name="idMedicoSel"
                                   id="idMedicoSelHelper"
                                   value="${idMedicoSeleccionado}">

                            <input type="hidden"
                                   name="fechaSel"
                                   id="fechaSelHelper"
                                   value="${fn:escapeXml(fechaSeleccionada)}">

                            <input type="hidden"
                                   name="horaSel"
                                   id="horaSelHelper"
                                   value="${fn:escapeXml(horaSeleccionada)}">

                            <div class="col-md-12">
                                <label class="form-label">Especialidad</label>

                                <select class="form-select"
                                        name="idEspecialidadSel"
                                        required
                                        onchange="this.form.submit()">
                                    <option value="">Seleccione especialidad</option>

                                    <c:forEach var="e" items="${especialidades}">
                                        <option value="${e.idEspecialidad}"
                                                ${idEspecialidadSeleccionada == e.idEspecialidad ? 'selected' : ''}>
                                            <c:out value="${e.nombre}" />
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                        </form>

                        <div class="alert alert-info border-0 shadow-sm mb-3">
                            <div class="d-flex align-items-start gap-2">
                                <i class="bi bi-cash-coin fs-5"></i>
                                <div>
                                    <strong>Tarifas vigentes:</strong>
                                    <span class="text-muted">Use esta guía para informar precios al paciente antes de confirmar la cita.</span>
                                    <div class="d-flex flex-wrap gap-2 mt-2">
                                        <c:forEach var="t" items="${tarifasActivas}">
                                            <span class="badge rounded-pill text-bg-light border">
                                                <c:out value="${t.especialidad}" />: S/ <c:out value="${t.monto}" />
                                            </span>
                                        </c:forEach>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!--
                            Formulario principal de cita.
                            Fase 3: guardar/actualizar se mantiene por POST.
                        -->
                        <form id="formCita"
                              method="post"
                              action="${pageContext.request.contextPath}/citas"
                              class="row g-3"><%@ include file="/WEB-INF/views/layout/csrf-token.jspf" %>

                            <input type="hidden"
                                   name="idCita"
                                   value="${empty cita.idCita ? 0 : cita.idCita}">

                            <input type="hidden"
                                   name="dniBusqueda"
                                   value="${fn:escapeXml(dniBusquedaValor)}">

                            <input type="hidden"
                                   name="idEspecialidadSel"
                                   value="${idEspecialidadSeleccionada}">

                            <c:choose>
                                <c:when test="${not empty pacienteEncontrado}">
                                    <input type="hidden"
                                           name="idPaciente"
                                           value="${pacienteEncontrado.idPaciente}">
                                </c:when>

                                <c:otherwise>
                                    <input type="hidden" name="idPaciente" value="0">
                                </c:otherwise>
                            </c:choose>

                            <div class="col-md-6">
                                <label class="form-label">Médico *</label>

                                <select class="form-select"
                                        id="idMedico"
                                        name="idMedico"
                                        required
                                        onchange="recargarHorarios()">
                                    <option value="">Seleccione médico</option>

                                    <c:forEach var="m" items="${medicos}">
                                        <option value="${m.idMedico}"
                                                ${idMedicoSeleccionado == m.idMedico ? 'selected' : ''}>
                                            <c:out value="${m.nombreCompleto}" />
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="col-md-3">
                                <label class="form-label">Fecha *</label>

                                <input type="date"
                                       class="form-control"
                                       id="fecha"
                                       name="fecha"
                                       required
                                       value="${fn:escapeXml(fechaSeleccionada)}"
                                       onchange="recargarHorarios()">
                            </div>

                            <div class="col-md-3">
                                <label class="form-label">Hora *</label>

                                <select class="form-select"
                                        id="hora"
                                        name="hora"
                                        required>
                                    <option value="">Seleccione hora</option>

                                    <c:if test="${not horaActualIncluida and not empty horaSeleccionada}">
                                        <option value="${fn:escapeXml(horaSeleccionada)}" selected>
                                            <c:out value="${horaSeleccionada}" /> (actual)
                                        </option>
                                    </c:if>

                                    <c:forEach var="h" items="${horariosDisponibles}">
                                        <option value="${fn:escapeXml(h)}"
                                                ${h == horaSeleccionada ? 'selected' : ''}>
                                            <c:out value="${h}" />
                                        </option>
                                    </c:forEach>
                                </select>

                                <c:choose>
                                    <c:when test="${not filtrosCompletos}">
                                        <div class="form-text">
                                            Seleccione médico y fecha para ver horarios disponibles.
                                        </div>
                                    </c:when>

                                    <c:when test="${sinHorarios}">
                                        <div class="text-danger small mt-1">
                                            No hay horarios disponibles para ese médico en esa fecha.
                                        </div>
                                    </c:when>

                                    <c:otherwise>
                                        <div class="text-success small mt-1">
                                            Se muestran solo horas válidas y disponibles.
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <div class="col-md-4">
                                <label class="form-label">Estado *</label>
                                <c:choose>
                                    <c:when test="${editandoCita}">
                                        <select class="form-select" name="estado" required>
                                            <option value="PROGRAMADA" ${estadoSeleccionado == 'PROGRAMADA' ? 'selected' : ''}>PROGRAMADA</option>
                                            <option value="CONFIRMADA" ${estadoSeleccionado == 'CONFIRMADA' ? 'selected' : ''}>CONFIRMADA</option>
                                            <option value="REPROGRAMADA" ${estadoSeleccionado == 'REPROGRAMADA' ? 'selected' : ''}>REPROGRAMADA</option>
                                            <option value="EN_ESPERA" ${estadoSeleccionado == 'EN_ESPERA' ? 'selected' : ''}>EN ESPERA</option>
                                            <option value="CANCELADA" ${estadoSeleccionado == 'CANCELADA' ? 'selected' : ''}>CANCELADA</option>
                                            <option value="NO_ASISTIO" ${estadoSeleccionado == 'NO_ASISTIO' ? 'selected' : ''}>NO ASISTIÓ</option>
                                        </select>
                                        <div class="form-text">La atención final la registra únicamente el médico desde su agenda.</div>
                                    </c:when>
                                    <c:otherwise>
                                        <input type="hidden" name="estado" value="PROGRAMADA">
                                        <input type="text" class="form-control" value="PROGRAMADA" readonly>
                                        <div class="form-text">Toda cita nueva inicia como PROGRAMADA y queda pendiente de pago en caja.</div>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <div class="col-md-8">
                                <label class="form-label">Observaciones</label>

                                <input type="text"
                                       class="form-control"
                                       name="observaciones"
                                       maxlength="255"
                                       value="${fn:escapeXml(cita.observaciones)}">
                            </div>

                            <div class="col-12 d-flex justify-content-end gap-2">
                                <a href="${pageContext.request.contextPath}/citas"
                                   class="btn btn-outline-secondary">
                                    Cancelar
                                </a>

                                <button type="submit"
                                        class="btn btn-success"
                                        ${sinHorarios ? 'disabled' : ''}>
                                    <i class="bi bi-save"></i>

                                    <c:choose>
                                        <c:when test="${editandoCita}">
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

    <script>
        async function recargarHorarios() {
            const medico = document.getElementById("idMedico").value;
            const fecha = document.getElementById("fecha").value;
            const selectHora = document.getElementById("hora");
            if (!selectHora) return;

            document.getElementById("idMedicoSelHelper").value = medico;
            document.getElementById("fechaSelHelper").value = fecha;

            if (!medico || !fecha) {
                return;
            }

            try {
                selectHora.innerHTML = '<option value="">Cargando horarios disponibles...</option>';
                const url = '${pageContext.request.contextPath}/citas?accion=form&idMedicoSel=' + medico + '&fechaSel=' + encodeURIComponent(fecha);
                const resp = await fetch(url);
                if (resp.ok) {
                    const htmlText = await resp.text();
                    const parser = new DOMParser();
                    const doc = parser.parseFromString(htmlText, 'text/html');
                    const nuevoSelect = doc.getElementById("hora");
                    if (nuevoSelect) {
                        selectHora.innerHTML = nuevoSelect.innerHTML;
                        return;
                    }
                }
            } catch (e) {
                console.warn('Fallback a recarga de página:', e);
            }

            document.getElementById("formEspecialidad").submit();
        }
    </script>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>