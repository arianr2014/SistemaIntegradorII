<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Iniciar Sesión - NovaSalud Farmacia</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body class="login-body">
    <div class="login-card">
        <div class="login-brand">
            <div class="logo">
                <i class="fa-solid fa-pills"></i>
            </div>
            <h2 style="font-size: 24px; font-weight: 800; color: #0f172a;">NovaSalud Farmacia</h2>
            <p style="color: #64748b; font-size: 14px; margin-top: 4px;">Ingrese sus credenciales de acceso</p>
        </div>

        <c:if test="${not empty error}">
            <div class="alert-danger">
                <i class="fa-solid fa-triangle-exclamation"></i> ${error}
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/login" method="post">
            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}"/>

            <div class="form-group">
                <label class="form-label" for="username">Usuario</label>
                <input type="text" id="username" name="username" class="form-control" placeholder="Ej. admin.farmacia" required autofocus>
            </div>

            <div class="form-group">
                <label class="form-label" for="password">Contraseña</label>
                <input type="password" id="password" name="password" class="form-control" placeholder="••••••••" required>
            </div>

            <button type="submit" class="btn btn-primary" style="width: 100%; justify-content: center; padding: 12px; margin-top: 8px; font-size: 15px;">
                <i class="fa-solid fa-right-to-bracket"></i> Iniciar Sesión
            </button>
        </form>
    </div>
</body>
</html>
