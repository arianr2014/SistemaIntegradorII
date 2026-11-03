<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/views/layout/header.jsp">
    <jsp:param name="title" value="Ticket de Venta - NovaSalud Farmacia" />
    <jsp:param name="pageTitle" value="Comprobante de Venta Electrónico" />
    <jsp:param name="active" value="ventas" />
</jsp:include>

<!-- Librería html2pdf para descarga directa en PDF Ticket -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/html2pdf.js/0.10.1/html2pdf.bundle.min.js"></script>

<style>
    #ticketContainer * {
        box-sizing: border-box !important;
    }

    @media print {
        body * {
            visibility: hidden;
        }
        #ticketContainer, #ticketContainer * {
            visibility: visible;
        }
        #ticketContainer {
            position: absolute;
            left: 0;
            top: 0;
            width: 280px !important;
            padding: 5px !important;
            margin: 0 !important;
            border: none !important;
            box-shadow: none !important;
        }
        .no-print {
            display: none !important;
        }
    }

    .ticket-wrapper {
        background: #ffffff;
        width: 290px;
        max-width: 100%;
        margin: 0 auto;
        padding: 12px 10px;
        border: 1px solid #d1d5db;
        border-radius: 4px;
        box-shadow: 0 4px 12px rgba(0,0,0,0.08);
        font-family: 'Courier New', Courier, monospace, sans-serif;
        color: #000000;
        font-size: 11px;
        line-height: 1.35;
        box-sizing: border-box;
        overflow: hidden;
    }

    .ticket-header-title {
        text-align: center;
        font-weight: 800;
        font-size: 14px;
        text-transform: uppercase;
        margin-bottom: 2px;
    }

    .ticket-number {
        text-align: center;
        font-weight: 700;
        font-size: 13px;
        margin-bottom: 4px;
    }

    .ticket-divider {
        border-top: 1px dashed #000000;
        margin: 5px 0;
    }

    .ticket-table {
        width: 100%;
        border-collapse: collapse;
        margin: 6px 0;
        table-layout: fixed;
        box-sizing: border-box;
    }

    .ticket-table th {
        border-bottom: 1px solid #000000;
        text-align: left;
        font-size: 11px;
        padding: 3px 1px;
        white-space: nowrap;
    }

    .ticket-table td {
        padding: 3px 1px;
        font-size: 11px;
        vertical-align: top;
        word-wrap: break-word;
        overflow-wrap: break-word;
    }

    .totals-grid {
        width: 100%;
        font-size: 11px;
        margin-top: 4px;
        table-layout: fixed;
        border-collapse: collapse;
        box-sizing: border-box;
    }

    .totals-grid td {
        padding: 2px 0;
        vertical-align: top;
    }

    .monto-letras {
        text-align: center;
        font-weight: 700;
        font-size: 10.5px;
        margin: 6px 0;
        text-transform: uppercase;
        word-break: break-word;
        overflow-wrap: break-word;
    }

    .ticket-footer {
        text-align: center;
        font-size: 10px;
        margin-top: 8px;
        word-break: break-word;
        overflow-wrap: break-word;
    }

    .qr-container {
        text-align: center;
        margin-top: 8px;
    }
</style>

<!-- Barra de Acciones de Impresión / Navegación -->
<div class="no-print" style="max-width: 750px; margin: 0 auto 20px auto; display: flex; justify-content: space-between; align-items: center; background: #ffffff; padding: 16px 20px; border-radius: 8px; border: 1px solid #e2e8f0; box-shadow: 0 4px 6px rgba(0,0,0,0.02);">
    <a href="${pageContext.request.contextPath}/ventas" class="btn btn-secondary btn-sm">
        <i class="fa-solid fa-arrow-left"></i> Volver al Historial
    </a>
    <div style="display: flex; gap: 10px;">
        <button onclick="downloadPDFTicket()" class="btn btn-primary btn-sm">
            <i class="fa-solid fa-file-pdf"></i> Descargar PDF Ticket
        </button>
        <button onclick="window.print()" class="btn btn-dark btn-sm">
            <i class="fa-solid fa-print"></i> Imprimir Ticket
        </button>
        <a href="${pageContext.request.contextPath}/ventas?mode=pos" class="btn btn-success btn-sm">
            <i class="fa-solid fa-cart-shopping"></i> Nueva Venta (POS)
        </a>
    </div>
</div>

<!-- Ticket de Venta Térmico PDF -->
<div id="ticketContainer" class="ticket-wrapper">
    <!-- Título y Serie/Número del Comprobante -->
    <div class="ticket-header-title">
        <c:choose>
            <c:when test="${not empty venta.tipoComprobante}">
                ${venta.tipoComprobante}
            </c:when>
            <c:when test="${not empty venta.codigoVenta && venta.codigoVenta.startsWith('F')}">
                FACTURA ELECTRÓNICA
            </c:when>
            <c:otherwise>
                BOLETA ELECTRÓNICA
            </c:otherwise>
        </c:choose>
    </div>
    <div class="ticket-number">
        ${not empty venta.codigoVenta ? venta.codigoVenta : (not empty venta.numeroBoleta ? venta.numeroBoleta : 'F002-00003848')}
    </div>

    <div class="ticket-divider"></div>

    <!-- Datos del Cliente -->
    <div>
        <strong>Señores :</strong> ${not empty venta.nombreCliente ? venta.nombreCliente : 'PUBLICO GENERAL'}<br>
        <strong><c:choose><c:when test="${not empty venta.codigoVenta && venta.codigoVenta.startsWith('F')}">RUC. :</c:when><c:otherwise>DNI. :</c:otherwise></c:choose></strong> ${not empty venta.dniCliente ? venta.dniCliente : '99999999'}
    </div>

    <div class="ticket-divider"></div>

    <!-- Fecha de Emisión -->
    <div>
        <strong>Fecha :</strong> <span id="fechaVentaSpan">${not empty venta.fechaVentaFormateada ? venta.fechaVentaFormateada : (not empty venta.fechaVenta ? venta.fechaVenta : '06/09/2026 11:14:48')}</span>
    </div>

    <div class="ticket-divider"></div>

    <!-- Tabla de Productos/Medicamentos -->
    <table class="ticket-table">
        <thead>
            <tr>
                <th style="width: 40%;">Producto</th>
                <th style="width: 15%; text-align: right;">Cant.</th>
                <th style="width: 21%; text-align: right;">Precio</th>
                <th style="width: 24%; text-align: right;">Importe</th>
            </tr>
        </thead>
        <tbody>
            <c:choose>
                <c:when test="${not empty venta.detalles}">
                    <c:forEach items="${venta.detalles}" var="d">
                        <tr>
                            <td>${d.nombreMedicamento}</td>
                            <td style="text-align: right;"><fmt:formatNumber value="${d.cantidad}" pattern="0.0"/></td>
                            <td style="text-align: right;"><fmt:formatNumber value="${d.precioUnitario}" pattern="0.00"/></td>
                            <td style="text-align: right;"><fmt:formatNumber value="${d.subtotal}" pattern="0.00"/></td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td>PARACETAMOL 500MG TAB</td>
                        <td style="text-align: right;">2.0</td>
                        <td style="text-align: right;">3.50</td>
                        <td style="text-align: right;">7.00</td>
                    </tr>
                    <tr>
                        <td>IBUPROFENO 400MG CAP</td>
                        <td style="text-align: right;">1.0</td>
                        <td style="text-align: right;">5.00</td>
                        <td style="text-align: right;">5.00</td>
                    </tr>
                </c:otherwise>
            </c:choose>
        </tbody>
    </table>

    <div class="ticket-divider"></div>

    <!-- Desglose SUNAT de Totales -->
    <table class="totals-grid">
        <tr>
            <td style="width: 58%;">OP. GRAVADAS</td>
            <td style="width: 42%; text-align: right;">S/ <fmt:formatNumber value="${not empty venta.subtotal ? venta.subtotal : 10.17}" pattern="0.00"/></td>
        </tr>
        <tr>
            <td style="width: 58%;">OP. GRATUITAS</td>
            <td style="width: 42%; text-align: right;">S/ 00.00</td>
        </tr>
        <tr>
            <td style="width: 58%;">OP. EXONERADAS</td>
            <td style="width: 42%; text-align: right;">S/ 00.00</td>
        </tr>
        <tr>
            <td style="width: 58%;">OP. INAFECTAS</td>
            <td style="width: 42%; text-align: right;">S/ 00.00</td>
        </tr>
        <tr>
            <td style="width: 58%;">I.G.V</td>
            <td style="width: 42%; text-align: right;">S/ <fmt:formatNumber value="${not empty venta.igv ? venta.igv : 1.83}" pattern="0.00"/></td>
        </tr>
        <tr>
            <td style="width: 58%;">SUB TOTAL</td>
            <td style="width: 42%; text-align: right;">S/ <fmt:formatNumber value="${not empty venta.subtotal ? venta.subtotal : 10.17}" pattern="0.00"/></td>
        </tr>
        <tr style="font-weight: 800; font-size: 12.5px;">
            <td style="width: 58%;">TOTAL VENTA</td>
            <td style="width: 42%; text-align: right;">S/ <fmt:formatNumber value="${not empty venta.montoTotal ? venta.montoTotal : 12.00}" pattern="0.00"/></td>
        </tr>
    </table>

    <!-- Monto en Letras -->
    <div class="monto-letras" id="montoEnLetrasText">
        DOS CON 40/100 PEN
    </div>

    <div class="ticket-divider"></div>

    <!-- Nombre del Vendedor -->
    <div style="font-size: 11px; font-weight: 700;">
        VENDEDOR(A): ${not empty venta.usernameUsuario ? venta.usernameUsuario : (not empty sessionScope.usuarioLogueado.nombres ? sessionScope.usuarioLogueado.nombres : 'QF. MARTINEZ')}
    </div>

    <div class="ticket-divider"></div>

    <!-- Leyenda SUNAT y Código QR -->
    <div class="ticket-footer">
        Representacion impresa de la <c:choose><c:when test="${not empty venta.codigoVenta && venta.codigoVenta.startsWith('F')}">factura</c:when><c:otherwise>boleta</c:otherwise></c:choose> electrónica<br>
        Gracias por su preferencia
    </div>

    <div class="qr-container">
        <img id="qrImage" src="https://api.qrserver.com/v1/create-qr-code/?size=120x120&data=RUC20601234567|${not empty venta.codigoVenta ? venta.codigoVenta : 'F002-00003848'}|${not empty venta.montoTotal ? venta.montoTotal : '12.00'}" alt="Código QR SUNAT" style="width: 90px; height: 90px; display: block; margin: 4px auto 0 auto;">
    </div>
</div>

<script>
    function numeroALetras(monto) {
        const unidades = ["", "UN", "DOS", "TRES", "CUATRO", "CINCO", "SEIS", "SIETE", "OCHO", "NUEVE"];
        const decenas = ["", "DIEZ", "VEINTE", "TREINTA", "CUARENTA", "CINCUENTA", "SESENTA", "SETENTA", "OCHENTA", "NOVENTA"];
        const especiales = {
            11: "ONCE", 12: "DOCE", 13: "TRECE", 14: "CATORCE", 15: "QUINCE",
            16: "DIECISEIS", 17: "DIECISIETE", 18: "DIECIOCHO", 19: "DIECINUEVE"
        };
        
        let num = parseFloat(monto) || 0;
        let entero = Math.floor(num);
        let centavos = Math.round((num - entero) * 100);
        let centavosText = String(centavos).padStart(2, '0') + "/100 PEN";

        if (entero === 0) return "CERO " + centavosText;
        if (entero === 100) return "CIEN " + centavosText;

        let texto = "";
        if (entero >= 100) {
            let c = Math.floor(entero / 100);
            if (c === 1) texto += "CIENTO ";
            else if (c === 2) texto += "DOSCIENTOS ";
            else if (c === 5) texto += "QUINIENTOS ";
            else texto += unidades[c] + "CIENTOS ";
            entero %= 100;
        }

        if (especiales[entero]) {
            texto += especiales[entero] + " ";
        } else if (entero >= 10) {
            let d = Math.floor(entero / 10);
            let u = entero % 10;
            if (u === 0) texto += decenas[d] + " ";
            else texto += decenas[d] + " Y " + unidades[u] + " ";
        } else if (entero > 0) {
            texto += unidades[entero] + " ";
        }

        return texto.trim() + " CON " + centavosText;
    }

    document.addEventListener("DOMContentLoaded", function() {
        // Formatear Fecha si aún viene como timestamp
        const fechaElem = document.getElementById('fechaVentaSpan');
        if (fechaElem && fechaElem.innerText) {
            const rawFecha = fechaElem.innerText.trim();
            if (/^\d+$/.test(rawFecha)) {
                const dateObj = new Date(parseInt(rawFecha));
                if (!isNaN(dateObj.getTime())) {
                    const pad = (n) => String(n).padStart(2, '0');
                    const formattedDate = pad(dateObj.getDate()) + '/' + 
                                          pad(dateObj.getMonth() + 1) + '/' + 
                                          dateObj.getFullYear() + ' ' + 
                                          pad(dateObj.getHours()) + ':' + 
                                          pad(dateObj.getMinutes()) + ':' + 
                                          pad(dateObj.getSeconds());
                    fechaElem.innerText = formattedDate;
                }
            }
        }

        const totalVenta = "${not empty venta.montoTotal ? venta.montoTotal : (not empty venta.total ? venta.total : '12.00')}";
        document.getElementById('montoEnLetrasText').innerText = numeroALetras(totalVenta);
    });

    function downloadPDFTicket() {
        const element = document.getElementById('ticketContainer');
        const filename = 'Ticket_' + ('${not empty venta.codigoVenta ? venta.codigoVenta : "Comprobante"}') + '.pdf';
        const opt = {
            margin:       [2, 2, 2, 2],
            filename:     filename,
            image:        { type: 'jpeg', quality: 0.98 },
            html2canvas:  { scale: 2, useCORS: true, windowWidth: 290 },
            jsPDF:        { unit: 'mm', format: [80, 200], orientation: 'portrait' }
        };
        html2pdf().set(opt).from(element).save();
    }
</script>

<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
