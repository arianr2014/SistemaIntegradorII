<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/layout/header.jsp">
    <jsp:param name="title" value="Prescripciones - NovaSalud Farmacia" />
    <jsp:param name="pageTitle" value="Control de Prescripciones Médicas" />
    <jsp:param name="active" value="prescripciones" />
</jsp:include>

<div class="table-card">
    <div class="table-header">
        <div class="table-title"><i class="fa-solid fa-file-prescription"></i> Registro de Recetas Médicas</div>
        <button onclick="openModal('modalPrescripcion')" class="btn btn-primary btn-sm">
            <i class="fa-solid fa-plus"></i> Registrar Prescripción
        </button>
    </div>
    <table class="data-table">
        <thead>
            <tr>
                <th>ID</th>
                <th>Médico (CMP)</th>
                <th>Paciente</th>
                <th>DNI Paciente</th>
                <th>Diagnóstico</th>
                <th>F. Emisión</th>
                <th>F. Vencimiento</th>
                <th>Estado</th>
                <th style="text-align: center;">Acciones</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${prescripciones}" var="p">
                <tr>
                    <td><strong>#${p.idPrescripcion}</strong></td>
                    <td>Dr. ${p.nombreMedico} <span style="font-size: 11px; color: #64748b;">(CMP: ${p.numeroColegiaturaMedico})</span></td>
                    <td>${p.nombrePaciente}</td>
                    <td>${p.dniPaciente}</td>
                    <td>${p.diagnostico}</td>
                    <td>${p.fechaEmision}</td>
                    <td>${p.fechaVencimiento}</td>
                    <td>
                        <c:choose>
                            <c:when test="${p.estado == 'VIGENTE' || p.estado == 'ATENDIDA'}">
                                <span class="badge badge-success">${p.estado}</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge badge-danger">${p.estado}</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td style="text-align: center;">
                        <button onclick="verDetallePrescripcion('${p.idPrescripcion}', '${p.codigoPrescripcion}', '${p.nombreMedico}', '${p.numeroColegiaturaMedico}', '${p.nombrePaciente}', '${p.dniPaciente}', '${p.diagnostico}', '${p.fechaEmision}')" class="btn btn-secondary btn-sm">
                            <i class="fa-solid fa-file-medical"></i> Ver Receta
                        </button>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

<!-- Modal Ver Receta Médica -->
<div id="modalVerPrescripcion" class="modal-backdrop">
    <div class="modal-content" style="max-width: 650px;">
        <div class="modal-header" style="background: #f8fafc; border-bottom: 1px solid #e2e8f0;">
            <h3 style="margin: 0; font-size: 16px; font-weight: 700; color: #0f172a;"><i class="fa-solid fa-file-prescription" style="color: #10b981;"></i> Detalle de Receta Médica</h3>
            <button onclick="closeModal('modalVerPrescripcion')" class="modal-close">&times;</button>
        </div>
        <div class="modal-body" style="padding: 20px;">
            <div style="background: #f1f5f9; padding: 14px; border-radius: 8px; margin-bottom: 16px; display: grid; grid-template-columns: 1fr 1fr; gap: 12px; font-size: 13px;">
                <div><strong>Médico Tratante:</strong> <span id="viewMedico"></span> (<span id="viewCmp"></span>)</div>
                <div><strong>Paciente:</strong> <span id="viewPaciente"></span> (DNI: <span id="viewDni"></span>)</div>
                <div><strong>Código Receta:</strong> <span id="viewCodigo"></span></div>
                <div><strong>Fecha Emisión:</strong> <span id="viewFecha"></span></div>
            </div>

            <div style="margin-bottom: 16px;">
                <label style="font-size: 12px; font-weight: 700; color: #64748b; text-transform: uppercase;">Diagnóstico / Indicaciones Principales</label>
                <div id="viewDiagnostico" style="background: #ffffff; border: 1px solid #cbd5e1; padding: 12px; border-radius: 6px; font-size: 14px; margin-top: 4px; color: #1e293b;"></div>
            </div>

            <div style="background: #ecfdf5; border: 1px solid #a7f3d0; padding: 12px; border-radius: 6px; color: #065f46; font-size: 13px;">
                <i class="fa-solid fa-circle-check"></i> Prescripción activa verificada por el departamento farmacéutico de NovaSalud.
            </div>
        </div>
        <div class="modal-footer">
            <button type="button" onclick="closeModal('modalVerPrescripcion')" class="btn btn-secondary">Cerrar</button>
        </div>
    </div>
</div>

<script>
    function verDetallePrescripcion(id, codigo, medico, cmp, paciente, dni, diagnostico, fecha) {
        document.getElementById('viewMedico').innerText = medico;
        document.getElementById('viewCmp').innerText = cmp;
        document.getElementById('viewPaciente').innerText = paciente;
        document.getElementById('viewDni').innerText = dni;
        document.getElementById('viewCodigo').innerText = codigo || ('PRES-2026-000' + id);
        document.getElementById('viewFecha').innerText = fecha;
        document.getElementById('viewDiagnostico').innerText = diagnostico;
        openModal('modalVerPrescripcion');
    }
</script>

<!-- Modal Nueva Prescripción -->
<div id="modalPrescripcion" class="modal-backdrop">
    <div class="modal-content" style="max-width: 600px;">
        <div class="modal-header">
            <h3>Registrar Prescripción Médica</h3>
            <button onclick="closeModal('modalPrescripcion')" class="modal-close">&times;</button>
        </div>
        <form action="${pageContext.request.contextPath}/prescripciones" method="post">
            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}"/>
            <input type="hidden" name="action" value="crear"/>
            
            <div class="modal-body" style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
                <div class="form-group">
                    <label class="form-label">Nº CMP Médico</label>
                    <input type="text" name="numeroColegiaturaMedico" class="form-control" placeholder="Ej. CMP-45892" required>
                </div>
                <div class="form-group">
                    <label class="form-label">Nombre del Médico</label>
                    <input type="text" name="nombreMedico" class="form-control" placeholder="Ej. Dr. Carlos Mendoza" required>
                </div>

                <div class="form-group">
                    <label class="form-label">DNI Paciente</label>
                    <input type="text" name="dniPaciente" class="form-control" placeholder="Ej. 70123456" required maxlength="8">
                </div>
                <div class="form-group">
                    <label class="form-label">Nombre Paciente</label>
                    <input type="text" name="nombrePaciente" class="form-control" placeholder="Ej. María Torres" required>
                </div>

                <div class="form-group" style="grid-column: span 2;">
                    <label class="form-label">Diagnóstico / Indicaciones</label>
                    <input type="text" name="diagnostico" class="form-control" placeholder="Ej. Infección respiratoria aguja, sintomático" required>
                </div>

                <div class="form-group">
                    <label class="form-label">Fecha Emisión</label>
                    <input type="date" name="fechaEmision" class="form-control" required>
                </div>
                <div class="form-group">
                    <label class="form-label">Fecha Vencimiento</label>
                    <input type="date" name="fechaVencimiento" class="form-control" required>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" onclick="closeModal('modalPrescripcion')" class="btn btn-secondary">Cancelar</button>
                <button type="submit" class="btn btn-primary">Registrar Receta</button>
            </div>
        </form>
    </div>
</div>

<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
