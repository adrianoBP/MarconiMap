package com.heysidisi.marconimap.marconimap;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;

public class pianoTerra extends AppCompatActivity implements View.OnTouchListener {

    float xDelta, yDelta = 0;
    float xBase, yBase = 0;
    float xBias, yBias = 0;
    int height = 0;
    int width = 0;
    ImageView img;
    ConstraintLayout.LayoutParams params1;
    ConstraintLayout lyt;
    ArrayList<elementoArco> archi = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piano_terra);
        getSupportActionBar().setTitle("Piano terra");
        width = getResources().getDisplayMetrics().widthPixels;
        height = getResources().getDisplayMetrics().heightPixels;
        ImageView tmp = (ImageView) findViewById(R.id.imgPianoTerra);
        tmp.setOnTouchListener(this);
        lyt = (ConstraintLayout) findViewById(R.id.layPianoTerra);
        lyt.setOnTouchListener(this);
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.floatingActionButton);
        final String[] arraySpinner = new String[] {
                "ENTRATA","SCALE ENTRATA","ASCENSORE","SCALE","AULA MAGNA","TL01","TL08","TL09","TL10","TL13","TL14","TL15","TL16","TA31","TA32","TA34","TA35","TA36","TA41","TA42","TA43","TA44","TL45","TA49","TA50","TA51","TA52","TA53","TA58"
        };
        fab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(pianoTerra.this);
                        dialog.setTitle("Seleziona:");
                        Context context = pianoTerra.this;
                        LinearLayout layout = new LinearLayout(context);
                        layout.setOrientation(LinearLayout.VERTICAL);
                        final Spinner spn = new Spinner(context);
                        final Spinner spn1 = new Spinner(context);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(pianoTerra.this,
                                android.R.layout.simple_spinner_dropdown_item, arraySpinner);
                        spn.setAdapter(adapter);
                        spn1.setAdapter(adapter);
                        layout.addView(spn);
                        layout.addView(spn1);

                        dialog.setView(layout);

                        dialog.setPositiveButton("Cerca", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                partenza = spn.getSelectedItem().toString();
                                switch (partenza) {
                                    case "ENTRATA":
                                        partenza = "ENTR";
                                        break;
                                    case "SCALE ENTRATA":
                                    case "ASCENSORE":
                                        partenza = "INTR";
                                        break;
                                    case "SCALE":
                                        partenza = "SCA2";
                                        break;
                                    case "AULA MAGNA":
                                        partenza = "AMGN";
                                        break;
                                }
                                destinazione = spn1.getSelectedItem().toString();
                                switch (destinazione) {
                                    case "ENTRATA":
                                        destinazione = "ENTR";
                                        break;
                                    case "SCALE ENTRATA":
                                    case "ASCENSORE":
                                        destinazione = "INTR";
                                        break;
                                    case "SCALE":
                                        destinazione = "SCA2";
                                        break;
                                    case "AULA MAGNA":
                                        destinazione = "AMGN";
                                        break;
                                }
                                clearPath();
                                    elementoArco a = new elementoArco(partenza);
                                    route(funzione(a, ""));
                            }
                        });
                        dialog.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        dialog.show();
                    }
                }
        );

        clearPath();
    }

    public void clearPath()
    {
        for(int i=0; i<lyt.getChildCount(); i++) {
            ImageView child = (ImageView) lyt.getChildAt(i);
            String id = child.getResources().getResourceEntryName(child.getId());
            if(!id.equals("imgPianoTerra")&&id.length()==8)
                child.setVisibility(View.INVISIBLE);
        }
    }

    public void route(String path){
        inizializzazione(collegamenti, archi);
        fine = "";
        passati = new ArrayList<>();
        Integer counter = 0;
        clearPath();
        for(int i=0; i<lyt.getChildCount(); i++) {
            ImageView child = (ImageView) lyt.getChildAt(i);
            if(counter+8<=path.length()) {
                String read = path.substring(counter, counter+8);
                String id = child.getResources().getResourceEntryName(child.getId());
                if(id.contains(read.substring(0,4)) && id.contains(read.substring(4,8))) {
                    child.setVisibility(View.VISIBLE);
                    counter+=4;
                    i=0;
                }
            }

        }
    }

    public static String[] collegamenti = {"IN05SCA2","SCA2IN05","IN51AMGN","AMGNIN51","INTRIN00","IN00INTR","ENTRIN00","IN00ENTR","TL10IN19","IN19TL10","IN19IN18","IN18IN19","IN18TL09","TL09IN18","IN18IN17","IN17IN18","IN17TL08","TL08IN17","IN17IN14","IN14IN17","IN14IN15","IN15IN14","IN15TL13","TL13IN15","IN15TL16","TL16IN15","IN15IN16","IN16IN15","IN16TL14","TL14IN16","IN16TL15","TL15IN16","IN14IN13","IN13IN14","IN13TL01","TL01IN13","IN13IN00","IN00IN13","IN00IN03","IN03IN00","IN03IN04","IN04IN03","IN04IN50","IN50IN04","IN00IN01","IN01IN00","IN01IN02","IN02IN01","IN01TA32","TA32IN01","IN02TA31","TA31IN02","TA34IN03","IN03TA34","IN04TA35","TA35IN04","IN50TA36","TA36IN50","IN05IN50","IN50IN05","IN05IN06","IN06IN05","IN06IN07","IN07IN06","IN07IN08","IN08IN07","IN06TA41","TA41IN06","IN07TA42","TA42IN07","IN08TA43","TA43IN08","IN05IN09","IN09IN05","IN09IN10","IN10IN09","IN10IN11","IN11IN10","IN09TA44","TA44IN09","IN09TA51","TA51IN09","IN10TL45","TL45IN10","IN11TA58","TA58IN11","IN11IN12","IN12IN11","IN12IN51","IN51IN12","IN12TA49","TA49IN12","IN12TA53","TA53IN12","IN51TA50","TA50IN51","IN51TA52","TA52IN51"};
    public static String partenza;
    public static String destinazione;
    static ArrayList<String> passati = new ArrayList<>();
    public static String fine = "";

    public static String funzione(elementoArco x,String prova) //funzione ricorsiva per il calcolo del percorso
    {
        if(x.from.equals(destinazione))
            fine = prova +x.from;
        else
        {
            if(x.figli(collegamenti).size() == 1 && passati.contains(x.figli(collegamenti).get(0)))
                return fine;
            prova+=x.from;
            passati.add(x.from);
            for (String a:x.to
                    ) {
                if(!passati.contains(a) && fine.equals(""))
                {
                    passati.add(a);
                    elementoArco f = new elementoArco(a);
                    f.to = f.figli(collegamenti);
                    funzione(f, prova);
                }
            }
        }
        return fine;
    }
    public static void inizializzazione(String[] archi, ArrayList<elementoArco> elementi)
    {
        ArrayList<String> supp = new ArrayList<>();

        for (String w:archi
                ) {
            String a = w.substring(0, Math.min(w.length(), 4));
            if(!supp.contains(a))
            {
                supp.add(a);
                elementoArco b = new elementoArco(a);
                elementi.add(b);
            }
        }
    }
    public boolean onTouch(View v, MotionEvent event) {
        img = (ImageView)findViewById(R.id.imgPianoTerra);
        params1 = (ConstraintLayout.LayoutParams)(img.getLayoutParams());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                xBase=event.getRawX();
                yBase=event.getRawY();
                xBias = params1.horizontalBias;
                yBias = params1.verticalBias;

                break;
            case MotionEvent.ACTION_UP:

                break;
            case MotionEvent.ACTION_MOVE: {

                //a3.setText((int)event.getX() + " - " + (int)event.getY());
                xDelta=event.getRawX();
                yDelta=event.getRawY();


                float xMove = Math.abs (event.getRawX() - xBase);
                float yMove = Math.abs (event.getRawY() - yBase);

                float xPos = xMove/width;
                float yPos = yMove/height;

                if (xBase > event.getRawX()) {
                    if ((xBias + xPos)<1)
                        params1.horizontalBias = xBias + xPos;
                } else if (xBase < event.getRawX()) {
                    if ((xBias - xPos)>0)
                        params1.horizontalBias = xBias - xPos;
                }

                if (yBase > event.getRawY()) {
                    if ((yBias + yPos*2) < 1)
                        params1.verticalBias = yBias + yPos*2;

                } else if (yBase < event.getRawY()) {
                    if (yBias - yPos*2 > 0)
                        params1.verticalBias = yBias - yPos*2;

                }

                img.setLayoutParams(params1);
                break;
            }

        }
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
