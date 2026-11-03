<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>

<c:set var="pageTitle" value="Login - NovaSalud V3.2.1" scope="request" />

<!DOCTYPE html>
<html lang="es">
<%@ include file="/WEB-INF/views/layout/head.jspf" %>
<body class="login-page">
    <div class="container-fluid min-vh-100 d-flex align-items-center justify-content-center py-4">
        <div class="login-shell shadow-lg">
            <div class="row g-0 h-100">

                <aside class="col-12 col-lg-5 login-panel-left">
                    <div class="login-panel-inner">
                        <div class="login-brand mb-4">
                            <div class="login-brand-icon">
                                <i class="bi bi-heart-pulse-fill"></i>
                            </div>
                            <div>
                                <h2 class="login-brand-title mb-0">NOVASALUD</h2>
                                <p class="login-brand-subtitle mb-0">
                                    Sistema de Gesti&oacute;n de Citas M&eacute;dicas
                                </p>
                            </div>
                        </div>

                        <h3 class="login-welcome-title">Bienvenido</h3>

                        <p class="login-welcome-text">
                            Accede al sistema para gestionar pacientes, m&eacute;dicos y citas m&eacute;dicas de manera eficiente y segura.
                        </p>

                        <ul class="login-feature-list list-unstyled mb-0">
                            <li>
                                <i class="bi bi-calendar2-check"></i>
                                <div>
                                    <strong>Gesti&oacute;n de Citas</strong>
                                    <span>Agenda y administra citas m&eacute;dicas.</span>
                                </div>
                            </li>

                            <li>
                                <i class="bi bi-people"></i>
                                <div>
                                    <strong>Pacientes y M&eacute;dicos</strong>
                                    <span>Registra y consulta informaci&oacute;n.</span>
                                </div>
                            </li>

                            <li>
                                <i class="bi bi-shield-lock"></i>
                                <div>
                                    <strong>Acceso Seguro</strong>
                                    <span>Protegemos tus datos.</span>
                                </div>
                            </li>
                        </ul>
                    </div>
                </aside>

                <section class="col-12 col-lg-7 login-panel-right">
                    <div class="login-form-wrap">
                        <h1 class="login-form-title">Iniciar sesi&oacute;n</h1>
                        <p class="login-form-subtitle">Ingresa tus credenciales para continuar</p>

                        <c:if test="${not empty param.logout}">
                            <div class="alert alert-success py-2">
                                Sesi&oacute;n cerrada correctamente.
                            </div>
                        </c:if>

                        <c:if test="${param.error == 'sesion_invalida'}">
                            <div class="alert alert-warning py-2">
                                Tu sesión fue cerrada porque el usuario fue modificado o desactivado por administración. Ingresa nuevamente.
                            </div>
                        </c:if>

                        <c:if test="${not empty error}">
                            <div class="alert alert-danger py-2">
                                <c:out value="${error}" />
                            </div>
                        </c:if>

                        <form method="post" action="${pageContext.request.contextPath}/login" class="login-form"><%@ include file="/WEB-INF/views/layout/csrf-token.jspf" %>
                            <div class="mb-3">
                                <label class="form-label fw-semibold">Usuario</label>
                                <div class="input-group">
                                    <span class="input-group-text">
                                        <i class="bi bi-person"></i>
                                    </span>
                                    <input type="text"
                                           name="username"
                                           class="form-control"
                                           placeholder="Ingrese su usuario"
                                           autocomplete="username"
                                           required>
                                </div>
                            </div>

                            <div class="mb-3">
                                <label class="form-label fw-semibold">Contrase&ntilde;a</label>
                                <div class="input-group">
                                    <span class="input-group-text">
                                        <i class="bi bi-lock"></i>
                                    </span>
                                    <input type="password"
                                           name="password"
                                           id="password"
                                           class="form-control"
                                           placeholder="Ingrese su contrase&ntilde;a"
                                           autocomplete="current-password"
                                           required>
                                    <button class="btn btn-outline-secondary" type="button" id="togglePassword" aria-label="Mostrar u ocultar contrase&ntilde;a">
                                        <i class="bi bi-eye"></i>
                                    </button>
                                </div>
                            </div>

                            <button type="submit" class="btn btn-success w-100 login-submit-btn">
                                Ingresar
                            </button>
                        </form>

                        <p class="login-footer-note mb-0">NOVASALUD 2026</p>
                    </div>
                </section>

            </div>
        </div>
    </div>
    <script>
        // Mejora UX: permite mostrar u ocultar la contraseña sin agregar campos innecesarios al login.
        (function () {
            const input = document.getElementById('password');
            const button = document.getElementById('togglePassword');
            if (!input || !button) return;
            button.addEventListener('click', function () {
                const visible = input.type === 'text';
                input.type = visible ? 'password' : 'text';
                button.innerHTML = visible ? '<i class="bi bi-eye"></i>' : '<i class="bi bi-eye-slash"></i>';
            });
        })();
    </script>
</body>
</html>
