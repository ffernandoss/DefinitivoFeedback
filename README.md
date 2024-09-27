https://github.com/ffernandoss/DefinitivoFeedback.git

Feedback 1 sistemas operativos

este ejercicio está compuesto por 3 clases, la clase MainActivity,ListaNovelasActivity y novelaAdapter, tambiés se creó un xml nuevo en la carpeta de layout dentro de res

clase MainActivity: esta formada por una colunma que contiene un texto que dice GESTION DE NOVELAS, y un boton que al pulsarlo te lleva a ListaNovelasActivity, esta formada aparte de la funcion onCreate, por la funcion MainScreen la cual tiene la logica para mostrar el texto y el boton, ambos elementos metidos en una columna



----clase ListaNovelasActivity: en esta clase se hace uso de SharedPreferences y Gson para añadir, eliminar novelas y que ademas cuando hay novelas añadidas que si se vuelve a la pantalla principal y luego se vuelve a la lista de novelas que se mantengan ahi las novelas escritas
tambien se hace uso de remember, botones para añadir,eliminar novelas,para solo mostrar las favoritas y tambien para volver al menu principal,tambien se usa recylcerView y AlertDialog para la hora de manejar errores que el programa no se cierre y en su caso salga un mensaje de error, tambien se usa un Adapter para asginar los datos introducidos a la novela creada y tambien se usa para la accion de añadir una novela a favoritos pulsando la caja, esta formada por el metodo ListaNovelasCreen esta tiene toda la logica de lo que se va a ver por pantalla, tiene al inicio todas las variables que serán usadas tanto para las novelas como para los errores, launchEffect muestra las novelas que habian al cerrar la ventana, una columna donde van a estar los diferentes botones y las novelas, tambien están los diferentes casos de posibles errores al añadir novelas



---clase NovelaAdapter: la cual tiene los atributos de nombre,año,descripcion,valoracion y un booleano para añadir a favoritos, esta formada por la clase interna NovelaViewHolder la cual referencia cada elemento con la novela, la funcion onCreateViewHolder la cual devuelve un objeto de CreateViewHolder, la funcion de onBindViewHolder la cual asigna los datos de las novelas a la vista, y la funcion getItemCount que devuelve el numero de novelas creadas
