<%-- 
    Document   : acceso-denegado
    Created on : 6 jul. 2026, 10:09:45 a. m.
    Author     : FRANCK
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>

<c:set var="pageTitle" value="Acceso denegado" scope="request" />

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
                Se mantiene visible el contexto de sesión sin usar salidas JSP directas.
            -->
            <div class="d-flex flex-column flex-md-row align-items-md-center gap-2 ms-auto">
                <span class="text-white small">Acceso denegado</span>
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
                <div class="card border-0 shadow-sm">
                    <div class="card-body p-5 text-center">

                        <div class="mb-4">
                            <i class="bi bi-shield-lock text-danger" style="font-size: 4rem;"></i>
                        </div>

                        <h2 class="fw-bold text-danger mb-3">Acceso denegado</h2>

                        <p class="fs-5 text-muted mb-3">
                            No tienes permisos para ingresar al módulo solicitado.
                        </p>

                        <div class="alert alert-warning text-start mx-auto" style="max-width: 700px;">
                            <strong>Detalle:</strong><br>
                            Tu rol actual no tiene autorización para acceder a esta ruta.
                        </div>

                        <div class="text-start mx-auto mb-4" style="max-width: 700px;">
                            <p class="mb-1">
                                <strong>Rol actual:</strong>
                                <c:out value="${rolUsuario}" />
                            </p>

                            <p class="mb-1">
                                <strong>Ruta solicitada:</strong>
                                <c:out value="${rutaBloqueada}" />
                            </p>

                            <p class="mb-0 text-muted">
                                Esta validación forma parte del control de acceso por roles del sistema.
                            </p>
                        </div>

                        <a href="${pageContext.request.contextPath}${rutaInicio}"
                           class="btn btn-success">
                            <i class="bi bi-arrow-left-circle"></i>
                            Volver a mi módulo principal
                        </a>

                        <a href="${pageContext.request.contextPath}/logout"
                           class="btn btn-outline-danger ms-2">
                            <i class="bi bi-box-arrow-right"></i>
                            Cerrar sesion
                        </a>

                    </div>
                </div>
            </div>

        </div>
    </main>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>