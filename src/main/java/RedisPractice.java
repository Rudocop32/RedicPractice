import org.redisson.api.RScoredSortedSet;

import java.util.Random;

public class RedisPractice {

    static Random random = new Random();
    private static double INC = 0.1;
    private static final int users=20;

    public static void main(String[] args) {
        RedisStorage redisStorage = new RedisStorage();
        redisStorage.init();
        for(int i=1;i<20;i++){
            redisStorage.logPageVisit(i);
        }
        RScoredSortedSet<String> users = redisStorage.getOnlineUsers();
        while (true){
            String currentUser = users.first();
            Double currentUserScore =users.firstScore();
            System.out.println("— На главной странице показываем пользователя " + currentUser);
            users.pollFirst();
            users.addScore(currentUser,currentUserScore + INC);
            if(payment()){
                int randomUser = random.nextInt(users.size());
                String paidUser = users.valueRange(randomUser,randomUser).iterator().next();
                double paidUserScore = users.getScore(paidUser);
                System.out.println("> Пользователь " + paidUser + " оплатил платную услугу");
                System.out.println("— На главной странице показываем пользователя " + paidUser);
                users.remove(paidUser);
                users.addScore(paidUser,paidUserScore + INC);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    public static boolean payment(){
        int chance = new Random().nextInt(10);
        if(chance == 0){
            return true;
        }
        return false;
    }

}
