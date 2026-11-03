<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/layout/header.jsp">
    <jsp:param name="title" value="Inventario - NovaSalud Farmacia" />
    <jsp:param name="pageTitle" value="Inventario & Niveles de Stock" />
    <jsp:param name="active" value="inventario" />
</jsp:include>

<div class="table-card">
    <div class="table-header">
        <div class="table-title"><i class="fa-solid fa-warehouse"></i> Estado de Inventario General</div>
    </div>
    <table class="data-table">
        <thead>
            <tr>
                <th>ID</th>
                <th>Medicamento</th>
                <th>Stock Actual</th>
                <th>Stock Mínimo</th>
                <th>Stock Máximo</th>
                <th>Ubicación</th>
                <th>Estado Stock</th>
                <th>Acciones</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${inventarios}" var="i">
                <tr>
                    <td><strong>#${i.idInventario}</strong></td>
                    <td>${i.nombreMedicamento != null ? i.nombreMedicamento : i.idMedicamento}</td>
                    <td><strong style="font-size: 16px; color: #0f172a;">${i.stockActual} u.</strong></td>
                    <td>${i.stockMinimo} u.</td>
                    <td>${i.stockMaximo} u.</td>
                    <td><span class="badge badge-info"><i class="fa-solid fa-location-dot"></i> ${i.ubicacion}</span></td>
                    <td>
                        <c:choose>
                            <c:when test="${i.stockActual <= i.stockMinimo}">
                                <span class="badge badge-danger"><i class="fa-solid fa-circle-exclamation"></i> Reabastecer</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge badge-success"><i class="fa-solid fa-check"></i> Óptimo</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <button onclick="editarInventario('${i.idInventario}', '${i.idMedicamento}', '${i.stockActual}', '${i.stockMinimo}', '${i.stockMaximo}', '${i.ubicacion}')" class="btn btn-secondary btn-sm">
                            <i class="fa-solid fa-pen-to-square"></i> Ajustar
                        </button>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

<!-- Modal Editar Inventario -->
<div id="modalInventario" class="modal-backdrop">
    <div class="modal-content">
        <div class="modal-header">
            <h3>Ajuste de Stock e Inventario</h3>
            <button onclick="closeModal('modalInventario')" class="modal-close">&times;</button>
        </div>
        <form action="${pageContext.request.contextPath}/inventario" method="post">
            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}"/>
            <input type="hidden" name="action" value="actualizar"/>
            <input type="hidden" id="inv_idInventario" name="idInventario"/>
            <input type="hidden" id="inv_idMedicamento" name="idMedicamento"/>

            <div class="modal-body">
                <div class="form-group">
                    <label class="form-label">Stock Actual</label>
                    <input type="number" id="inv_stockActual" name="stockActual" class="form-control" required>
                </div>
                <div class="form-group">
                    <label class="form-label">Stock Mínimo Alerta</label>
                    <input type="number" id="inv_stockMinimo" name="stockMinimo" class="form-control" required>
                </div>
                <div class="form-group">
                    <label class="form-label">Stock Máximo Capacidad</label>
                    <input type="number" id="inv_stockMaximo" name="stockMaximo" class="form-control" required>
                </div>
                <div class="form-group">
                    <label class="form-label">Ubicación Física (Estante/Anaquel)</label>
                    <input type="text" id="inv_ubicacion" name="ubicacion" class="form-control" required>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" onclick="closeModal('modalInventario')" class="btn btn-secondary">Cancelar</button>
                <button type="submit" class="btn btn-primary">Actualizar Stock</button>
            </div>
        </form>
    </div>
</div>

<script>
    function editarInventario(id, idMed, actual, min, max, ubicacion) {
        document.getElementById('inv_idInventario').value = id;
        document.getElementById('inv_idMedicamento').value = idMed;
        document.getElementById('inv_stockActual').value = actual;
        document.getElementById('inv_stockMinimo').value = min;
        document.getElementById('inv_stockMaximo').value = max;
        document.getElementById('inv_ubicacion').value = ubicacion;
        openModal('modalInventario');
    }
</script>

<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
