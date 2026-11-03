<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>403 - Acceso Denegado</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body class="login-body">
    <div class="login-card" style="text-align: center;">
        <i class="fa-solid fa-user-lock" style="font-size: 64px; color: #ef4444; margin-bottom: 16px;"></i>
        <h1 style="font-size: 32px; font-weight: 800; color: #0f172a;">Acceso Denegado</h1>
        <p style="color: #64748b; margin: 12px 0 24px;">No tienes los permisos suficientes o el token es inválido.</p>
        <a href="${pageContext.request.contextPath}/home" class="btn btn-primary" style="justify-content: center;">
            <i class="fa-solid fa-house"></i> Volver al Inicio
        </a>
    </div>
</body>
</html>
