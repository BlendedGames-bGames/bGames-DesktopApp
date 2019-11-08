/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mod.sensors;
import bgapp.utiilities.SensorNeed;
import bgapp.utiilities.CrunchifyFindClassesFromJar;
import bgapp.utiilities.AttributePlayer;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author David Calistro
 */
public class SensorSubject implements Subject,Runnable {

    private String playername;
    private int id;
    private File archivename;
    private Thread ThreadExPlug;
    private ArrayList<Observer> Observerss; 
    private Class<?> pluginClass;
    private Object ob = null;
    
    //Para inicializar el Sensor observable se le deben asignar el nombre del jugador y el nombre del archivo el cuel es un .jar
    public SensorSubject(String playername, File archivename, int id){
        this.id = id;
        this.playername = playername;
        this.archivename = archivename;
        this.Observerss=new ArrayList();
    }
        
    @Override
    public void notify(AttributePlayer AP) {
        for(Observer o: Observerss){o.update(AP);}
    }
    @Override
    public void notifyOk() {
        for(Observer o: Observerss){o.update();}
    }

    @Override
    public void addObvserver(Observer obs) {
        Observerss.add(obs);
    }

    @Override
    public void removeObserver(Observer obs) {
        Observerss.remove(obs);
    }
    public void notificar(SensorNeed SN) {
        for(Observer o: Observerss){o.update(SN);}
    }

    //Metodo que inicia la ejecución del hilo con el comportamiendo definido
    public void start() {
        setThreadExPlug(new Thread(this));
        getThreadExPlug().start();
    }
    
    public void Stopp() {
        
        try {
            Method metodo = this.pluginClass.getDeclaredMethod("StopThis");
            metodo.invoke(this.ob);
            
        } catch (IllegalAccessException ex) {
            Logger.getLogger(SensorSubject.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(SensorSubject.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(SensorSubject.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(SensorSubject.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(SensorSubject.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /*
    
    Aquí se ejecuta cada hilo con cada ".JAR", por lo que si vas a agregar un nuevo plugin mira aquí.
    
    Si buscas cambiar lo que hace cada hijo o revisar el orden de ejecución, este es el lugar que tienes que editar!
        
    */
    @Override
    public void run() {
        
        System.out.println("ABSOLUTE CACA: "+ this.archivename.getAbsolutePath());
        CrunchifyFindClassesFromJar thasas = new CrunchifyFindClassesFromJar();
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException ex) {
            Logger.getLogger(SensorSubject.class.getName()).log(Level.SEVERE, null, ex);
        }
        String path = thasas.getCrunchifyClassNamesFromJar(this.archivename.getAbsolutePath(),"initializeValues");
        System.out.println("ACA ESTA EL PATH: "+path);
        if (path == null){
            System.out.println(this.archivename.getAbsolutePath()+": NO EXISTE EN  ESTE JAR LA MINIMA CONDICION PARA QUE EL JAR SEA UN PLUGGIN");
            
        } else{
            System.out.println(this.archivename.getAbsolutePath()+"PLUGIN INICIADO CON EXITO");
            URL myJarFile;
            
            try {
                myJarFile = new URL("jar","","file:"+this.archivename.getAbsolutePath()+"!/");
                ClassLoader cl = URLClassLoader.newInstance(new URL[]{myJarFile},this.getClass().getClassLoader());
                
                try {
                    //CLASE INICIALIZADA DESDE EL .JAR
                    pluginClass = Class.forName(path, false, cl);
                    //OBJETO INICIALIZADO AL UTILIZAR LA CLASE ANTERIOR
                    this.ob = pluginClass.newInstance();
                    
                    /*Field mtd[] = PlugginClass.getDeclaredFields();
                    for(int i = 0; i<mtd.length;i++){
                        System.out.println(mtd[i].getName());
                    }*/
                    
                    
                    /*Class[] argTypes = new Class[] { Observadores.getClass() }; //Integer.TYPE;
                    Method metodo = PlugginClass.getDeclaredMethod("iniObs",argTypes);
                    metodo.invoke(ob,Observadores);*/
                    
                    Method metodo = pluginClass.getDeclaredMethod("initializeValues");
                    metodo.invoke(this.ob);
                    
                    Class[] argTypes = new Class[] { Observerss.getClass() };
                    metodo = pluginClass.getDeclaredMethod("setSensorNeedObservers",argTypes);
                    metodo.invoke(this.ob,Observerss);
                    
                    System.out.println("ALOSDAFASDFSADGFASDGSADGSDG ++++++++++++++++++++++++++++++++++++");
                    metodo = pluginClass.getDeclaredMethod("getSensorNeed");
                    SensorNeed AuxOB =(SensorNeed)metodo.invoke(this.ob);
                    notificar(AuxOB);
                    
                    AuxOB.setHost("8001");
                    metodo = pluginClass.getDeclaredMethod("run");
                    metodo.invoke(this.ob);
                     
                    
                    
                    getThreadExPlug().interrupt();
                    System.out.println("\n\nTERMINA LA EJECUCIÓN DEL SENSOR: "+ getThreadExPlug().getId() +" Y TODO \n\n");


                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(SensorSubject.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InstantiationException ex) {
                    Logger.getLogger(SensorSubject.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(SensorSubject.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NoSuchMethodException ex) {
                    Logger.getLogger(SensorSubject.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SecurityException ex) {
                    Logger.getLogger(SensorSubject.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(SensorSubject.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    Logger.getLogger(SensorSubject.class.getName()).log(Level.SEVERE, null, ex);
                } 
            } catch (MalformedURLException ex) {
                Logger.getLogger(SensorSubject.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        
           
    }
    
    
        /**
     * @return the ThreadExPlug
     */
    public Thread getThreadExPlug() {
        return ThreadExPlug;
    }

    /**
     * @param ThreadExPlug the ThreadExPlug to set
     */
    public void setThreadExPlug(Thread ThreadExPlug) {
        this.ThreadExPlug = ThreadExPlug;
    }

    
    //Matar al hijo
    public void stopSon(){
        getThreadExPlug().interrupt();
    }
    /**
     * @return the playername
     */
    public String getPlayername() {
        return playername;
    }

    /**
     * @param playername the playername to set
     */
    public void setPlayername(String playername) {
        this.playername = playername;
    }

    
}
