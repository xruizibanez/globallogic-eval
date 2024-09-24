### Evaluación JAVA GlobalLogic - Creación y consulta de usuarios

Microservicio desarrollado utilizando **SpringBoot 2.5.14** y 
**Gradle hasta 7.4**, para la creación y consulta de usuarios.
	
## Documentación

En el archivo *README.txt* o *README.md* en la carpeta de proyecto encontrará 
esta misma documentación. También en la carpeta:

	${workspace_loc}/globallogic-eval/documentacion
	
encontrará los diagramas *UML* de componentes y de secuencia que describen
el desarrollo realizado para este microservicio. Ver la siguiente sección para
la obtención del proyecto.

## Instalación

Clonar el repositorio, y luego ejecutar el comando *Gradle* que permite
construir el proyecto (desde la carpeta donde reside el proyecto):
		
	git clone https://github.com/xruizibanez/globallogic-eval.git
	./gradlew clean build

## Ejecución

El microservicio puede ejecutarse desde línea de comandos mediante el
comando *Gradle* (desde la carpeta donde reside el proyecto):

	 ./gradlew bootRun
	 
Alternativamente, desde Eclipse como IDE puede ejecutar **Run as** y 
**Spring Boot App**.

Una vez que el proyecto está en ejecución, pueden ejecutarse los siguientes
comandos CURL (ya sea utilizando el comando *curl* de *Unix/Linux*, o importando
dichos comandos a un cliente como ser *Postman*).

Para la creación de usuarios mediante el método "sign-up" del endpoint:

	curl --location 'http://localhost:8080/user/sign-up' \
	--header 'Content-Type: application/json' \
	--data-raw '{
	    "name": "Xavier",
	    "email": "xavier@mail.com",
	    "password": "a2asfGfdfdf4",
	    "phones": [
	        {
	            "number": 1111111111,
	            "citycode": 2255,
	            "countrycode": "54"
	        },
	        {
	            "number": 2222222222,
	            "citycode": 11,
	            "countrycode": "54"
	        }
	    ]
	}'

Para el login del usuario mediante el método *login* del endpoint. Tener en 
cuenta que para el login se accede mediante el token devuelto por el método
*sign-up*, y este será pasado en los headers de este GET request. Tener en 
cuenta que este token se actualiza en cada login de usuario, por lo que debe 
cambiarse este token en cada ejecución (se pasa un token de ejemplo, cambiarlo
al valor adecuado):

	curl --location 'http://localhost:8080/user/login' \
	--header 'Authorization: eyJhbGciOiJub25lIn0.eyJzdWIiOiJkZTg3ZWMxMC1jMjNlLTQ2MWItOWI4Yy03MjMwMWY1OWVkMGMiLCJpYXQiOjE3MjcxNjI4NjIsImV4cCI6MTcyNzU5NDg2Mn0.'

Al ejecutar una sucesión de requests (ya sean "sign-up" o "login"), pueden 
consultarse los datos registrados en la base de datos H2 utilizando su
consola web mediante la siguiente URL, JDBC, usuario, y password:

	URL: http://localhost:8080/h2-console/login.do
	JDBC URL: jdbc:h2:~/test
	Usuario: sa
	Password: evaluser
	
Notar que la base de datos *H2* está configurada para ejecutarse con persistencia 
en archivo. Si desea cambiar la configuración para utilizar una base de datos
en memoria, debe cambiar el valor de *JDBC URL* correspodientemente.

## Pruebas y cobertura

Para ejecutar las pruebas unitarias y el análisis de cobertura de código, debe
ejecutar los siguientes comandos Gradle desde la carpeta raiz del proyecto
(en el orden mencionado):

	./gradlew test
	./gradlew jacocoTestReport

Alternativamente puede ejecutar ambas tareas desde un entorno IDE, como ser
*Eclipse* (desde la pestaña **Gradle Tasks**). Se ejecutan 33 pruebas unitarias,
con un nivel de cobertura de código de 86% en total. Una vez finalizadas ambas tareas, 
puede consultar los reportes de pruebas unitarias y de análisis de cobertura 
de código (obtenido mediante *JaCoCo*) en los siguientes archivos HTML, 
respectivamente:

	${workspace_loc}/globallogic-eval/build/reports/tests/test/index.html
	${workspace_loc}/globallogic-eval/build/reports/jacoco/test/html/index.html

