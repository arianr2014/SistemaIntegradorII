<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/layout/header.jsp">
    <jsp:param name="title" value="Medicamentos - NovaSalud Farmacia" />
    <jsp:param name="pageTitle" value="Gestión de Medicamentos" />
    <jsp:param name="active" value="medicamentos" />
</jsp:include>

<div class="table-card">
    <div class="table-header">
        <div class="table-title"><i class="fa-solid fa-capsules"></i> Catálogo de Medicamentos</div>
        <button onclick="openModal('modalMedicamento')" class="btn btn-primary btn-sm">
            <i class="fa-solid fa-plus"></i> Nuevo Medicamento
        </button>
    </div>
    <table class="data-table">
        <thead>
            <tr>
                <th>Código Barras</th>
                <th>Nombre Comercial</th>
                <th>Nombre Genérico</th>
                <th>Categoría</th>
                <th>Presentación / Concentración</th>
                <th>Precio Venta</th>
                <th>Receta</th>
                <th>Estado</th>
                <th>Acciones</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${medicamentos}" var="m">
                <tr>
                    <td><strong>${m.codigoBarras}</strong></td>
                    <td>${m.nombreComercial}</td>
                    <td style="color: #64748b; font-style: italic;">${m.nombreGenerico}</td>
                    <td>${m.nombreCategoria != null ? m.nombreCategoria : m.idCategoria}</td>
                    <td>${m.presentacion} (${m.concentracion})</td>
                    <td><strong style="color: #059669;">S/ ${m.precioVenta}</strong></td>
                    <td>
                        <c:choose>
                            <c:when test="${m.requiereReceta}">
                                <span class="badge badge-warning">Receta</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge badge-success">Libre</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${m.estado}">
                                <span class="badge badge-success">Activo</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge badge-danger">Inactivo</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <form action="${pageContext.request.contextPath}/medicamentos" method="post" style="display:inline;">
                            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}"/>
                            <input type="hidden" name="action" value="eliminar"/>
                            <input type="hidden" name="idMedicamento" value="${m.idMedicamento}"/>
                            <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('¿Seguro de eliminar este medicamento?');">
                                <i class="fa-solid fa-trash"></i>
                            </button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

<!-- Modal Nuevo Medicamento -->
<div id="modalMedicamento" class="modal-backdrop">
    <div class="modal-content" style="max-width: 650px;">
        <div class="modal-header">
            <h3>Registrar Nuevo Medicamento</h3>
            <button onclick="closeModal('modalMedicamento')" class="modal-close">&times;</button>
        </div>
        <form action="${pageContext.request.contextPath}/medicamentos" method="post">
            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}"/>
            <input type="hidden" name="action" value="crear"/>
            
            <div class="modal-body" style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
                <div class="form-group">
                    <label class="form-label">Código de Barras</label>
                    <input type="text" name="codigoBarras" class="form-control" placeholder="Ej. 775000000001" required>
                </div>
                <div class="form-group">
                    <label class="form-label">Categoría</label>
                    <select name="idCategoria" class="form-control" required>
                        <c:forEach items="${categorias}" var="c">
                            <option value="${c.idCategoria}">${c.nombreCategoria}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label class="form-label">Nombre Comercial</label>
                    <input type="text" name="nombreComercial" class="form-control" placeholder="Ej. Panadol Forte" required>
                </div>
                <div class="form-group">
                    <label class="form-label">Nombre Genérico</label>
                    <input type="text" name="nombreGenerico" class="form-control" placeholder="Ej. Paracetamol" required>
                </div>

                <div class="form-group">
                    <label class="form-label">Presentación</label>
                    <input type="text" name="presentacion" class="form-control" placeholder="Ej. Tableta, Jarabe, CJA x 100" required>
                </div>
                <div class="form-group">
                    <label class="form-label">Concentración</label>
                    <input type="text" name="concentracion" class="form-control" placeholder="Ej. 500mg, 100mg/5ml" required>
                </div>

                <div class="form-group">
                    <label class="form-label">Precio de Venta (S/)</label>
                    <input type="number" step="0.01" name="precioVenta" class="form-control" placeholder="0.00" required>
                </div>
                <div class="form-group" style="grid-column: span 2;">
                    <label class="form-label">
                        <input type="checkbox" name="requiereReceta" value="true"> Requiere Receta Médica Obligatoria
                    </label>
                    <input type="hidden" name="estado" value="true">
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" onclick="closeModal('modalMedicamento')" class="btn btn-secondary">Cancelar</button>
                <button type="submit" class="btn btn-primary">Guardar Medicamento</button>
            </div>
        </form>
    </div>
</div>

<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
