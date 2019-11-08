/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgapp.utiilities;

import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Bad-K
 */
public class CrunchifyFindClassesFromJar {
 
	@SuppressWarnings("resource")
	public static JSONObject getCrunchifyClassNamesFromJar(String crunchifyJarName) {
		JSONArray listofClasses = new JSONArray();
		JSONObject crunchifyObject = new JSONObject();
		try {
			JarInputStream crunchifyJarFile = new JarInputStream(new FileInputStream(crunchifyJarName));
			JarEntry crunchifyJar;
 
			while (true) {
				crunchifyJar = crunchifyJarFile.getNextJarEntry();
				if (crunchifyJar == null) {
					break;
				}
				if ((crunchifyJar.getName().endsWith(".class"))) {
					String className = crunchifyJar.getName().replaceAll("/", "\\.");
					String myClass = className.substring(0, className.lastIndexOf('.'));
                                        System.out.println("Clase detectada: "+ myClass);
					listofClasses.put(myClass);
				}
			}
			crunchifyObject.put("Jar File Name", crunchifyJarName);
			crunchifyObject.put("List of Class", listofClasses);
		} catch (Exception e) {
			System.out.println("Oops.. Encounter an issue while parsing jar" + e.toString());
		}
		return crunchifyObject;
	}
        
        
        public String getCrunchifyClassNamesFromJar(String crunchifyJarName,String metodo) {
            
            URL myJarFile;// 1adsfasfsd
            try {
                    JarInputStream crunchifyJarFile = new JarInputStream(new FileInputStream(crunchifyJarName));
                    JarEntry crunchifyJar;
                    myJarFile = new URL("jar","","file:"+crunchifyJarName+"!/");// wsdfasdfsadf
                    while (true) {
                        crunchifyJar = crunchifyJarFile.getNextJarEntry();
                        if (crunchifyJar == null) {
                                break;
                        }
                        if ((crunchifyJar.getName().endsWith(".class"))) {
                            String className = crunchifyJar.getName().replaceAll("/", "\\.");
                            String myClass = className.substring(0, className.lastIndexOf('.'));
                            
                            //System.out.println("Clase detectada1: "+ myClass);
                            //System.out.println("Clase detectada2: "+ className);
                            try {
                                
                                ClassLoader cl = URLClassLoader.newInstance(new URL[]{myJarFile},this.getClass().getClassLoader());// asdfasdf
                                Class<?> PlugginClass = Class.forName(myClass, false, cl);
                                                           
                                if(!"bgapp.utiilities.PluginMethods".equals(PlugginClass.getName())){
                                    Method main = PlugginClass.getDeclaredMethod(metodo);
                                    System.out.println("GETNAME: "+PlugginClass.getName());
                                    //if ()
                                    System.out.println("ESTA COSA FUNCIONA? ------ "+main.toString());
                                    PlugginClass = null;
                                    return myClass;
                                } else {
                                    System.out.println("ASDASDASDDSADSADSADA NO >:O");
                                }
                                
                                
                                
                            } catch (Exception e) {
                                    //System.out.println("No posee la función buscada "+e);
                            }
                        }
                    }
            } catch (Exception e) {
                    System.out.println("Oops.. Encounter an issue while parsing jar" + e.toString());
            }
            return null;
	}
        
 
}
