package alg.acnode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

//1.找到给定点与其他点的欧几里得距离
//2.对欧几里得距离进行排序，找到前5位的点，并同时记下k距离
//3.计算每个点的可达密度
//4.计算每个点的局部离群点因子
//5.对每个点的局部离群点因子进行排序，输出。
public class Acnode {
    private static int MIN_PTS=5;
    
    public List<Node> getOutlierNode(List<Node> allNodes){
    	
    	List<Node> kdAndKnList=getKDAndKN(allNodes);
    	calReachDis(kdAndKnList);
    	calReachDensity(kdAndKnList);
    	calLof(kdAndKnList);
    	Collections.sort(kdAndKnList, new LofComparator());
    	
        return kdAndKnList;
    }
   
    private void calLof(List<Node> kdAndKnList){
        for(Node node:kdAndKnList){
             List<Node> tempNodes=node.getkNeighbor();
             double sum=0.0;
             for(Node tempNode:tempNodes){
                 double rd=getRD(tempNode.getNodeName(),kdAndKnList);
                 sum=rd/node.getReachDensity()+sum;
             }
             sum=sum/(double)MIN_PTS;
             node.setLof(sum);
        }
    }

    private void calReachDensity(List<Node> kdAndKnList){
        for(Node node:kdAndKnList){
             List<Node> tempNodes=node.getkNeighbor();
             double sum=0.0;
             double rd=0.0;
             for(Node tempNode:tempNodes){
                 sum=tempNode.getReachDis()+sum;
             }
             rd=(double)MIN_PTS/sum;
             node.setReachDensity(rd);
        }
    }

   
    private void calReachDis(List<Node> kdAndKnList){
       for(Node node:kdAndKnList){
           List<Node> tempNodes=node.getkNeighbor();
           for(Node tempNode:tempNodes){
              double kDis=getKDis(tempNode.getNodeName(),kdAndKnList);
              if(kDis<tempNode.getDistance()){
                  tempNode.setReachDis(tempNode.getDistance());
              }else{
                  tempNode.setReachDis(kDis);
              }
           }
       }
    }
   
    private double getKDis(String nodeName,List<Node> nodeList){
        double kDis=0;
        for(Node node:nodeList){
            if(nodeName.trim().equals(node.getNodeName().trim())){
                kDis=node.getkDistance();
                break;
            }
        }
        return kDis;

    }

   
    private double getRD(String nodeName,List<Node> nodeList){
        double kDis=0;
        for(Node node:nodeList){
            if(nodeName.trim().equals(node.getNodeName().trim())){
                kDis=node.getReachDensity();
                break;
            }
        }
        return kDis;

    }

   
    private List<Node> getKDAndKN(List<Node> allNodes){
       List<Node> kdAndKnList=new ArrayList<Node>();
       for(int i=0;i<allNodes.size();i++){
           List<Node> tempNodeList=new ArrayList<Node>();
           Node nodeA=new Node(allNodes.get(i).getNodeName(),allNodes.get(i).getDimensioin());
           for(int j=0;j<allNodes.size();j++){
              Node nodeB=new Node(allNodes.get(j).getNodeName(),allNodes.get(j).getDimensioin());
              double tempDis=getDis(nodeA,nodeB);
              nodeB.setDistance(tempDis);
              tempNodeList.add(nodeB);
           }

           //对tempNodeList进行排序
           Collections.sort(tempNodeList, new DistComparator());
           for(int k=1;k<MIN_PTS;k++){
               nodeA.getkNeighbor().add(tempNodeList.get(k));
               if(k==MIN_PTS-1){
                   nodeA.setkDistance(tempNodeList.get(k).getDistance());
               }
           }
           kdAndKnList.add(nodeA);
       }

       return kdAndKnList;
    }

   
    private double getDis(Node A, Node B){
        double dis=0.0;
        double[] dimA=A.getDimensioin();
        double[] dimB=B.getDimensioin();
        if (dimA.length == dimB.length) {
            for (int i = 0; i < dimA.length; i++) {
                double temp = Math.pow(dimA[i] - dimB[i], 2);
                dis = dis + temp;
            }
            dis=Math.pow(dis, 0.5);
        }
        return dis;
    }

    class DistComparator implements Comparator<Node>{
        public int compare(Node A, Node B){
           return A.getDistance()-B.getDistance()<0?-1:1;
        }
    }

    class LofComparator implements Comparator<Node>{
        public int compare(Node A, Node B){
           return A.getLof()-B.getLof()<0?-1:1;
        }
    }

    
}






























