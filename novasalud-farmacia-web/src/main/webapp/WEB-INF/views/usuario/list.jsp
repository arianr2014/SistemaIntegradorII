<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/layout/header.jsp">
    <jsp:param name="title" value="Usuarios - NovaSalud Farmacia" />
    <jsp:param name="pageTitle" value="Gestión de Usuarios del Sistema" />
    <jsp:param name="active" value="usuarios" />
</jsp:include>

<div class="table-card">
    <div class="table-header">
        <div class="table-title"><i class="fa-solid fa-users-gear"></i> Usuarios y Permisos</div>
        <button onclick="openModal('modalUsuario')" class="btn btn-primary btn-sm">
            <i class="fa-solid fa-plus"></i> Nuevo Usuario
        </button>
    </div>
    <table class="data-table">
        <thead>
            <tr>
                <th>ID</th>
                <th>Usuario</th>
                <th>Nombres y Apellidos</th>
                <th>Email</th>
                <th>Rol</th>
                <th>Estado</th>
                <th>Acciones</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${usuarios}" var="u">
                <tr>
                    <td><strong>#${u.idUsuario}</strong></td>
                    <td><strong>${u.username}</strong></td>
                    <td>${u.nombres} ${u.apellidos}</td>
                    <td>${u.email}</td>
                    <td>
                        <c:choose>
                            <c:when test="${u.rol == 'ADMINISTRADOR'}">
                                <span class="badge badge-danger">ADMINISTRADOR</span>
                            </c:when>
                            <c:when test="${u.rol == 'FARMACEUTICO'}">
                                <span class="badge badge-info">FARMACÉUTICO</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge badge-success">${u.rol}</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${u.estado}">
                                <span class="badge badge-success">Activo</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge badge-danger">Inactivo</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <form action="${pageContext.request.contextPath}/usuarios" method="post" style="display:inline;">
                            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}"/>
                            <input type="hidden" name="action" value="eliminar"/>
                            <input type="hidden" name="idUsuario" value="${u.idUsuario}"/>
                            <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('¿Seguro de deshabilitar este usuario?');">
                                <i class="fa-solid fa-user-slash"></i>
                            </button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

<!-- Modal Nuevo Usuario -->
<div id="modalUsuario" class="modal-backdrop">
    <div class="modal-content" style="max-width: 600px;">
        <div class="modal-header">
            <h3>Registrar Nuevo Usuario</h3>
            <button onclick="closeModal('modalUsuario')" class="modal-close">&times;</button>
        </div>
        <form action="${pageContext.request.contextPath}/usuarios" method="post">
            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}"/>
            <input type="hidden" name="action" value="crear"/>
            
            <div class="modal-body" style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
                <div class="form-group">
                    <label class="form-label">Username</label>
                    <input type="text" name="username" class="form-control" placeholder="Ej. juan.perez" required>
                </div>
                <div class="form-group">
                    <label class="form-label">Contraseña</label>
                    <input type="password" name="password" class="form-control" placeholder="••••••••" required>
                </div>

                <div class="form-group">
                    <label class="form-label">Nombres</label>
                    <input type="text" name="nombres" class="form-control" placeholder="Juan Carlos" required>
                </div>
                <div class="form-group">
                    <label class="form-label">Apellidos</label>
                    <input type="text" name="apellidos" class="form-control" placeholder="Pérez Gómez" required>
                </div>

                <div class="form-group">
                    <label class="form-label">Correo Electrónico</label>
                    <input type="email" name="email" class="form-control" placeholder="juan.perez@novasalud.pe" required>
                </div>
                <div class="form-group">
                    <label class="form-label">Rol de Sistema</label>
                    <select name="rol" class="form-control" required>
                        <option value="FARMACEUTICO">FARMACÉUTICO</option>
                        <option value="CAJERO">CAJERO</option>
                        <option value="ADMINISTRADOR">ADMINISTRADOR</option>
                    </select>
                    <input type="hidden" name="estado" value="true">
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" onclick="closeModal('modalUsuario')" class="btn btn-secondary">Cancelar</button>
                <button type="submit" class="btn btn-primary">Registrar Usuario</button>
            </div>
        </form>
    </div>
</div>

<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
