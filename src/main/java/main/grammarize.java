package main;

public class grammarize {
    
    public grammarize(String s) {
        int counter = 0;

        String[] ary = s.split(" ");

            for (String str : ary) {

                if (str.equals("im")) {
                    ary[counter] = "I'm";
                } else if (str.equals("ur")) {
                    ary[counter] = "your";
                } else if (str.equals("yea")) {
                    ary[counter] = "yes";
                } else if (str.equals("yuh")) {
                    ary[counter] = "yes";
                } else if (str.equals("mhm")) {
                    ary[counter] = "yes";
                } else if (str.equals("hallo")) {
                    ary[counter] = "hello";
                } else if (str.equals("sup")) {
                    ary[counter] = "What's up";
                } else if (str.equals("gae")) {
                    ary[counter] = "gay";
                } else if (str.equals("heyy")) {
                    ary[counter] = "hey";
                } else if (str.equals("heyyy")) {
                    ary[counter] = "hey";
                } else if (str.equals("heyyyy")) {
                    ary[counter] = "hey";
                }
                counter++;
            }

    }
}
