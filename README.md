# YouTube Video Idea Generator

## Descripción

Esta aplicación de Spring Boot permite a los usuarios cargar sus videos de YouTube para generar ideas de títulos y descripciones basadas en el contenido del audio del video.

## Características

- **Extracción de audio**: Utiliza FFmpeg para extraer el audio de los videos de YouTube cargados.
- **Transcripción**: Convierte el audio extraído en texto utilizando la API de Google Speech.
- **Generación de ideas**: Analiza la transcripción y genera sugerencias de títulos y descripciones mediante la API de OpenAI.
- **Interfaz fácil de usar**: Permite a los usuarios cargar videos y recibir sugerencias de forma rápida y sencilla.

## Tecnologías utilizadas

- **Spring Boot**: Framework para construir la aplicación web.
- **FFmpeg**: Herramienta de línea de comandos para manejar archivos de audio y video.
- **Google Speech API**: Servicio de reconocimiento de voz que convierte audio en texto.
- **OpenAI API**: Utilizada para generar ideas creativas a partir del texto transcrito.
- **Thymeleaf**: Motor de plantillas para la generación de vistas.
- **HTMX**: Biblioteca para crear aplicaciones web dinámicas sin necesidad de recargar la página.
- **Tailwind CSS**: Framework CSS para diseñar interfaces modernas y responsivas.

## Instalación

1. **Clona el repositorio:**
git clone https://github.com/nicosal1080/video-ai-analyzer.git

3. **Navega al directorio del proyecto:**
cd video-ai-analyzer

4. **Compila el proyecto:**
./mvnw clean install

5. **Configura las credenciales:**
Obtén tus credenciales de la API de Google y OpenAI.
Configura las variables de entorno o el archivo application.properties con tus claves de API.

6. **Ejecuta la aplicación:**
./mvnw spring-boot:run

7. **Accede a la aplicación:**
Abre tu navegador y dirígete a http://localhost:8080.

## Uso

Carga tu video de YouTube en el formulario proporcionado. La aplicación extraerá automáticamente el audio y lo transcribirá.

## Contacto
Para preguntas o más información, puedes contactarme en admin@nicolassalgado.com.

