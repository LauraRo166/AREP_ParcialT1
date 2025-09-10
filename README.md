# AREP Parcial Primer Tercio
---
## Ejecutar
1. Para ejecutar clone el repositorio y cambie el repositorio:
```
git clone https://github.com/LauraRo166/AREP_ParcialT1.git
```
```
cd AREP_ParcialT1
```
2. Compile el proyecto.
```
mvn clean package
```
Debe verse de la siguiente forma:
<img width="943" height="134" alt="image" src="https://github.com/user-attachments/assets/51b9de50-628a-41b4-8fec-d08e6b8d2f2e" />

Nota: En caso de que no compile, verifique la versión de java del pom, cambiela si es necesario. (En los computadores del laboratorio debe ser 17, no 21, por ejemplo.)

3. Por último ejecute los siguiente:
```
java -cp target/classes co.edu.escuelaing.arep.parcialt1.BackendServer
```
```
java -cp target/classes co.edu.escuelaing.arep.parcialt1.FacadeServer
```

4. Ingrese a http://localhost:35000/cliente

---
## Prueba
Nota: Es necesario usar extrictamente los botones.

1. Añadir un número:
<img width="1894" height="395" alt="image" src="https://github.com/user-attachments/assets/be252249-7d08-4179-9e5f-41eb2b85c533" />

2. Consultar la lista de números:
<img width="1387" height="169" alt="image" src="https://github.com/user-attachments/assets/9d237fba-6973-405b-8410-b2c0939f6007" />

3. Calcular las estadisticas:
<img width="1317" height="337" alt="image" src="https://github.com/user-attachments/assets/96eaa1df-8a84-404b-96dd-b67e1b5b9481" />

4. Borrar toda la lista:
<img width="1484" height="250" alt="image" src="https://github.com/user-attachments/assets/f5e749dd-4a28-4f5c-99f4-b11cdeabd2a1" />




