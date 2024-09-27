https://github.com/ffernandoss/DefinitivoFeedback.git

# Feedback 1 Sistemas Operativos

Este ejercicio está compuesto por 3 clases: `MainActivity`, `ListaNovelasActivity` y `NovelaAdapter`. También se creó un XML nuevo en la carpeta de `layout` dentro de `res`.

## Clase `MainActivity`

Está formada por una columna que contiene un texto que dice "GESTIÓN DE NOVELAS" y un botón que, al pulsarlo, te lleva a `ListaNovelasActivity`. Está formada, aparte de la función `onCreate`, por la función `MainScreen`, la cual tiene la lógica para mostrar el texto y el botón, ambos elementos metidos en una columna.

## Clase `ListaNovelasActivity`

En esta clase se hace uso de `SharedPreferences` y `Gson` para añadir y eliminar novelas, y además, cuando hay novelas añadidas, si se vuelve a la pantalla principal y luego se vuelve a la lista de novelas, se mantienen ahí las novelas escritas. También se hace uso de `remember`, botones para añadir y eliminar novelas, para solo mostrar las favoritas y también para volver al menú principal. También se usa `RecyclerView` y `AlertDialog` para manejar errores y que el programa no se cierre, mostrando un mensaje de error en su lugar. También se usa un `Adapter` para asignar los datos introducidos a la novela creada y para la acción de añadir una novela a favoritos pulsando la caja. Está formada por el método `ListaNovelasScreen`, que tiene toda la lógica de lo que se va a ver por pantalla. Tiene al inicio todas las variables que serán usadas tanto para las novelas como para los errores. `LaunchedEffect` muestra las novelas que había al cerrar la ventana. Una columna donde van a estar los diferentes botones y las novelas. También están los diferentes casos de posibles errores al añadir novelas.

## Clase `NovelaAdapter`

Tiene los atributos de `nombre`, `año`, `descripcion`, `valoracion` y un booleano para añadir a favoritos. Está formada por la clase interna `NovelaViewHolder`, la cual referencia cada elemento con la novela. La función `onCreateViewHolder` devuelve un objeto de `CreateViewHolder`. La función `onBindViewHolder` asigna los datos de las novelas a la vista. La función `getItemCount` devuelve el número de novelas creadas.
