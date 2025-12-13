# API RESTful de Tickets de Soporte Técnico

Microservicio para gestionar incidentes de soporte técnico (SupportTicket) desarrollado con Java 17 y Spring Boot 3.

## Requisitos del Entorno

### Software Necesario
- **Docker Desktop** instalado y corriendo
- Puertos disponibles: **3306** (MySQL) y **8080** (API)

## Descargar Imagen Docker

```bash
docker pull santiagoarroyo/arroyo_santiago_leccion1:1.0
```

### Construcción de Imagen (Opcional - solo si modificas el código)

```bash
# Desde la raíz del proyecto
docker build -t santiagoarroyo/arroyo_santiago_leccion1:1.0 .
```

## Comando para Ejecutar Contenedor

### Paso 1: Crear red Docker

```bash
docker network create leccion-network
```

### Paso 2: Ejecutar MySQL

```bash
docker run -d --name mysql-support --network leccion-network -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=support_tickets_db -p 3306:3306 mysql:8.0
```

⏱️ **Esperar 20-30 segundos** hasta que MySQL esté listo:

```bash
docker logs mysql-support
```

Espera hasta ver: `ready for connections`

### Paso 3: Ejecutar la aplicación

```bash
docker run -d --name support-tickets-api --network leccion-network -p 8080:8080 -e DB_HOST=mysql-support -e DB_PORT=3306 -e DB_NAME=support_tickets_db -e DB_USER=root -e DB_PASSWORD=root santiagoarroyo/arroyo_santiago_leccion1:1.0
```

### Paso 4: Verificar que está corriendo

```bash
docker ps
docker logs -f support-tickets-api
```

✅ Espera hasta ver: `Started ArroyoSantiagoLeccion1Application`

## URL Base y Ejemplos de Consumo

### URL Base
```
http://localhost:8080
```

## Pruebas con Postman

### 1. Crear Ticket

- **Method:** `POST`
- **URL:** `http://localhost:8080/api/v1/support-tickets`
- **Headers:** `Content-Type: application/json`
- **Body (raw JSON):**
```json
{
  "ticketNumber": "ST-2025-000001",
  "requesterName": "Juan Chicaiza",
  "status": "OPEN",
  "priority": "HIGH",
  "category": "NETWORK",
  "estimatedCost": 150.50,
  "currency": "USD",
  "dueDate": "2025-12-31"
}
```

**Respuesta esperada:** `201 Created`
```json
{
  "id": 1,
  "ticketNumber": "ST-2025-000001",
  "requesterName": "Juan Chicaiza",
  "status": "OPEN",
  "priority": "HIGH",
  "category": "NETWORK",
  "estimatedCost": 150.50,
  "currency": "USD",
  "createdAt": "2025-12-12T20:05:54.864179",
  "dueDate": "2025-12-31"
}
```

### 2. Listar Todos los Tickets

- **Method:** `GET`
- **URL:** `http://localhost:8080/api/v1/support-tickets`

**Respuesta esperada:** `200 OK`
```json
{
  "content": [
    {
      "id": 1,
      "ticketNumber": "ST-2025-000001",
      "requesterName": "Juan Chicaiza",
      "status": "OPEN",
      "priority": "HIGH",
      "category": "NETWORK",
      "estimatedCost": 150.50,
      "currency": "USD",
      "createdAt": "2025-12-12T20:05:54.864179",
      "dueDate": "2025-12-31"
    }
  ],
  "totalElements": 1,
  "totalPages": 1,
  "size": 10,
  "number": 0
}
```

### 3. Filtrar por Búsqueda de Texto

- **Method:** `GET`
- **URL:** `http://localhost:8080/api/v1/support-tickets?q=chicaiza`

### 4. Filtrar por Estado

- **Method:** `GET`
- **URL:** `http://localhost:8080/api/v1/support-tickets?status=OPEN`

Valores permitidos: `OPEN`, `IN_PROGRESS`, `RESOLVED`, `CLOSED`, `CANCELLED`

### 5. Filtrar por Moneda

- **Method:** `GET`
- **URL:** `http://localhost:8080/api/v1/support-tickets?currency=USD`

Valores permitidos: `USD`, `EUR`

### 6. Filtrar por Rango de Costo

- **Method:** `GET`
- **URL:** `http://localhost:8080/api/v1/support-tickets?minCost=100&maxCost=500`

### 7. Filtrar por Rango de Fechas

- **Method:** `GET`
- **URL:** `http://localhost:8080/api/v1/support-tickets?from=2025-01-01T00:00:00&to=2025-12-31T23:59:59`

Formato ISO-8601: `yyyy-MM-dd'T'HH:mm:ss`

### 8. Filtros Combinados + Paginación

- **Method:** `GET`
- **URL:** `http://localhost:8080/api/v1/support-tickets?q=network&status=OPEN&currency=USD&minCost=100&maxCost=500&page=0&size=10&sort=createdAt,desc`

### 9. Más Datos de Prueba

**Ticket 2:**
```json
{
  "ticketNumber": "ST-2025-000002",
  "requesterName": "Maria Rodriguez",
  "status": "IN_PROGRESS",
  "priority": "MEDIUM",
  "category": "HARDWARE",
  "estimatedCost": 250.00,
  "currency": "EUR",
  "dueDate": "2025-12-25"
}
```

**Ticket 3:**
```json
{
  "ticketNumber": "ST-2025-000003",
  "requesterName": "Carlos Sanchez",
  "status": "RESOLVED",
  "priority": "LOW",
  "category": "SOFTWARE",
  "estimatedCost": 75.25,
  "currency": "USD",
  "dueDate": "2025-12-20"
}
```

**Ticket 4:**
```json
{
  "ticketNumber": "ST-2025-000004",
  "requesterName": "Ana Chicaiza",
  "status": "OPEN",
  "priority": "CRITICAL",
  "category": "NETWORK",
  "estimatedCost": 500.00,
  "currency": "USD",
  "dueDate": "2025-12-15"
}
```

### 10. Probar Error de Validación

- **Method:** `POST`
- **URL:** `http://localhost:8080/api/v1/support-tickets`
- **Body (raw JSON):**
```json
{
  "ticketNumber": "INVALID",
  "requesterName": "",
  "status": "OPEN",
  "priority": "HIGH",
  "category": "NETWORK",
  "estimatedCost": -10,
  "currency": "USD",
  "dueDate": "2025-12-31"
}
```

**Respuesta esperada:** `400 Bad Request`
```json
{
  "timestamp": "2025-12-12T20:30:00.000Z",
  "status": 400,
  "message": "Error de validación",
  "errors": {
    "ticketNumber": "El formato del ticket debe ser ST-YYYY-NNNNNN (ej: ST-2025-000145)",
    "requesterName": "El nombre del solicitante es obligatorio",
    "estimatedCost": "El costo estimado debe ser mayor o igual a 0"
  }
}
```

### 11. Probar Filtro Inválido

- **Method:** `GET`
- **URL:** `http://localhost:8080/api/v1/support-tickets?status=INVALID_STATUS`

**Respuesta esperada:** `400 Bad Request`
```json
{
  "timestamp": "2025-12-12T20:30:00.000Z",
  "status": 400,
  "message": "Filtro inválido",
  "errors": {
    "filter": "Estado inválido: INVALID_STATUS. Valores permitidos: OPEN, IN_PROGRESS, RESOLVED, CLOSED, CANCELLED"
  }
}
```

## Filtros Disponibles

Todos los filtros son **opcionales** y se combinan con lógica **AND**:

| Filtro | Descripción | Ejemplo |
|--------|-------------|---------|
| **q** | Busca en ticketNumber y requesterName | `?q=chicaiza` |
| **status** | OPEN, IN_PROGRESS, RESOLVED, CLOSED, CANCELLED | `?status=OPEN` |
| **currency** | USD, EUR | `?currency=USD` |
| **minCost** | Costo mínimo (>= 0) | `?minCost=100` |
| **maxCost** | Costo máximo (>= 0) | `?maxCost=500` |
| **from** | Fecha desde (ISO-8601) | `?from=2025-01-01T00:00:00` |
| **to** | Fecha hasta (ISO-8601) | `?to=2025-12-31T23:59:59` |
| **page** | Número de página (default: 0) | `?page=0` |
| **size** | Tamaño de página (default: 10) | `?size=20` |
| **sort** | Campo,dirección (default: id,asc) | `?sort=estimatedCost,desc` |

## Validaciones y Códigos de Respuesta

### Códigos HTTP
- `200 OK`: Consulta exitosa
- `201 Created`: Ticket creado exitosamente
- `400 Bad Request`: Error de validación o filtro inválido
- `500 Internal Server Error`: Error del servidor

### Validaciones en Creación de Tickets
- **ticketNumber**: Formato `ST-YYYY-NNNNNN` obligatorio
- **requesterName**: No puede estar vacío
- **status**: Valor del enum obligatorio
- **priority**: Valor del enum obligatorio
- **estimatedCost**: Debe ser >= 0
- **currency**: USD o EUR
- **dueDate**: No puede ser nulo
- **createdAt**: Generado automáticamente por el sistema

## Comandos Útiles de Docker

### Ver contenedores corriendo
```bash
docker ps
```

### Ver logs en tiempo real
```bash
docker logs -f support-tickets-api
docker logs -f mysql-support
```

### Detener contenedores
```bash
docker stop support-tickets-api mysql-support
```

### Iniciar contenedores
```bash
docker start mysql-support
docker start support-tickets-api
```

### Eliminar todo
```bash
docker rm -f support-tickets-api mysql-support
docker network rm leccion-network
```

### Reiniciar aplicación
```bash
docker restart support-tickets-api
```

## Entidad SupportTicket

| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | Long | Identificador único (auto-generado) |
| ticketNumber | String | Formato: ST-YYYY-NNNNNN (único) |
| requesterName | String | Nombre del solicitante |
| status | Enum | OPEN, IN_PROGRESS, RESOLVED, CLOSED, CANCELLED |
| priority | Enum | LOW, MEDIUM, HIGH, CRITICAL |
| category | String | NETWORK, HARDWARE, SOFTWARE, etc. |
| estimatedCost | BigDecimal | Costo estimado (>= 0) |
| currency | Enum | USD, EUR |
| createdAt | LocalDateTime | Generado automáticamente |
| dueDate | LocalDate | Fecha máxima de atención |

## Tecnologías

- Java 17
- Spring Boot 3.4.12
- Spring Data JPA
- MySQL 8
- Docker
- Maven

## Arquitectura

- **Controller**: Maneja peticiones HTTP y validaciones
- **Service**: Lógica de negocio
- **Repository**: Acceso a datos con JPA
- **Models**: Entidades y enums
- **Exceptions**: Manejo global de errores

## Autor

Santiago Arroyo - Lección 1
