package com.example.mitsichury.jpo2;

import android.app.Activity;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;


public class MainActivity extends Activity{
    private static final int TEMPS = 120;
    ZoneDeDessin zoneDeDessin;
    Handler handler;
    int hauteur;
    int largeur;
    TextView minuteur;
    int tempsRestant;
    Button boutonRecommencer;
    Runnable quiGereLeTemps;
    Runnable testIps;
    float xTouchUp;
    float yTouchUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        minuteur = (TextView)findViewById(R.id.textView);
        zoneDeDessin = (ZoneDeDessin)findViewById(R.id.drawview);
        boutonRecommencer = (Button)findViewById(R.id.boutonRecommencer);

        zoneDeDessin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    xTouchUp = event.getX();
                    yTouchUp = event.getY();
                }

                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    zoneDeDessin.definirLaPositionDuPoint(zoneDeDessin.recuperePositionXduPoint() + (int) (xTouchUp - event.getX()), zoneDeDessin.recuperePositionYduPoint() + (int) (yTouchUp - event.getY()));
                    verifieQueSortDecran();

                    xTouchUp = event.getX();
                    yTouchUp = event.getY();
                }

                return true;
            }
        });

        boutonRecommencer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nouveauJeu();
            }
        });

        recupereTailleEcran();
        zoneDeDessin.donnerTailleEcran(largeur, hauteur);

        handler = new Handler();

        quiGereLeTemps = new Runnable() {
            @Override
            public void run() {
                if(tempsRestant >0){
                    minuteur.setText(convertisseurSecondeEnTexte());
                    tempsRestant = tempsRestant -1;
                    handler.postDelayed(this, 1000);
                }else{
                    faireApparaitreRecommencerAvecScore();
                }
            }
        };

        testIps = new Runnable() {
            @Override
            public void run() {
                zoneDeDessin.dessiner();
                handler.postDelayed(this, 10);
            }
        };
        handler.post(testIps);

        nouveauJeu();
    }

    /**
     * Récupére la taille de l'écran et la stocke dans heuteur et largeur
     */
    private void recupereTailleEcran() {
        Display ecran = getWindowManager().getDefaultDisplay();
        Point taille = new Point();
        // Si la version est inférieur à celle de honeycomb (13)
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2){
            hauteur = ecran.getHeight();
            largeur = ecran.getWidth();
        }
        else{
            ecran.getSize(taille);
            largeur = taille.x;
            hauteur = taille.y;
        }
    }

    /**
     * Cache le minuteur, affiche le bouton avec le score
     */
    private void faireApparaitreRecommencerAvecScore() {
        enleverLeCarre();
        cacherMinuteur();
        afficherBouton();
        enleverLeCarre();
    }

    /**
     * Enleve la nourriture du serpent
     */
    private void enleverLeCarre() {
        zoneDeDessin.cacherLeCarre();
    }

    private void afficherCarre(){
        zoneDeDessin.afficherLeCarre();
    }

    /**
     * Affiche le bouton recommencer avec le nombre de carré mangé
     */
    private void afficherBouton() {
        String texte = String.valueOf(zoneDeDessin.savoirLenombreDeCarreMange());
        boutonRecommencer.setText(texte);
        boutonRecommencer.setVisibility(View.VISIBLE);
    }

    /**
     * Cache le minuteur
     */
    private void cacherMinuteur() {
        minuteur.setVisibility(View.GONE);
    }

    /**
     * Cache le bouton
     */
    private void cacherBouton(){
        boutonRecommencer.setVisibility(View.GONE);
    }

    /**
     * Convertie le nombre de seconde en m:ss
     * @return
     * Le temps formatté
     */
    private String convertisseurSecondeEnTexte() {
        DecimalFormat deuxChiffresPourLesSecondes = new DecimalFormat("00");
        int minutes = tempsRestant /60;
        int secondes = tempsRestant %60;

        return minutes+":"+deuxChiffresPourLesSecondes.format(secondes);
    }

    /**
     * Initialise le jeu
     */
    private void nouveauJeu(){
        tempsRestant = TEMPS;
        cacherBouton();
        afficherMinuteur();
        afficherCarre();
        zoneDeDessin.viderLaZoneDeDessin();
        handler.post(quiGereLeTemps);
    }

    /**
     * Affiche le menu
     */
    private void afficherMinuteur() {
        minuteur.setVisibility(View.VISIBLE);
    }

    /**
     * Permet au serpent de passeer d'un bord a l'autre
     */
    public void verifieQueSortDecran(){
        if(zoneDeDessin.recuperePositionXduPoint()<0){
            zoneDeDessin.definirLaPositionDuPoint(largeur, zoneDeDessin.recuperePositionYduPoint());}
        if(zoneDeDessin.recuperePositionXduPoint()> largeur){
            zoneDeDessin.definirLaPositionDuPoint(0, zoneDeDessin.recuperePositionYduPoint());}
        if(zoneDeDessin.recuperePositionYduPoint()<0){
            zoneDeDessin.definirLaPositionDuPoint(zoneDeDessin.recuperePositionXduPoint(), hauteur);}
        if(zoneDeDessin.recuperePositionYduPoint()>hauteur){
            zoneDeDessin.definirLaPositionDuPoint(zoneDeDessin.recuperePositionXduPoint(), 0);}
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
