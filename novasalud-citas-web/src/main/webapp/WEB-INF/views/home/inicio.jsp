<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>

<c:set var="pageTitle" value="Inicio - Citas Médicas" scope="request" />

<!DOCTYPE html>
<html lang="es">
<%@ include file="/WEB-INF/views/layout/head.jspf" %>
<body class="bg-light">

    <nav class="navbar navbar-expand-lg navbar-dark bg-success shadow-sm">
        <div class="container-fluid px-4">

            <button class="btn btn-outline-light d-lg-none me-2 mobile-menu-toggle"
                    type="button"
                    data-bs-toggle="offcanvas"
                    data-bs-target="#sidebarMenu"
                    aria-controls="sidebarMenu"
                    aria-label="Abrir menu">
                <i class="bi bi-list fs-4"></i>
            </button>

            <span class="navbar-brand fw-semibold">NovaSalud V3.2.1</span>

            <!--
                Fase 5.1:
                Se mantiene el módulo actual, usuario autenticado y rol activo
                sin usar salidas directas JSP.
            -->
            <div class="d-flex flex-column flex-md-row align-items-md-center gap-2 ms-auto">
                <span class="text-white small">Módulo: Inicio</span>
                <%@ include file="/WEB-INF/views/layout/usuario-sesion.jspf" %>
            </div>

        </div>
    </nav>

    <%@ include file="/WEB-INF/views/layout/menu-mobile.jspf" %>

    <main class="container-fluid py-4 px-4">
        <div class="row g-4">

            <div class="col-12 col-lg-3 order-1 order-lg-1">
                <%@ include file="/WEB-INF/views/layout/menu-right.jspf" %>
            </div>

            <div class="col-12 col-lg-9 order-2 order-lg-2">
                <h4 class="text-success fw-bold mb-3">
                    <i class="bi bi-house"></i>
                    Dashboard
                </h4>

                <p class="text-muted mb-3">
                    Resumen operativo del dia y tendencia semanal.
                </p>

                <div class="row g-3 mb-3">

                    <c:forEach var="estado" items="${citasPorEstadoHoy}">
                        <div class="col-12 col-sm-6 col-xl-3">
                            <div class="card border-0 shadow-sm h-100">
                                <div class="card-body">
                                    <div class="text-muted small">Citas hoy</div>

                                    <div class="fw-semibold">
                                        <c:out value="${estado.key}" />
                                    </div>

                                    <div class="display-6 fw-bold text-success">
                                        <c:out value="${estado.value}" />
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>

                </div>

                <div class="row g-3">

                    <div class="col-12 col-xl-4">
                        <div class="card border-0 shadow-sm h-100">
                            <div class="card-body">
                                <h6 class="fw-bold mb-3">
                                    <i class="bi bi-person-badge"></i>
                                    Médicos en turno hoy
                                </h6>

                                <div class="display-5 fw-bold text-success mb-2">
                                    <c:out value="${empty medicosTurnoHoy ? 0 : medicosTurnoHoy}" />
                                </div>

                                <p class="text-muted mb-0">
                                    Cantidad de médicos con citas activas para hoy.
                                </p>
                            </div>
                        </div>
                    </div>

                    <div class="col-12 col-xl-4">
                        <div class="card border-0 shadow-sm h-100">
                            <div class="card-body">
                                <h6 class="fw-bold mb-3">
                                    <i class="bi bi-award"></i>
                                    Top 3 especialidades (semana)
                                </h6>

                                <ul class="list-group list-group-flush">
                                    <c:choose>
                                        <c:when test="${empty topEspecialidades}">
                                            <li class="list-group-item px-0 text-muted">
                                                Sin datos en la semana.
                                            </li>
                                        </c:when>

                                        <c:otherwise>
                                            <c:forEach var="item" items="${topEspecialidades}">
                                                <li class="list-group-item px-0 d-flex justify-content-between align-items-center">
                                                    <span>
                                                        <c:out value="${item.key}" />
                                                    </span>

                                                    <span class="badge bg-success rounded-pill">
                                                        <c:out value="${item.value}" />
                                                    </span>
                                                </li>
                                            </c:forEach>
                                        </c:otherwise>
                                    </c:choose>
                                </ul>
                            </div>
                        </div>
                    </div>

                    <div class="col-12 col-xl-4">
                        <div class="card border-0 shadow-sm h-100">
                            <div class="card-body">
                                <h6 class="fw-bold mb-3">
                                    <i class="bi bi-bar-chart"></i>
                                    Resumen semanal
                                </h6>

                                <div class="dashboard-chart-wrap">
                                    <canvas id="chartResumenSemanal"></canvas>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>
            </div>

        </div>
    </main>

    <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.3/dist/chart.umd.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

    <script>
        (function () {
            /*
                Fase 5.1:
                Datos del resumen semanal renderizados con JSTL.
                Se elimina el uso de salidas directas JSP dentro del script.
            */
            const labels = [
                <c:forEach var="punto" items="${resumenSemanal}" varStatus="estadoLoop">
                    "<c:out value="${punto.key}" />"<c:if test="${not estadoLoop.last}">,</c:if>
                </c:forEach>
            ];

            const data = [
                <c:forEach var="punto" items="${resumenSemanal}" varStatus="estadoLoop">
                    <c:out value="${punto.value}" /><c:if test="${not estadoLoop.last}">,</c:if>
                </c:forEach>
            ];

            const canvas = document.getElementById("chartResumenSemanal");

            if (!canvas) {
                return;
            }

            new Chart(canvas, {
                type: "line",
                data: {
                    labels,
                    datasets: [{
                        label: "Citas",
                        data,
                        borderColor: "#3f8fd6",
                        backgroundColor: "rgba(63, 143, 214, 0.2)",
                        borderWidth: 2,
                        tension: 0.35,
                        fill: true,
                        pointRadius: 3
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    animation: false,
                    resizeDelay: 200,
                    plugins: {
                        legend: {
                            display: false
                        }
                    },
                    scales: {
                        y: {
                            beginAtZero: true,
                            ticks: {
                                precision: 0
                            }
                        }
                    }
                }
            });
        })();
    </script>

</body>
</html>