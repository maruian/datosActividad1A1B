package gestionficherosapp;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import gestionficheros.FormatoVistas;
import gestionficheros.GestionFicheros;
import gestionficheros.GestionFicherosException;
import gestionficheros.TipoOrden;

public class GestionFicherosImpl implements GestionFicheros {
	private File carpetaDeTrabajo = null;
	private Object[][] contenido;
	private int filas = 0;
	private int columnas = 3;
	private FormatoVistas formatoVistas = FormatoVistas.NOMBRES;
	private TipoOrden ordenado = TipoOrden.DESORDENADO;

	public GestionFicherosImpl() {
		carpetaDeTrabajo = File.listRoots()[0];
		actualiza();
	}

	private void actualiza() {

		String[] ficheros = carpetaDeTrabajo.list(); // obtener los nombres
		// calcular el número de filas necesario
		filas = ficheros.length / columnas;
		if (filas * columnas < ficheros.length) {
			filas++; // si hay resto necesitamos una fila más
		}

		// dimensionar la matriz contenido según los resultados

		contenido = new String[filas][columnas];
		// Rellenar contenido con los nombres obtenidos
		for (int i = 0; i < columnas; i++) {
			for (int j = 0; j < filas; j++) {
				int ind = j * columnas + i;
				if (ind < ficheros.length) {
					contenido[j][i] = ficheros[ind];
				} else {
					contenido[j][i] = "";
				}
			}
		}
	}

	@Override
	public void arriba() {

		System.out.println("holaaa");
		if (carpetaDeTrabajo.getParentFile() != null) {
			carpetaDeTrabajo = carpetaDeTrabajo.getParentFile();
			actualiza();
		}

	}

	@Override
	public void creaCarpeta(String arg0) throws GestionFicherosException {
		File file = new File(carpetaDeTrabajo,arg0);
		//que se pueda escribir -> lanzará una excepción
		if (!carpetaDeTrabajo.canWrite()) {
			throw new GestionFicherosException("No tienes permiso de escritura");
		}
		//que no exista -> lanzará una excepción
		if (file.exists()) {
			throw new GestionFicherosException("La carpeta ya existe");
		}
		//crear la carpeta -> lanzará una excepción
		if (file.mkdir()) {
			actualiza();
		} else {
			throw new GestionFicherosException("Error creando la carpeta");
		}
		
	}

	@Override
	public void creaFichero(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
        File file = new File(carpetaDeTrabajo,arg0);
        //comprobamos que se pueda escribir sobre la carpeta padre
        if (!carpetaDeTrabajo.canWrite()) {
        	throw new GestionFicherosException("No tienes permiso de escritura");
        }
        //comprueba si existe otro fichero con el mismo nombre
        if (file.exists()) {
        	throw new GestionFicherosException("El fichero ya existe");
        }
        //crea el fichero
        try {
			file.createNewFile();
			actualiza();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new GestionFicherosException("No se ha podido crear el fichero\n\nError: "+e.getMessage());
		}
	}

	@Override
	public void elimina(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		File file = new File(carpetaDeTrabajo,arg0);
		//Comprobamos que tenemos el fichero existe
		if (!file.exists()) {
			actualiza();
			throw new GestionFicherosException("El fichero no existe");
		}
		//Comprobamos los permisos sobre el fichero
		if (!file.canWrite()) {
			throw new GestionFicherosException("No tienes permiso para eliminar el fichero");
		}
		//Comprobamos que se ha borrado
		if (!file.delete()) {
			throw new GestionFicherosException("El directorio no esta vacio");
		} else {
			actualiza();
		}

	}

	@Override
	public void entraA(String arg0) throws GestionFicherosException {
		File file = new File(carpetaDeTrabajo, arg0);
		// se controla que el nombre corresponda a una carpeta existente
		if (!file.isDirectory()) {
			throw new GestionFicherosException("Error. Se ha encontrado "
					+ file.getAbsolutePath()
					+ " pero se esperaba un directorio");
		}
		// se controla que se tengan permisos para leer carpeta
		if (!file.canRead()) {
			throw new GestionFicherosException("Alerta. No se puede acceder a "
					+ file.getAbsolutePath() + ". No hay permiso");
		}
		// nueva asignación de la carpeta de trabajo
		carpetaDeTrabajo = file;
		// se requiere actualizar contenido
		actualiza();

	}

	@Override
	public int getColumnas() {
		return columnas;
	}

	@Override
	public Object[][] getContenido() {
		return contenido;
	}

	@Override
	public String getDireccionCarpeta() {
		return carpetaDeTrabajo.getAbsolutePath();
	}

	@Override
	public String getEspacioDisponibleCarpetaTrabajo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEspacioTotalCarpetaTrabajo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getFilas() {
		return filas;
	}

	@Override
	public FormatoVistas getFormatoContenido() {
		return formatoVistas;
	}

	@Override
	public String getInformacion(String arg0) throws GestionFicherosException {
		
		StringBuilder strBuilder=new StringBuilder();
		File file = new File(carpetaDeTrabajo,arg0);
		
		//Controlar que existe. Si no, se lanzará una excepción
		//Controlar que haya permisos de lectura. Si no, se lanzará una excepción
		if (!file.exists()) {
			throw new GestionFicherosException("El fichero "+file.getName()+" no existe");
		}
		
		if (!file.canRead()) {
			throw new GestionFicherosException("No tienes permiso de lectura sobre este fichero: " + file.getName());
		}
		
		//Título
		strBuilder.append("INFORMACIÓN DEL SISTEMA");
		strBuilder.append("\n\n");
		
		//Nombre
		strBuilder.append("Nombre: ");
		strBuilder.append(arg0);
		strBuilder.append("\n");
		
		//Tipo: fichero o directorio
		if(file.isDirectory()){
		   strBuilder.append("Tipo: directorio");
		   strBuilder.append("\n");
		   strBuilder.append("Elementos: " + file.list().length);
		   strBuilder.append("\n");
		} else {
		   strBuilder.append("Tipo: fichero");
		   strBuilder.append("\n");
		   //Muestra numero de bytes
		   strBuilder.append("Número de bytes: " + file.length());
		   strBuilder.append("\n");
		}
		
		//Ubicación
		strBuilder.append("Ubicación: " + file.getAbsolutePath());
		strBuilder.append("\n");
		
		//Fecha de última modificación
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		strBuilder.append("Fecha última modificacón: " + sdf.format(file.lastModified()));
		strBuilder.append("\n");
		
		//Si es un fichero oculto o no
		if (file.isHidden()) {
		   strBuilder.append("Fichero oculto: Sí");
		   strBuilder.append("\n");
		} else {
		   strBuilder.append("Fichero oculto: No");
		   strBuilder.append("\n");
		}
		
		//Si es directorio: Espacio libre, espacio disponible, espacio total
		//bytes
		if (file.isDirectory()) {
			strBuilder.append("Espacio libre: " + file.getFreeSpace());
			strBuilder.append("\n");
			strBuilder.append("Espacio disponible: " + file.getUsableSpace());
			strBuilder.append("\n");
			strBuilder.append("Espacio total: " + file.getTotalSpace());
			strBuilder.append("\n");
		}
		
		return strBuilder.toString();
	}

	@Override
	public boolean getMostrarOcultos() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getNombreCarpeta() {
		return carpetaDeTrabajo.getName();
	}

	@Override
	public TipoOrden getOrdenado() {
		return ordenado;
	}

	@Override
	public String[] getTituloColumnas() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getUltimaModificacion(String arg0)
			throws GestionFicherosException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String nomRaiz(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int numRaices() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void renombra(String arg0, String arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub
		File file = new File(carpetaDeTrabajo, arg0);
		//Comprobamos que el fichero exista
		if (!file.exists()) {
			throw new GestionFicherosException("El fichero o directorio no existe");
		}
		//Comprobamos los permisos sobre el fichero
		if (!file.canWrite()) {
			throw new GestionFicherosException("No tienes permisos de escritura");
		}
		File ficheroNuevo = new File(carpetaDeTrabajo,arg1);
		//Comprobamos que no exista el fichero o directorio
		if (ficheroNuevo.exists()) {
			throw new GestionFicherosException("El fichero o directorio ya existe");
		}
		//Comprobamos que se puede renombrar el fichero
		if (file.renameTo(ficheroNuevo)) {
			actualiza();
		}else {
			throw new GestionFicherosException("Hubo un problema al renombrar el fichero o el directorio");
		}
	}

	@Override
	public boolean sePuedeEjecutar(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sePuedeEscribir(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sePuedeLeer(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setColumnas(int arg0) {
		columnas = arg0;

	}

	@Override
	public void setDirCarpeta(String arg0) throws GestionFicherosException {
		File file = new File(arg0);

		// se controla que la dirección exista y sea directorio
		if (!file.isDirectory()) {
			throw new GestionFicherosException("Error. Se esperaba "
					+ "un directorio, pero " + file.getAbsolutePath()
					+ " no es un directorio.");
		}

		// se controla que haya permisos para leer carpeta
		if (!file.canRead()) {
			throw new GestionFicherosException(
					"Alerta. No se puede acceder a  " + file.getAbsolutePath()
							+ ". No hay permisos");
		}

		// actualizar la carpeta de trabajo
		carpetaDeTrabajo = file;

		// actualizar el contenido
		actualiza();

	}

	@Override
	public void setFormatoContenido(FormatoVistas arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMostrarOcultos(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOrdenado(TipoOrden arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSePuedeEjecutar(String arg0, boolean arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSePuedeEscribir(String arg0, boolean arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSePuedeLeer(String arg0, boolean arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setUltimaModificacion(String arg0, long arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

}
