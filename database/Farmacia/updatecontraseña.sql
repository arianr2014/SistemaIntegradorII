UPDATE usuario_farmacia 
SET password_hash = '$2a$12$ACgLkSwZBvZztr9EI3sZsOggQdTNAxBmRDjVdtoXk7KxCZoZymz9G' 
WHERE id_usuario IN (1, 2, 3);
