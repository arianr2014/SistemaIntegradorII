<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fn" uri="jakarta.tags.functions"%>
<c:set var="pageTitle" value="Historias Clínicas - NovaSalud V3.2.1" scope="request" />
<c:set var="moduloActivo" value="historias" scope="request" />
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
            <div class="d-flex flex-column flex-md-row align-items-md-center gap-2 ms-auto">
                <span class="text-white small">Módulo: Historias Clínicas</span>
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
                <div class="card mb-4 border-0 shadow-sm" style="border-radius: 12px; background: linear-gradient(135deg, #1e3a8a 0%, #3b82f6 100%); color: #ffffff;">
                    <div class="card-body p-4 d-flex justify-content-between align-items-center">
                        <div>
                            <h3 class="fw-bold mb-1"><i class="bi bi-file-earmark-medical me-2"></i> Historias Clínicas</h3>
                            <p class="mb-0 text-white-50">Visualiza el expediente clínico, antecedentes y atenciones registradas de cada paciente.</p>
                        </div>
                        <div>
                            <span class="badge bg-white text-primary fs-6 px-3 py-2 rounded-pill shadow-sm">
                                <i class="bi bi-person-workspace me-1"></i> Expediente Digital
                            </span>
                        </div>
                    </div>
                </div>

                <!-- Buscador de Historias -->
                <div class="card mb-4 border-0 shadow-sm" style="border-radius: 10px;">
                    <div class="card-body p-3">
                        <form method="GET" action="${pageContext.request.contextPath}/historias-clinicas" class="row g-3 align-items-center">
                            <div class="col-md-9">
                                <div class="input-group">
                                    <span class="input-group-text bg-light border-end-0"><i class="bi bi-search text-muted"></i></span>
                                    <input type="text" name="buscar" class="form-control border-start-0 bg-light" placeholder="Buscar por DNI, Código de Historia o Nombre del Paciente..." value="${filtro}">
                                </div>
                            </div>
                            <div class="col-md-3 d-flex gap-2">
                                <button type="submit" class="btn btn-primary w-100 fw-semibold">
                                    <i class="bi bi-search me-1"></i> Buscar
                                </button>
                                <c:if test="${not empty filtro}">
                                    <a href="${pageContext.request.contextPath}/historias-clinicas" class="btn btn-outline-secondary">
                                        <i class="bi bi-x-lg"></i>
                                    </a>
                                </c:if>
                            </div>
                        </form>
                    </div>
                </div>

                <!-- Tabla de Pacientes / Historias -->
                <div class="card border-0 shadow-sm" style="border-radius: 10px; overflow: hidden;">
                    <div class="card-body p-0">
                        <div class="table-responsive">
                            <table class="table table-hover align-middle mb-0">
                                <thead class="table-light">
                                    <tr>
                                        <th class="ps-4" style="width: 15%;">Código Historia</th>
                                        <th style="width: 12%;">DNI</th>
                                        <th style="width: 28%;">Paciente</th>
                                        <th style="width: 25%;">Teléfono / Correo</th>
                                        <th class="text-end pe-4" style="width: 20%;">Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:choose>
                                        <c:when test="${not empty historias}">
                                            <c:forEach items="${historias}" var="h">
                                                <tr>
                                                    <td class="ps-4">
                                                        <span class="badge bg-primary-subtle text-primary fw-bold fs-6 px-2.5 py-1">
                                                            #${not empty h.codigoHistoria ? h.codigoHistoria : h.paciente.dni}
                                                        </span>
                                                    </td>
                                                    <td class="fw-semibold text-dark">${h.paciente.dni}</td>
                                                    <td>
                                                        <div class="fw-bold text-dark">${h.paciente.nombres} ${h.paciente.apellidos}</div>
                                                        <small class="text-muted"><i class="bi bi-gender-ambiguous me-1"></i>${h.paciente.sexo} | <i class="bi bi-calendar3 me-1"></i>${h.paciente.fechaNacimiento}</small>
                                                    </td>
                                                    <td>
                                                        <div><i class="bi bi-telephone text-muted me-1"></i>${h.paciente.telefono}</div>
                                                        <small class="text-muted"><i class="bi bi-envelope me-1"></i>${h.paciente.correo}</small>
                                                    </td>
                                                    <td class="text-end pe-4">
                                                        <a href="${pageContext.request.contextPath}/historia-clinica?idPaciente=${h.idPaciente}" class="btn btn-sm btn-primary fw-semibold">
                                                            <i class="bi bi-folder2-open me-1"></i> Ver Historia Clínica
                                                        </a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <tr>
                                                <td colspan="5" class="text-center py-5 text-muted">
                                                    <i class="bi bi-folder2-open display-4 mb-3 text-secondary d-block"></i>
                                                    <h5>No se encontraron historias clínicas</h5>
                                                    <p class="mb-0">Ingresa otro criterio de búsqueda o verifica que existan pacientes registrados en el sistema.</p>
                                                </td>
                                            </tr>
                                        </c:otherwise>
                                    </c:choose>
                                </tbody>
                            </table>
                        </div>
                        <div class="px-3 pb-3">
                            <nav id="paginacion"
                                 class="mt-3 d-flex flex-column align-items-center gap-2"
                                 style="display:none">
                                <span id="pag-info" class="text-muted small text-center"></span>
                                <ul class="pagination pagination-sm mb-0 justify-content-center"
                                    id="pag-controles"></ul>
                            </nav>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        document.addEventListener("DOMContentLoaded", function () {
            (function () {
                const FILAS_POR_PAGINA = 10;
                const tbody = document.querySelector(".table tbody");

                if (!tbody) {
                    return;
                }

                const filas = Array.from(tbody.querySelectorAll("tr")).filter(function (tr) {
                    return tr.querySelectorAll("td").length > 1;
                });

                if (filas.length === 0) {
                    return;
                }

                const totalPaginas = Math.ceil(filas.length / FILAS_POR_PAGINA);
                let paginaActual = 1;

                const paginacion = document.getElementById("paginacion");
                const info = document.getElementById("pag-info");
                const controles = document.getElementById("pag-controles");

                paginacion.style.display = "flex";

                function mostrarPagina(pagina) {
                    paginaActual = pagina;

                    const inicio = (pagina - 1) * FILAS_POR_PAGINA;
                    const fin = inicio + FILAS_POR_PAGINA;

                    filas.forEach(function (tr, i) {
                        tr.style.display = (i >= inicio && i < fin) ? "" : "none";
                    });

                    info.textContent = "Mostrando "
                            + (inicio + 1)
                            + " - "
                            + Math.min(fin, filas.length)
                            + " de "
                            + filas.length
                            + " historias clínicas";

                    renderControles();
                }

                function renderControles() {
                    controles.innerHTML = "";

                    if (totalPaginas <= 1) {
                        return;
                    }

                    var prev = document.createElement("li");
                    prev.className = "page-item" + (paginaActual === 1 ? " disabled" : "");
                    prev.innerHTML = '<a class="page-link" href="#">&laquo;</a>';

                    prev.addEventListener("click", function (e) {
                        e.preventDefault();
                        if (paginaActual > 1) {
                            mostrarPagina(paginaActual - 1);
                        }
                    });

                    controles.appendChild(prev);

                    for (var p = 1; p <= totalPaginas; p++) {
                        (function (pg) {
                            var li = document.createElement("li");
                            li.className = "page-item" + (pg === paginaActual ? " active" : "");
                            li.innerHTML = '<a class="page-link" href="#">' + pg + '</a>';

                            li.addEventListener("click", function (e) {
                                e.preventDefault();
                                mostrarPagina(pg);
                            });

                            controles.appendChild(li);
                        })(p);
                    }

                    var next = document.createElement("li");
                    next.className = "page-item" + (paginaActual === totalPaginas ? " disabled" : "");
                    next.innerHTML = '<a class="page-link" href="#">&raquo;</a>';

                    next.addEventListener("click", function (e) {
                        e.preventDefault();
                        if (paginaActual < totalPaginas) {
                            mostrarPagina(paginaActual + 1);
                        }
                    });

                    controles.appendChild(next);
                }

                mostrarPagina(1);
            })();
        });
    </script>
</body>
</html>
