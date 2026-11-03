<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/views/layout/header.jsp">
    <jsp:param name="title" value="Historial de Ventas - NovaSalud Farmacia" />
    <jsp:param name="pageTitle" value="Historial de Ventas Realizadas" />
    <jsp:param name="active" value="ventas" />
</jsp:include>

<div class="table-card">
    <div class="table-header">
        <div class="table-title"><i class="fa-solid fa-receipt"></i> Boletas Emitidas y Transacciones</div>
        <a href="${pageContext.request.contextPath}/ventas?mode=pos" class="btn btn-primary btn-sm">
            <i class="fa-solid fa-cash-register"></i> Nueva Venta (POS)
        </a>
    </div>
    <table class="data-table">
        <thead>
            <tr>
                <th>Nº Boleta</th>
                <th>Fecha / Hora</th>
                <th>Cliente</th>
                <th>DNI Cliente</th>
                <th>Método Pago</th>
                <th>Subtotal</th>
                <th>IGV</th>
                <th>Total</th>
                <th>Estado</th>
                <th style="text-align: center;">Acciones</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${ventas}" var="v">
                <tr>
                    <td><strong>${v.numeroBoleta}</strong></td>
                    <td>${v.fechaVenta}</td>
                    <td>${v.nombreCliente}</td>
                    <td>${v.dniCliente}</td>
                    <td><span class="badge badge-info">${v.tipoPago}</span></td>
                    <td>S/ ${v.subtotal}</td>
                    <td>S/ ${v.igv}</td>
                    <td><strong style="color: #059669; font-size: 15px;">S/ ${v.montoTotal}</strong></td>
                    <td><span class="badge badge-success">COMPLETADA</span></td>
                    <td style="text-align: center;">
                        <a href="${pageContext.request.contextPath}/ventas?action=boleta&id=${v.idVenta}" class="btn btn-secondary btn-sm">
                            <i class="fa-solid fa-file-invoice-dollar"></i> Ver Boleta
                        </a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
