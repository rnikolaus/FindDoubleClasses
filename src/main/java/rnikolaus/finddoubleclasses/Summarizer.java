package rnikolaus.finddoubleclasses;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author rapnik
 */
public class Summarizer {

    private boolean hasDuplicates;
    final private Map<String, List<String>> values = new TreeMap<>();

    public void add(String jarEntry, String filePath) {
        if (!values.containsKey(jarEntry)) {
            values.put(jarEntry, new ArrayList<String>());
        }else{
            hasDuplicates=true;
        }
        values.get(jarEntry).add(filePath);
    }

    public boolean hasDuplicates(){
        return hasDuplicates;
    }
    public void printDuplicateResult() {
        for (String jarEntry :getMultipleEntries()) {
            int size = values.get(jarEntry).size();
            
                System.out.println(jarEntry + " " + size);
                for (String path:values.get(jarEntry)){
                    System.out.println("\t"+path);
                }
            
        }
    }
    public void printAllResult() {
        for (Map.Entry<String, List<String>> result :values.entrySet()) {
                System.out.println(result.getKey());
                for (String path:result.getValue()){
                    System.out.println("\t"+path);
                }
            
        }
    }
    public Set<String> getMultipleEntries(){
            TreeSet<String> result = new TreeSet<>();
        for (Map.Entry<String,List<String>> e : values.entrySet()){
            if (e.getValue().size()>1)result.add(e.getKey());
        }
        return result;
    }
    public List<String> getLocations(String jarEntry){
        return values.get(jarEntry);
    }
    

}
