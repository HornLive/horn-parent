package alg.acnode;

import java.util.ArrayList;
import java.util.List;

public class Engine_Acnode {
	public static void main(String[] args){
        ArrayList<Node> dpoints = new ArrayList<Node>();
       
        double[] a={2,3,3};
        double[] b={2,4,2};
        double[] c={1,4,6};
        double[] d={1,3,1};
        double[] e={2,2,5};
        double[] f={3,2,8};
        double[] g={8,7,1};
        double[] h={8,6,7};
        double[] i={7,7,0};
        double[] j={7,6,3};
        double[] k={8,5,2};
        double[] l={100,2,1};//孤立点
        double[] m={8,20,21};
        double[] n={8,19,2};
        double[] o={7,18,2};
        double[] p={7,17,2};
        double[] q={8,21,2};
        dpoints.add(new Node("a",a));
        dpoints.add(new Node("b",b));
        dpoints.add(new Node("c",c));
        dpoints.add(new Node("d",d));
        dpoints.add(new Node("e",e));
        dpoints.add(new Node("f",f));
        dpoints.add(new Node("g",g));
        dpoints.add(new Node("h",h));
        dpoints.add(new Node("i",i));
        dpoints.add(new Node("j",j));
        dpoints.add(new Node("k",k));
        dpoints.add(new Node("l",l));
        dpoints.add(new Node("m",m));
        dpoints.add(new Node("n",n));
        dpoints.add(new Node("o",o));
        dpoints.add(new Node("p",p));
        dpoints.add(new Node("q",q));

        Acnode ac = new Acnode();
        List<Node> nodeList = ac.getOutlierNode(dpoints);

        for(Node node:nodeList){
            System.out.println(node.getNodeName()+"  "+node.getLof());
        }
    }
}
