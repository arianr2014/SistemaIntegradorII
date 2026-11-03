<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fn" uri="jakarta.tags.functions"%>
<% request.setAttribute("pageTitle", "Horarios"); %>
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
                Fase 4:
                Se muestra el módulo actual, el usuario autenticado y el rol activo.
            -->
            <div class="d-flex flex-column flex-md-row align-items-md-center gap-2 ms-auto">
                <span class="text-white small">Módulo: Horarios</span>
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
                    <div class="card-body p-4 d-flex justify-content-between align-items-center flex-wrap gap-3">
                        <div>
                            <h3 class="fw-bold mb-1"><i class="bi bi-clock-history me-2"></i> Horarios Médicos</h3>
                            <p class="mb-0 text-white-50">Programación de turnos, disponibilidades horarias y bloques de atención.</p>
                        </div>
                        <div class="d-flex align-items-center gap-2">
                            <span class="badge bg-white text-primary fs-6 px-3 py-2 rounded-pill shadow-sm d-none d-md-inline-block">
                                <i class="bi bi-calendar3 me-1"></i> Programación
                            </span>
                            <a href="${pageContext.request.contextPath}/horarios?accion=form" class="btn btn-light text-primary fw-bold px-3 py-2 rounded-pill shadow-sm">
                                <i class="bi bi-plus-lg me-1"></i> Nuevo Horario
                            </a>
                        </div>
                    </div>
                </div>

                <!--
                    Fase 5:
                    Se escapa el valor del buscador para evitar que texto malicioso
                    quede reflejado dentro del atributo value del input.
                -->
                <div class="card mb-3 border-0 shadow-sm">
                    <div class="card-body">
                        <form method="get"
                              action="${pageContext.request.contextPath}/horarios"
                              class="row g-2">

                            <input type="hidden" name="accion" value="listar">

                            <div class="col-12 col-md-8">
                                <input type="text"
                                       class="form-control"
                                       name="q"
                                       placeholder="Buscar por médico, especialidad o dia"
                                       value="${fn:escapeXml(q)}">
                            </div>

                            <div class="col-12 col-md-4 d-grid d-md-flex gap-2 justify-content-md-end">
                                <button type="submit" class="btn btn-success">
                                    <i class="bi bi-search"></i> Buscar
                                </button>

                                <a href="${pageContext.request.contextPath}/horarios"
                                   class="btn btn-outline-success">
                                    Limpiar
                                </a>
                            </div>
                        </form>
                    </div>
                </div>

                <!--
                    Mensajes funcionales.
                    Se reemplaza el scriptlet de Java por JSTL.
                -->
                <c:choose>
                    <c:when test="${param.msg == 'ok'}">
                        <div class="alert alert-success">Operación realizada correctamente.</div>
                    </c:when>

                    <c:when test="${param.msg == 'deleted'}">
                        <div class="alert alert-warning">Horario eliminado correctamente.</div>
                    </c:when>

                    <c:when test="${param.msg == 'noexiste'}">
                        <div class="alert alert-info">El horario solicitado no existe.</div>
                    </c:when>

                    <c:when test="${param.msg == 'invalid'}">
                        <div class="alert alert-danger">Solicitud inválida.</div>
                    </c:when>

                    <c:when test="${param.msg == 'metodo_invalido'}">
                        <div class="alert alert-danger">Operación no permitida por este metodo.</div>
                    </c:when>

                    <c:when test="${param.msg == 'errorDelete'}">
                        <div class="alert alert-danger">
                            No se pudo eliminar el horario porque esta relacionado con otros registros.
                        </div>
                    </c:when>
                </c:choose>

                <div class="card border-0 shadow-sm">
                    <div class="card-body table-responsive">
                        <table class="table table-hover align-middle table-green">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Médico</th>
                                    <th>Especialidad</th>
                                    <th>Dia</th>
                                    <th>Inicio</th>
                                    <th>Fin</th>
                                    <th class="text-center">Acciones</th>
                                </tr>
                            </thead>

                            <tbody>
                                <c:choose>
                                    <c:when test="${empty horarios}">
                                        <tr>
                                            <td colspan="7" class="text-center text-muted py-4">
                                                No hay registros.
                                            </td>
                                        </tr>
                                    </c:when>

                                    <c:otherwise>
                                        <c:forEach var="h" items="${horarios}">
                                            <tr>
                                                <!--
                                                    Fase 5:
                                                    Se usa c:out para escapar datos antes de mostrarlos.
                                                    Esto ayuda a prevenir Cross-Site Scripting, XSS.
                                                -->
                                                <td><c:out value="${h.idHorario}" /></td>
                                                <td><c:out value="${h.medicoNombreCompleto}" /></td>
                                                <td><c:out value="${h.especialidad}" /></td>
                                                <td class="text-capitalize">
                                                    <c:out value="${h.dia}" />
                                                </td>
                                                <td><c:out value="${h.horaInicio}" /></td>
                                                <td><c:out value="${h.horaFin}" /></td>

                                                <td class="text-center">
                                                    <a href="${pageContext.request.contextPath}/horarios?accion=editar&id=${h.idHorario}"
                                                       class="btn btn-outline-success btn-sm"
                                                       title="Editar horario">
                                                        <i class="bi bi-pencil"></i>
                                                    </a>

                                                    <!--
                                                        Fase 3:
                                                        La eliminación se mantiene mediante POST.
                                                        No se ejecuta por enlace GET.
                                                    -->
                                                    <form action="${pageContext.request.contextPath}/horarios"
                                                          method="post"
                                                          class="d-inline"><%@ include file="/WEB-INF/views/layout/csrf-token.jspf" %>

                                                        <input type="hidden" name="accion" value="eliminar">
                                                        <input type="hidden" name="id" value="${h.idHorario}">

                                                        <button type="submit"
                                                                class="btn btn-outline-danger btn-sm"
                                                                title="Eliminar horario"
                                                                onclick="return confirm('¿Eliminar horario?');">
                                                            <i class="bi bi-x-lg"></i>
                                                        </button>
                                                    </form>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>

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
    </main>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

    <script>
        document.addEventListener("DOMContentLoaded", function () {

            /*
                Paginado simple del lado del cliente.
                Muestra 10 registros por página.
            */
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
                            + " registros";

                    renderControles();
                }

                function renderControles() {
                    controles.innerHTML = "";

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