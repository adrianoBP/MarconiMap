package com.heysidisi.marconimap.marconimap;

/**
 * Created by lului on 26/04/2017.
 */

import java.util.ArrayList;

public class elementoArco {
    public String from;
    public ArrayList<String> to;

//    public elementoArco(String from, ArrayList<String> tx)
//    {
//        to = new ArrayList<>();
//        this.from = from;
//        for (String t: tx
//             ) {
//            to.add(t);
//        }
//    }

    public elementoArco(String from)
    {
        this.from = from;
        to = new ArrayList<>();
    }

    public ArrayList<String> figli(String[] collegamenti)
    {
        ArrayList<String> ritorno = new ArrayList<>();
        for (String s:collegamenti
             ) {
            String first = s.substring(0, Math.min(s.length(), 4));
            if (first.equals(from))
                ritorno.add(s.substring(s.length() - 4));
        }
        to = new ArrayList<>(ritorno);
        return ritorno;
    }

}
