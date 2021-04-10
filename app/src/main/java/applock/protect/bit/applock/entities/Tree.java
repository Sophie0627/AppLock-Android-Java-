package applock.protect.bit.applock.entities;

import java.util.Iterator;
import java.util.LinkedList;


public final class Tree {

final static String TAG=Tree.class.getSimpleName();

final private String name;
final private int id;

private Tree parent;
private LinkedList<Tree> childs = new LinkedList<Tree>();


public Tree(String name, int id) {
    parent = null;
    this.name = name;
    this.id = id;
}

public void addChild(Tree child){
    child.parent = this;
    childs.add(child);
}


public int getId() {
    return id;
}

public String getName() {
    return name;
}

public Tree getParent() {
    return parent;
}

public LinkedList<Tree> getChilds() {
    return childs;
}



@Override
public String toString() {
    Iterator<Tree> iter = childs.iterator();
    String childs = "[";
    while(iter.hasNext()){
        Tree ch = iter.next();
        childs = childs.concat(ch+",");
    }
    childs = childs.concat("]");
    return name + " "+ id + childs;
}


}