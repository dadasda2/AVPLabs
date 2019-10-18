package lab2;

import java.awt.*;

public class User extends Object{
  public Point point;
  public String name;
  public Color color;

  @Override
  public String toString(){

      return point.toString() + " " + name
              + " " + color.toString();
  }

  public User(){
       point = new Point(1,1); // TODO: 17.10.2019  задать дефолтное начало лабиринта
       name = "ss";
       color =  new Color(255, 160, 0);
    }

  public User(Point p,String n,Color c){
      point = p;
      name = n;
      color = c;
  }

  public void genFromString(String str){
    String[] res = str.split(",");
    this.point.x = Integer.parseInt(res[0]);
    this.point.y = Integer.parseInt(res[1]);
    this.name = res[2];
    this.color = new Color(Integer.parseInt(res[3]),Integer.parseInt(res[4]),Integer.parseInt(res[5]));
  }

  public String genToString(){
      String res = new String();
      res += this.point.getX();
      res +=",";
      res += this.point.getY();
      res +=",";
      res += this.name;
      res +=",";
      res += this.color.getRed();
      res +=",";
      res += this.color.getGreen();
      res +=",";
      res += this.color.getBlue();
  return res;
  }

    public static void main(String[] args) {
            User u = new User(new Point(1,1),"ss", new Color(255, 160, 0));
            String s = new String("1,2,qwerty,255,255,255");
            u.genFromString(s);
            u.genToString();
  }
}
