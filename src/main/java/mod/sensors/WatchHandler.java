/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mod.sensors;

/**
 *
 * @author Bad-K
 */
import java.io.File;

public interface WatchHandler {
    public void onCreate(File file);
    public void onDelete(File file);
    public void onModify(File file);
}
