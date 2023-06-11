package three.kingdom.backend;
import java.util.ArrayList;
import java.util.List;
public class TreeNode<E>{
    E element;
    private List<TreeNode<E>> children;

    public TreeNode(E element) {
        this.element = element;
        children = new ArrayList<>();
    }

    public List<TreeNode<E>> getChildren() {
        return children;
    }

    public E getElement() {
        return element;
    }

    public void addChild(TreeNode<E> child) {
        children.add(child);
    }
}