<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fn" uri="jakarta.tags.functions"%>
<c:set var="pageTitle" value="Expediente Clínico - ${historia.paciente.nombres} ${historia.paciente.apellidos}" scope="request" />
<c:set var="moduloActivo" value="historias" scope="request" />
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
            <div class="d-flex flex-column flex-md-row align-items-md-center gap-2 ms-auto">
                <span class="text-white small">Módulo: Historia Clínica</span>
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
                <!-- Barra de Navegación / Acciones Superior -->
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <a href="${pageContext.request.contextPath}/historias-clinicas" class="btn btn-outline-secondary fw-semibold">
                        <i class="bi bi-arrow-left me-1"></i> Volver a Historias Clínicas
                    </a>
                    <div>
                        <span class="badge bg-primary fs-6 px-3 py-2 rounded-pill shadow-sm">
                            <i class="bi bi-folder2-open me-1"></i> Historia Clínica N° ${not empty historia.codigoHistoria ? historia.codigoHistoria : historia.paciente.dni}
                        </span>
                    </div>
                </div>

                <c:if test="${param.msg == 'updated'}">
                    <div class="alert alert-success alert-dismissible fade show border-0 shadow-sm mb-4" role="alert">
                        <i class="bi bi-check-circle me-2"></i> Antecedentes y alergias actualizados correctamente.
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                </c:if>

                <!-- ========================================================================= -->
                <!-- SECCIÓN SUPERIOR (ARRIBA): DATOS DE LA HISTORIA CLÍNICA Y DEL PACIENTE     -->
                <!-- ========================================================================= -->
                <div class="row g-4 mb-4">
                    <!-- Tarjeta de Datos Personales del Paciente -->
                    <div class="col-lg-7">
                        <div class="card h-100 border-0 shadow-sm" style="border-radius: 12px; background: #ffffff;">
                            <div class="card-header bg-white border-0 pt-4 px-4 pb-0 d-flex justify-content-between align-items-center">
                                <h5 class="fw-bold text-dark mb-0">
                                    <i class="bi bi-person-heart text-primary me-2"></i> Datos del Paciente
                                </h5>
                                <span class="badge bg-info-subtle text-info fw-bold px-3 py-1.5">
                                    DNI: ${historia.paciente.dni}
                                </span>
                            </div>
                            <div class="card-body p-4">
                                <div class="row g-3">
                                    <div class="col-md-6">
                                        <small class="text-muted d-block text-uppercase fw-semibold" style="font-size: 11px;">Nombre Completo</small>
                                        <div class="fs-5 fw-bold text-dark">${historia.paciente.nombres} ${historia.paciente.apellidos}</div>
                                    </div>
                                    <div class="col-md-3">
                                        <small class="text-muted d-block text-uppercase fw-semibold" style="font-size: 11px;">Sexo</small>
                                        <div class="fw-semibold text-dark">${historia.paciente.sexo}</div>
                                    </div>
                                    <div class="col-md-3">
                                        <small class="text-muted d-block text-uppercase fw-semibold" style="font-size: 11px;">F. Nacimiento</small>
                                        <div class="fw-semibold text-dark">${historia.paciente.fechaNacimiento}</div>
                                    </div>

                                    <div class="col-md-6">
                                        <small class="text-muted d-block text-uppercase fw-semibold" style="font-size: 11px;">Teléfono / Celular</small>
                                        <div class="fw-semibold text-dark"><i class="bi bi-telephone text-muted me-1"></i>${historia.paciente.telefono}</div>
                                    </div>
                                    <div class="col-md-6">
                                        <small class="text-muted d-block text-uppercase fw-semibold" style="font-size: 11px;">Correo Electrónico</small>
                                        <div class="fw-semibold text-dark"><i class="bi bi-envelope text-muted me-1"></i>${historia.paciente.correo}</div>
                                    </div>

                                    <div class="col-md-12">
                                        <small class="text-muted d-block text-uppercase fw-semibold" style="font-size: 11px;">Contacto de Emergencia</small>
                                        <div class="fw-semibold text-dark">
                                            <i class="bi bi-telephone-plus text-danger me-1"></i>
                                            ${not empty historia.paciente.contactoEmergencia ? historia.paciente.contactoEmergencia : 'No registrado'}
                                            <c:if test="${not empty historia.paciente.telefonoEmergencia}">
                                                (${historia.paciente.telefonoEmergencia})
                                            </c:if>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Tarjeta de Antecedentes Generales y Alergias (Editable) -->
                    <div class="col-lg-5">
                        <div class="card h-100 border-0 shadow-sm" style="border-radius: 12px; background: #ffffff;">
                            <div class="card-header bg-white border-0 pt-4 px-4 pb-0 d-flex justify-content-between align-items-center">
                                <h5 class="fw-bold text-dark mb-0">
                                    <i class="bi bi-journal-medical text-danger me-2"></i> Antecedentes y Alergias
                                </h5>
                                <button type="button" class="btn btn-sm btn-outline-primary fw-semibold" data-bs-toggle="modal" data-bs-target="#modalEditarAntecedentes">
                                    <i class="bi bi-pencil-square me-1"></i> Editar
                                </button>
                            </div>
                            <div class="card-body p-4">
                                <div class="mb-3">
                                    <label class="small text-muted d-block text-uppercase fw-bold mb-1" style="font-size: 11px;">Antecedentes Generales Patológicos</label>
                                    <div class="p-2.5 rounded bg-light border text-dark" style="min-height: 55px; font-size: 13px;">
                                        <c:out value="${historia.antecedentesGenerales}" default="Sin antecedentes críticos registrados." />
                                    </div>
                                </div>
                                <div>
                                    <label class="small text-muted d-block text-uppercase fw-bold mb-1" style="font-size: 11px;">Alergias Conocidas</label>
                                    <div class="p-2.5 rounded bg-danger-subtle border border-danger-subtle text-danger-emphasis fw-semibold" style="min-height: 45px; font-size: 13px;">
                                        <i class="bi bi-exclamation-triangle me-1"></i>
                                        <c:out value="${historia.alergias}" default="No refiere alergias conocidas." />
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- ========================================================================= -->
                <!-- SECCIÓN INFERIOR (ABAJO): HISTORIAL DE ATENCIONES MÉDICAS Y CONSULTAS       -->
                <!-- ========================================================================= -->
                <div class="card border-0 shadow-sm" style="border-radius: 12px; background: #ffffff;">
                    <div class="card-header bg-white border-0 pt-4 px-4 pb-2 d-flex justify-content-between align-items-center">
                        <div>
                            <h4 class="fw-bold text-dark mb-1">
                                <i class="bi bi-clock-history text-primary me-2"></i> Historial de Atenciones Médicas
                            </h4>
                            <p class="text-muted mb-0 small">Listado cronológico de todas las consultas y diagnósticos realizados por el cuerpo médico.</p>
                        </div>
                        <span class="badge bg-secondary-subtle text-secondary fw-bold px-3 py-2 rounded-pill fs-6">
                            Total Atenciones: ${not empty historia.atenciones ? historia.atenciones.size() : 0}
                        </span>
                    </div>

                    <div class="card-body p-0">
                        <div class="table-responsive">
                            <table class="table table-hover align-middle mb-0">
                                <thead class="table-light">
                                    <tr>
                                        <th class="ps-4" style="width: 8%;">N° ID</th>
                                        <th style="width: 17%;">Fecha y Hora</th>
                                        <th style="width: 25%;">Médico Tratante</th>
                                        <th style="width: 18%;">Especialidad</th>
                                        <th style="width: 20%;">Motivo de Consulta</th>
                                        <th class="text-end pe-4" style="width: 12%;">Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:choose>
                                        <c:when test="${not empty historia.atenciones}">
                                            <c:forEach items="${historia.atenciones}" var="a">
                                                <tr>
                                                    <td class="ps-4 fw-bold text-primary">#${a.idAtencion}</td>
                                                    <td>
                                                        <div class="fw-semibold text-dark"><i class="bi bi-calendar-event me-1 text-muted"></i>${a.fechaAtencion}</div>
                                                    </td>
                                                    <td>
                                                        <div class="fw-bold text-dark">${not empty a.nombreMedico ? a.nombreMedico : 'Dr. Médico Asignado'}</div>
                                                    </td>
                                                    <td>
                                                        <span class="badge bg-info-subtle text-info fw-semibold">
                                                            ${not empty a.especialidadNombre ? a.especialidadNombre : 'Medicina General'}
                                                        </span>
                                                    </td>
                                                    <td>
                                                        <div class="text-truncate" style="max-width: 220px;" title="${a.motivoConsulta}">
                                                            ${a.motivoConsulta}
                                                        </div>
                                                    </td>
                                                    <td class="text-end pe-4">
                                                        <button type="button" class="btn btn-sm btn-primary fw-semibold" 
                                                                onclick="abrirModalAtencion('${a.idAtencion}', '${a.fechaAtencion}', '${a.nombreMedico}', '${a.especialidadNombre}', '${a.motivoConsulta}', '${a.sintomas}', '${a.diagnostico}', '${a.tratamiento}', '${a.recetaMedica}', '${a.indicaciones}')">
                                                            <i class="bi bi-eye me-1"></i> Ver Atención
                                                        </button>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <tr>
                                                <td colspan="6" class="text-center py-5 text-muted">
                                                    <i class="bi bi-file-earmark-medical display-4 mb-3 text-secondary d-block"></i>
                                                    <h5>Sin atenciones médicas registradas</h5>
                                                    <p class="mb-0">El paciente aún no cuenta con consultas médicas cerradas en su historial.</p>
                                                </td>
                                            </tr>
                                        </c:otherwise>
                                    </c:choose>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <!-- ========================================================================= -->
    <!-- MODAL POPUP: DETALLE COMPLETO DE LA ATENCIÓN MÉDICA                       -->
    <!-- ========================================================================= -->
    <div class="modal fade" id="modalDetalleAtencion" tabindex="-1" aria-labelledby="modalDetalleAtencionLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered modal-lg">
            <div class="modal-content border-0 shadow-lg" style="border-radius: 14px;">
                <div class="modal-header bg-primary text-white border-0 px-4 py-3" style="border-top-left-radius: 14px; border-top-right-radius: 14px;">
                    <h5 class="modal-title fw-bold" id="modalDetalleAtencionLabel">
                        <i class="bi bi-file-earmark-text me-2"></i> Detalle de Atención Médica <span id="modalIdAtencion"></span>
                    </h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>

                <div class="modal-body p-4">
                    <!-- Encabezado con Médico, Especialidad y Fecha -->
                    <div class="p-3 mb-4 rounded" style="background: #f8fafc; border-left: 4px solid #3b82f6;">
                        <div class="row align-items-center">
                            <div class="col-md-7">
                                <small class="text-muted d-block text-uppercase fw-semibold" style="font-size: 11px;">Médico Tratante</small>
                                <div class="fw-bold text-dark fs-5" id="modalMedico"></div>
                                <span class="badge bg-primary-subtle text-primary fw-bold px-2 py-1 mt-1" id="modalEspecialidad"></span>
                            </div>
                            <div class="col-md-5 text-md-end">
                                <small class="text-muted d-block text-uppercase fw-semibold" style="font-size: 11px;">Fecha y Hora de Atención</small>
                                <div class="fw-semibold text-dark fs-6" id="modalFecha"></div>
                            </div>
                        </div>
                    </div>

                    <!-- Rejilla de Información Clínica -->
                    <div class="row g-3">
                        <div class="col-md-12">
                            <label class="fw-bold text-dark small text-uppercase mb-1"><i class="bi bi-chat-left-medical text-primary me-1"></i> Motivo de Consulta</label>
                            <div class="p-2.5 bg-light rounded text-dark" id="modalMotivo" style="font-size: 13.5px;"></div>
                        </div>

                        <div class="col-md-12">
                            <label class="fw-bold text-dark small text-uppercase mb-1"><i class="bi bi-thermometer-half text-warning me-1"></i> Síntomas Registrados</label>
                            <div class="p-2.5 bg-light rounded text-dark" id="modalSintomas" style="font-size: 13.5px;"></div>
                        </div>

                        <div class="col-md-12">
                            <label class="fw-bold text-dark small text-uppercase mb-1"><i class="bi bi-clipboard2-pulse text-success me-1"></i> Diagnóstico Médico</label>
                            <div class="p-3 bg-success-subtle border border-success-subtle rounded text-success-emphasis fw-semibold" id="modalDiagnostico" style="font-size: 14px;"></div>
                        </div>

                        <div class="col-md-12">
                            <label class="fw-bold text-dark small text-uppercase mb-1"><i class="bi bi-capsule text-info me-1"></i> Tratamiento y Medicación</label>
                            <div class="p-2.5 bg-light rounded text-dark" id="modalTratamiento" style="font-size: 13.5px;"></div>
                        </div>

                        <div class="col-md-12">
                            <label class="fw-bold text-dark small text-uppercase mb-1"><i class="bi bi-receipt text-secondary me-1"></i> Receta Médica Prescrita</label>
                            <div class="p-3 bg-white border border-2 rounded text-dark font-monospace" id="modalReceta" style="font-size: 13px; background: #fafafa;"></div>
                        </div>

                        <div class="col-md-12">
                            <label class="fw-bold text-dark small text-uppercase mb-1"><i class="bi bi-check2-square text-primary me-1"></i> Indicaciones Clínicas Adicionales</label>
                            <div class="p-2.5 bg-light rounded text-dark" id="modalIndicaciones" style="font-size: 13.5px;"></div>
                        </div>
                    </div>
                </div>

                <div class="modal-footer border-0 bg-light px-4 py-3" style="border-bottom-left-radius: 14px; border-bottom-right-radius: 14px;">
                    <button type="button" class="btn btn-secondary px-4 fw-semibold" data-bs-dismiss="modal">
                        <i class="bi bi-x-lg me-1"></i> Cerrar
                    </button>
                </div>
            </div>
        </div>
    </div>

    <!-- MODAL PARA EDITAR ANTECEDENTES Y ALERGIAS -->
    <div class="modal fade" id="modalEditarAntecedentes" tabindex="-1" aria-labelledby="modalEditarAntecedentesLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content border-0 shadow-lg" style="border-radius: 12px;">
                <form method="POST" action="${pageContext.request.contextPath}/historia-clinica">
                    <input type="hidden" name="accion" value="actualizar_antecedentes">
                    <input type="hidden" name="idPaciente" value="${historia.idPaciente}">

                    <div class="modal-header bg-primary text-white border-0 px-4 py-3">
                        <h5 class="modal-title fw-bold" id="modalEditarAntecedentesLabel">
                            <i class="bi bi-pencil-square me-2"></i> Editar Antecedentes y Alergias
                        </h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body p-4">
                        <div class="mb-3">
                            <label for="antecedentesGenerales" class="form-label fw-bold">Antecedentes Generales Patológicos</label>
                            <textarea class="form-control" id="antecedentesGenerales" name="antecedentesGenerales" rows="3" required>${historia.antecedentesGenerales}</textarea>
                        </div>
                        <div class="mb-3">
                            <label for="alergias" class="form-label fw-bold text-danger">Alergias Conocidas</label>
                            <textarea class="form-control" id="alergias" name="alergias" rows="2" required>${historia.alergias}</textarea>
                        </div>
                    </div>
                    <div class="modal-footer bg-light border-0 px-4 py-3">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                        <button type="submit" class="btn btn-primary fw-semibold"><i class="bi bi-check-lg me-1"></i> Guardar Cambios</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    <script>
        function abrirModalAtencion(idAtencion, fecha, medico, especialidad, motivo, sintomas, diagnostico, tratamiento, receta, indicaciones) {
            document.getElementById('modalIdAtencion').innerText = '#' + idAtencion;
            document.getElementById('modalFecha').innerText = fecha || '-';
            document.getElementById('modalMedico').innerText = medico || 'Médico Asignado';
            document.getElementById('modalEspecialidad').innerText = especialidad || 'Medicina General';
            document.getElementById('modalMotivo').innerText = motivo || 'Sin motivo registrado.';
            document.getElementById('modalSintomas').innerText = sintomas || 'Sin síntomas registrados.';
            document.getElementById('modalDiagnostico').innerText = diagnostico || 'Sin diagnóstico registrado.';
            document.getElementById('modalTratamiento').innerText = tratamiento || 'Sin tratamiento registrado.';
            document.getElementById('modalReceta').innerText = receta || 'No se emitieron medicamentos prescriptos.';
            document.getElementById('modalIndicaciones').innerText = indicaciones || 'Sin indicaciones adicionales.';

            const modal = new bootstrap.Modal(document.getElementById('modalDetalleAtencion'));
            modal.show();
        }
    </script>
</body>
</html>
