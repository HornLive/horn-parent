package org.horn.commons.java.serialize;

import java.io.*;

/**
 * @author Administrator
 *
 */
public class SerializeUtil {
      public static byte[] serialize(Object object) {
           ObjectOutputStream oos = null;
            ByteArrayOutputStream baos = null;
            try {
                 // 序列化
                baos = new ByteArrayOutputStream();
                oos = new ObjectOutputStream(baos);
                oos.writeObject(object);
                 byte[] bytes = baos.toByteArray();
                 return bytes;
           } catch (Exception e) {

           }
            return null;
     }

      public static Object unserialize( byte[] bytes) {
           ByteArrayInputStream bais = null;
            try {
                 // 反序列化
                bais = new ByteArrayInputStream(bytes);
                ObjectInputStream ois = new ObjectInputStream(bais);
                 return ois.readObject();
           } catch (Exception e) {

           }
            return null;
     }
      
      public static void main(String[] args) {
          // 操作实体类对象
         Goods good= new Goods();  // 这个Goods实体我就不写了啊
         good.setName( "洗衣机" );
         good.setPrice(191);
         
         byte[] b = SerializeUtil.serialize(good);
         Object object = SerializeUtil. unserialize(b); 
                   
          if(object!= null){
              Goods goods=(Goods) object;
              System. out.println(goods.name);
              System. out.println(goods.price);
         }
   }
}

class Goods implements Serializable{
	private static final long serialVersionUID = 1L;
	public String name;
	public int price;
	
	public void setName(String name0){
		this.name = name0;
	}
	public void setPrice(int pri){
		this.price = pri;
	}
}