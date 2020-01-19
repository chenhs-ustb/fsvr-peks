package tree;

import com.n1analytics.paillier.EncryptedNumber;

import java.util.ArrayList;

public class Tree {
    public Node root;
    Node next;
    int count = 0;

    public Tree() {

        next = root = new Node();
        count = 1;
    }

    public int binaryTree_count_recursive(Node root) {
        int numberOfNode = 1;

        if (root.left != null && root.left.data != null) {
            numberOfNode += binaryTree_count_recursive(root.left);
        }
        if (root.right != null && root.right.data != null) {
            numberOfNode += binaryTree_count_recursive(root.right);
        }

        return numberOfNode;
    }

    public int countNode() {
        int numberOfNode = 0;

        if (root.data != null) {
            numberOfNode = binaryTree_count_recursive(root);
        }
        return numberOfNode;
    }


    public void insert(EncryptedNumber[] data, String id) {

        if (next == null) {

            ArrayList<Node> listOfNode = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                Node e = new Node();
                if (i > 0) {
                    listOfNode.get(i - 1).next = e;
                }
                listOfNode.add(e);
            }
            next = listOfNode.get(0);

            while (listOfNode.size() > 1) {
                ArrayList<Node> temp = new ArrayList<>();
                for (int i = 0; i < listOfNode.size(); i = i + 2) {
                    Node parent = new Node();
                    parent.left = listOfNode.get(i);
                    parent.right = listOfNode.get(i + 1);
                    listOfNode.get(i).parent = parent;
                    listOfNode.get(i + 1).parent = parent;
                    temp.add(parent);
                }
                listOfNode = temp;
            }

            Node newRoot = new Node();
            newRoot.left = root;
            newRoot.right = listOfNode.get(0);
            newRoot.data = root.data;
            root.parent = newRoot;
            listOfNode.get(0).parent = newRoot;
            root = newRoot;
            count = count * 2;

        }
        next.data = data;
        next.id = id;
        Node parent = next.parent;
        while (parent != null) {
            if (parent.data == null) {
                parent.data = data;
            } else {
                parent.data = add(parent.data, data);
            }
            parent = parent.parent;

        }
        next = next.next;

        //System.out.println("current count is " + count);

    }

    public EncryptedNumber[] add(EncryptedNumber[] a, EncryptedNumber[] b) {
        EncryptedNumber[] r = new EncryptedNumber[a.length];
        for (int i = 0; i < a.length; i++) {
            r[i] = a[i].add(b[i]);
        }
        return r;
    }

    public EncryptedNumber[] subtract(EncryptedNumber[] a, EncryptedNumber[] b) {
        if (b == null)
            return a;
        EncryptedNumber[] r = new EncryptedNumber[a.length];
        for (int i = 0; i < a.length; i++) {
            r[i] = a[i].subtract(b[i]);
        }
        return r;
    }

    public EncryptedNumber[] currentNode(Node root) {

        if (root.left != null && root.right != null) {
            EncryptedNumber[] subtractedValue = subtract(root.left.data, root.right.data);
            return subtractedValue;
        } else
            return null;
    }

}
