package sort;

import time.TimeCheck;

import java.util.*;
import java.util.stream.Stream;

public class 성적이_낮은_순서로_학생_출력하기 {

  private static void solution1(ArrayList<Entity> n) {
    int maxScore = 100;
    ArrayList[] answerAry = new ArrayList[maxScore + 1];

    for (int i = 0; i < answerAry.length; i++) {
      answerAry[i] = new ArrayList();
    }

    for (Entity e : n) {
      answerAry[e.score].add(e);
    }

    for (ArrayList arrayList : answerAry) {
      for (Object o : arrayList) {
        Entity e = (Entity) o;
        System.out.print(e.name + " ");
      }
    }

  }

  private static void solution2(ArrayList<Entity> n) {
    n.stream().sorted(Comparator.comparing(Entity::getScore)).forEach(
      (entity) -> {System.out.print(entity.getName() + " ");}
    );
  }

  public static void execute() {
    Scanner scn = new Scanner(System.in);
    int size = scn.nextInt();
    ArrayList<Entity> n = new ArrayList<>();
    scn.nextLine();
    for (int i = 0; i < size; i++) {
      String[] tmp = scn.nextLine().split(" ");
      n.add(new Entity(tmp[0], Integer.parseInt(tmp[1])));
    }

    TimeCheck.start();

    solution2(n);

    TimeCheck.end();

  }

  private static class Entity {
    private String name;
    private int score;

    public Entity(String name, int score) {
      this.name = name;
      this.score = score;
    }

    public String getName() {
      return name;
    }

    public int getScore() {
      return score;
    }
  }

}
