package ggc.core;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class Recipe implements Serializable{
    private Collection <Component> _components;
    private double _alpha;
    public Recipe(Collection <Component> components, double alpha){
        _components = components;
        _alpha = alpha;
    }


    public void addComponent(Component component){
        _components.add(component);
    }

    public Collection<Component> getComponents(){
        return _components;
    }
    


    @Override
    public String toString(){
        String res = "";
        List<Component> list = new ArrayList<>();
        for(Component c: _components){
            list.add(c);
        }
        Collections.sort(list,Component.getComparatorComponents());
        for (Component c: list){
            res += c.getProduct().getId() + ":" + c.getQuantity() + "#";
        }
        res = res.substring(0, res.length() - 1);
        return res;
    }
}