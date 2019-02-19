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

public class interrato extends AppCompatActivity implements View.OnTouchListener {


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
        setContentView(R.layout.activity_interrato);

        getSupportActionBar().setTitle("Interrato");
        width = getResources().getDisplayMetrics().widthPixels;
        height = getResources().getDisplayMetrics().heightPixels;
        ImageView tmp = (ImageView) findViewById(R.id.imgInterrato);
        tmp.setOnTouchListener(this);
        lyt = (ConstraintLayout) findViewById(R.id.layInterrato);
        lyt.setOnTouchListener(this);
        final String[] arraySpinner = new String[] {
                "SCALE ENTRATA","ASCENSORE","IL01","IL08","IL03","IL00"
        };
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.floatingActionButton4);
        fab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(interrato.this);
                        dialog.setTitle("Seleziona");
                        Context context = interrato.this;
                        LinearLayout layout = new LinearLayout(context);
                        layout.setOrientation(LinearLayout.VERTICAL);

                        final Spinner spn = new Spinner(context);
                        final Spinner spn1 = new Spinner(context);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(interrato.this,
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
                                partenza = spn.getSelectedItem().toString();
                                switch (partenza) {
                                    case "SCALE ENTRATA":
                                    case "ASCENSORE":
                                        partenza = "INTR";
                                        break;
                                }
                                destinazione = spn1.getSelectedItem().toString();
                                switch (destinazione) {
                                    case "SCALE ENTRATA":
                                    case "ASCENSORE":
                                        destinazione = "INTR";
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

    public static String[] collegamenti = {"INTRIN20","IN20INTR","IN20IN21","IN21IN20","IN21IL08","IL08IN21","IN21IL01","IL01IN21","IN21IN22","IN22IN21","IN22IL03","IL03IN22","IN22IL00","IL00IN22"};
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
        img = (ImageView)findViewById(R.id.imgInterrato);
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
