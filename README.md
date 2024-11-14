https://github.com/ffernandoss/DefinitivoFeedback.git

# Feedback 1 Sistemas Operativos

Este ejercicio está compuesto por 3 clases: `MainActivity`, `ListaNovelasActivity` y `NovelaAdapter`. También se creó un XML nuevo en la carpeta de `layout` dentro de `res`.

## Clase `MainActivity`

Está formada por una columna que contiene un texto que dice "GESTIÓN DE NOVELAS" y un botón que, al pulsarlo, te lleva a `ListaNovelasActivity`. Está formada, aparte de la función `onCreate`, por la función `MainScreen`, la cual tiene la lógica para mostrar el texto y el botón, ambos elementos metidos en una columna.

## Clase `ListaNovelasActivity`

En esta clase se hace uso de `SharedPreferences` y `Gson` para añadir y eliminar novelas, y además, cuando hay novelas añadidas, si se vuelve a la pantalla principal y luego se vuelve a la lista de novelas, se mantienen ahí las novelas escritas. También se hace uso de `remember`, botones para añadir y eliminar novelas, para solo mostrar las favoritas y también para volver al menú principal. También se usa `RecyclerView` y `AlertDialog` para manejar errores y que el programa no se cierre, mostrando un mensaje de error en su lugar. También se usa un `Adapter` para asignar los datos introducidos a la novela creada y para la acción de añadir una novela a favoritos pulsando la caja. Está formada por el método `ListaNovelasScreen`, que tiene toda la lógica de lo que se va a ver por pantalla. Tiene al inicio todas las variables que serán usadas tanto para las novelas como para los errores. `LaunchedEffect` muestra las novelas que había al cerrar la ventana. Una columna donde van a estar los diferentes botones y las novelas. También están los diferentes casos de posibles errores al añadir novelas.

## Clase `NovelaAdapter`

Tiene los atributos de `nombre`, `año`, `descripcion`, `valoracion` y un booleano para añadir a favoritos. Está formada por la clase interna `NovelaViewHolder`, la cual referencia cada elemento con la novela. La función `onCreateViewHolder` devuelve un objeto de `CreateViewHolder`. La función `onBindViewHolder` asigna los datos de las novelas a la vista. La función `getItemCount` devuelve el número de novelas creadas.

------ ENTREGA 2------
## Clase `NovelaStorage`

es la encargada de realizar las operaciones de añadir, eliminar, mostrar como favoritas... todos estos cambios se muestran en la base de datos de firebase en tiempo real, si se modifica lo de ser favortia se cambia el atributo booleano



## Entrega 3

### Modo Oscuro

Se ha añadido la funcionalidad de modo oscuro en la aplicación. Los usuarios pueden activar o desactivar el modo oscuro desde la pantalla de ajustes. Esta preferencia se guarda en `SharedPreferences` y se aplica en toda la aplicación.

### Novelas por Usuario

Cada usuario tiene sus propias novelas. Se ha modificado la base de datos para que las novelas estén asociadas a un usuario específico. Esto se ha logrado añadiendo un campo `userId` en la tabla de novelas.

### Cambios en `UserDatabaseHelper`

Se han añadido métodos para gestionar las novelas por usuario, incluyendo `addNovelaForUser`, `getNovelasByUser` y `deleteNovelaForUser`.

### Cambios en `ListaNovelasActivity`

Se ha modificado la actividad para que muestre solo las novelas del usuario actualmente logueado. Se utiliza el `userId` del usuario actual para filtrar las novelas.

### Cambios en `AjustesActivity`

Se ha añadido la funcionalidad para actualizar el modo oscuro en la base de datos del usuario. Cuando el usuario cambia el modo oscuro, se actualiza tanto en `SharedPreferences` como en la base de datos.

### Cambios en `LoginActivity`

Se ha añadido la funcionalidad para guardar el `userId` del usuario logueado en `SharedPreferences`. Esto permite que otras actividades puedan acceder al `userId` del usuario actual.

### Cambios en `NovelaAdapter`

Se ha añadido la funcionalidad para actualizar el estado de favorito de una novela en Firebase. Cuando el usuario marca o desmarca una novela como favorita, se actualiza en tiempo real en Firebase.



## Entrega 3

en esta entrega no hay fragments, ya que con composable no sé hacerlas, lo que he hecho ha sido añadir un pop up al pulsar la novela donde aparecen todos los datos
y el widget lo que hace es mostrar todas las novelas de todos los usuarios
