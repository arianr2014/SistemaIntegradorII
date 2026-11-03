<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/layout/header.jsp">
    <jsp:param name="title" value="Proveedores - NovaSalud Farmacia" />
    <jsp:param name="pageTitle" value="Gestión de Proveedores" />
    <jsp:param name="active" value="proveedores" />
</jsp:include>

<div class="table-card">
    <div class="table-header">
        <div class="table-title"><i class="fa-solid fa-truck-field"></i> Lista de Proveedores Farmacéuticos</div>
        <button onclick="openModal('modalProveedor')" class="btn btn-primary btn-sm">
            <i class="fa-solid fa-plus"></i> Nuevo Proveedor
        </button>
    </div>
    <table class="data-table">
        <thead>
            <tr>
                <th>RUC</th>
                <th>Razón Social</th>
                <th>Contacto</th>
                <th>Teléfono</th>
                <th>Email</th>
                <th>Estado</th>
                <th>Acciones</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${proveedores}" var="p">
                <tr>
                    <td><strong>${p.ruc}</strong></td>
                    <td>${p.razonSocial}</td>
                    <td>${p.contacto}</td>
                    <td>${p.telefono}</td>
                    <td>${p.email}</td>
                    <td>
                        <c:choose>
                            <c:when test="${p.estado}">
                                <span class="badge badge-success">Activo</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge badge-danger">Inactivo</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <form action="${pageContext.request.contextPath}/proveedores" method="post" style="display:inline;">
                            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}"/>
                            <input type="hidden" name="action" value="eliminar"/>
                            <input type="hidden" name="idProveedor" value="${p.idProveedor}"/>
                            <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('¿Seguro de eliminar este proveedor?');">
                                <i class="fa-solid fa-trash"></i>
                            </button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

<!-- Modal Nuevo Proveedor -->
<div id="modalProveedor" class="modal-backdrop">
    <div class="modal-content" style="max-width: 600px;">
        <div class="modal-header">
            <h3>Registrar Nuevo Proveedor</h3>
            <button onclick="closeModal('modalProveedor')" class="modal-close">&times;</button>
        </div>
        <form action="${pageContext.request.contextPath}/proveedores" method="post">
            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}"/>
            <input type="hidden" name="action" value="crear"/>
            
            <div class="modal-body" style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
                <div class="form-group">
                    <label class="form-label">RUC (11 dígitos)</label>
                    <input type="text" name="ruc" class="form-control" placeholder="20123456789" required maxlength="11">
                </div>
                <div class="form-group">
                    <label class="form-label">Razón Social</label>
                    <input type="text" name="razonSocial" class="form-control" placeholder="Ej. Laboratorios Bago S.A." required>
                </div>

                <div class="form-group">
                    <label class="form-label">Contacto / Representante</label>
                    <input type="text" name="contacto" class="form-control" placeholder="Ej. Juan Pérez" required>
                </div>
                <div class="form-group">
                    <label class="form-label">Teléfono</label>
                    <input type="text" name="telefono" class="form-control" placeholder="014567890" required>
                </div>

                <div class="form-group" style="grid-column: span 2;">
                    <label class="form-label">Correo Electrónico</label>
                    <input type="email" name="email" class="form-control" placeholder="contacto@laboratorio.com" required>
                </div>
                <div class="form-group" style="grid-column: span 2;">
                    <label class="form-label">Dirección Fiscal</label>
                    <input type="text" name="direccion" class="form-control" placeholder="Av. Los Frutales 456, Ate, Lima" required>
                    <input type="hidden" name="estado" value="true">
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" onclick="closeModal('modalProveedor')" class="btn btn-secondary">Cancelar</button>
                <button type="submit" class="btn btn-primary">Guardar Proveedor</button>
            </div>
        </form>
    </div>
</div>

<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
