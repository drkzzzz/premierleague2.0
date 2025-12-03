// ===============================================
// === 1. CONFIGURACIÓN GLOBAL (DECLARACIÓN ÚNICA)
// ===============================================

// URL base de tu backend Spring Boot (PROXY API)
const BASE_API_URL = 'http://localhost:8080/api';

// ===============================================
// === 2. LÓGICA DE AUTENTICACIÓN Y ADMIN
// ===============================================

// --- FUNCIONES DE AUTENTICACIÓN ---

function getJwtToken() {
    return localStorage.getItem('accessToken');
}

function checkAdminAccess() {
    // Si no es la página de login y no hay token, redirigir al login
    if (!window.location.pathname.includes('admin_login.html') && !getJwtToken()) {
        window.location.href = '../premieradmin/admin_login.html'; // Ajustar ruta
        return false;
    }
    return true;
}

function handleLogin(username, password) {
    const loginUrl = `${BASE_API_URL}/auth/login`;

    $.ajax({
        url: loginUrl,
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({ nombreUsuario: username, password: password }),
        success: function(response) {
            if (response.success && response.data) {
                // Guardar el token, refresh token e información del usuario
                localStorage.setItem('accessToken', response.data.accessToken);
                localStorage.setItem('refreshToken', response.data.refreshToken);
                localStorage.setItem('idUsuario', response.data.idUsuario);
                localStorage.setItem('nombreUsuario', response.data.nombreUsuario);
                localStorage.setItem('email', response.data.email);

                $('#login-mensaje').removeClass('error').text('Login exitoso. Redirigiendo...');
                
                // Redirigir al dashboard si el login es exitoso
                setTimeout(() => {
                    window.location.href = '../premieradmin/admin_dashboard.htm'; 
                }, 1000);
            } else {
                $('#login-mensaje').text('Error: Respuesta inesperada del servidor').addClass('error');
            }
        },
        error: function(xhr) {
            let errorMsg = 'Error al iniciar sesión. Credenciales incorrectas o servidor no disponible.';
            if (xhr.responseJSON && xhr.responseJSON.message) {
                errorMsg = xhr.responseJSON.message;
            } else if (xhr.status === 401) {
                errorMsg = 'Usuario o contraseña inválida.';
            } else if (xhr.status === 0) {
                errorMsg = 'No se puede conectar al servidor. Verifica que el backend está corriendo en http://localhost:8080';
            }
            $('#login-mensaje').text(errorMsg).addClass('error');
            console.error('Error de login:', xhr);
        }
    });
}

function handleLogout() {
    const logoutUrl = `${BASE_API_URL}/auth/logout`;
    const refreshToken = localStorage.getItem('refreshToken');

    $.ajax({
        url: logoutUrl,
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({ refreshToken: refreshToken }),
        complete: function() {
            // Limpiar localStorage
            localStorage.removeItem('accessToken');
            localStorage.removeItem('refreshToken');
            localStorage.removeItem('idUsuario');
            localStorage.removeItem('nombreUsuario');
            localStorage.removeItem('email');
            
            alert('Sesión cerrada.');
            // Redirigir al login del admin
            window.location.href = '../premieradmin/admin_login.html';
        }
    });
}

// --- FUNCIONES DEL PANEL ADMIN ---

// --- FUNCIONES DEL CRUD DE USUARIOS EN ADMIN ---

let usuariosGlobal = []; // Guardar usuarios para búsqueda
let usuarioAEliminar = null; // Guardar ID del usuario a eliminar

function loadAdminUsers() {
    const usersUrl = `${BASE_API_URL}/admin/usuarios`;
    const token = getJwtToken();

    $.ajax({
        url: usersUrl,
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`
        },
        success: function(response) {
            console.log('Usuarios cargados:', response);
            
            // El backend retorna un array directamente, no en .data
            let usuarios = Array.isArray(response) ? response : (response.data || []);
            usuariosGlobal = usuarios;
            
            if (usuarios && usuarios.length > 0) {
                renderizarTablaUsuarios(usuarios);
            } else {
                $('#usuarios-table-body').html('<tr><td colspan="6" style="text-align: center; padding: 2rem;">No hay usuarios registrados</td></tr>');
            }
        },
        error: function(xhr) {
            console.error('Error al cargar usuarios:', xhr);
            showAlert('Error al cargar los usuarios', 'error');
            $('#usuarios-table-body').html('<tr><td colspan="6" style="text-align: center; padding: 2rem; color: red;">Error al cargar usuarios</td></tr>');
        }
    });
}

function renderizarTablaUsuarios(usuarios) {
    const $tablaUsuarios = $('#usuarios-table-body');
    $tablaUsuarios.empty();

    if (!usuarios || usuarios.length === 0) {
        $tablaUsuarios.html('<tr><td colspan="6" style="text-align: center; padding: 2rem;">No hay usuarios</td></tr>');
        return;
    }

    usuarios.forEach(usuario => {
        const fechaRegistro = usuario.fechaRegistro ? new Date(usuario.fechaRegistro).toLocaleDateString('es-ES') : '-';
        const rolNombre = usuario.rol && usuario.rol.nombre ? usuario.rol.nombre : 'N/A';
        
        const fila = `
            <tr>
                <td>${usuario.idUsuario || usuario.id || '-'}</td>
                <td>${usuario.nombreUsuario || '-'}</td>
                <td>${usuario.email || '-'}</td>
                <td>${rolNombre}</td>
                <td>${fechaRegistro}</td>
                <td>
                    <button class="btn-accion btn-editar" onclick="editarUsuario(${usuario.idUsuario || usuario.id})">Editar</button>
                    <button class="btn-accion btn-eliminar" onclick="abrirModalEliminar(${usuario.idUsuario || usuario.id})">Eliminar</button>
                </td>
            </tr>
        `;
        $tablaUsuarios.append(fila);
    });
}

function mostrarFormularioAgregar() {
    $('#usuario-id').val('');
    $('#usuario-nombre').val('');
    $('#usuario-nombre').prop('readonly', false);
    $('#usuario-email').val('');
    $('#usuario-rol').val('');
    $('#modal-title').text('Agregar Usuario');
    $('#usuario-nombre-hint').text('');
    $('#usuario-modal').addClass('show');
}

function cerrarModal() {
    $('#usuario-modal').removeClass('show');
    $('#usuario-form')[0].reset();
}

function editarUsuario(usuarioId) {
    const usuario = usuariosGlobal.find(u => (u.idUsuario || u.id) === usuarioId);
    
    if (!usuario) {
        showAlert('Usuario no encontrado', 'error');
        return;
    }
    
    $('#usuario-id').val(usuario.idUsuario || usuario.id);
    $('#usuario-nombre').val(usuario.nombreUsuario);
    $('#usuario-nombre').prop('readonly', true);
    $('#usuario-email').val(usuario.email);
    $('#usuario-rol').val(usuario.rol && usuario.rol.idRol ? usuario.rol.idRol : '');
    $('#modal-title').text('Editar Usuario');
    $('#usuario-nombre-hint').text('(Solo lectura)');
    $('#usuario-modal').addClass('show');
}

function guardarUsuario(event) {
    event.preventDefault();
    
    const usuarioId = $('#usuario-id').val();
    const nombreUsuario = $('#usuario-nombre').val();
    const email = $('#usuario-email').val();
    const idRol = $('#usuario-rol').val();
    const token = getJwtToken();
    
    if (!nombreUsuario || !email || !idRol) {
        showAlert('Por favor completa todos los campos', 'error');
        return;
    }
    
    const usuarioData = {
        nombreUsuario: nombreUsuario,
        email: email,
        idRol: parseInt(idRol)
    };
    
    if (usuarioId) {
        // Editar usuario
        $.ajax({
            url: `${BASE_API_URL}/admin/usuarios/${usuarioId}`,
            method: 'PUT',
            contentType: 'application/json',
            headers: {
                'Authorization': `Bearer ${token}`
            },
            data: JSON.stringify(usuarioData),
            success: function(response) {
                console.log('Usuario actualizado:', response);
                showAlert('Usuario actualizado exitosamente', 'success');
                cerrarModal();
                loadAdminUsers();
            },
            error: function(xhr) {
                console.error('Error al actualizar usuario:', xhr);
                const errorMsg = xhr.responseJSON && xhr.responseJSON.message ? xhr.responseJSON.message : 'Error al actualizar el usuario';
                showAlert(errorMsg, 'error');
            }
        });
    } else {
        // Crear usuario
        $.ajax({
            url: `${BASE_API_URL}/admin/usuarios`,
            method: 'POST',
            contentType: 'application/json',
            headers: {
                'Authorization': `Bearer ${token}`
            },
            data: JSON.stringify(usuarioData),
            success: function(response) {
                console.log('Usuario creado:', response);
                showAlert('Usuario creado exitosamente. Contraseña por defecto: password123', 'success');
                cerrarModal();
                loadAdminUsers();
            },
            error: function(xhr) {
                console.error('Error al crear usuario:', xhr);
                const errorMsg = xhr.responseJSON && xhr.responseJSON.message ? xhr.responseJSON.message : 'Error al crear el usuario';
                showAlert(errorMsg, 'error');
            }
        });
    }
}

function abrirModalEliminar(usuarioId) {
    usuarioAEliminar = usuarioId;
    $('#eliminar-modal').addClass('show');
}

function cerrarModalEliminar() {
    $('#eliminar-modal').removeClass('show');
    usuarioAEliminar = null;
}

function confirmarEliminar() {
    if (usuarioAEliminar) {
        eliminarUsuario(usuarioAEliminar);
        cerrarModalEliminar();
    }
}

function eliminarUsuario(usuarioId) {
    const token = getJwtToken();
    
    $.ajax({
        url: `${BASE_API_URL}/admin/usuarios/${usuarioId}`,
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${token}`
        },
        success: function(response) {
            console.log('Usuario eliminado:', response);
            showAlert('Usuario eliminado exitosamente', 'success');
            loadAdminUsers();
        },
        error: function(xhr) {
            console.error('Error al eliminar usuario:', xhr);
            const errorMsg = xhr.responseJSON && xhr.responseJSON.message ? xhr.responseJSON.message : 'Error al eliminar el usuario';
            showAlert(errorMsg, 'error');
        }
    });
}

function buscarUsuarios() {
    const searchTerm = $('#search-input').val().toLowerCase();
    
    if (!searchTerm) {
        renderizarTablaUsuarios(usuariosGlobal);
        return;
    }
    
    const usuariosFiltrados = usuariosGlobal.filter(usuario => 
        usuario.nombreUsuario.toLowerCase().includes(searchTerm) ||
        usuario.email.toLowerCase().includes(searchTerm)
    );
    
    renderizarTablaUsuarios(usuariosFiltrados);
}

function showAlert(message, type) {
    const $alert = $('#alert-message');
    $alert.removeClass('show alert-success alert-error');
    $alert.addClass(`show alert-${type}`);
    $alert.text(message);
    
    // Auto-hide después de 5 segundos
    setTimeout(() => {
        $alert.removeClass('show');
    }, 5000);
}

// ===============================================
// === 3. FUNCIONES GENÉRICAS DE FETCH
// ===============================================

function fetchPremierData(endpoint) {
    return fetch(`${BASE_API_URL}${endpoint}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${getJwtToken()}`
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.json();
    })
    .catch(error => {
        console.error('Error en fetchPremierData:', error);
        throw error;
    });
}

// ===============================================
// === 4. FUNCIONES DE CARGA DE DATOS DEPORTIVOS
// ===============================================

// 1. Vista: tabla.html (Clasificación)
function cargarTablaPosiciones() {
    const $tablaCuerpo = $('#tabla-posiciones-body');
    $tablaCuerpo.html('<tr><td colspan="10">Cargando tabla de posiciones...</td></tr>');

    // Endpoint: /api/datos-deportivos/clasificacion
    fetchPremierData('/datos-deportivos/clasificacion')
        .then(data => {
            $tablaCuerpo.empty();
            
            // Parsear estructura de football-data.org
            if (!data || !data.standings || !data.standings[0] || !data.standings[0].table) {
                console.error('Estructura de datos inesperada:', data);
                $tablaCuerpo.html('<tr><td colspan="10">Error: Estructura de datos inválida de la API.</td></tr>');
                return;
            }
            
            const standings = data.standings[0].table;
            
            if (!standings || standings.length === 0) {
                $tablaCuerpo.html('<tr><td colspan="10">No hay datos de clasificación disponibles.</td></tr>');
                return;
            }

            standings.forEach(equipo => {
                const fila = `
                    <tr>
                        <td>${equipo.position}</td>
                        <td class="equipo-nombre">
                            <img src="${equipo.team.crest}" alt="Escudo ${equipo.team.name}" style="height: 30px;">
                            <p>${equipo.team.name}</p>
                        </td>
                        <td>${equipo.points}</td>
                        <td>${equipo.playedGames}</td>
                        <td>${equipo.won}</td>
                        <td>${equipo.draw}</td>
                        <td>${equipo.lost}</td>
                        <td>${equipo.goalsFor}</td>
                        <td>${equipo.goalsAgainst}</td>
                        <td>${equipo.goalDifference}</td>
                    </tr>
                `;
                $tablaCuerpo.append(fila);
            });
        })
        .catch(error => {
            console.error('Error al cargar la clasificación:', error);
            $tablaCuerpo.html('<tr><td colspan="10">Error al cargar datos. Verifica la API Key y el Backend.</td></tr>');
        });
}

// 2. Vista: jornadas.html (Próximos Partidos)
function cargarPartidosJornada(jornadaNumero) {
    // Ajusta este ID en tu jornadas.html si es necesario
    const $contenedorResultados = $('#contenedor-partidos'); 
    $contenedorResultados.html('<p style="color:white; text-align:center;">Cargando partidos de la Jornada ' + jornadaNumero + '...</p>');
    
    const roundApi = 'Matchday ' + jornadaNumero; 

    // Endpoint: /api/datos-deportivos/jornadas/{round}
    fetchPremierData('/datos-deportivos/jornadas/' + encodeURIComponent(roundApi))
        .then(data => {
            $contenedorResultados.empty();
            
            // Parsear estructura de football-data.org
            if (!data || !data.matches || data.matches.length === 0) {
                $contenedorResultados.html('<p style="color:white; text-align:center;">No hay partidos programados para esta jornada.</p>');
                return;
            }
            
            const partidos = data.matches;
            
            // Agrupar partidos por día
            const partidosPorDia = partidos.reduce((acc, partido) => {
                const fecha = new Date(partido.utcDate);
                // Formato local del día: "lunes, 2 de diciembre"
                const diaSemana = fecha.toLocaleDateString('es-ES', { weekday: 'long', day: 'numeric', month: 'long' });
                if (!acc[diaSemana]) acc[diaSemana] = [];
                acc[diaSemana].push(partido);
                return acc;
            }, {});

            for (const dia in partidosPorDia) {
                // Título del día (ej: Sábado, 18 de octubre)
                $contenedorResultados.append(`<h3 style="text-align: center;">${dia.charAt(0).toUpperCase() + dia.slice(1)}</h3>`); 
                const $diaContenedor = $('<div class="contenedor-resultados"></div>');
                
                partidosPorDia[dia].forEach(partido => {
                    const fecha = new Date(partido.utcDate);
                    const hora = fecha.toLocaleTimeString('es-ES', { hour: '2-digit', minute: '2-digit' });
                    const fechaFormato = fecha.toLocaleDateString('es-ES');
                    
                    // Mostrar goles si el partido está finalizado
                    let golesHtml = '<p class="goles">vs</p>';
                    if (partido.status === 'FINISHED' && partido.score && partido.score.fullTime) {
                        const golesLocal = partido.score.fullTime.home || '0';
                        const golesVisita = partido.score.fullTime.away || '0';
                        golesHtml = `<p class="goles">${golesLocal} - ${golesVisita}</p>`;
                    } else if (partido.status === 'IN_PLAY') {
                        golesHtml = '<p class="goles">EN VIVO</p>';
                    }
                    
                    const tarjetaPartido = `
                        <div class="partido">
                            <div class="info-partido"><p>${fechaFormato} - ${hora}</p></div>
                            <div class="equipo">
                                <img src="${partido.homeTeam.crest}" alt="Escudo ${partido.homeTeam.name}">
                                <p>${partido.homeTeam.name}</p>
                            </div>
                            ${golesHtml}
                            <div class="equipo">
                                <img src="${partido.awayTeam.crest}" alt="Escudo ${partido.awayTeam.name}">
                                <p>${partido.awayTeam.name}</p>
                            </div>
                        </div>
                    `;
                    $diaContenedor.append(tarjetaPartido);
                });
                $contenedorResultados.append($diaContenedor);
            }
        })
        .catch(error => {
            console.error('Error al cargar partidos:', error);
            $contenedorResultados.html('<p style="color:white; text-align:center;">Error al obtener los datos de partidos.</p>');
        });
}

// 3. Vista: estadisticas.html (Goleadores)
function cargarEstadisticas() {
    const $tablaGoleadores = $('#tabla-goleadores-body');
    $tablaGoleadores.html('<tr><td colspan="4">Cargando estadísticas de goleadores...</td></tr>');

    // Endpoint: /api/datos-deportivos/estadisticas/goleadores
    fetchPremierData('/datos-deportivos/estadisticas/goleadores')
        .then(data => {
            $tablaGoleadores.empty();
            
            // Parsear estructura de football-data.org
            if (!data || !data.scorers || data.scorers.length === 0) {
                $tablaGoleadores.html('<tr><td colspan="4">No hay datos de goleadores disponibles.</td></tr>');
                return;
            }
            
            const jugadores = data.scorers;

            jugadores.slice(0, 10).forEach((item, index) => { 
                const jugador = item.player;
                const equipo = item.team;
                const goles = item.goals || 0;

                const fila = `
                    <tr>
                        <td>${index + 1}</td>
                        <td>${jugador.name}</td>
                        <td class="equipo-nombre">
                            <img src="${equipo.crest}" alt="Escudo ${equipo.name}" style="height: 30px;">
                            <p>${equipo.name}</p>
                        </td>
                        <td>${goles}</td>
                    </tr>
                `;
                $tablaGoleadores.append(fila);
            });
        })
        .catch(error => {
            console.error('Error al cargar estadísticas:', error);
            $tablaGoleadores.html('<tr><td colspan="4">Error al cargar datos.</td></tr>');
        });
}

// 3b. Vista: estadisticas.html (Asistencias)
function cargarAsistencias() {
    const $tablaAsistencias = $('#tabla-asistencias-body');
    $tablaAsistencias.html('<tr><td colspan="4">Cargando estadísticas de asistencias...</td></tr>');

    // Endpoint: /api/datos-deportivos/estadisticas/goleadores (mismo endpoint, filtramos por asistencias)
    fetchPremierData('/datos-deportivos/estadisticas/goleadores')
        .then(data => {
            $tablaAsistencias.empty();
            
            // Parsear estructura de football-data.org
            if (!data || !data.scorers || data.scorers.length === 0) {
                $tablaAsistencias.html('<tr><td colspan="4">No hay datos de asistencias disponibles.</td></tr>');
                return;
            }
            
            // Ordenar por asistencias y tomar los top 10
            const jugadoresOrdenados = data.scorers
                .filter(item => item.assists && item.assists > 0)
                .sort((a, b) => (b.assists || 0) - (a.assists || 0))
                .slice(0, 10);

            if (jugadoresOrdenados.length === 0) {
                $tablaAsistencias.html('<tr><td colspan="4">No hay datos de asistencias disponibles.</td></tr>');
                return;
            }

            jugadoresOrdenados.forEach((item, index) => { 
                const jugador = item.player;
                const equipo = item.team;
                const asistencias = item.assists || 0;

                const fila = `
                    <tr>
                        <td>${index + 1}</td>
                        <td>${jugador.name}</td>
                        <td class="equipo-nombre">
                            <img src="${equipo.crest}" alt="Escudo ${equipo.name}" style="height: 30px;">
                            <p>${equipo.name}</p>
                        </td>
                        <td>${asistencias}</td>
                    </tr>
                `;
                $tablaAsistencias.append(fila);
            });
        })
        .catch(error => {
            console.error('Error al cargar asistencias:', error);
            $tablaAsistencias.html('<tr><td colspan="4">Error al cargar datos.</td></tr>');
        });
}

// 4. Vista: resultados.html (Resultados Finalizados)
function cargarResultadosFinalizados(jornadaNumero) {
    // Ajusta este ID en tu resultados.html si es necesario
    const $contenedorResultados = $('#contenedor-partidos'); 
    
    if (!$contenedorResultados.length) {
        console.warn('Elemento #contenedor-partidos no encontrado');
        return;
    }
    
    let endpoint = '/datos-deportivos/resultados';
    if (jornadaNumero) {
        endpoint += '/' + encodeURIComponent('Matchday ' + jornadaNumero);
    }
    
    $contenedorResultados.html('<p style="color:white; text-align:center;">Cargando resultados finalizados...</p>');

    // Endpoint: /api/datos-deportivos/resultados o /api/datos-deportivos/resultados/Matchday {n}
    fetchPremierData(endpoint)
        .then(data => {
            $contenedorResultados.empty();
            
            // Parsear estructura de football-data.org
            if (!data || !data.matches || data.matches.length === 0) {
                $contenedorResultados.html('<p style="color:white; text-align:center;">No hay resultados disponibles.</p>');
                return;
            }
            
            const partidos = data.matches;
            
            // Filtrar solo partidos finalizados
            const resultados = partidos.filter(p => p.status === 'FINISHED');
            
            if (resultados.length === 0) {
                $contenedorResultados.html('<p style="color:white; text-align:center;">No hay resultados finalizados en esta jornada. Mostrando todos los partidos:</p>');
                // Si no hay resultados, mostrar todos los partidos
                mostrarPartidos(partidos, $contenedorResultados);
                return;
            }
            
            // Mostrar resultados finalizados
            mostrarPartidos(resultados, $contenedorResultados);
        })
        .catch(error => {
            console.error('Error al cargar resultados:', error);
            $contenedorResultados.html('<p style="color:white; text-align:center;">Error al obtener los resultados.</p>');
        });
}

// Función auxiliar para mostrar partidos (reutilizable)
function mostrarPartidos(partidos, $contenedor) {
    console.log('DEBUG: mostrarPartidos recibió:', partidos);
    
    // Agrupar por día
    const partidosPorDia = partidos.reduce((acc, partido) => {
        const fecha = new Date(partido.utcDate);
        const diaSemana = fecha.toLocaleDateString('es-ES', { weekday: 'long', day: 'numeric', month: 'long' });
        if (!acc[diaSemana]) acc[diaSemana] = [];
        acc[diaSemana].push(partido);
        return acc;
    }, {});

    for (const dia in partidosPorDia) {
        $contenedor.append(`<h3 style="text-align: center; color: white;">${dia.charAt(0).toUpperCase() + dia.slice(1)}</h3>`); 
        const $diaContenedor = $('<div class="contenedor-resultados"></div>');
        
        partidosPorDia[dia].forEach(partido => {
            const fecha = new Date(partido.utcDate);
            const hora = fecha.toLocaleTimeString('es-ES', { hour: '2-digit', minute: '2-digit' });
            const fechaFormato = fecha.toLocaleDateString('es-ES');
            
            console.log('DEBUG: Partido completo:', partido);
            console.log('DEBUG: score:', partido.score);
            
            // Mostrar goles si el partido está finalizado
            let golesHtml = 'vs';
            
            if (partido.status === 'FINISHED') {
                // Intentar obtener goles de fullTime
                let golesLocal = null;
                let golesVisita = null;
                
                if (partido.score) {
                    if (partido.score.fullTime) {
                        golesLocal = partido.score.fullTime.home;
                        golesVisita = partido.score.fullTime.away;
                    }
                    // Si fullTime no tiene valores, intentar con halfTime
                    if ((golesLocal === null || golesLocal === undefined) && partido.score.halfTime) {
                        golesLocal = partido.score.halfTime.home;
                        golesVisita = partido.score.halfTime.away;
                    }
                }
                
                // Si aún no tenemos goles, usar 0
                if (golesLocal === null || golesLocal === undefined) golesLocal = 0;
                if (golesVisita === null || golesVisita === undefined) golesVisita = 0;
                
                golesHtml = `${golesLocal} - ${golesVisita}`;
                console.log('DEBUG: Mostrando goles finalizados:', golesLocal, '-', golesVisita);
            } else if (partido.status === 'IN_PLAY') {
                golesHtml = 'EN VIVO';
            } else if (partido.status === 'SCHEDULED') {
                golesHtml = 'PRÓXIMO';
            } else if (partido.status === 'POSTPONED') {
                golesHtml = 'APLAZADO';
            }
            
            const tarjetaPartido = `
                <div class="partido">
                    <div class="info-partido"><p>${fechaFormato} - ${hora}</p></div>
                    <div class="equipo">
                        <img src="${partido.homeTeam.crest}" alt="Escudo ${partido.homeTeam.name}">
                        <p>${partido.homeTeam.name}</p>
                    </div>
                    <div class="equipo">
                        <p class="goles">${golesHtml}</p>
                    </div>
                    <div class="equipo">
                        <img src="${partido.awayTeam.crest}" alt="Escudo ${partido.awayTeam.name}">
                        <p>${partido.awayTeam.name}</p>
                    </div>
                </div>
            `;
            $diaContenedor.append(tarjetaPartido);
        });
        $contenedor.append($diaContenedor);
    }
}

// ===============================================
// === 5. FUNCIONES PARA SELECTOR DE JORNADAS
// ===============================================

// Función para cargar todas las jornadas disponibles
function cargarListaJornadas() {
    // Generamos jornadas del 1 al 38 (temporada normal de Premier League)
    const jornadas = Array.from({length: 38}, (_, i) => i + 1);
    return jornadas;
}

// Función para popular los selectores de jornadas
function popularSelectorJornadas() {
    const $selectorJornadas = $('#selector-jornadas');
    const $selectorResultados = $('#selector-jornadas-resultados');
    const jornadas = cargarListaJornadas();
    
    // Limpiar opciones previas excepto la primera
    $selectorJornadas.find('option:not(:first)').remove();
    $selectorResultados.find('option:not(:first)').remove();
    
    // Agregar todas las jornadas
    jornadas.forEach(jornada => {
        $selectorJornadas.append(`<option value="${jornada}">Jornada ${jornada}</option>`);
        $selectorResultados.append(`<option value="${jornada}">Jornada ${jornada}</option>`);
    });
    
    // Establecer la jornada 8 como predeterminada
    $selectorJornadas.val('8');
    $selectorResultados.val('7');
    
    // Event listeners para cambios en los selectores
    $selectorJornadas.on('change', function() {
        const jornada = $(this).val();
        if (jornada) {
            cargarPartidosJornada(parseInt(jornada));
        }
    });
    
    $selectorResultados.on('change', function() {
        const jornada = $(this).val();
        if (jornada) {
            cargarResultadosFinalizados(parseInt(jornada));
        }
    });
}

// ===============================================
// === 6. INICIALIZACIÓN DE LA APLICACIÓN
// ===============================================

$(document).ready(function() {
    // --- Inicialización de Lógica de Admin/Auth ---
    const currentPath = window.location.pathname;

    // A. Manejar el envío del formulario de Login (Solo si estamos en la página de Login)
    if (currentPath.includes('admin_login.html')) {
        $('#login-form').on('submit', function(e) {
            e.preventDefault();
            const username = $('#username').val();
            const password = $('#password').val();
            handleLogin(username, password);
        });
    }

    // B. Lógica para el Panel de Administración (requiere token)
    if (currentPath.includes('admin_dashboard.htm')) {
        $('#logout-btn').on('click', function(e) {
            e.preventDefault();
            handleLogout();
        });
        if (checkAdminAccess()) {
            loadAdminUsers();
        }
    }
    
    // --- Inicialización de Carga de Datos Deportivos (Usuario) ---
    if (currentPath.includes('tabla.html')) {
        cargarTablaPosiciones();
    }
    
    if (currentPath.includes('jornadas.html')) {
        popularSelectorJornadas();
        cargarPartidosJornada(8);
    }
    
    if (currentPath.includes('resultados.html')) {
        popularSelectorJornadas();
        cargarResultadosFinalizados(7);
    }
    
    if (currentPath.includes('estadisticas.html')) {
        cargarEstadisticas();
        cargarAsistencias();
    }

    // --- Efectos Visuales y Animaciones ---
    
    $(window).on('scroll', function() {
        if ($(this).scrollTop() > 100) {
            if (!$('#boton-arriba').length) {
                $('body').append('<button id="boton-arriba">↑</button>');
                $('#boton-arriba').css({
                    position: 'fixed',
                    bottom: '20px',
                    right: '20px',
                    padding: '10px 15px',
                    background: '#1a0033',
                    color: 'white',
                    border: 'none',
                    borderRadius: '5px',
                    cursor: 'pointer',
                    display: 'none'
                }).fadeIn();
            }
            $('#boton-arriba').fadeIn();
        } else {
            $('#boton-arriba').fadeOut();
        }
    });

    $(document).on('click', '#boton-arriba', function() {
        $('html, body').animate({ scrollTop: 0 }, 800);
    });

    // Contador de goles animado en la tabla de estadísticas
    $('.tabla-contenedor td:last-child').each(function() {
        let $this = $(this);
        let numero = parseInt($this.text());
        if (!isNaN(numero)) {
            $this.prop('Counter', 0).animate({
                Counter: numero
            }, {
                duration: 2000,
                step: function(now) {
                    $this.text(Math.ceil(now));
                }
            });
        }
    });

    // Efecto de resaltado para los resultados de partidos
    $('.goles').each(function() {
        let goles = parseInt($(this).text());
        if (goles > 2) {
            $(this).addClass('muchos-goles').fadeIn(400);
        }
    });

    // Tooltip personalizado para los equipos
    $(document).on('mouseenter', '.equipo', function() {
        let equipoNombre = $(this).find('p').text();
        let tooltip = $('<div class="tooltip">' + equipoNombre + '</div>');
        $(this).append(tooltip);
        tooltip.fadeIn(200);
    }).on('mouseleave', '.equipo', function() {
        $(this).find('.tooltip').remove();
    });

    // Efecto para la fecha de los partidos
    $('.info-partido p').each(function() {
        $(this).hover(
            function() {
                $(this).stop().animate({ fontSize: '1.8rem' }, 200);
            },
            function() {
                $(this).stop().animate({ fontSize: '1.6rem' }, 200);
            }
        );
    });
});
