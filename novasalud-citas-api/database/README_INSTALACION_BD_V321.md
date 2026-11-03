# NovaSalud V3.2.1 - Base de datos

Ejecutar únicamente este script en MySQL Workbench:

```sql
database/v321/00_instalacion_completa_bd_citasmedicas_v321.sql
```

El script crea desde cero la base independiente:

```text
bd_citasmedicas_v321
```

No modifica versiones anteriores. La historia clínica de cada paciente usa el DNI como código único. Los médicos pueden registrar y editar la atención clínica desde su agenda personal, incluyendo síntomas, diagnóstico, tratamiento, receta e indicaciones.

Usuarios demo: todos usan contraseña `123456`.
