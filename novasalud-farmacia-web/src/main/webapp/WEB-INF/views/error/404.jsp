<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>404 - Página no encontrada</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body class="login-body">
    <div class="login-card" style="text-align: center;">
        <i class="fa-solid fa-compass" style="font-size: 64px; color: #f59e0b; margin-bottom: 16px;"></i>
        <h1 style="font-size: 32px; font-weight: 800; color: #0f172a;">Error 404</h1>
        <p style="color: #64748b; margin: 12px 0 24px;">La página o recurso que buscas no existe o ha sido movido.</p>
        <a href="${pageContext.request.contextPath}/home" class="btn btn-primary" style="justify-content: center;">
            <i class="fa-solid fa-house"></i> Volver al Inicio
        </a>
    </div>
</body>
</html>
