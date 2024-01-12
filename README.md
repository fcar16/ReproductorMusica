# Reproductor de Música Multihilo

Este proyecto implementa un reproductor de música simple en Java con capacidad multihilo utilizando JavaFX para la interfaz gráfica y la API de sonido de Java.

## Configuración

1. **Clonar el Repositorio:**
   ```bash
   git clone https://github.com/fcar16/ReproductorMusica.git



**2.- Uso**
Ejecuta la aplicación y carga tu lista de reproducción.
Haz clic en "Play" para iniciar la reproducción de la primera canción.
Utiliza los botones "Stop", "Pause" y "Resume" según sea necesario.
Cambia a la siguiente canción con el botón "Next".
**3.- Funcionalidades**
Reproducción de música multihilo.
Controles de reproducción (Play, Stop, Pause, Resume, Next).
Interfaz gráfica simple con JavaFX.


**4.-Estructura del Proyecto**
src/com/example/reproductormusica: Contiene los archivos fuente del proyecto


**5.-Problemas Conocidos**


5.1. **Diseño de la Pantalla:**
   - Puede haber problemas de diseño en la interfaz gráfica, especialmente en diferentes tamaños de pantalla o sistemas operativos. Se recomienda ajustar los estilos y diseño según sea necesario.

5.2. **Reproducción y Pausa:**
   - Al pausar la reproducción y luego resumirla, la canción comienza desde el principio en lugar de continuar desde donde se pausó. 

5.3. **Cambio de Canción al Pausar:**
   - Al pausar la reproducción, a veces se produce un cambio de canción. Este problema está relacionado con la lógica de pausa y reproducción multihilo y se está investigando.

5.4. **Problemas al Cargar Múltiples Canciones:**
   - Pueden surgir problemas al intentar cargar y reproducir varias canciones de la lista de reproducción. Asegúrate de proporcionar rutas de archivos correctas y formatos de audio compatibles.

