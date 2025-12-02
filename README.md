# ⚽ Premier League Tracker: Interfaz de Usuario (Frontend)

## 📄 Descripción

Este repositorio contiene la capa de **presentación** de la aplicación Premier League Tracker. Es una interfaz de usuario responsiva, diseñada para consumir una API RESTful y visualizar estadísticas, resultados y la tabla de clasificación. **Utiliza HTML, CSS y JavaScript (Vanilla JS).**

### **Estructura del Proyecto**

La estructura refleja la organización de las diferentes vistas de la aplicación:

* **`css/`**: Archivos de estilos para toda la interfaz.
* **`img/`**: Recursos gráficos e imágenes (logos de equipos, iconos, etc.).
* **`js/`**: Lógica de la interfaz y manejo de la API (peticiones `fetch`, manipulación del DOM).
* **`paginaprincipal.html`**: La vista principal de la aplicación (Landing Page).
* **`estadisticas.html`**: Vista dedicada a las estadísticas detalladas (goleadores, asistencias, etc.).
* **`jornadas.html`**: Vista para el calendario y resultados de las jornadas.
* **`resultados.html`**: Vista enfocada únicamente en los resultados de partidos ya disputados.
* **`tabla.html`**: Vista de la tabla de clasificación actual.

## 🚀 Puesta en Marcha

Para visualizar y probar la interfaz de usuario, sigue estos pasos:

### 1. Requisitos Previos

Asegúrate de que el **Backend (Java/Spring Boot)** esté corriendo y accesible en `http://localhost:8080` (o el puerto configurado).

### 2. Ejecución Local

1.  Clona el repositorio:
    ```bash
    git clone https://github.com/drkzzzz/premierleague.git
    ```
2.  Simplemente abre el archivo **`paginaprincipal.html`** en tu navegador web para comenzar.

> **⚠️ Nota:** Si la API del Backend no está en el puerto `8080`, deberás editar el archivo principal de JavaScript (dentro de la carpeta `js/`) y actualizar la URL base de la API para que apunte a la dirección correcta.
