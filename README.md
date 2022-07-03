# ApiRest-Java-SpringBoot-PostgreSQL
API Rest Java

En este proyecto de ejemplo solo se repasan cosas básicas de un API Rest con Java

Lo primero es descargar un nuevo proyecto de   https://start.spring.io/
	Descargar dependencias
		-PostgreSQL
		-JPA

Descargamos el .RAR generado
Descomprimir y abrir con un editor de código

Api con:
	-services:		El modelo llama un servicio que es el que tiene la logica de la app
	-models:     	El repositorio usa el modelo para saber que tipo de información va a traer
	-repositrios 	El servicio utiliza un repositorio que es donde hace las conexiones con la base de datos
	-controladores:	Recibe la petición Web
