<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/layout/header.jsp">
    <jsp:param name="title" value="Categorías - NovaSalud Farmacia" />
    <jsp:param name="pageTitle" value="Gestión de Categorías" />
    <jsp:param name="active" value="categorias" />
</jsp:include>

<div class="table-card">
    <div class="table-header">
        <div class="table-title"><i class="fa-solid fa-tags"></i> Categorías de Medicamentos</div>
        <button onclick="openModal('modalCategoria')" class="btn btn-primary btn-sm">
            <i class="fa-solid fa-plus"></i> Nueva Categoría
        </button>
    </div>
    <table class="data-table">
        <thead>
            <tr>
                <th>ID</th>
                <th>Categoría</th>
                <th>Descripción</th>
                <th>Req. Receta</th>
                <th>Acciones</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${categorias}" var="c">
                <tr>
                    <td><strong>#${c.idCategoria}</strong></td>
                    <td>${c.nombreCategoria}</td>
                    <td>${c.descripcion}</td>
                    <td>
                        <c:choose>
                            <c:when test="${c.requiereReceta}">
                                <span class="badge badge-warning">Sí</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge badge-success">No</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <form action="${pageContext.request.contextPath}/categorias" method="post" style="display:inline;">
                            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}"/>
                            <input type="hidden" name="action" value="eliminar"/>
                            <input type="hidden" name="idCategoria" value="${c.idCategoria}"/>
                            <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('¿Seguro de eliminar esta categoría?');">
                                <i class="fa-solid fa-trash"></i>
                            </button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

<!-- Modal Nueva Categoría -->
<div id="modalCategoria" class="modal-backdrop">
    <div class="modal-content">
        <div class="modal-header">
            <h3>Nueva Categoría de Medicamentos</h3>
            <button onclick="closeModal('modalCategoria')" class="modal-close">&times;</button>
        </div>
        <form action="${pageContext.request.contextPath}/categorias" method="post">
            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}"/>
            <input type="hidden" name="action" value="crear"/>
            
            <div class="modal-body">
                <div class="form-group">
                    <label class="form-label">Nombre de Categoría</label>
                    <input type="text" name="nombreCategoria" class="form-control" required placeholder="Ej. Analgésicos">
                </div>
                <div class="form-group">
                    <label class="form-label">Descripción</label>
                    <textarea name="descripcion" class="form-control" rows="3" placeholder="Descripción de la categoría..."></textarea>
                </div>
                <div class="form-group">
                    <label class="form-label">
                        <input type="checkbox" name="requiereReceta" value="true"> Requiere Receta Médica por defecto
                    </label>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" onclick="closeModal('modalCategoria')" class="btn btn-secondary">Cancelar</button>
                <button type="submit" class="btn btn-primary">Guardar Categoría</button>
            </div>
        </form>
    </div>
</div>

<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
