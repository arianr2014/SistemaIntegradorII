<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${param.title != null ? param.title : 'NovaSalud - Farmacia'}</title>
    <!-- Fonts & Icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<div class="app-container">
    <%@ include file="/WEB-INF/views/layout/sidebar.jsp" %>
    <main class="main-content">
        <header class="top-header">
            <div class="header-title">
                <h1>${param.pageTitle != null ? param.pageTitle : 'Panel Principal'}</h1>
            </div>
            <div class="header-user">
                <div class="user-info">
                    <div class="user-name">${sessionScope.usuarioLogueado.nombres} ${sessionScope.usuarioLogueado.apellidos}</div>
                    <div class="user-role">${sessionScope.usuarioLogueado.rol}</div>
                </div>
                <div class="avatar">
                    ${sessionScope.usuarioLogueado.nombres.substring(0,1)}
                </div>
            </div>
        </header>
        <div class="content-body">
