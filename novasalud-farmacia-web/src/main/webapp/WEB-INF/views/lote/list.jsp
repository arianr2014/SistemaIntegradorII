<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/layout/header.jsp">
    <jsp:param name="title" value="Lotes - NovaSalud Farmacia" />
    <jsp:param name="pageTitle" value="Gestión de Lotes de Medicamentos" />
    <jsp:param name="active" value="lotes" />
</jsp:include>

<div class="table-card">
    <div class="table-header">
        <div class="table-title"><i class="fa-solid fa-boxes-stacked"></i> Lotes de Medicamentos en Almacén</div>
        <button onclick="openModal('modalLote')" class="btn btn-primary btn-sm">
            <i class="fa-solid fa-plus"></i> Registrar Lote
        </button>
    </div>
    <table class="data-table">
        <thead>
            <tr>
                <th>Nº Lote</th>
                <th>Medicamento</th>
                <th>F. Fabricación</th>
                <th>F. Vencimiento</th>
                <th>Stock Inicial</th>
                <th>Stock Actual</th>
                <th>P. Compra</th>
                <th>Acciones</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${lotes}" var="l">
                <tr>
                    <td><strong>${l.numeroLote}</strong></td>
                    <td>${l.nombreMedicamento != null ? l.nombreMedicamento : l.idMedicamento}</td>
                    <td>${l.fechaFabricacion}</td>
                    <td><span class="badge badge-warning">${l.fechaVencimiento}</span></td>
                    <td>${l.cantidadInicial} u.</td>
                    <td><strong style="color: #0284c7;">${l.cantidadActual} u.</strong></td>
                    <td>S/ ${l.precioCompra}</td>
                    <td>
                        <form action="${pageContext.request.contextPath}/lotes" method="post" style="display:inline;">
                            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}"/>
                            <input type="hidden" name="action" value="eliminar"/>
                            <input type="hidden" name="idLote" value="${l.idLote}"/>
                            <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('¿Seguro de eliminar este lote?');">
                                <i class="fa-solid fa-trash"></i>
                            </button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

<!-- Modal Nuevo Lote -->
<div id="modalLote" class="modal-backdrop">
    <div class="modal-content" style="max-width: 650px;">
        <div class="modal-header">
            <h3>Ingreso de Nuevo Lote</h3>
            <button onclick="closeModal('modalLote')" class="modal-close">&times;</button>
        </div>
        <form action="${pageContext.request.contextPath}/lotes" method="post">
            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}"/>
            <input type="hidden" name="action" value="crear"/>
            
            <div class="modal-body" style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
                <div class="form-group">
                    <label class="form-label">Medicamento</label>
                    <select name="idMedicamento" class="form-control" required>
                        <c:forEach items="${medicamentos}" var="m">
                            <option value="${m.idMedicamento}">${m.nombreComercial} (${m.presentacion})</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label class="form-label">Número de Lote</label>
                    <input type="text" name="numeroLote" class="form-control" placeholder="Ej. LOT-2026-001" required>
                </div>

                <div class="form-group">
                    <label class="form-label">Proveedor</label>
                    <select name="idProveedor" class="form-control" required>
                        <c:forEach items="${proveedores}" var="p">
                            <option value="${p.idProveedor}">${p.razonSocial}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label class="form-label">Precio Compra Unitario (S/)</label>
                    <input type="number" step="0.01" name="precioCompra" class="form-control" placeholder="0.00" required>
                </div>

                <div class="form-group">
                    <label class="form-label">Fecha Fabricación</label>
                    <input type="date" name="fechaFabricacion" class="form-control" required>
                </div>
                <div class="form-group">
                    <label class="form-label">Fecha Vencimiento</label>
                    <input type="date" name="fechaVencimiento" class="form-control" required>
                </div>

                <div class="form-group">
                    <label class="form-label">Cantidad Inicial</label>
                    <input type="number" name="cantidadInicial" class="form-control" placeholder="100" required>
                </div>
                <div class="form-group">
                    <label class="form-label">Cantidad Actual</label>
                    <input type="number" name="cantidadActual" class="form-control" placeholder="100" required>
                    <input type="hidden" name="estado" value="true">
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" onclick="closeModal('modalLote')" class="btn btn-secondary">Cancelar</button>
                <button type="submit" class="btn btn-primary">Registrar Lote</button>
            </div>
        </form>
    </div>
</div>

<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
