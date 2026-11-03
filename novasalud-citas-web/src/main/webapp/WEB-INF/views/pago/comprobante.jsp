<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="pageTitle" value="Comprobante de pago - NovaSalud V3.2.1" scope="request" />
<!DOCTYPE html>
<html lang="es">
<%@ include file="/WEB-INF/views/layout/head.jspf" %>
<body class="bg-light">
<main class="container py-4">
    <div class="d-flex justify-content-between align-items-center mb-3 no-print">
        <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/pagos"><i class="bi bi-arrow-left"></i> Volver</a>
        <button class="btn btn-success" onclick="window.print()"><i class="bi bi-printer"></i> Imprimir / Guardar PDF</button>
    </div>
    <c:if test="${empty pago}"><div class="alert alert-warning">No se encontró el comprobante solicitado.</div></c:if>
    <c:if test="${not empty pago}">
    <div class="card shadow border-0 mx-auto" style="max-width:760px">
        <div class="card-body p-4">
            <div class="text-center border-bottom pb-3 mb-3">
                <h3 class="fw-bold text-success mb-1"><i class="bi bi-hospital"></i> NovaSalud</h3>
                <div class="text-muted">Comprobante interno de atención médica</div>
                <div class="fw-semibold mt-2"><c:out value="${pago.codigoPago}" /></div>
            </div>
            <div class="row g-3 mb-3">
                <div class="col-md-6"><strong>Paciente:</strong><br><c:out value="${pago.paciente}" /></div>
                <div class="col-md-6"><strong>DNI:</strong><br><c:out value="${pago.pacienteDni}" /></div>
                <div class="col-md-6"><strong>Especialidad:</strong><br><c:out value="${pago.especialidad}" /></div>
                <div class="col-md-6"><strong>Médico:</strong><br><c:out value="${pago.medico}" /></div>
                <div class="col-md-6"><strong>Fecha cita:</strong><br><c:out value="${pago.fechaCita}" /> <c:out value="${pago.horaCita}" /></div>
                <div class="col-md-6"><strong>Cajero:</strong><br><c:out value="${pago.usuarioCobro}" /></div>
            </div>
            <table class="table table-bordered">
                <tr><th>Subtotal referencial</th><td class="text-end">S/ <c:out value="${pago.montoSubtotal}" /></td></tr>
                <tr><th>IGV referencial 18%</th><td class="text-end">S/ <c:out value="${pago.montoIgv}" /></td></tr>
                <tr><th>Total</th><td class="text-end fw-bold">S/ <c:out value="${pago.montoTotal}" /></td></tr>
                <tr><th>Pagado</th><td class="text-end">S/ <c:out value="${pago.montoPagado}" /></td></tr>
                <tr><th>Saldo</th><td class="text-end">S/ <c:out value="${pago.saldoPendiente}" /></td></tr>
                <tr><th>Estado</th><td class="text-end"><c:out value="${pago.estadoPago}" /></td></tr>
                <tr><th>Métodos</th><td class="text-end"><c:out value="${pago.metodosPago}" /></td></tr>
                <tr><th>Operaciones externas</th><td class="text-end"><c:out value="${pago.operacionesExternas}" /></td></tr>
            </table>
            <p class="small text-muted mb-0">Documento interno generado por el sistema NovaSalud V3.2.1. Para facturación electrónica SUNAT se requiere integración posterior con proveedor autorizado.</p>
        </div>
    </div>
    </c:if>
</main>
<style>@media print {.no-print{display:none!important} body{background:#fff!important}}</style>
</body>
</html>
